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
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.*;


public final class CsrBeans {
    private static final Logger logger = LoggerFactory.getLogger(CsrBeans.class);
    private static HashMap<String, String> labels;
    private Properties properties = null;
    private ResourceBundle bundle = null;


    public CsrBeans () {
        if (properties==null) {
            properties = PropertyLoader.getProperties();
        }
    }


    public String getLang (String strLocale) {
        final String acceptLanguage = StringEscapeUtils.escapeJava(strLocale);
        if (bundle==null) {
            List<Locale.LanguageRange> languageRanges;
            try {
                languageRanges = Locale.LanguageRange.parse(acceptLanguage);
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
                }
            }
        }

        logger.debug("getLang: " + bundle.getLocale().getLanguage());
        return "";
    }


    private void addCsrOptions(StringBuilder form, String what) {
        if (properties.getProperty(what)!=null) {
            String allowedOptions[] = properties.getProperty(what).split("\\s+");

            form.append("<div id=\"").append(what).append("_group\" >\n");
            form.append("  <label class=\"left\" for=\"").append(what).append("\">").append(bundle.getString("lbl_" + what)).append("</label>\n");
            form.append("  <span class=\"left2\"><select id=\"").append(what).append("\">\n");
            for (String item: allowedOptions) {
                form.append("<option value=\"").append(item).append("\">").append(bundle.getString(item)).append("</option>\n");
            }
            form.append("</select></span>\n");
            form.append("</div>\n");
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
                    form.append("<div id=\"").append(k).append("_group\" >\n");
                    form.append("  <label class=\"left\" for=\"").append(k).append("\">").append(v).append("</label>\n");
                    form.append("  <span class=\"left2\"><select id=\"c\"></select></span>\n");
                    form.append("</div>\n");
                    break;
                case "st":
                    form.append("<div id=\"").append(k).append("_group\" >\n");
                    form.append("  <label class=\"left\" for=\"").append(k).append("\">").append(v).append("</label>\n");
                    form.append("  <span class=\"left2\"><select id=\"st\"></select></span>\n");
                    form.append("</div>\n");
                    break;
                default:
                    form.append("<div id=\"").append(k).append("_group\" >\n");
                    form.append("  <label class=\"left\" for=\"").append(k).append("\">").append(v).append("</label>\n");
                    form.append("  <span class=\"left2\"><input type=\"text\" id=\"").append(k).append("\"/></span>\n");
                    form.append("</div>\n");
                    break;
            }
        });

        addCsrOptions(form, "hashes");
        addCsrOptions(form, "sign");

        return form.toString();
    }

    
    public boolean getLoginStatus () {
        if (properties.getProperty(Constants.propertyAuthRequired)==null) return true;

        try {
            Subject subject = SecurityUtils.getSubject();
            boolean sufficientRole = true;
            String raRole = properties.getProperty(Constants.propertyRaGroup);
            if (raRole != null) {
                sufficientRole = subject.hasRole(raRole);
            }
            return subject.isAuthenticated() && sufficientRole;
        } catch (UnknownSessionException e) {
            logger.debug("Invalid session: " + e.getMessage());
        } catch (Exception e) {
            logger.warn("Bad session", e);
        }

        return false;
    }


    private String getLoginName() {
        String loginName = "anonymous";

        if (SecurityUtils.getSubject().isAuthenticated()) {
            try {
                loginName = SecurityUtils.getSubject().getPrincipal().toString();
            } catch (UnknownSessionException e) {
                logger.debug("Invalid session: " + e.getMessage());
            } catch (Exception e) {
                logger.warn("Bad session", e);
            }
        }

        return loginName;
    }


    public boolean getAuthenticationEnabled () {

        return properties.getProperty(Constants.propertyAuthRequired)!=null;
    }


    public String getTitle() {

        return properties.getProperty(Constants.title);
    }


    public String bundleEntry (String key) {
        //loadLocale(lang);

        try {
            return bundle.getString(key);
        } catch (NullPointerException e) {
            return "UNDEFINED";
        }
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

        assert (bundle!=null);
        JsonObject jsonBundle = new JsonObject();
        for (String key: bundle.keySet()) {
            jsonBundle.add(key, new JsonPrimitive(bundle.getString(key)));
        }
        localJsonObject.add("bundle", jsonBundle);

        return localJsonObject.toString();
    }


    public static void log (int level, String msg) {
        switch (level) {
            case 0: logger.error(msg); break;
            case 1: logger.warn(msg); break;
            case 2: logger.info(msg); break;
            default: logger.debug(msg); break;
        }
    }


    public String getDummyLogin () {
        if (PropertyLoader.getProperties().getProperty(Constants.propertyAuthRequired)==null) {
            try {
                if (!SecurityUtils.getSubject().isAuthenticated()) {
                    final String username = properties.getProperty(Constants.propertyDummyUser);
                    final String password = properties.getProperty(Constants.propertyDummyPassword);

                    Subject currentUser = SecurityUtils.getSubject();
                    currentUser.logout();
                    AuthenticationToken token = new UsernamePasswordToken(username, password);
                    currentUser.login(token);
                }
            } catch (Exception e) {
                logger.warn ("dummyLogin: " + e.getMessage());
            }
        }

        return "";
    }

} // class
