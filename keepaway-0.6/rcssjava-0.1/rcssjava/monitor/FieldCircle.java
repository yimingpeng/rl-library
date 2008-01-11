package rcssjava.monitor;

import java.awt.Color;
import rcssjava.VecPosition;

/**
 * Circle drawn on field
 * @author Gregory Kuhlmann
 */
public class FieldCircle extends FieldShape
{
    private Color color;
    private boolean filled;
    private VecPosition center;
    private double radius;

    public FieldCircle( Color color, boolean filled,
			VecPosition center, double radius,
			int depth )
    {
        this.color = color;
        this.filled = filled;
        this.center = center;
        this.radius = radius;
	this.depth = depth;
    }

    public void draw( FieldCanvas fc )
    {
	fc.drawCircle( center, radius, color, filled );
    }
}
