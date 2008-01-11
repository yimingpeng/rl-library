package rcssjava;

import java.util.*;
import java.text.*;
import static rcssjava.SoccerTypes.*;

/**
 * Math and other utility functions that don't depend on
 * the world state.
 * @author Gregory Kuhlmann
 */
public class Utils
{
    private Utils() {}

    /**
     * Cosine of angle in degrees
     * @param angDeg angle in degrees
     * @return cosine of angle
     */
    public static double cosDeg( double angDeg )
    {
	return Math.cos( Math.toRadians( angDeg ) );
    }

    /**
     * Sine of angle in degrees
     * @param angDeg angle in degrees
     * @return sine of angle
     */
    public static double sinDeg( double angDeg )
    {
	return Math.sin( Math.toRadians( angDeg ) );
    }

    /**
     * Tangent of angle in degrees
     * @param angDeg angle in degrees
     * @return tangent of angle
     */
    public static double tanDeg( double angDeg )
    {
	return Math.tan( Math.toRadians( angDeg ) );
    }

    /**
     * Arctangent (in degrees)
     * @param x tangent of angle
     * @return angle in degrees
     */
    public static double atanDeg( double x )
    {
	return Math.toDegrees( Math.atan( x ) );
    }

    /**
     * Arctangent (in degrees) of ratio
     * @param y numerator
     * @param x denominator
     * @return angle in degrees of <code>y / x</code>
     */
    public static double atan2Deg( double y, double x )
    {
	if ( Math.abs( x ) < EPSILON && Math.abs( y ) < EPSILON )
	    return 0;
	return Math.toDegrees( Math.atan2( y, x ) );
    }

    /**
     * Arccosine (in degrees)
     * @param x cosine of angle
     * @return angle in degrees
     */
    public static double acosDeg( double x )
    {
	if ( x >= 1 )
	    return 0;
	else if ( x <= -1 )
	    return 180;
	return Math.toDegrees( Math.acos( x ) );
    }

    /**
     * Arcsine (in degrees)
     * @param x sine of angle
     * @return angle in degrees
     */
    public static double asinDeg( double x )
    {
	if ( x >= 1 )
	    return 90;
	else if ( x <= -1 )
	    return  -90;
	return Math.toDegrees( Math.asin( x ) );
    }

    /** 
     * Normalize angle to be between <code>-180</code> and <code>180</code>
     * @param angDeg angle in degrees
     * @return normalized angle in degrees
     */
    public static double normalizeAngle( double angDeg )
    {
	while( angDeg > 180  ) 
	    angDeg -= 360;
	while( angDeg < -180 ) 
	    angDeg += 360;
	return angDeg;
    }

    /**
     * Integer sign of value
     * @param d some value
     * @return <code>1</code> if <code>d > 0</code> 
     * and <code>-1</code> otherwise
     */
    public static int sign( double d )
    {
	return ( d > 0 ) ? 1 : -1;
    }

    /**
     * Bisector of smallest angle made between given angles
     * @param ang1 angle in degrees
     * @param ang2 angle in degrees
     * @return bisecting angle in degrees
     */
    public static double getBisectorTwoAngles( double ang1, double ang2 )
    {
	// separate sine and cosine part to circumvent boundary problem
	return normalizeAngle( atan2Deg( sinDeg( ang1 ) + sinDeg( ang2 ),
					 cosDeg( ang1 ) + cosDeg( ang2 ) ) );
    }

    /**
     * Is player on left team?
     * @param id player ID
     * @return <code>true</code> if player is on left team
     */
    public static boolean isLeftPlayer( int id )
    {
	return id >= ID_PLAYER_L_1 && id <= ID_PLAYER_L_11;
    }

    /**
     * Is player on right team?
     * @param id player ID
     * @return <code>true</code> if player is on right team
     */
    public static boolean isRightPlayer( int id )
    {
	return id >= ID_PLAYER_R_1 && id <= ID_PLAYER_R_11;
    }

    /**
     * Is this a player?
     * @param id object ID
     * @return <code>true</code> if the object ID refers to a player
     */
    public static boolean isPlayer( int id )
    {
	return isLeftPlayer( id ) || isRightPlayer( id );
    }

    /**
     * Is this the left goal?
     * @param id object ID
     * @return <code>true</code> if the object ID corresponds to the left goal
     */
    public static boolean isLeftGoal( int id )
    {
	return id == ID_GOAL_L;
    }

    /**
     * Is this the right goal?
     * @param id object ID
     * @return <code>true</code> if the object ID corresponds to the right goal
     */
    public static boolean isRightGoal( int id )
    {
	return id == ID_GOAL_R;
    }

    /**
     * Is this a goal?
     * @param id object ID
     * @return <code>true</code> if the object ID corresponds one of the goals
     */
    public static boolean isGoal( int id )
    {
	return isLeftGoal( id ) || isRightGoal( id );
    }

    /**
     * Is this the ball?
     * @param id object ID
     * @return <code>true</code> if the object ID corresponds to the ball
     */
    public static boolean isBall( int id )
    {
	return id == ID_BALL;
    }

    /**
     * Get player's uniform number
     * @param id player ID
     * @return player's uniform number 1-11
     */
    public static int getUnumFromID( int id )
    {
	if ( isLeftPlayer( id ) )
	    return id - ID_PLAYER_L_1 + 1;
	if ( isRightPlayer( id ) )
	    return id - ID_PLAYER_R_1 + 1;
	return 0;
    }

    /**
     * Get player's or other object's side
     * @param id object ID
     * @return side as int
     * @see SoccerTypes#SIDE_ILLEGAL
     * @see SoccerTypes#SIDE_LEFT
     * @see SoccerTypes#SIDE_RIGHT
     */
    public static int getSideFromID( int id )
    {
	if ( isLeftPlayer( id ) || isLeftGoal( id ) )
	    return SIDE_LEFT;
	if ( isRightPlayer( id ) || isRightGoal( id ) )
	    return SIDE_RIGHT;
	return SIDE_ILLEGAL;
    }

    /**
     * Get ball's object ID
     * @return ball's object ID
     */
    public static int getBallID()
    {
	return ID_BALL;
    }

    /**
     * Get left player's ID from uniform number
     * @param unum uniform number 1-11
     * @return player ID
     */
    public static int getLeftPlayerID( int unum )
    {
	return ID_PLAYER_L_1 + unum - 1;
    }

    /**
     * Get right player's ID from uniform number
     * @param unum uniform number 1-11
     * @return player ID
     */
    public static int getRightPlayerID( int unum )
    {
	return ID_PLAYER_R_1 + unum - 1;
    }

    /**
     * Get player's ID from side and uniform number
     * @param side side as int
     * @see rcssjava.SoccerTypes#SIDE_ILLEGAL
     * @see rcssjava.SoccerTypes#SIDE_LEFT
     * @see rcssjava.SoccerTypes#SIDE_RIGHT
     * @param unum uniform number 1-11
     * @return player ID
     */
    public static int getPlayerID( int side, int unum )
    {
	if ( side == SIDE_LEFT )
	    return getLeftPlayerID( unum );
	if ( side == SIDE_RIGHT )
	    return getRightPlayerID( unum );
	return ID_ILLEGAL;
    }

    /**
     * Get text label for a playmode
     * @param pm playmode
     * @return name of playmode
     */
    public static String getPlayModeString( int pm )
    {
	return PLAYMODE_STRINGS[ pm ];
    }

    /** 
     * Remove absolute path from filename
     * @param filename full path to file
     * @return file name without path
     */
    public static String stripPath( String filename )
    {
	String retVal = new String();
	StringTokenizer st =
	    new StringTokenizer( filename, "/" );
	while ( st.hasMoreTokens() ) {
	    retVal = st.nextToken();
	}
	return retVal;
    }

    /**
     * Replace characters escaped using backslashes with their 
     * escaped values
     * @param string original string
     * @return unescaped string
     */
    public static String unescape( String string )
    {
	final StringBuffer result = new StringBuffer();

	final StringCharacterIterator iterator = 
	    new StringCharacterIterator( string );
	char c = iterator.current();
	
	boolean escaped = false;
	while ( c != StringCharacterIterator.DONE ) {
	    if ( escaped ) {
		switch ( c ) {
		case 'n': 
		    result.append( '\n' );
		    break;
		default:
		    result.append( c );
		    break;
		}
		escaped = false;
	    }
	    else { 
		switch ( c ) {
		case '\\':
		    escaped = true;
		    break;
		case '"':
		    // Don't print quotes
		    break;
		default:
		    result.append( c );
		    break;
		}
	    }
	    c = iterator.next();
	}
	
	return result.toString();
    }
}
