
package net.felsing.client_cert.ejbca;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für userDataSourceVOWS complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="userDataSourceVOWS"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isModifyable" type="{http://www.w3.org/2001/XMLSchema}int" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="userDataVOWS" type="{http://ws.protocol.core.ejbca.org/}userDataVOWS" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userDataSourceVOWS", propOrder = {
    "isModifyable",
    "userDataVOWS"
})
public class UserDataSourceVOWS {

    @XmlElement(nillable = true)
    protected List<Integer> isModifyable;
    protected UserDataVOWS userDataVOWS;

    /**
     * Gets the value of the isModifyable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the isModifyable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIsModifyable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getIsModifyable() {
        if (isModifyable == null) {
            isModifyable = new ArrayList<Integer>();
        }
        return this.isModifyable;
    }

    /**
     * Ruft den Wert der userDataVOWS-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link UserDataVOWS }
     *     
     */
    public UserDataVOWS getUserDataVOWS() {
        return userDataVOWS;
    }

    /**
     * Legt den Wert der userDataVOWS-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link UserDataVOWS }
     *     
     */
    public void setUserDataVOWS(UserDataVOWS value) {
        this.userDataVOWS = value;
    }

}
