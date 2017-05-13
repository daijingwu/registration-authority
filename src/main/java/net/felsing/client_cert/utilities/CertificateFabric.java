/*
 * Copyright (c) 2016. by Christian Felsing
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.felsing.client_cert.utilities;


import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;


public class CertificateFabric {
    private static final Logger logger = LoggerFactory.getLogger(CertificateFabric.class);

    public class ReqData {
        public String subject;
        public String msg;
        public int status;
    }


    private static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter)
            throws Exception {
        String data = new String(pem);
        String[] tokens = data.split(beginDelimiter);
        tokens = tokens[1].split(endDelimiter);
        return DatatypeConverter.parseBase64Binary(tokens[0]);
    }


    public ReqData getReqSubject(String pkcs10string) {
        ReqData reqData = new ReqData();
        reqData.subject = null;
        reqData.msg = "Ok";
        reqData.status = 0;

        pkcs10string = pkcs10string.trim();
        try {
            byte[] reqBytes;
            reqBytes = CertificateFabric.parseDERFromPEM(pkcs10string.getBytes(),
                    "-----BEGIN CERTIFICATE REQUEST-----",
                    "-----END CERTIFICATE REQUEST-----");
            PKCS10CertificationRequest pkcs10CertificationRequest =
                    new PKCS10CertificationRequest(reqBytes);
            reqData.subject = pkcs10CertificationRequest.getSubject().toString().replaceAll("\\+", ",");

            getSubjectAlternativeNames(pkcs10CertificationRequest);

            return reqData;
        } catch (IOException e) {
            logger.warn("getReqSubject IO fucked up");
            reqData.msg = e.getLocalizedMessage();
            reqData.status = 1;
            return reqData;
        } catch (Exception e) {
            logger.warn("general fuck up");
            reqData.msg = e.getLocalizedMessage();
            reqData.status = 1;
            return reqData;
        }
    }


    static HashMap<String, String> getAttributes(String subject) {
        HashMap<String, String> hashMap = new HashMap<>();
        String[] subjectArr = subject.split(",");
        for (String item : subjectArr) {
            String[] attrArr = item.split("=");
            hashMap.put(attrArr[0].toLowerCase(), attrArr[1]);
        }
        return hashMap;
    }


    private void getSubjectAlternativeNames(PKCS10CertificationRequest pkcs10CertificationRequest) {
        Attribute[] attributes = pkcs10CertificationRequest.getAttributes();
        for (Attribute attr : attributes) {
            // process extension request
            if (attr.getAttrType().equals(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest)) {
                Extensions extensions = Extensions.getInstance(attr.getAttrValues().getObjectAt(0));
                Enumeration e = extensions.oids();
                while (e.hasMoreElements()) {
                    ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) e.nextElement();
                    Extension ext = extensions.getExtension(oid);
                    //logger.debug(ASN1Dump.dumpAsString(ext, true));
                    parseExtenstion(ext);
                }
            }
        }
    }


    private void parseExtenstion(Extension ext) {

        ASN1ObjectIdentifier extnId = ext.getExtnId();
        ASN1OctetString extnValue = ext.getExtnValue();
        if (extnId.getId().equals("2.5.29.17")) {
            DEROctetString derOctetString = (DEROctetString) extnValue;
            try {
                ASN1Primitive derObject = toDERObject(derOctetString.getOctets());
                ASN1Sequence derSequence = ASN1Sequence.getInstance(derObject);
                ASN1Encodable objectAt = derSequence.getObjectAt(0);
                ASN1TaggedObject asn1TaggedObject = ASN1TaggedObject.getInstance(objectAt);
                ASN1OctetString asn1OctetString = ASN1OctetString.getInstance(asn1TaggedObject.getObject());

                ASN1Primitive loadedObject = asn1OctetString.getLoadedObject();

                logger.debug ("Class: " + loadedObject.getClass().toString() + "\n" + ASN1Dump.dumpAsString(loadedObject, true));

                DERUTF8String derutf8String = DERUTF8String.getInstance(loadedObject.getEncoded());
                logger.debug(derutf8String.getString());
            } catch (Exception e) {
                logger.debug("toDERObject failed: " + e.getMessage());
            }
        }
    }


    private ASN1Primitive toDERObject(byte[] data) throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream(data);
        ASN1InputStream asnInputStream = new ASN1InputStream(inStream);

        return asnInputStream.readObject();
    }


    public void decode(byte[] encoding) throws Exception
    {
        ASN1Sequence seq = ASN1Sequence.getInstance(encoding);
        String url = DERUTF8String.getInstance(seq.getObjectAt(0)).getString();
    }

} // class


