
package net.felsing.client_cert.ejbca;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für userDataVOWS complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="userDataVOWS"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="caName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="certificateProfileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="certificateSerialNumber" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/&gt;
 *         &lt;element name="clearPwd" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="endEntityProfileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="extendedInformation" type="{http://ws.protocol.core.ejbca.org/}extendedInformationWS" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="hardTokenIssuerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="keyRecoverable" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sendNotification" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="subjectAltName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="subjectDN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tokenType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userDataVOWS", propOrder = {
    "caName",
    "cardNumber",
    "certificateProfileName",
    "certificateSerialNumber",
    "clearPwd",
    "email",
    "endEntityProfileName",
    "endTime",
    "extendedInformation",
    "hardTokenIssuerName",
    "keyRecoverable",
    "password",
    "sendNotification",
    "startTime",
    "status",
    "subjectAltName",
    "subjectDN",
    "tokenType",
    "username"
})
public class UserDataVOWS {

    protected String caName;
    protected String cardNumber;
    protected String certificateProfileName;
    protected BigInteger certificateSerialNumber;
    protected boolean clearPwd;
    protected String email;
    protected String endEntityProfileName;
    protected String endTime;
    @XmlElement(nillable = true)
    protected List<ExtendedInformationWS> extendedInformation;
    protected String hardTokenIssuerName;
    protected boolean keyRecoverable;
    protected String password;
    protected boolean sendNotification;
    protected String startTime;
    protected int status;
    protected String subjectAltName;
    protected String subjectDN;
    protected String tokenType;
    protected String username;

    /**
     * Ruft den Wert der caName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaName() {
        return caName;
    }

    /**
     * Legt den Wert der caName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaName(String value) {
        this.caName = value;
    }

    /**
     * Ruft den Wert der cardNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Legt den Wert der cardNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardNumber(String value) {
        this.cardNumber = value;
    }

    /**
     * Ruft den Wert der certificateProfileName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificateProfileName() {
        return certificateProfileName;
    }

    /**
     * Legt den Wert der certificateProfileName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificateProfileName(String value) {
        this.certificateProfileName = value;
    }

    /**
     * Ruft den Wert der certificateSerialNumber-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCertificateSerialNumber() {
        return certificateSerialNumber;
    }

    /**
     * Legt den Wert der certificateSerialNumber-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCertificateSerialNumber(BigInteger value) {
        this.certificateSerialNumber = value;
    }

    /**
     * Ruft den Wert der clearPwd-Eigenschaft ab.
     * 
     */
    public boolean isClearPwd() {
        return clearPwd;
    }

    /**
     * Legt den Wert der clearPwd-Eigenschaft fest.
     * 
     */
    public void setClearPwd(boolean value) {
        this.clearPwd = value;
    }

    /**
     * Ruft den Wert der email-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Legt den Wert der email-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Ruft den Wert der endEntityProfileName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndEntityProfileName() {
        return endEntityProfileName;
    }

    /**
     * Legt den Wert der endEntityProfileName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndEntityProfileName(String value) {
        this.endEntityProfileName = value;
    }

    /**
     * Ruft den Wert der endTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Legt den Wert der endTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndTime(String value) {
        this.endTime = value;
    }

    /**
     * Gets the value of the extendedInformation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extendedInformation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtendedInformation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtendedInformationWS }
     * 
     * 
     */
    public List<ExtendedInformationWS> getExtendedInformation() {
        if (extendedInformation == null) {
            extendedInformation = new ArrayList<ExtendedInformationWS>();
        }
        return this.extendedInformation;
    }

    /**
     * Ruft den Wert der hardTokenIssuerName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHardTokenIssuerName() {
        return hardTokenIssuerName;
    }

    /**
     * Legt den Wert der hardTokenIssuerName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHardTokenIssuerName(String value) {
        this.hardTokenIssuerName = value;
    }

    /**
     * Ruft den Wert der keyRecoverable-Eigenschaft ab.
     * 
     */
    public boolean isKeyRecoverable() {
        return keyRecoverable;
    }

    /**
     * Legt den Wert der keyRecoverable-Eigenschaft fest.
     * 
     */
    public void setKeyRecoverable(boolean value) {
        this.keyRecoverable = value;
    }

    /**
     * Ruft den Wert der password-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Legt den Wert der password-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Ruft den Wert der sendNotification-Eigenschaft ab.
     * 
     */
    public boolean isSendNotification() {
        return sendNotification;
    }

    /**
     * Legt den Wert der sendNotification-Eigenschaft fest.
     * 
     */
    public void setSendNotification(boolean value) {
        this.sendNotification = value;
    }

    /**
     * Ruft den Wert der startTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Legt den Wert der startTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartTime(String value) {
        this.startTime = value;
    }

    /**
     * Ruft den Wert der status-Eigenschaft ab.
     * 
     */
    public int getStatus() {
        return status;
    }

    /**
     * Legt den Wert der status-Eigenschaft fest.
     * 
     */
    public void setStatus(int value) {
        this.status = value;
    }

    /**
     * Ruft den Wert der subjectAltName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubjectAltName() {
        return subjectAltName;
    }

    /**
     * Legt den Wert der subjectAltName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubjectAltName(String value) {
        this.subjectAltName = value;
    }

    /**
     * Ruft den Wert der subjectDN-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubjectDN() {
        return subjectDN;
    }

    /**
     * Legt den Wert der subjectDN-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubjectDN(String value) {
        this.subjectDN = value;
    }

    /**
     * Ruft den Wert der tokenType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Legt den Wert der tokenType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTokenType(String value) {
        this.tokenType = value;
    }

    /**
     * Ruft den Wert der username-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Legt den Wert der username-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

}
