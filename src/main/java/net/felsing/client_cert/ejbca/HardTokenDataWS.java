
package net.felsing.client_cert.ejbca;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse für hardTokenDataWS complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="hardTokenDataWS"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="certificates" type="{http://ws.protocol.core.ejbca.org/}certificate" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="copies" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="copyOfSN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="createTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="encKeyKeyRecoverable" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="hardTokenSN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="label" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="modifyTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="pinDatas" type="{http://ws.protocol.core.ejbca.org/}pinDataWS" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="tokenType" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "hardTokenDataWS", propOrder = {
    "certificates",
    "copies",
    "copyOfSN",
    "createTime",
    "encKeyKeyRecoverable",
    "hardTokenSN",
    "label",
    "modifyTime",
    "pinDatas",
    "tokenType"
})
public class HardTokenDataWS {

    @XmlElement(nillable = true)
    protected List<Certificate> certificates;
    @XmlElement(nillable = true)
    protected List<String> copies;
    protected String copyOfSN;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createTime;
    protected boolean encKeyKeyRecoverable;
    protected String hardTokenSN;
    protected String label;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar modifyTime;
    @XmlElement(nillable = true)
    protected List<PinDataWS> pinDatas;
    protected int tokenType;

    /**
     * Gets the value of the certificates property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the certificates property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCertificates().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Certificate }
     * 
     * 
     */
    public List<Certificate> getCertificates() {
        if (certificates == null) {
            certificates = new ArrayList<Certificate>();
        }
        return this.certificates;
    }

    /**
     * Gets the value of the copies property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the copies property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCopies().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCopies() {
        if (copies == null) {
            copies = new ArrayList<String>();
        }
        return this.copies;
    }

    /**
     * Ruft den Wert der copyOfSN-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCopyOfSN() {
        return copyOfSN;
    }

    /**
     * Legt den Wert der copyOfSN-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCopyOfSN(String value) {
        this.copyOfSN = value;
    }

    /**
     * Ruft den Wert der createTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreateTime() {
        return createTime;
    }

    /**
     * Legt den Wert der createTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreateTime(XMLGregorianCalendar value) {
        this.createTime = value;
    }

    /**
     * Ruft den Wert der encKeyKeyRecoverable-Eigenschaft ab.
     * 
     */
    public boolean isEncKeyKeyRecoverable() {
        return encKeyKeyRecoverable;
    }

    /**
     * Legt den Wert der encKeyKeyRecoverable-Eigenschaft fest.
     * 
     */
    public void setEncKeyKeyRecoverable(boolean value) {
        this.encKeyKeyRecoverable = value;
    }

    /**
     * Ruft den Wert der hardTokenSN-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHardTokenSN() {
        return hardTokenSN;
    }

    /**
     * Legt den Wert der hardTokenSN-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHardTokenSN(String value) {
        this.hardTokenSN = value;
    }

    /**
     * Ruft den Wert der label-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Legt den Wert der label-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Ruft den Wert der modifyTime-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getModifyTime() {
        return modifyTime;
    }

    /**
     * Legt den Wert der modifyTime-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setModifyTime(XMLGregorianCalendar value) {
        this.modifyTime = value;
    }

    /**
     * Gets the value of the pinDatas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pinDatas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPinDatas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PinDataWS }
     * 
     * 
     */
    public List<PinDataWS> getPinDatas() {
        if (pinDatas == null) {
            pinDatas = new ArrayList<PinDataWS>();
        }
        return this.pinDatas;
    }

    /**
     * Ruft den Wert der tokenType-Eigenschaft ab.
     * 
     */
    public int getTokenType() {
        return tokenType;
    }

    /**
     * Legt den Wert der tokenType-Eigenschaft fest.
     * 
     */
    public void setTokenType(int value) {
        this.tokenType = value;
    }

}
