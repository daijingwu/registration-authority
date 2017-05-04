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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.validator.routines.RegexValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
        String subject;


        try {
            subject = certificateFabric.getReqData(getCertificate(req).replace("œ", "@"));
        } catch (NullPointerException e) {
            subject = null;
        }
        return subject;
    }

} // class
