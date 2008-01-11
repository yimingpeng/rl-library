package rcssjava.comm;

import rcssjava.*;

/**
 * Handles contruction of outgoing commands to the server for
 * online coaches and offline trainers.  This class contains all 
 * of the commands that a coach can send, which is a subset of
 * what a trainer can send. 
 * @see TrainerCommandHandler
 * @author Gregory Kuhlmann
 */
public class CoachCommandHandler
{
    protected Connection C;
    protected WorldState WS;

    /**
     * Constructs a command handler with a null world state
     * @param c connection
     * @see #setWorldState(WorldState)
     */
    public CoachCommandHandler( Connection c )
    {
	this( c, null );
    }

    /**
     * Constructs a command handler with the given world state
     * @param c connection
     * @param ws world state to connect to this parser
     */
    public CoachCommandHandler( Connection c, WorldState ws )
    {
	C = c;
	setWorldState( ws );
    }

    /**
     * Connect the parser to the supplied world state
     * @param ws world state to connect to this parser
     */
    public void setWorldState( WorldState ws )
    {
	WS = ws;
    }

    /**
     * Send init message to server
     * @param teamName name of team to coach
     * @param version protocol version number supported
     */
    public void init( String teamName, double version )
    {
	String cmd = "(init " + teamName + " (version " +
	    version + "))";
	C.send( cmd );
    }

    /**
     * Send say message to server
     * @param message
     */
    public void say( String message )
    {
	String cmd = "(say " + message + ")";
	C.send( cmd );
    }

    /**
     * Change the heterogenous player type of a given player.  This
     * version ensures that the player is on the coach's team
     * @param id player ID of player to change
     * @param playerType new heterogenous type for player
     */
    public void changePlayerType( int id, int playerType )
    {
	if ( !WS.isTeammate( id ) ) {
	    System.err.println( "changePlayerType: Object " + id + 
				" is not a teammate" );
	    return;
	}
	    
	int unum = Utils.getUnumFromID( id );
	String cmd = "(change_player_type " +
	    unum + " " + playerType + ")";
	C.send( cmd );
    }

    /**
     * Turn "see" messages on or off
     * @param mode set to <code>true</code> to turn "see" messages on
     */
    public void eye( boolean mode )
    {
	String cmd = "(eye " + ( mode ? "on" : "off" ) + ")";
	C.send( cmd );
    }

    /**
     * Request team names message from server
     */
    public void teamNames()
    {
	String cmd = "(team_names)";
	C.send( cmd );
    }

    /**
     * Tell server thinking time is over.  Used in synchronous mode
     */
    public void done()
    {
	String cmd = "(done)";
	C.send( cmd );
    }
}








