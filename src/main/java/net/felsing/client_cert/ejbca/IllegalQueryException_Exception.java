
package net.felsing.client_cert.ejbca;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.2
 * 2015-08-25T09:25:39.360+02:00
 * Generated source version: 3.1.2
 */

@WebFault(name = "IllegalQueryException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class IllegalQueryException_Exception extends Exception {
    
    private net.felsing.client_cert.ejbca.IllegalQueryException illegalQueryException;

    public IllegalQueryException_Exception() {
        super();
    }
    
    public IllegalQueryException_Exception(String message) {
        super(message);
    }
    
    public IllegalQueryException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalQueryException_Exception(String message, net.felsing.client_cert.ejbca.IllegalQueryException illegalQueryException) {
        super(message);
        this.illegalQueryException = illegalQueryException;
    }

    public IllegalQueryException_Exception(String message, net.felsing.client_cert.ejbca.IllegalQueryException illegalQueryException, Throwable cause) {
        super(message, cause);
        this.illegalQueryException = illegalQueryException;
    }

    public net.felsing.client_cert.ejbca.IllegalQueryException getFaultInfo() {
        return this.illegalQueryException;
    }
}