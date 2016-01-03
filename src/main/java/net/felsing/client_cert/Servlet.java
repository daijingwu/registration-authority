package net.felsing.client_cert;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.felsing.client_cert.utilities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


/**
 * Created by cf on 23.08.15.
 * <p>
 * Servlet handler
 */


@WebServlet(urlPatterns = "/req")
public class Servlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(Servlet.class);

    private boolean servletIsReady = false;
    private static Properties properties = null;


    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!servletIsReady) return;

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
        String subject = jsonProcessor.getSubject(sb.toString());
        String pkcs10req=jsonProcessor.getCertificate(sb.toString());
        HashMap<String,String> attributes=CertificateFabric.getAttributes(subject);

        EjbcaToolBox ejbcaToolBox = new EjbcaToolBox(properties);
        String cn=attributes.get("cn");
        try {
            String email = attributes.get("e");
            String country = attributes.get("c");

            String password=Utilities.generatePassword();

            JsonArray certChain=new JsonArray();
            String pem=ejbcaToolBox.ejbcaCertificateRequest(cn,password,pkcs10req,email,country);
            certChain.add(new JsonPrimitive(pem));
            ejbcaToolBox.ejbcaGetLastCAChain().forEach((k)->{
                certChain.add(new JsonPrimitive(k));
            });
            JsonObject jsonObject=new JsonObject();
            jsonObject.add("certificateChain",certChain);
            assert pw != null;
            pw.print(jsonObject.toString());
        } catch (NullPointerException e) {
            logger.warn("Parameter missing: "+e.getMessage());
        }

        assert pw!=null;
        pw.flush();
    }


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!servletIsReady) return;
        PrintWriter pw = resp.getWriter();

        pw.println("Hello world!");
        pw.flush();
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        properties=PropertyLoader.getProperties();

        assert properties != null;
        servletIsReady = true;
    }

}  //class
