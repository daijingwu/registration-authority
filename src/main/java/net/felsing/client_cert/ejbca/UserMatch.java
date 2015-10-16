
package net.felsing.client_cert.ejbca;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für userMatch complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="userMatch"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="matchtype" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="matchvalue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="matchwith" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userMatch", propOrder = {
    "matchtype",
    "matchvalue",
    "matchwith"
})
public class UserMatch {

    protected int matchtype;
    protected String matchvalue;
    protected int matchwith;

    /**
     * Ruft den Wert der matchtype-Eigenschaft ab.
     * 
     */
    public int getMatchtype() {
        return matchtype;
    }

    /**
     * Legt den Wert der matchtype-Eigenschaft fest.
     * 
     */
    public void setMatchtype(int value) {
        this.matchtype = value;
    }

    /**
     * Ruft den Wert der matchvalue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatchvalue() {
        return matchvalue;
    }

    /**
     * Legt den Wert der matchvalue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatchvalue(String value) {
        this.matchvalue = value;
    }

    /**
     * Ruft den Wert der matchwith-Eigenschaft ab.
     * 
     */
    public int getMatchwith() {
        return matchwith;
    }

    /**
     * Legt den Wert der matchwith-Eigenschaft fest.
     * 
     */
    public void setMatchwith(int value) {
        this.matchwith = value;
    }

}
