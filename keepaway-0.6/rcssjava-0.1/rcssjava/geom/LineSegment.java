package rcssjava.geom;

import rcssjava.VecPosition;

/**
 * Implementation of 2d line segment
 * @author Gregory Kuhlmann
 */
public class LineSegment
{
    private VecPosition p1, p2;

    /**
     * Constructs a line segment with both endpoints at the origin
     */
    public LineSegment()
    {
	p1 = new VecPosition();
	p2 = new VecPosition();
    }

    /**
     * Constructs a line segment with the given endpoints
     * @param p1 first endpoint
     * @param p2 second endpoint
     */
    public LineSegment( VecPosition p1, VecPosition p2 )
    {
	setPoints( p1, p2 );
    }

    /**
     * Set endpoints of line segment
     * @param p1 first endpoint
     * @param p2 second endpoint
     */
    public void setPoints( VecPosition p1, VecPosition p2 )
    {
	this.p1 = p1;
	this.p2 = p2;
    }

    /**
     * Get distance between endpoints
     * @return euclidean distance between endpoints
     */
    public double getLength()
    {
	return p1.getDistanceTo( p2 );
    }
    
    /**
     * Get point on line segment halfway between endpoints
     * @return midpoint
     */
    public VecPosition getMidpoint()
    {
	return p1.add( p2 ).divide( 2 );
    }

    /**
     * Create the rectangle that has this line segment as a diagonal
     * @return bounding rectangle
     */
    public Rectangle getBoundingRectangle()
    {
	return new Rectangle( p1, p2 );
    }

    /**
     * Create infinite line from this segment
     * @return extrapolated line
     */
    public Line getLine()
    {
	return Line.makeLineFromTwoPoints( p1, p2 );
    }

    /**
     * Get intersection with line
     * @param l line to intersect with
     * @return intersection point or <code>null</code> if none
     */
    public VecPosition getIntersection( Line l )
    {
	Line line = getLine();
	if ( line == null )
	    return null;
	VecPosition intersect = line.getIntersection( l );
	if ( intersect == null ||
	     !getBoundingRectangle().isInside( intersect ) )
	    return null;
	return intersect;
    }
    
    /**
     * Get intersection with line segment
     * @param ls line segment to intersect with
     * @return intersection point or <code>null</code> if none
     */
    public VecPosition getIntersection( LineSegment ls )
    {
	Line line = ls.getLine();
	if ( line == null )
	    return null;
	VecPosition intersect = getIntersection( line );
	if ( intersect == null ||
	     !ls.getBoundingRectangle().isInside( intersect ) )
	    return null;
	return intersect;
    }
}
