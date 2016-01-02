package net.felsing.client_cert.utilities;


import org.apache.cxf.common.i18n.Exception;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.bind.DatatypeConverter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;


public class CertificateFabric {
    private static final Logger logger = LogManager.getLogger(CertificateFabric.class);


    public static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter)
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
            e.printStackTrace();
            logger.warn("getReqData IO fucked up");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("getReqData general fuck up");
            return null;
        }
    }


    public static HashMap<String,String> getAttributes (String subject) {
        HashMap<String,String> hashMap=new HashMap<>();
        String[] subjectArr=subject.split(",");
        for (String item : subjectArr) {
            String[] attrArr=item.split("=");
            hashMap.put(attrArr[0].toLowerCase(),attrArr[1]);
        }
        return hashMap;
    }


} // class


