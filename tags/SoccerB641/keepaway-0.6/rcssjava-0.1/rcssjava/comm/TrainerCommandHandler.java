package rcssjava.comm;

import rcssjava.*;
import static rcssjava.SoccerTypes.*;

/**
 * Handles contruction of outgoing commands to the server for
 * offline trainers.  This class contains all 
 * of the commands that a trainer can send but a coach
 * cannot.  A trainer always uses the left team's coordinate system.
 * @see CoachCommandHandler
 * @author Gregory Kuhlmann
 */
public class TrainerCommandHandler 
    extends CoachCommandHandler 
{
    /**
     * Constructs a command handler with a null world state
     * @param c connection
     * @see CoachCommandHandler#setWorldState(WorldState)
     */
    public TrainerCommandHandler( Connection c )
    {
	super( c );
    }

    /**
     * Constructs a command handler with the given world state
     * @param c connection
     * @param ws world state to connect to this parser
     */
    public TrainerCommandHandler( Connection c, WorldState ws )
    {
	super( c, ws );
    }

    private String getObjectStringFromID( int id )
    {
	if ( Utils.isBall( id ) ) {
	    return "(ball)";
	}
	else if ( Utils.isPlayer( id ) ) {
	    String teamName = WS.getTeamName( Utils.getSideFromID( id ) );
	    int unum = Utils.getUnumFromID( id );
	    return "(player " + teamName + " " + unum + ")";
	}

	System.err.println( "getObjectStringFromID: Object " + id + 
			    " is not a ball or player" );
	return null;
    }

    /**
     * Change the current play mode
     * @param playMode play mode to change to
     */
    public void changeMode( int playMode )
    {
	String cmd = "(change_mode " + PLAYMODE_STRINGS[ playMode ] + ")";
	C.send( cmd );
    }

    /**
     * Move object to a given position with zero velocity
     * @param id object ID
     * @param pos new position
     */
    public void move( int id, VecPosition pos )
    {
	move( id, pos, null );
    }

    /**
     * Move object to a given position and set its velocity
     * @param id object ID
     * @param pos new position
     * @param vel new velocity
     */
    public void move( int id, VecPosition pos,
			VecPosition vel )
    {
	move( id, pos, 0, vel );
    }

    /**
     * Move object to a given position and 
     * set the direction in which it's facing.  Used only for players.
     * @param id object ID
     * @param pos new position
     * @param vDir new angle in degrees
     */
    public void move( int id, VecPosition pos,
		      double vDir )
    {
	move( id, pos, vDir, new VecPosition() );
    }    

    /**
     * Move object to a given position, set its velocity and
     * set the direction in which it's facing.  Used only for players.
     * @param id object ID
     * @param pos new position
     * @param vDir new angle in degrees
     * @param vel new velocity
     */
    public void move( int id, VecPosition pos,
		      double vDir, VecPosition vel )
    {
	String cmd = "(move " + getObjectStringFromID( id ) + " " +
	    pos.getX() + " " + pos.getY();
	if ( vel != null ) {
	    cmd += " " + vDir + " " + 
		vel.getX() + " " + vel.getY();
	}
	cmd += ")";
	C.send( cmd );
    }

    /** 
     * Request message from server to determine what part of the field the
     * ball is in.
     */
    public void checkBall()
    {
	String cmd = "(check_ball)";
	C.send( cmd );
    }

    /**
     * Send start message to server
     */
    public void start()
    {
	String cmd = "(start)";
	C.send( cmd );
    }

    /**
     * Restore players' stamina
     */
    public void recover()
    {
	String cmd = "(recover)";
	C.send( cmd );
    }

    /**
     * Turn "hear" messages on or off
     * @param mode set to <code>true</code> to turn "hear" messages on
     */
    public void ear( boolean mode )
    {
	String cmd = "(ear " + ( mode ? "on" : "off" ) + ")";
	C.send( cmd );
    }

    /**
     * Send init message to server without specifying a team.  The
     * trainer must use this version of "init".
     * @param version protocol version number supported
     */
    public void init( double version )
    {
	init( "", version );
    }

    /**
     * Change the heterogenous player type of a given player.
     * @param id player ID of player to change
     * @param playerType new heterogenous type for player
     */
    public void changePlayerType( int id, int playerType )
    {
	String teamName = WS.getTeamName( Utils.getSideFromID( id ) );
	int unum = Utils.getUnumFromID( id );
	String cmd = "(change_player_type " + teamName +
	    " " + unum + " " + playerType + ")";
	C.send( cmd );
    }    
}
