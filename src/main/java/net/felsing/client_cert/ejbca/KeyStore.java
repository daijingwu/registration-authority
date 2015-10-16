
package net.felsing.client_cert.ejbca;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für keyStore complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="keyStore"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.protocol.core.ejbca.org/}tokenCertificateResponseWS"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="keystoreData" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "keyStore", propOrder = {
    "keystoreData"
})
public class KeyStore
    extends TokenCertificateResponseWS
{

    protected byte[] keystoreData;

    /**
     * Ruft den Wert der keystoreData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getKeystoreData() {
        return keystoreData;
    }

    /**
     * Legt den Wert der keystoreData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setKeystoreData(byte[] value) {
        this.keystoreData = value;
    }

}
