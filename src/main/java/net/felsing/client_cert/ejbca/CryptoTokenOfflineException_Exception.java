
package net.felsing.client_cert.ejbca;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.2
 * 2015-08-25T09:25:39.354+02:00
 * Generated source version: 3.1.2
 */

@WebFault(name = "CryptoTokenOfflineException", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class CryptoTokenOfflineException_Exception extends Exception {
    
    private net.felsing.client_cert.ejbca.CryptoTokenOfflineException cryptoTokenOfflineException;

    public CryptoTokenOfflineException_Exception() {
        super();
    }
    
    public CryptoTokenOfflineException_Exception(String message) {
        super(message);
    }
    
    public CryptoTokenOfflineException_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptoTokenOfflineException_Exception(String message, net.felsing.client_cert.ejbca.CryptoTokenOfflineException cryptoTokenOfflineException) {
        super(message);
        this.cryptoTokenOfflineException = cryptoTokenOfflineException;
    }

    public CryptoTokenOfflineException_Exception(String message, net.felsing.client_cert.ejbca.CryptoTokenOfflineException cryptoTokenOfflineException, Throwable cause) {
        super(message, cause);
        this.cryptoTokenOfflineException = cryptoTokenOfflineException;
    }

    public net.felsing.client_cert.ejbca.CryptoTokenOfflineException getFaultInfo() {
        return this.cryptoTokenOfflineException;
    }
}
