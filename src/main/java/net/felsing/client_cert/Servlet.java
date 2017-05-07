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


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.felsing.client_cert.utilities.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.SecurityUtils;


@WebServlet(urlPatterns = "/req",loadOnStartup = 1)
public class Servlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Servlet.class);

    private boolean servletIsReady = false;
    private static Properties properties = null;


    private void session () {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        logger.debug ("session: " + session.getAttribute("someKey"));
        logger.debug ("session: " + Long.toString(session.getTimeout()));
    }


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!servletIsReady) return;

        session();
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            BufferedReader bufferedReader = req.getReader();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        PrintWriter pw = null;
        try {
            pw = resp.getWriter();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        JsonProcessor jsonProcessor=new JsonProcessor();
        String pkcs10req=jsonProcessor.getCertificate(sb.toString());

        EjbcaToolBox ejbcaToolBox = new EjbcaToolBox(properties);
        JsonObject jsonObject = new JsonObject();
        try {
            JsonArray certChain=new JsonArray();

            CertificateFabric certificateFabric=new CertificateFabric();
            String subject=certificateFabric.getReqData(pkcs10req);
            if (subject==null) {
                logger.warn("CSR not valid");
                return;
            }

            String pem = ejbcaToolBox.ejbcaCertificateRequest(pkcs10req);
            certChain.add(new JsonPrimitive(pem));
            ejbcaToolBox.ejbcaGetLastCAChain().forEach((k) -> certChain.add(new JsonPrimitive(k)));

            jsonObject.add("certificateChain", certChain);
            assert pw != null;
            pw.print(jsonObject.toString());
        } catch (Exception e) {
            jsonObject.add("error", new JsonPrimitive("invalid request"));
            logger.warn("Parameter missing: "+e.getMessage());
        }

        assert pw!=null;
        pw.flush();
    }


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!servletIsReady) return;

        session();

        Subject currentUser = SecurityUtils.getSubject();
        logger.debug(currentUser.getPrincipal().toString());

        resp.sendRedirect("./");
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        properties=PropertyLoader.getProperties();
        assert properties != null;
        logger.info("Servlet ready for service");
        servletIsReady = true;
    }

}  //class
