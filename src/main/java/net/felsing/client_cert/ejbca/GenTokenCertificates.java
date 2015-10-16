
package net.felsing.client_cert.ejbca;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für genTokenCertificates complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="genTokenCertificates"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="arg0" type="{http://ws.protocol.core.ejbca.org/}userDataVOWS" minOccurs="0"/&gt;
 *         &lt;element name="arg1" type="{http://ws.protocol.core.ejbca.org/}tokenCertificateRequestWS" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="arg2" type="{http://ws.protocol.core.ejbca.org/}hardTokenDataWS" minOccurs="0"/&gt;
 *         &lt;element name="arg3" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="arg4" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "genTokenCertificates", propOrder = {
    "arg0",
    "arg1",
    "arg2",
    "arg3",
    "arg4"
})
public class GenTokenCertificates {

    protected UserDataVOWS arg0;
    protected List<TokenCertificateRequestWS> arg1;
    protected HardTokenDataWS arg2;
    protected boolean arg3;
    protected boolean arg4;

    /**
     * Ruft den Wert der arg0-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link UserDataVOWS }
     *     
     */
    public UserDataVOWS getArg0() {
        return arg0;
    }

    /**
     * Legt den Wert der arg0-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link UserDataVOWS }
     *     
     */
    public void setArg0(UserDataVOWS value) {
        this.arg0 = value;
    }

    /**
     * Gets the value of the arg1 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the arg1 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArg1().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TokenCertificateRequestWS }
     * 
     * 
     */
    public List<TokenCertificateRequestWS> getArg1() {
        if (arg1 == null) {
            arg1 = new ArrayList<TokenCertificateRequestWS>();
        }
        return this.arg1;
    }

    /**
     * Ruft den Wert der arg2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link HardTokenDataWS }
     *     
     */
    public HardTokenDataWS getArg2() {
        return arg2;
    }

    /**
     * Legt den Wert der arg2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link HardTokenDataWS }
     *     
     */
    public void setArg2(HardTokenDataWS value) {
        this.arg2 = value;
    }

    /**
     * Ruft den Wert der arg3-Eigenschaft ab.
     * 
     */
    public boolean isArg3() {
        return arg3;
    }

    /**
     * Legt den Wert der arg3-Eigenschaft fest.
     * 
     */
    public void setArg3(boolean value) {
        this.arg3 = value;
    }

    /**
     * Ruft den Wert der arg4-Eigenschaft ab.
     * 
     */
    public boolean isArg4() {
        return arg4;
    }

    /**
     * Legt den Wert der arg4-Eigenschaft fest.
     * 
     */
    public void setArg4(boolean value) {
        this.arg4 = value;
    }

}
