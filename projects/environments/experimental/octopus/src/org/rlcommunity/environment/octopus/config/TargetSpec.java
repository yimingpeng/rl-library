//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b26-ea3 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.05.02 at 05:14:34 PM EDT 
//


package org.rlcommunity.environment.octopus.config;

import java.util.ArrayList;
import java.util.List;
import org.rlcommunity.environment.octopus.config.ObjectiveSpec;
import org.rlcommunity.environment.octopus.config.TargetSpec;


/**
 * <p>Java class for TargetSpec complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TargetSpec">
 *   &lt;complexContent>
 *     &lt;extension base="{}ObjectiveSpec">
 *       &lt;attribute name="position" type="{}Duple" />
 *       &lt;attribute name="reward" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

public class TargetSpec
    extends ObjectiveSpec
{

    protected List<Double> position;
    protected Double reward;

    public TargetSpec(double[] positionArray, double reward){
        position=new ArrayList<Double>();
        for (double d : positionArray) {
            position.add(d);
        }
        this.reward=reward;
    }
    /**
     * Gets the value of the position property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the position property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPosition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Double }
     * 
     * 
     */
    public List<Double> getPosition() {
        if (position == null) {
            position = new ArrayList<Double>();
        }
        return this.position;
    }

    /**
     * Gets the value of the reward property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getReward() {
        return reward;
    }

    /**
     * Sets the value of the reward property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setReward(Double value) {
        this.reward = value;
    }

}
