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


import org.apache.cxf.common.i18n.Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import javax.xml.bind.DatatypeConverter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;


public class CertificateFabric {
    private static final Logger logger = LoggerFactory.getLogger(CertificateFabric.class);


    private static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter)
            throws Exception {
        String data = new String(pem);
        String[] tokens = data.split(beginDelimiter);
        tokens = tokens[1].split(endDelimiter);
        return DatatypeConverter.parseBase64Binary(tokens[0]);
    }


    public String getReqData(String pkcs10string) {
        pkcs10string=pkcs10string.trim();
        try {
            byte[] reqBytes;
            reqBytes = CertificateFabric.parseDERFromPEM(pkcs10string.getBytes(),
                    "-----BEGIN CERTIFICATE REQUEST-----",
                    "-----END CERTIFICATE REQUEST-----");
            PKCS10CertificationRequest pkcs10CertificationRequest =
                    new PKCS10CertificationRequest(reqBytes);
            return pkcs10CertificationRequest.getSubject().toString().replaceAll("\\+",",");
        } catch (IOException e) {
            logger.warn("getReqData IO fucked up");
            return null;
        } catch (Exception e) {
            logger.warn("general fuck up");
            return null;
        }
    }


    static HashMap<String,String> getAttributes(String subject) {
        HashMap<String,String> hashMap=new HashMap<>();
        String[] subjectArr=subject.split(",");
        for (String item : subjectArr) {
            String[] attrArr=item.split("=");
            hashMap.put(attrArr[0].toLowerCase(),attrArr[1]);
        }
        return hashMap;
    }


} // class


