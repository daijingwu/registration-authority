package net.felsing.client_cert.ejbca;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.2
 * 2017-05-15T16:18:08.045+02:00
 * Generated source version: 3.1.2
 * 
 */
@WebServiceClient(name = "EjbcaWSService", 
                  wsdlLocation = "file:/home/cf/Development/idea-workspace/ejbca-new-ra/src/main/java/net/felsing/client_cert/ejbca/ejbcaws.wsdl",
                  targetNamespace = "http://ws.protocol.core.ejbca.org/") 
public class EjbcaWSService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://ws.protocol.core.ejbca.org/", "EjbcaWSService");
    public final static QName EjbcaWSPort = new QName("http://ws.protocol.core.ejbca.org/", "EjbcaWSPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/home/cf/Development/idea-workspace/ejbca-new-ra/src/main/java/net/felsing/client_cert/ejbca/ejbcaws.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(EjbcaWSService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/home/cf/Development/idea-workspace/ejbca-new-ra/src/main/java/net/felsing/client_cert/ejbca/ejbcaws.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public EjbcaWSService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public EjbcaWSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public EjbcaWSService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public EjbcaWSService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public EjbcaWSService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public EjbcaWSService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns EjbcaWS
     */
    @WebEndpoint(name = "EjbcaWSPort")
    public EjbcaWS getEjbcaWSPort() {
        return super.getPort(EjbcaWSPort, EjbcaWS.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns EjbcaWS
     */
    @WebEndpoint(name = "EjbcaWSPort")
    public EjbcaWS getEjbcaWSPort(WebServiceFeature... features) {
        return super.getPort(EjbcaWSPort, EjbcaWS.class, features);
    }

}
