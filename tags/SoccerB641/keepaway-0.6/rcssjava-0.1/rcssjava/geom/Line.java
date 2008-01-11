package rcssjava.geom;

import java.util.*;
import rcssjava.*;
import static rcssjava.SoccerTypes.EPSILON;

/**
 * Implementation of 2d line
 * @author Gregory Kuhlmann
 */
public class Line
{
    private double a, b, c;

    /**
     * Construct line given the three coefficients in
     * <code>ay + bx + c = 0</code>.
     * @param a a coefficient
     * @param b b coefficient
     * @param c c coefficient
     */
    public Line( double a, double b, double c )
    {
	this.a = a;
	this.b = b;
	this.c = c;
    }

    /**
     * Get a coefficient
     * @return a coefficient
     */
    public double getA()
    {
	return a;
    }

    /**
     * Get a coefficient
     * @return b coefficient
     */
    public double getB()
    {
	return b;
    }

    /**
     * Get a coefficient
     * @return c coefficient
     */
    public double getC()
    {
	return c;
    }

    /**
     * Get slope
     * @return slope, <code>m</code> in <code>y = mx + b</code>
     */
    public double getSlope()
    {
	return -getB() / getA();
    }

    /**
     * Get y-intercept
     * @return y-intercept, <code>b</code> in <code>y = mx + b</code>
     */
    public double getYIntercept()
    {
	return -getC() / getA();
    }

    /**
     * Get intersecton with a line segment
     * @param ls line segment
     * @return intersection point or <code>null</code> if none
     */
    public VecPosition getIntersection( LineSegment ls )
    {
	return ls.getIntersection( this );
    }

    /**
     * Get intersecton with a line
     * @param line another line
     * @return intersection point or <code>null</code> if none
     */
    public VecPosition getIntersection( Line line )
    {
	double x, y;

	if ( ( a / b ) == ( line.getA() / line.getB() ) ) {
	    return null; 
	}

	if ( a == 0 ) {
	    x = -c / b;
	    y = line.getYGivenX( x );  
	}                   
	else if ( line.getA() == 0 ) {
	    x = -line.getC() / line.getB();
	    y = getYGivenX( x );
	}
	else {
	    x = ( a * line.getC() - line.getA() * c ) /
		( line.getA() * b - a * line.getB() );
	    y = getYGivenX( x );
	}

	return new VecPosition( x, y );	
    }

    /**
     * Get line orthogonal to this that passes through the given point
     * @param pos intersection point
     * @return orthogonal line
     */
    public Line getTangentLine( VecPosition pos )
    {
	return new Line( b, -a, a * pos.getX() - b * pos.getY() );
    }

    /**
     * Get point on line with smallest euclidean distance to given point
     * @param pos point to which closest should be determined
     * @return point on line closest to given point
     */
    public VecPosition getPointOnLineClosestTo( VecPosition pos )
    {
	Line l2 = getTangentLine( pos );
	return getIntersection( l2 );
    }

    /**
     * Get euclidean distance between a given point and the closest point
     * on the line to that point
     * @param pos point to which closest should be determined
     * @return distance to given point
     */
    public double getDistanceToPoint( VecPosition pos )
    {                                                   
	return pos.getDistanceTo( getPointOnLineClosestTo( pos ) );
    }            

    /**
     * Get the y coordinate on the line for a given x coordinate
     * @param x x coordinate
     * @return y coordinate
     */
    public double getYGivenX( double x )
    {
	if ( a == 0 ) {
	    System.err.println( "getYGivenX(): Cannot calculate Y coordinate" );
	    return 0;
	}

	return -( b * x + c ) / a;
    }

    /**
     * Get the x coordinate on the line for a given y coordinate
     * @param y y coordinate
     * @return x coordinate
     */
    public double getXGivenY( double y )
    {
	if ( b == 0 ) {
	    System.err.println( "getXGivenY(): Cannot calculate X coordinate" );
	    return 0;
	}

	return -( a * y + c ) / b;
    }

    /**
     * Create the line that passes through the two given points
     * @param pos1 first point
     * @param pos2 second point
     * @return line passing through both points
     */
    public static Line makeLineFromTwoPoints( VecPosition pos1, VecPosition pos2 )
    {
	double dA, dB, dC;
	double dTemp = pos2.getX() - pos1.getX();

	if ( Math.abs( dTemp ) < EPSILON ) {                             
	    dA = 0.0;                                             
	    dB = 1.0;
	}
	else {
	    dA = 1.0;
	    dB = -( pos2.getY() - pos1.getY() ) / dTemp;
	}
	dC = -dA * pos2.getY() - dB * pos2.getX();

	return new Line( dA, dB, dC );
    }

    public String toString()
    {
	String retVal;

	if ( a == 0 ) {
	    retVal = "x = " + ( -c / b );
	}
	else {
	    retVal = "y = ";
	    if ( b != 0 )
		retVal += ( -b / a ) + "x ";
	    if ( c > 0 ) {
		retVal += "- " + Math.abs( c / a );
	    }
	    else if ( c < 0 ) {
		retVal += "+ " + Math.abs( c / a );
	    }
	}

	return retVal;
    }
}
