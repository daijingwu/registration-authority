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
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.HashMap;
import java.util.Properties;

import org.apache.shiro.SecurityUtils;




public final class CsrBeans {
    private static final Logger logger = LoggerFactory.getLogger(CsrBeans.class);
    private static HashMap<String, String> labels;
    private Properties properties = null;


    public CsrBeans () {
        if (properties==null) {
            properties = PropertyLoader.getProperties();
        }
    }


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
        String schemaFile=properties.getProperty(Constants.schemaFileName);
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
        if (properties.getProperty(Constants.propertyAuthRequired)==null) return true;

        Subject subject = SecurityUtils.getSubject();
        boolean sufficientRole = true;
        String raRole = properties.getProperty(Constants.propertyRaGroup);
        if (raRole!=null) {
            sufficientRole = subject.hasRole(raRole);
        }
        return subject.isAuthenticated() && sufficientRole;
    }


    public String getLoginName () {
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return SecurityUtils.getSubject().getPrincipal().toString();
        } else {
            return "anonymous";
        }
    }


    public String getTitle() {

        return properties.getProperty(Constants.title);
    }


    public String getJsConfiguration() {
        JsonObject localJsonObject = PropertyLoader.getJavaScriptProperties();
        localJsonObject.add("downloadName", new JsonPrimitive(getLoginName()));

        return localJsonObject.toString();
    }

} // class
