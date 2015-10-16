
package net.felsing.client_cert.ejbca;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.2
 * 2015-08-25T09:25:39.378+02:00
 * Generated source version: 3.1.2
 */

@WebFault(name = "DateNotValidException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class DateNotValidException_Exception extends Exception {
    
    private net.felsing.client_cert.ejbca.DateNotValidException dateNotValidException;

    public DateNotValidException_Exception() {
        super();
    }
    
    public DateNotValidException_Exception(String message) {
        super(message);
    }
    
    public DateNotValidException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public DateNotValidException_Exception(String message, net.felsing.client_cert.ejbca.DateNotValidException dateNotValidException) {
        super(message);
        this.dateNotValidException = dateNotValidException;
    }

    public DateNotValidException_Exception(String message, net.felsing.client_cert.ejbca.DateNotValidException dateNotValidException, Throwable cause) {
        super(message, cause);
        this.dateNotValidException = dateNotValidException;
    }

    public net.felsing.client_cert.ejbca.DateNotValidException getFaultInfo() {
        return this.dateNotValidException;
    }
}
