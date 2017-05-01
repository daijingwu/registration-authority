package net.felsing.client_cert;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.felsing.client_cert.utilities.*;
import org.apache.commons.validator.routines.RegexValidator;
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
