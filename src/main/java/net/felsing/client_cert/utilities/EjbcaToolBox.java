package net.felsing.client_cert.utilities;

import com.google.gson.JsonObject;
import net.felsing.client_cert.ejbca.*;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;


/**
 * Created by cf on 25.08.15.
 *
 * EJBCA Interface Class
 */

public class EjbcaToolBox {
    private static final Logger logger = LogManager.getLogger(EjbcaToolBox.class);

    private EjbcaWS port;
    private Properties properties;
    private static final QName SERVICE_NAME = new QName("http://ws.protocol.core.ejbca.org/", "EjbcaWSService");


    public EjbcaToolBox(Properties properties) {
        this.properties = properties;

        // proxy stuff
        String proxyHost = properties.getProperty(Constants.proxyHost, null);
        String proxyPort = properties.getProperty(Constants.proxyPort, null);
        if ((proxyHost != null) && (proxyPort != null)) {
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", proxyPort);
        }
        String proxyUser = properties.getProperty(Constants.proxyUser, null);
        String proxyPassword = properties.getProperty(Constants.proxyPassword, null);
        if (proxyUser != null) {
            System.setProperty("http.proxyUser", proxyUser);
        }
        if (proxyPassword != null) {
            System.setProperty("http.proxyPassword", proxyPassword);
        }

        URL wsdlURL = EjbcaWSService.WSDL_LOCATION;
        try {
            URL newWsdlURL = new URL (properties.getProperty("wsdlLocationUrl"));
            File wsdlFile=new File(newWsdlURL.getFile());
            if (!wsdlFile.canRead()) throw new FileNotFoundException("File "+wsdlFile.getAbsolutePath()+" not found");
            wsdlURL =newWsdlURL;
        } catch (MalformedURLException e) {
            logger.warn("Not an URL: "+e.getMessage());
        } catch (FileNotFoundException e) {
            logger.warn("WSDL not found on specified location: "+e.getMessage());
        }
        EjbcaWSService ss = new EjbcaWSService(wsdlURL, SERVICE_NAME);
        port = ss.getEjbcaWSPort();

        Client client = ClientProxy.getClient(port);
        HTTPConduit http = (HTTPConduit) client.getConduit();
        try {
            TLS_Utils.initializeConduitForSSL(properties);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        http.setTlsClientParameters(TLS_Utils.getTlsParams());

    }


    public JsonObject getAvailableCas() {
        JsonObject jsonObject = new JsonObject();
        try {
            java.util.List<NameAndId> _getAvailableCAs__return = port.getAvailableCAs();
            _getAvailableCAs__return.forEach((k) -> {
                int id = k.getId();
                String name = k.getName();
                jsonObject.addProperty(Integer.toString(id), name);
            });

        } catch (AuthorizationDeniedException_Exception e) {
            logger.warn("Expected exception: AuthorizationDeniedException has occurred: "+e.getMessage());
        } catch (EjbcaException_Exception e) {
            logger.warn("Expected exception: EjbcaException has occurred: "+e.getMessage());
        }

        return jsonObject;
    }


    public JsonObject ejbcaFindUser(String username) {

        UserMatch userMatch = new UserMatch();
        userMatch.setMatchtype(0);
        userMatch.setMatchvalue(username);
        userMatch.setMatchwith(0);

        JsonObject jsonObject = new JsonObject();
        int count;
        try {
            List<UserDataVOWS> found = port.findUser(userMatch);
            count = found.size();
            jsonObject.addProperty("found", Integer.toString(count));
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("result", "failed");
        }

        return jsonObject;
    }


    public JsonObject ejbcaEditUser(String username,
                                    String password,
                                    String email
    ) {


        UserDataVOWS userDataVOWS = new UserDataVOWS();
        userDataVOWS.setCaName(properties.getProperty("caName"));
        userDataVOWS.setCertificateProfileName(properties.getProperty("CertificateProfileName"));
        userDataVOWS.setEmail(email);
        userDataVOWS.setEndEntityProfileName(properties.getProperty("EndEntityProfileName"));
        userDataVOWS.setPassword(password);
        userDataVOWS.setStatus(10);
        userDataVOWS.setSubjectAltName("rfc822name=" + email);
        userDataVOWS.setSubjectDN("CN=" + username + ",C=DE");
        userDataVOWS.setTokenType("USERGENERATED");
        userDataVOWS.setUsername(username);

        JsonObject jsonObject = new JsonObject();
        try {
            port.editUser(userDataVOWS);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.addProperty("result", "failed");
        }

        return jsonObject;
    }


    public String ejbcaCertificateRequest(String username, String password, String pkcs10req, String email, String c) {

        String ou = properties.getProperty("ou");
        String o = properties.getProperty("o");

        UserDataVOWS userDataVOWS = new UserDataVOWS();
        userDataVOWS.setCaName(properties.getProperty("caName"));
        userDataVOWS.setCertificateProfileName(properties.getProperty("CertificateProfileName"));
        userDataVOWS.setEmail(email);
        userDataVOWS.setEndEntityProfileName(properties.getProperty("EndEntityProfileName"));
        userDataVOWS.setPassword(password);
        userDataVOWS.setStatus(10);
        userDataVOWS.setSubjectAltName("rfc822name=" + email);
        userDataVOWS.setSubjectDN(
                "CN=" + username
                        + ",EMAIL=" + email
                        // + ",UID=" + username
                        + ",OU=" + ou
                        + ",O=" + o
                        + ",C=" + c
        );
        userDataVOWS.setTokenType("USERGENERATED");
        userDataVOWS.setUsername(username);

        final String hardTokenSn = null; // no hard token
        final String responseType = "CERTIFICATE";
        String pem = null;
        try {
            CertificateResponse crt = port.certificateRequest(userDataVOWS, pkcs10req, 0, hardTokenSn, responseType);
            String pemCertificate = new String(crt.getData());
            pem = "-----BEGIN CERTIFICATE-----\n"
                    + pemCertificate + "\n"
                    + "-----END CERTIFICATE-----";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pem;
    }


} // class
