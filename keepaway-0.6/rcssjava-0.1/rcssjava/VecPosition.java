package rcssjava;

import java.io.Serializable;
import static rcssjava.SoccerTypes.EPSILON;

/**
 * Implementation of a 2d point/vector.
 * @author Gregory Kuhlmann
 */
public class VecPosition 
    implements Serializable
{
    private double m_x;
    private double m_y;
 
    /**
     * Default constructor sets point at origin
     */
    public VecPosition()
    {
	this( 0, 0 );
    }

    /**
     * Cartesian constructor
     * @param vx value of x coordinate
     * @param vy value of y coordinate
     */
    public VecPosition( double vx, double vy )
    {
	this( vx, vy, false );
    }

    /**
     * Cartesian/Polar constructor
     * @param vx x coordinate for cartesian, or magnitude for polar
     * @param vy y coordinate for cartesian, or angle in degrees for polar
     * @param polar set to <code>true</code> for polar 
     * interpretation of arguments
     * @see #getVecPositionFromPolar(double,double)
     */
    public VecPosition( double vx, double vy, boolean polar )
    {
	if ( polar ) {
	    VecPosition p = getVecPositionFromPolar( vx, vy );
	    m_x = p.getX();
	    m_y = p.getY();
	}
	else { // cartesian
	    m_x = vx;
	    m_y = vy;
	}
    }

    /**
     * Copy constructor
     * @param p another <code>VecPosition</code>
     */
    public VecPosition( VecPosition p )
    {
	this( p.getX(), p.getY() );
    }

    /**
     * Copy value of another <code>VecPosition</code> into this
     * @param p another <code>VecPosition</code>
     */
    public void copy( VecPosition p )
    {
	m_x = p.getX();
	m_y = p.getY();
    }

    /**
     * Get x coordinate
     * @return x coordinate
     */
    public double getX()
    {
	return m_x;
    }

    /**
     * Get y coordinate
     * @return y coordinate
     */
    public double getY()
    {
	return m_y;
    }

    /**
     * Get magnitude
     * @return euclidean length (L2 norm) of vector
     */
    public double getMagnitude()
    {
	return Math.sqrt( m_x * m_x + m_y * m_y );
    }

    /**
     * Get direction
     * @return angle in degrees
     */
    public double getDirection()
    {
	return Utils.atan2Deg( m_y, m_x );
    }

    /**
     * Get distanct to a given point
     * @param p another point
     * @return euclidean distance to point
     */
    public double getDistanceTo( VecPosition p )
    {
	return subtract( p ).getMagnitude();
    }

    /**
     * Is this in front of given x coordinate?
     * @param d x coordinate
     * @return <code>true</code> if this has a greater x coordinate
     */
    public boolean isInFrontOf( double d )
    {
	return m_x > d;
    }

    /**
     * Is this in front of given point?
     * @param p another point
     * @return <code>true</code> if this has a greater x coordinate
     */
    public boolean isInFrontOf( VecPosition p )
    {
	return m_x > p.m_x;
    }

    /**
     * Is this behind given x coordinate?
     * @param d x coordinate
     * @return <code>true</code> if this has a smaller x coordinate
     */
    public boolean isBehind( double d )
    {
	return m_x < d;
    }

    /**
     * Is this behind given point?
     * @param p another point
     * @return <code>true</code> if this has a smaller x coordinate
     */
    public boolean isBehind( VecPosition p )
    {
	return m_x < p.m_x;
    }

    /**
     * Is this left of given y coordinate?
     * @param d y coordinate
     * @return <code>true</code> if this has a smaller y coordinate
     */
    public boolean isLeftOf( double d )
    {
	return m_y < d;
    }

    /**
     * Is this left of given point?
     * @param p another point
     * @return <code>true</code> if this has a smaller y coordinate
     */
    public boolean isLeftOf( VecPosition p )
    {
	return m_y < p.m_y;
    }  
  
    /**
     * Is this right of given y coordinate?
     * @param d y coordinate
     * @return <code>true</code> if this has a greater y coordinate
     */
    public boolean isRightOf( double d )
    {
	return m_y > d;
    }

    /**
     * Is this right of given point?
     * @param p another point
     * @return <code>true</code> if this has a greater y coordinate
     */
    public boolean isRightOf( VecPosition p )
    {
	return m_y > p.m_y;
    }  

    /** 
     * Is the x coordinate between the x coordinates of the given points?
     * @param p1 back point
     * @param p2 front point
     * @return <code>true</code> if in front of back point 
     * and behind front point
     */
    public boolean isBetweenX( VecPosition p1, VecPosition p2 )
    {
	return isBetweenX( p1.getX(), p2.getX() );
    }

    /** 
     * Is the x coordinate between the given x coordinates 
     * @param d1 back x coordinate
     * @param d2 front x coordinate
     * @return <code>true</code> if in front of back coordinate
     * and behind front coordinate
     */
    public boolean isBetweenX( double d1, double d2 )
    {
	return isInFrontOf( d1 ) && isBehind( d2 ) ||
	    m_x == d1 || m_x == d2;
    }

    /** 
     * Is the y coordinate between the y coordinates of the given points?
     * @param p1 left point
     * @param p2 right point
     * @return <code>true</code> if right of left point
     * and left of right point
     */
    public boolean isBetweenY( VecPosition p1, VecPosition p2 )
    {
	return isBetweenY( p1.getY(), p2.getY() );
    }
    
    /** 
     * Is the y coordinate between the given y coordinates
     * @param d1 left y coordinate
     * @param d2 right y coordinate
     * @return <code>true</code> if right of left coordinate
     * and left of right coordinate
     */
    public boolean isBetweenY( double d1, double d2 )
    {
	return isRightOf( d1 ) && isLeftOf( d2 ) ||
	    m_y == d1 || m_y == d2;
    }

    /**
     * Get new vector which is the negation of this 
     * @return new <code>VecPosition</code> with x and y negated
     */
    public VecPosition negate()
    {
	return new VecPosition( -m_x, -m_y );
    }

    /**
     * Get new vector with a constant value added to each coordinate
     * @param d value to add to both x and y
     * @return new <code>VecPosition (x+d,y+d)</code>
     */
    public VecPosition add( double d )
    {
	return new VecPosition( m_x + d, m_y + d );
    }

    /**
     * Sum of two vectors
     * @param p another vector
     * @return sum of this and given vector
     */
    public VecPosition add( VecPosition p )
    {
	return new VecPosition( m_x + p.m_x, m_y + p.m_y );
    }

    /**
     * Get new vector with a constant value subtracted from each coordinate
     * @param d value to subtract from both x and y
     * @return new <code>VecPosition (x-d,y-d)</code>
     */
    public VecPosition subtract( double d )
    {
	return new VecPosition( m_x - d, m_y - d );
    }

    /**
     * Difference of two vectors
     * @param p another vector
     * @return this minus given vector
     */
    public VecPosition subtract( VecPosition p )
    {
	return new VecPosition( m_x - p.m_x, m_y - p.m_y );
    }

    /**
     * Get new vector with each coordinate muliplied by a constant
     * @param d value to multiply both x and y by
     * @return new <code>VecPosition (x*d,y*d)</code>
     */
    public VecPosition multiply( double d )
    {
	return new VecPosition( m_x * d, m_y * d );
    }

    /**
     * Pairwise product of two vectors
     * @param p another vector
     * @return this times given vector, coordinate-by-coordinate
     * @see #dotProduct(VecPosition)
     */
    public VecPosition multiply( VecPosition p )
    {
	return new VecPosition( m_x * p.m_x, m_y * p.m_y );
    }

    /**
     * Get new vector with each coordinate divided by a constant
     * @param d value to divide both x and y by
     * @return new <code>VecPosition (x/d,y/d)</code>
     */
    public VecPosition divide( double d )
    {
	return new VecPosition( m_x / d, m_y / d );
    }

    /**
     * Pairwise quotient of two vectors
     * @param p another vector
     * @return this divided by given vector, coordinate-by-coordinate
     */
    public VecPosition divide( VecPosition p )
    {
	return new VecPosition( m_x / p.m_x, m_y / p.m_y );
    }

    /**
     * Scale to given magnitude
     * @param d magnitude
     * @return new <code>VecPosition</code> with given magnitude
     * @see #normalize()
     */
    public VecPosition withMagnitude( double d )
    {
	if ( getMagnitude() > EPSILON )
	    return multiply( d / getMagnitude() );
	return new VecPosition( getX(), getY() );
    }

    /**
     * Scale to magnitude 1.0
     * @return new <code>VecPosition</code> with magnitude 1.0
     * @see #withMagnitude(double)
     */
    public VecPosition normalize()
    {
	return withMagnitude( 1.0 );
    }

    /**
     * Rotate by angle
     * @param angDeg angle in degrees
     * @return new <code>VecPosition</code> rotated by given angle
     */
    public VecPosition rotate( double angDeg )
    {
	double dMag = getMagnitude();
	double dNewDir = getDirection() + angDeg;
	return new VecPosition( dMag, dNewDir, true );
    }

    /**
     * Convert global coordinates to relative coordinates
     * @param origin position of relative coordinate system
     * @param angDeg direction of relative coordinate system in degrees
     * @return point in relative coordinates
     * @see #relativeToGlobal(VecPosition,double)
     */
    public VecPosition globalToRelative( VecPosition origin, double angDeg )
    {
	return subtract( origin ).rotate( -angDeg );
    }
    
    /**
     * Convert relative coordinates to global coordinates
     * @param origin position of relative coordinate system
     * @param angDeg direction of relative coordinate system in degrees
     * @return point in global coordinates
     * @see #globalToRelative(VecPosition,double)
     */
    public VecPosition relativeToGlobal( VecPosition origin, double angDeg )
    {
	return rotate( angDeg ).add( origin );
    }

    /**
     * Dot (inner) product of two vectors
     * @param p another vector
     * @return dot product of this and given vector
     * @see #multiply(VecPosition)
     */
    public double dotProduct( VecPosition p )
    {
	VecPosition v = multiply( p );
	return v.getX() + v.getY();
    }

    /**
     * Get angle formed between the vectors connecting this point to
     * the given points
     * @param p1 first point
     * @param p2 second point
     * @return angle in degrees
     */
    public double getAngleBetweenPoints( VecPosition p1, VecPosition p2 )
    {
	VecPosition v1 = subtract( p1 ).normalize();
	VecPosition v2 = subtract( p2 ).normalize();
	return Math.abs( Utils.acosDeg( v1.dotProduct( v2 ) ) );
    }
    
    public String toString()
    {
	return "( " + m_x + ", " + m_y + " )";
    }
    

    /**
     * Convert from polar to cartesian coordinates
     * @param mag magnitude
     * @param angDeg direction in degrees
     * @return new <code>VecPosition</code> for the given values
     */
    public static VecPosition getVecPositionFromPolar( double mag, double angDeg )
    {
	return new VecPosition( mag * Utils.cosDeg( angDeg ), 
				mag * Utils.sinDeg( angDeg ) );
    }
}
