//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b26-ea3 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.05.02 at 05:14:34 PM EDT 
//


package org.rlcommunity.environment.octopus.config;

import java.util.ArrayList;
import java.util.List;
import org.rlcommunity.environment.octopus.config.ChoiceSpec;
import org.rlcommunity.environment.octopus.config.ObjectiveSetSpec;
import org.rlcommunity.environment.octopus.config.ObjectiveSpec;
import org.rlcommunity.environment.octopus.config.SequenceSpec;
import org.rlcommunity.environment.octopus.config.TargetSpec;


/**
 * <p>Java class for ObjectiveSetSpec complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ObjectiveSetSpec">
 *   &lt;complexContent>
 *     &lt;extension base="{}ObjectiveSpec">
 *       &lt;sequence>
 *         &lt;element ref="{}objective" maxOccurs="unbounded" minOccurs="2"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */

public class ObjectiveSetSpec
    extends ObjectiveSpec
{

    protected List<ObjectiveSpec> objective;

    public ObjectiveSetSpec(ObjectiveSpec[] objectiveArray){
        objective=new ArrayList<ObjectiveSpec>();
        for (ObjectiveSpec objectiveSpec : objectiveArray) {
            objective.add(objectiveSpec);
        }

    }
    /**
     * Gets the value of the objective property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the objective property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObjective().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ChoiceSpec }{@code >}
     * {@link JAXBElement }{@code <}{@link TargetSpec }{@code >}
     * {@link JAXBElement }{@code <}{@link ObjectiveSpec }{@code >}
     * {@link JAXBElement }{@code <}{@link SequenceSpec }{@code >}
     * 
     * 
     */
    public List<ObjectiveSpec> getObjective() {
        if (objective == null) {
            objective = new ArrayList<ObjectiveSpec>();
        }
        return this.objective;
    }

}
