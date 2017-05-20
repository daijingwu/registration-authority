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

package net.felsing.client_cert;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import net.felsing.client_cert.utilities.CertificateAttributes;
import net.felsing.client_cert.utilities.Constants;
import net.felsing.client_cert.utilities.PropertyLoader;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;


@WebServlet(urlPatterns = "/index",loadOnStartup = 1)
public class Index extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Index.class);
    private HashMap<String, String> labels;
    private ResourceBundle bundle = null;

    private boolean servletIsReady = false;
    private static Properties properties = null;


    @Override
    public void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }


    @Override
    public void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }


    private void handleRequest (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!servletIsReady) return;

        logger.debug("handleRequest (" + req.getMethod() + "), locale: " + req.getLocale().getLanguage());
        try {
            bundle = ResourceBundle.getBundle("text", req.getLocale());
            logger.info("doGet create: " + bundle.getString("create"));
        } catch (Exception e) {
            logger.info("doGet (2): " + e.getMessage());
        }
        req.setAttribute("form", getForm());
        req.setAttribute("authenticationEnabled", getAuthenticationEnabled());
        req.setAttribute("jsConfiguration", getJsConfiguration());
        getDummyLogin();
        req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
    }



    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        properties=PropertyLoader.getProperties();
        assert properties != null;
        servletIsReady = true;
    }


    private String getForm() {
        CertificateAttributes certificateAttributes = new CertificateAttributes(bundle);
        labels = certificateAttributes.getLabels();

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


    private void getDummyLogin() {
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
    }


    private boolean getAuthenticationEnabled() {

        return properties.getProperty(Constants.propertyAuthRequired)!=null;
    }


    private String getJsConfiguration() {
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


    private boolean getLoginStatus() {
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


    public static void log (int level, String msg) {
        switch (level) {
            case 0: logger.error(msg); break;
            case 1: logger.warn(msg); break;
            case 2: logger.info(msg); break;
            default: logger.debug(msg); break;
        }
    }



}  //class
