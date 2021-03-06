
package net.felsing.client_cert.ejbca;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.2
 * 2017-05-15T16:18:08.021+02:00
 * Generated source version: 3.1.2
 */

@WebFault(name = "UserDoesntFullfillEndEntityProfile", targetNamespace = "http://ws.protocol.core.ejbca.org/")
public class UserDoesntFullfillEndEntityProfile_Exception extends Exception {
    
    private net.felsing.client_cert.ejbca.UserDoesntFullfillEndEntityProfile userDoesntFullfillEndEntityProfile;

    public UserDoesntFullfillEndEntityProfile_Exception() {
        super();
    }
    
    public UserDoesntFullfillEndEntityProfile_Exception(String message) {
        super(message);
    }
    
    public UserDoesntFullfillEndEntityProfile_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesntFullfillEndEntityProfile_Exception(String message, net.felsing.client_cert.ejbca.UserDoesntFullfillEndEntityProfile userDoesntFullfillEndEntityProfile) {
        super(message);
        this.userDoesntFullfillEndEntityProfile = userDoesntFullfillEndEntityProfile;
    }

    public UserDoesntFullfillEndEntityProfile_Exception(String message, net.felsing.client_cert.ejbca.UserDoesntFullfillEndEntityProfile userDoesntFullfillEndEntityProfile, Throwable cause) {
        super(message, cause);
        this.userDoesntFullfillEndEntityProfile = userDoesntFullfillEndEntityProfile;
    }

    public net.felsing.client_cert.ejbca.UserDoesntFullfillEndEntityProfile getFaultInfo() {
        return this.userDoesntFullfillEndEntityProfile;
    }
}
