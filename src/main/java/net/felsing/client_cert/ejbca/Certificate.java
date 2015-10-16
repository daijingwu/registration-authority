
package net.felsing.client_cert.ejbca;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für certificate complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="certificate"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.protocol.core.ejbca.org/}tokenCertificateResponseWS"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="certificateData" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "certificate", propOrder = {
    "certificateData"
})
public class Certificate
    extends TokenCertificateResponseWS
{

    protected byte[] certificateData;

    /**
     * Ruft den Wert der certificateData-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCertificateData() {
        return certificateData;
    }

    /**
     * Legt den Wert der certificateData-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCertificateData(byte[] value) {
        this.certificateData = value;
    }

}
