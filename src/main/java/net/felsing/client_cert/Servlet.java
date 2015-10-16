package net.felsing.client_cert;

import net.felsing.client_cert.ejbca.*;
import net.felsing.client_cert.utilities.EjbcaToolBox;
import net.felsing.client_cert.utilities.LoadProperties;
import net.felsing.client_cert.utilities.TLS_Utils;
import org.json.simple.JSONObject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Properties;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import javax.xml.namespace.QName;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;




/**
 * Created by cf on 23.08.15.
 */

@WebServlet(urlPatterns = "/req")
public class Servlet extends HttpServlet {

    private boolean servletIsReady=false;
    private static Properties properties = null;


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!servletIsReady) return;

        StringBuffer sb=new StringBuffer();
        String line=null;
        try {
            BufferedReader bufferedReader = req.getReader();
            while ((line=bufferedReader.readLine())!=null) {
                sb.append(line+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter pw=resp.getWriter();
        EjbcaToolBox ejbcaToolBox=new EjbcaToolBox(properties);

        //String caList=ejbcaToolBox.getAvailableCas().toJSONString();
        //pw.print(caList);

        String pem=ejbcaToolBox.ejbcaCertificateRequest("JoeTest","geheim1234567",sb.toString(),"joe.test@felsing.net");
        pw.print(pem);

        pw.flush();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!servletIsReady) return;
        PrintWriter pw=resp.getWriter();

        pw.println("Hello world!");
        pw.flush();
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String realPath=getServletContext().getRealPath("/WEB-INF/classes/client-cert.xml");
        properties= LoadProperties.load(realPath);
        String fileName;
        fileName=properties.getProperty("trustStoreFile");
        properties.setProperty("trustStoreFile",getServletContext().getRealPath("/WEB-INF/classes/"+fileName));
        fileName=properties.getProperty("keyStoreFile");
        properties.setProperty("keyStoreFile",getServletContext().getRealPath("/WEB-INF/classes/"+fileName));

        servletIsReady=true;
    }

}  //class
