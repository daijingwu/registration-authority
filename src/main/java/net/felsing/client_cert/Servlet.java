package net.felsing.client_cert;

import net.felsing.client_cert.utilities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
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

        StringBuffer sb = new StringBuffer();
        String line;
        try {
            BufferedReader bufferedReader = req.getReader();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
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

        attributes.forEach((k,v)->{
            System.out.println("key: "+k+", value: "+v);
        });

        EjbcaToolBox ejbcaToolBox = new EjbcaToolBox(properties);
        String cn=attributes.get("cn");
        String email="joe.test@example.com";
        try {
            email = attributes.get("e_");
        } catch (NullPointerException e) {
            logger.warn("E-Mail ist null");
        }
        String password=Utilities.generatePassword();
        String pem=ejbcaToolBox.ejbcaCertificateRequest("Joe Test",password,pkcs10req,"joe.test@example.net");
        pw.print(pem);

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
        String realPath = getServletContext().getRealPath("/WEB-INF/classes/client-cert.xml");
        properties = LoadProperties.load(realPath);
        String fileName;
        assert properties != null;
        fileName = properties.getProperty(Constants.trustStoreFile);
        properties.setProperty(Constants.trustStoreFile, getServletContext().getRealPath("/WEB-INF/classes/" + fileName));
        fileName = properties.getProperty(Constants.keyStoreFile);
        properties.setProperty(Constants.keyStoreFile, getServletContext().getRealPath("/WEB-INF/classes/" + fileName));

        servletIsReady = true;
    }


    public boolean validateReq(String req) {
        if (req.length() > Constants.maxReqLength) {
            return false;
        }

        return true;
    }


}  //class
