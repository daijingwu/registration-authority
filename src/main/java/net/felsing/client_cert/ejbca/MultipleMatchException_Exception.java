
package net.felsing.client_cert.ejbca;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.2
 * 2015-08-25T09:25:39.381+02:00
 * Generated source version: 3.1.2
 */

@WebFault(name = "MultipleMatchException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class MultipleMatchException_Exception extends Exception {
    
    private net.felsing.client_cert.ejbca.MultipleMatchException multipleMatchException;

    public MultipleMatchException_Exception() {
        super();
    }
    
    public MultipleMatchException_Exception(String message) {
        super(message);
    }
    
    public MultipleMatchException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleMatchException_Exception(String message, net.felsing.client_cert.ejbca.MultipleMatchException multipleMatchException) {
        super(message);
        this.multipleMatchException = multipleMatchException;
    }

    public MultipleMatchException_Exception(String message, net.felsing.client_cert.ejbca.MultipleMatchException multipleMatchException, Throwable cause) {
        super(message, cause);
        this.multipleMatchException = multipleMatchException;
    }

    public net.felsing.client_cert.ejbca.MultipleMatchException getFaultInfo() {
        return this.multipleMatchException;
    }
}
