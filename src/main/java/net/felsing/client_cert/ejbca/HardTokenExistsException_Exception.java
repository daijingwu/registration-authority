
package net.felsing.client_cert.ejbca;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.2
 * 2017-05-15T16:18:08.019+02:00
 * Generated source version: 3.1.2
 */

@WebFault(name = "HardTokenExistsException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class HardTokenExistsException_Exception extends Exception {
    
    private net.felsing.client_cert.ejbca.HardTokenExistsException hardTokenExistsException;

    public HardTokenExistsException_Exception() {
        super();
    }
    
    public HardTokenExistsException_Exception(String message) {
        super(message);
    }
    
    public HardTokenExistsException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public HardTokenExistsException_Exception(String message, net.felsing.client_cert.ejbca.HardTokenExistsException hardTokenExistsException) {
        super(message);
        this.hardTokenExistsException = hardTokenExistsException;
    }

    public HardTokenExistsException_Exception(String message, net.felsing.client_cert.ejbca.HardTokenExistsException hardTokenExistsException, Throwable cause) {
        super(message, cause);
        this.hardTokenExistsException = hardTokenExistsException;
    }

    public net.felsing.client_cert.ejbca.HardTokenExistsException getFaultInfo() {
        return this.hardTokenExistsException;
    }
}
