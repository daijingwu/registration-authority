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
import java.util.*;

import org.apache.shiro.SecurityUtils;


public final class CsrBeans {
    private static final Logger logger = LoggerFactory.getLogger(CsrBeans.class);
    private static HashMap<String, String> labels;
    private Properties properties = null;
    private String lang = null;
    private ResourceBundle bundle = null;


    public CsrBeans () {
        if (properties==null) {
            properties = PropertyLoader.getProperties();
        }
    }


    public void setLang (String lang) {

        this.lang = lang;
    }


    private void loadLocale (String strLocale) {
        if (bundle==null) {
            List<Locale.LanguageRange> languageRanges;
            try {
                languageRanges = Locale.LanguageRange.parse(strLocale);
            } catch (Exception e) {
                languageRanges = Locale.LanguageRange.parse("en");
            }
            for (Locale.LanguageRange l : languageRanges) {
                Locale locale = new Locale(l.getRange());
                try {
                    bundle = ResourceBundle.getBundle("text", locale);
                } catch (Exception e) {
                    // ignore
                    logger.info("loadLocale (1): " + e.getMessage());
                }
            }

            if (bundle == null) {
                try {
                    bundle = ResourceBundle.getBundle("text");
                    logger.info("create: " + bundle.getString("create"));
                } catch (Exception e) {
                    logger.info("loadLocale (2): " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }


    public String getForm() {
        if (labels==null) {
            CertificateAttributes certificateAttributes = new CertificateAttributes(bundle);
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


    public boolean getAuthenticationEnabled () {

        return properties.getProperty(Constants.propertyAuthRequired)!=null;
    }


    public String getTitle() {

        return properties.getProperty(Constants.title);
    }


    public String bundleEntry (String key) {

        return bundle.getString(key);
    }


    public String getCleanUp () {
        try {
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            // hopefully really logged out now
        }

        return "";
    }


    public String getJsConfiguration() {
        String schemaFile=properties.getProperty(Constants.schemaFileName);
        JsonObject localJsonObject = PropertyLoader.getJavaScriptProperties();
        localJsonObject.add("downloadName", new JsonPrimitive(getLoginName()));
        localJsonObject.add("loginStatus", new JsonPrimitive(getLoginStatus()));
        localJsonObject.add("loginName", new JsonPrimitive(getLoginName()));
        try {
            JsonParser jsonParser=new JsonParser();
            localJsonObject.add("schema", jsonParser.parse(new JsonReader(new FileReader(schemaFile))).getAsJsonObject());
        } catch (FileNotFoundException e) {
            logger.error("Cannot open file: "+e.getMessage());
        }

        loadLocale(lang);
        assert (bundle!=null);
        JsonObject jsonBundle = new JsonObject();
        for (String key: bundle.keySet()) {
            jsonBundle.add(key, new JsonPrimitive(bundle.getString(key)));
        }
        localJsonObject.add("bundle", jsonBundle);

        return localJsonObject.toString();
    }

} // class
