
package net.felsing.client_cert.ejbca;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.2
 * 2015-08-25T09:25:39.333+02:00
 * Generated source version: 3.1.2
 */

@WebFault(name = "EjbcaException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class EjbcaException_Exception extends Exception {
    
    private net.felsing.client_cert.ejbca.EjbcaException ejbcaException;

    public EjbcaException_Exception() {
        super();
    }
    
    public EjbcaException_Exception(String message) {
        super(message);
    }
    
    public EjbcaException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public EjbcaException_Exception(String message, net.felsing.client_cert.ejbca.EjbcaException ejbcaException) {
        super(message);
        this.ejbcaException = ejbcaException;
    }

    public EjbcaException_Exception(String message, net.felsing.client_cert.ejbca.EjbcaException ejbcaException, Throwable cause) {
        super(message, cause);
        this.ejbcaException = ejbcaException;
    }

    public net.felsing.client_cert.ejbca.EjbcaException getFaultInfo() {
        return this.ejbcaException;
    }
}
