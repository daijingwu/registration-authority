
package net.felsing.client_cert.ejbca;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für tokenCertificateResponseWS complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="tokenCertificateResponseWS"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="certificate" type="{http://ws.protocol.core.ejbca.org/}certificate" minOccurs="0"/&gt;
 *         &lt;element name="keyStore" type="{http://ws.protocol.core.ejbca.org/}keyStore" minOccurs="0"/&gt;
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tokenCertificateResponseWS", propOrder = {
    "certificate",
    "keyStore",
    "type"
})
@XmlSeeAlso({
    Certificate.class,
    KeyStore.class
})
public class TokenCertificateResponseWS {

    protected Certificate certificate;
    protected KeyStore keyStore;
    protected int type;

    /**
     * Ruft den Wert der certificate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Certificate }
     *     
     */
    public Certificate getCertificate() {
        return certificate;
    }

    /**
     * Legt den Wert der certificate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Certificate }
     *     
     */
    public void setCertificate(Certificate value) {
        this.certificate = value;
    }

    /**
     * Ruft den Wert der keyStore-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link KeyStore }
     *     
     */
    public KeyStore getKeyStore() {
        return keyStore;
    }

    /**
     * Legt den Wert der keyStore-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyStore }
     *     
     */
    public void setKeyStore(KeyStore value) {
        this.keyStore = value;
    }

    /**
     * Ruft den Wert der type-Eigenschaft ab.
     * 
     */
    public int getType() {
        return type;
    }

    /**
     * Legt den Wert der type-Eigenschaft fest.
     * 
     */
    public void setType(int value) {
        this.type = value;
    }

}
