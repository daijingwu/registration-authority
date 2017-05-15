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


import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.*;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;


public class CertificateFabric {
    private static final Logger logger = LoggerFactory.getLogger(CertificateFabric.class);
    private ArrayList<ArrayList<String>> subjectAlternativeNames;


    public class ReqData {
        public String subject;
        @SuppressWarnings("WeakerAccess")
        public ArrayList<ArrayList<String>> subjectAlternativeNames;
        public String msg;
        public int status;
    }


    private static class san {
        private final static ArrayList<String> san = new ArrayList<>();

        static {
            san.add("otherName");
            san.add("rfc822Name");
            san.add("dNSName");
            san.add("x400Address");
            san.add("directoryName");
            san.add("ediPartyName");
            san.add("uniformResourceIdentifier");
            san.add("iPAddress");
            san.add("registeredID");
        }

        static String getSanOid(int oid) {
            try {
                return san.get(oid);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
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
                    Constants.csrBegin,
                    Constants.csrEnd);
            PKCS10CertificationRequest pkcs10CertificationRequest =
                    new PKCS10CertificationRequest(reqBytes);
            reqData.subject = pkcs10CertificationRequest.getSubject().toString().replaceAll("\\+", ",");

            getSubjectAlternativeNames(pkcs10CertificationRequest);
            reqData.subjectAlternativeNames = subjectAlternativeNames;

            return reqData;
        } catch (IOException e) {
            reqData.msg = e.getLocalizedMessage();
            reqData.status = 1;
            logger.warn("getReqSubject IO fucked up: " + reqData.msg);
            return reqData;
        } catch (Exception e) {
            reqData.msg = e.getLocalizedMessage();
            reqData.status = 1;
            logger.warn("general fuck up: " + reqData.msg);
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


    private void getSubjectAlternativeNames (PKCS10CertificationRequest csr) {
        subjectAlternativeNames = new ArrayList<>(new ArrayList<>());
        // GeneralName.otherName is lowest and
        // GeneralName.registeredID is highest id
        for (int i=GeneralName.otherName; i<=GeneralName.registeredID; i++) {
            subjectAlternativeNames.add(new ArrayList<>());
        }

        try {
            Attribute[] certAttributes = csr.getAttributes();
            for (Attribute attribute : certAttributes) {
                if (attribute.getAttrType().equals(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest)) {
                    // @ToDo: Is there really one object only?
                    Extensions extensions = Extensions.getInstance(attribute.getAttrValues().getObjectAt(0));
                    GeneralNames gns = GeneralNames.fromExtensions(extensions, Extension.subjectAlternativeName);
                    if (gns!=null) {
                        GeneralName[] names = gns.getNames();
                        for (GeneralName name : names) {
                            subjectAlternativeNames.get(name.getTagNo()).add(name.getName().toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ArrayList<ArrayList<String>> getSubjectAlternativeNames() {

        return subjectAlternativeNames;
    }


    @SuppressWarnings("WeakerAccess")
    public static String getSan (int oid) {

        return san.getSanOid (oid);
    }

} // class


