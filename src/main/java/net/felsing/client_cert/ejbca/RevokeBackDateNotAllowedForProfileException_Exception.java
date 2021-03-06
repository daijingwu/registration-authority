
package net.felsing.client_cert.ejbca;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.2
 * 2017-05-15T16:18:08.026+02:00
 * Generated source version: 3.1.2
 */

@WebFault(name = "RevokeBackDateNotAllowedForProfileException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class RevokeBackDateNotAllowedForProfileException_Exception extends Exception {
    
    private net.felsing.client_cert.ejbca.RevokeBackDateNotAllowedForProfileException revokeBackDateNotAllowedForProfileException;

    public RevokeBackDateNotAllowedForProfileException_Exception() {
        super();
    }
    
    public RevokeBackDateNotAllowedForProfileException_Exception(String message) {
        super(message);
    }
    
    public RevokeBackDateNotAllowedForProfileException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public RevokeBackDateNotAllowedForProfileException_Exception(String message, net.felsing.client_cert.ejbca.RevokeBackDateNotAllowedForProfileException revokeBackDateNotAllowedForProfileException) {
        super(message);
        this.revokeBackDateNotAllowedForProfileException = revokeBackDateNotAllowedForProfileException;
    }

    public RevokeBackDateNotAllowedForProfileException_Exception(String message, net.felsing.client_cert.ejbca.RevokeBackDateNotAllowedForProfileException revokeBackDateNotAllowedForProfileException, Throwable cause) {
        super(message, cause);
        this.revokeBackDateNotAllowedForProfileException = revokeBackDateNotAllowedForProfileException;
    }

    public net.felsing.client_cert.ejbca.RevokeBackDateNotAllowedForProfileException getFaultInfo() {
        return this.revokeBackDateNotAllowedForProfileException;
    }
}
