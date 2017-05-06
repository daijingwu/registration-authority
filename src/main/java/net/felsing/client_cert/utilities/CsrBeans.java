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
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.HashMap;
import org.apache.shiro.SecurityUtils;


public final class CsrBeans {
    private static final Logger logger = LoggerFactory.getLogger(CsrBeans.class);
    private static HashMap<String, String> labels;


    /*
    public static String getAttr(String k) {
        if (labels==null) {
            CertificateAttributes certificateAttributes = new CertificateAttributes();
            labels = certificateAttributes.getLabels();
        }
        return labels.get(k);
    }
    */

    /*
    public static ArrayList<String> getAttributeList() {
        ArrayList<String> arrayList=new ArrayList<>();
        labels.forEach((k,v)->{
            arrayList.add(k);
        });
        return arrayList;
    }
    */

    public String getForm() {
        if (labels==null) {
            CertificateAttributes certificateAttributes = new CertificateAttributes();
            labels = certificateAttributes.getLabels();
        }
        StringBuilder form = new StringBuilder();
        labels.forEach((k, v) -> {
            v=v.replaceAll("\"","");
            switch (k) {
                case "c":
                    form.append("<div id=\"").append(k).append("_group\" class=\"input_group\">\n");
                    form.append("  <label for=\"").append(k).append("\">").append(v).append("</label>\n");
                    form.append("  <select id=\"c\"></select>\n");
                    form.append("</div>\n");
                    break;
                case "st":
                    form.append("<div id=\"").append(k).append("_group\" class=\"input_group\">\n");
                    form.append("  <label for=\"").append(k).append("\">").append(v).append("</label>\n");
                    form.append("  <select id=\"st\"></select>\n");
                    form.append("</div>\n");
                    break;
                default:
                    form.append("<div id=\"").append(k).append("_group\" class=\"input_group\">\n");
                    form.append("  <label for=\"").append(k).append("\">").append(v).append("</label>\n");
                    form.append("  <input type=\"text\" id=\"").append(k).append("\"/>\n");
                    form.append("</div>\n");
                    break;
            }
        });

        return form.toString();
    }


    public String getFormSchema() {
        String schemaFile=PropertyLoader.getProperties().getProperty(Constants.schemaFileName);
        JsonParser jsonParser=new JsonParser();
        try {
            JsonObject jsonObject=jsonParser.parse(new JsonReader(new FileReader(schemaFile))).getAsJsonObject();
            return jsonObject.toString();
        } catch (FileNotFoundException e) {
            logger.error("Cannot open file: "+e.getMessage());
            return null;
        }
    }
    
    
    public boolean getLoginStatus () {
        return PropertyLoader.getProperties().getProperty(Constants.propertyAuthRequired)==null || 
                SecurityUtils.getSubject().isAuthenticated();

    }


    public String getLoginName () {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return SecurityUtils.getSubject().getPrincipal().toString();
        } else {
            return "anonymous";
        }
    }


    public String getTitle() {

        return PropertyLoader.getProperties().getProperty(Constants.title);
    }

} // class
