package rcssjava.monitor;

import java.awt.Color;
import rcssjava.VecPosition;

/**
 * Text written on field
 * @author Gregory Kuhlmann
 */
public class FieldText 
    extends FieldShape
{
    private Color color;
    private VecPosition position;
    private String text;

    public FieldText( Color color,
		      VecPosition position, String text,
		      int depth )
    {
        this.color = color;
	this.position = position;
	this.text = text;
	this.depth = depth;
    }

    public void draw( FieldCanvas fc )
    {
	fc.drawText( position, text, color );
    }
}
