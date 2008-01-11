package rcssjava.geom;

import java.util.*;
import rcssjava.VecPosition;

/**
 * Implementation of 2d axis-parallel rectangle
 * @author Gregory Kuhlmann
 */
public class Rectangle 
    implements Region
{
    private VecPosition topLeft, bottomRight;

    /**
     * Constructs a rectangle with all point at the origin
     */
    public Rectangle()
    {
	topLeft = new VecPosition();
	bottomRight = new VecPosition();
    }

    /**
     * Constructs a rectangle with the given width 
     * and length, and with center at the origin
     * @param width size in y direction
     * @param length size in x direction
     */
    public Rectangle( double width, double length )
    {
	this( width, length, new VecPosition( 0, 0 ) );
    }

    /**
     * Constructs a rectangle with the given width 
     * and length, and center position
     * @param width size in y direction
     * @param length size in x direction
     * @param center center of rectangle
     */
    public Rectangle( double width, double length, VecPosition center )
    {
	this( center.subtract( new VecPosition( length / 2, width / 2 ) ),
	      center.add(      new VecPosition( length / 2, width / 2 ) ) );
    }

    /**
     * Constructs a rectangle from two corners diagonal from each other
     * @param p1 first corner
     * @param p2 second corner
     */
    public Rectangle( VecPosition p1, VecPosition p2 )
    {
	setPoints( p1, p2 );
    }

    /**
     * Set rectangle from the two diagonal corners given
     * @param p1 first corner
     * @param p2 second corner
     */
    public void setPoints( VecPosition p1, VecPosition p2 )
    {
	double xTop =    ( p1.isInFrontOf( p2 ) ) ? p1.getX() : p2.getX();
	double xBottom = ( p1.isBehind   ( p2 ) ) ? p1.getX() : p2.getX();
	double yLeft =   ( p1.isLeftOf   ( p2 ) ) ? p1.getY() : p2.getY();
	double yRight =  ( p1.isRightOf  ( p2 ) ) ? p1.getY() : p2.getY();
	topLeft     = new VecPosition( xTop,    yLeft );
	bottomRight = new VecPosition( xBottom, yRight );
    }

    /**
     * Get position of top-left corner
     * @return corner with largest x and smallest y
     */
    public VecPosition getTopLeft()
    {
	return topLeft;
    }

    /**
     * Get position of bottom-right corner
     * @return corner with smallest x and largest y
     */
    public VecPosition getBottomRight()
    {
	return bottomRight;
    }

    /**
     * Get position of top-right corner
     * @return corner with largest x and largest y
     */
    public VecPosition getTopRight()
    {
	return new VecPosition( getTopX(), getRightY() );
    }

    /**
     * Get position of bottom-left corner
     * @return corner with smallest x and smallest y
     */
    public VecPosition getBottomLeft()
    {
	return new VecPosition( getBottomX(), getLeftY() );
    }

    /**
     * Get x coordinate of top edge
     * @return x coordinate of edge with largest x
     */
    public double getTopX()
    {
	return topLeft.getX();
    }

    /**
     * Get x coordinate of bottom edge
     * @return x coordinate of edge with smallest x
     */
    public double getBottomX()
    {
	return bottomRight.getX();
    }

    /**
     * Get y coordinate of left edge
     * @return y coordinate of edge with smallest y
     */
    public double getLeftY()
    {
	return topLeft.getY();
    }

    /**
     * Get y coordinate of right edge
     * @return y coordinate of edge with largest y
     */
    public double getRightY()
    {
	return bottomRight.getY();
    }

    /**
     * Get top edge
     * @return line segment for edge with largest x
     */
    public LineSegment getTopSide()
    {
	return new LineSegment( getTopLeft(), getTopRight() );
    }

    /**
     * Get bottom edge
     * @return line segment for edge with smallest x
     */
    public LineSegment getBottomSide()
    {
	return new LineSegment( getBottomLeft(), getBottomRight() );
    }

    /**
     * Get left edge
     * @return line segment for edge with smallest y
     */
    public LineSegment getLeftSide()
    {
	return new LineSegment( getTopLeft(), getBottomLeft() );
    }

    /**
     * Get right edge
     * @return line segment for edge with largest y
     */
    public LineSegment getRightSide()
    {
	return new LineSegment( getBottomRight(), getTopRight() );
    }

    /**
     * Get iterator over corners starting with the top-left and
     * moving clockwise
     * @return iterator over the corners
     */
    public Iterator<VecPosition> cornerIterator()
    {
	Vector<VecPosition> v = new Vector<VecPosition>();
	v.add( getTopLeft() );
	v.add( getTopRight() );
	v.add( getBottomRight() );
	v.add( getBottomLeft() );
	return v.iterator();
    }

    /**
     * Get iterator over sides starting with the top and
     * moving clockwise
     * @return iterator over the sides
     */
    public Iterator<LineSegment> sideIterator()
    {
	Vector<LineSegment> v = new Vector<LineSegment>();
	v.add( getTopSide() );
	v.add( getRightSide() );
	v.add( getBottomSide() );
	v.add( getLeftSide() );
	return v.iterator();
    }

    /**
     * Is the given point inside the rectangle?
     * @param p test point
     * @return <code>true</code> if point lies inside
     */
    public boolean isInside( VecPosition p )
    {
	return p.isBetweenX( bottomRight, topLeft ) &&
	    p.isBetweenY( topLeft, bottomRight );
    }

    /**
     * Get center
     * @return position of center
     */
    public VecPosition getCenter()
    {
	return bottomRight.add( topLeft ).divide( 2 );
    }
 
    /**
     * Get width
     * @return size of rectangle in y direction
     */
    public double getWidth()
    {
	return bottomRight.subtract( topLeft ).getY();
    }
 
    /**
     * Get length
     * @return size of rectangle in x direction
     */
    public double getLength()
    {
	return topLeft.subtract( bottomRight ).getX();
    }

    /**
     * Create a new rectangle shifted in x and y by the
     * given value
     * @param d distance to shift rectangle
     * @return shifted rectangle
     */
    public Rectangle add( double d )
    {
	return add( new VecPosition( d, d ) );
    }

    /**
     * Create a new rectangle shifted by the
     * given vector
     * @param v vector to shift rectangle
     * @return shifted rectangle
     */
    public Rectangle add( VecPosition v )
    {
	return new Rectangle( getWidth(), getLength(), getCenter().add( v ) );
    }

    /**
     * Create a new rectangle shifted in x and y by the
     * given value in the opposite direction
     * @param d distance to shift rectangle
     * @return shifted rectangle
     */
    public Rectangle subtract( double d )
    {
	return subtract( new VecPosition( d, d ) );
    }

    /**
     * Create a new rectangle shifted by the
     * given vector in the opposite direction
     * @param v vector to shift rectangle in opposite direction
     * @return shifted rectangle
     */
    public Rectangle subtract( VecPosition v )
    {
	return new Rectangle( getWidth(), getLength(), getCenter().subtract( v ) );
    }

    public String toString()
    {
	return "[" + topLeft + ", " + bottomRight + "]";
    }
}
