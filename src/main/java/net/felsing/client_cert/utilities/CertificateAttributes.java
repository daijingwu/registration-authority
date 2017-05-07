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


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CertificateAttributes {
    private static final Logger logger = LoggerFactory.getLogger(EjbcaToolBox.class);
    private HashMap<String,String> attributes=new HashMap<>();
    private HashMap<String,String> labels=new HashMap<>();


    CertificateAttributes(ResourceBundle bundle) {
        String schemaFile=PropertyLoader.getProperties().getProperty(Constants.schemaFileName);
        try {
            JsonParser jsonParser=new JsonParser();
            JsonObject jsonObject=jsonParser.parse(new JsonReader(new FileReader(schemaFile))).getAsJsonObject();

            for (Map.Entry<String,JsonElement> entry : jsonObject.entrySet()) {
                attributes.put(entry.getKey().toLowerCase(),"");
                labels.put(entry.getKey().toLowerCase(), bundle.getString(entry.getValue().toString().replaceAll("\"","")));
            }

        } catch (FileNotFoundException e) {
            logger.error ("File not found: "+e.getMessage());
        } catch (IllegalStateException e) {
            logger.error ("Cannot read JSON file: "+e.getMessage());
        }
    }


    public boolean add (String k,String v) {
        if (attributes.get(k.toLowerCase())!=null) {
            attributes.put(k.toLowerCase(), v);
            return true;
        } else {
            return false;
        }
    }


    HashMap<String,String> getLabels() {

        return labels;
    }

} // class
