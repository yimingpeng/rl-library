/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rlcommunity.environments.continuousgridworld;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 *
 * @author btanner
 */
public class Region implements Serializable {
      /** Change this when you make new versions that are not compatible **/
    private static final long serialVersionUID = 1L;

    protected Shape theRegion;

    public Region(Shape theRegion){
        assert(theRegion!=null);
        this.theRegion=theRegion;
    }

    public boolean intersects(Rectangle2D someRectangle){
        return theRegion.intersects(someRectangle);
    }

    public Shape getShape(){
        return theRegion;
    }
}