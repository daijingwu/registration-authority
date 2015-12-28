package net.felsing.client_cert.utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by cf on 28.12.15.
 *
 */

public class JsonProcessor {
    private static final Logger logger = LogManager.getLogger(JsonProcessor.class);


    public String getCertificate(String req) {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(req).getAsJsonObject();
            String reqString = jsonObject.get("pkcs10").toString();

            reqString = reqString.replaceAll("\\\\r", "");
            reqString = reqString.replaceAll("\\\\n", "\n");
            reqString = reqString.replaceAll("\"", "");

            return reqString;
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            return null;
        }
    }


    public String getSubject(String req) {
        CertificateFabric certificateFabric = new CertificateFabric();
        return certificateFabric.getReqData(getCertificate(req));
    }

} // class

