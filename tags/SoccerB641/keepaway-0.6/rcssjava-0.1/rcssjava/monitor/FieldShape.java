package rcssjava.monitor;

/**
 * Shape drawn on field
 * @author Gregory Kuhlmann
 */
public abstract class FieldShape 
    implements Comparable<FieldShape>
{
    protected int depth;

    /**
     * Allows shapes to be sorted by depth.  Higher values of
     * <code>depth</code> are drawn on top.  The field itself is 
     * <code>0</code>.
     */
    public int compareTo( FieldShape fs )
    {
	return depth - fs.depth;
    }

    public abstract void draw( FieldCanvas fc );
} 
