
package net.felsing.client_cert.ejbca;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.2
 * 2017-05-15T16:18:08.022+02:00
 * Generated source version: 3.1.2
 */

@WebFault(name = "ApprovalException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class ApprovalException_Exception extends Exception {
    
    private net.felsing.client_cert.ejbca.ApprovalException approvalException;

    public ApprovalException_Exception() {
        super();
    }
    
    public ApprovalException_Exception(String message) {
        super(message);
    }
    
    public ApprovalException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public ApprovalException_Exception(String message, net.felsing.client_cert.ejbca.ApprovalException approvalException) {
        super(message);
        this.approvalException = approvalException;
    }

    public ApprovalException_Exception(String message, net.felsing.client_cert.ejbca.ApprovalException approvalException, Throwable cause) {
        super(message, cause);
        this.approvalException = approvalException;
    }

    public net.felsing.client_cert.ejbca.ApprovalException getFaultInfo() {
        return this.approvalException;
    }
}
