package rcssjava;

import java.io.Serializable;
import static rcssjava.SoccerTypes.*;

/**
 * Contains all information relevant to the instantaneous 
 * state of a game including the positions and velocities
 * of the players and the ball.  The world state can be
 * from the perspective of the left or right sides, in which
 * case all coordinates are in team-centric coordinates.  If no
 * side is specified, the concepts of "our team" and 
 * "opponent team" don't make sense; all coordinates are
 * from the global (same as left team's) perspective.  Using
 * team-centric functions without setting a team has undefined
 * and possibly very bad consequences.
 * @author Gregory Kuhlmann
 */
public class WorldState 
    implements Serializable
{
    private ServerParams SP;

    private int time;
    private int playMode;
    private String[] teamNames;
    private int[] scores;

    private PlayerObject[] players;
    private BallObject ball;

    private int ourSide, oppSide;

    /**
     * Constructs a world state with the specified server parameters.
     * The perspective is not set to be from either side.
     * @param SP server parameters
     */
    public WorldState( ServerParams SP )
    {
	this( SP, SIDE_ILLEGAL );
    }

    /**
     * Constructs a world state from the given perspective
     * @param SP server parameters
     * @param side side of observer
     */
    public WorldState( ServerParams SP, int side )
    {
	this.SP = SP;
	setPlayMode( PM_Null );
	setSide( side );

	scores = new int[ NUM_TEAMS ];

	teamNames = new String[ NUM_TEAMS ];
	teamNames[ SIDE_LEFT ]  = new String();
	teamNames[ SIDE_RIGHT ] = new String();

	players = new PlayerObject[ MAX_PLAYERS ];
	for ( int i = 0; i < MAX_PLAYERS; i++ ) {
	    players[ i ] = new PlayerObject();
	    // Set initial position of player on sideline
	    int s = ( i < MAX_PLAYERS_TEAM ) ? -3 : 3;
	    players[ i ].setPosition( new VecPosition( s * ( i % MAX_PLAYERS_TEAM + 1 ), 
						       SIDELINES_Y ) );
	}
	ball = new BallObject();

    }

    /**
     * Deep copy another world state into this
     * @param ws another world state
     * @return pointer to this
     */
    public WorldState copy( WorldState ws )
    {
	SP = ws.SP;
	updateTime( ws.getTime() );
	setPlayMode( ws.getPlayMode() );
	setSide( ws.getOurSide() );

	for ( int i = 0; i < NUM_TEAMS; i++ ) {
	    setScore( i, ws.getScore( i ) );
	    setTeamName( i, ws.getTeamName( i ) );
	}

	for ( int i = 0; i < MAX_PLAYERS; i++ ) {
	    players[ i ].copy( ws.players[ i ] );
	}
	ball.copy( ws.ball );

	return this;
    }

    /**
     * Set current cycle
     * @param time time in cycles
     */
    public void updateTime( int time )
    {
	this.time = time;
    }

    /**
     * Get current cycle
     * @return current time in cycles
     */
    public int getTime()
    {
	return time;
    }

    /**
     * Set play mode
     * @param playMode play mode
     */
    public void setPlayMode( int playMode )
    {
	this.playMode = playMode;
    }

    /**
     * Get play mode
     * @return current play mode
     */
    public int getPlayMode()
    {
	return playMode;
    }

    /**
     * Get play mode as String
     * @return current play mode text label
     */
    public String getPlayModeString()
    {
	return Utils.getPlayModeString( getPlayMode() );
    }

    /** 
     * Set perspective
     * @param side side of observer
     */
    public void setSide( int side )
    {
	if ( side == SIDE_LEFT ) {
	    ourSide = SIDE_LEFT;
	    oppSide = SIDE_RIGHT;
	}
	else if ( side == SIDE_RIGHT ) {
	    ourSide = SIDE_RIGHT;
	    oppSide = SIDE_LEFT;
	}
	else {
	    ourSide = SIDE_ILLEGAL;
	    oppSide = SIDE_ILLEGAL;
	}
    }

    /**
     * Get perspective
     * @return side of observer
     */
    public int getOurSide()
    {
	return ourSide;
    }

    /**
     * Get opponent's side
     * @return side of observer's opponent
     */
    public int getOppSide()
    {
	return oppSide;
    }

    /**
     * Get side from team name
     * @param teamName name of team as sent to server
     * @return side of given team
     */
    public int getSideFromTeamName( String teamName )
    {
	if ( teamName.equals( teamNames[ SIDE_LEFT ] ) )
	    return SIDE_LEFT;
	if ( teamName.equals( teamNames[ SIDE_RIGHT ] ) )
	    return SIDE_RIGHT;
	return SIDE_ILLEGAL;
    }

    /**
     * Set perspective to the side corresponding to the given team name
     * @param teamName name of team
     */
    public void setSideFromTeamName( String teamName )
    {
	setSide( getSideFromTeamName( teamName ) );
    }

    /** 
     * Set team name for a given side
     * @param side side of team to update
     * @param teamName name of team
     */
    public void setTeamName( int side, String teamName )
    {
	teamNames[ side ] = teamName;
    }

    /** 
     * Set name of left team
     * @param teamName name of team
     */
    public void setLeftTeamName( String teamName )
    {
	setTeamName( SIDE_LEFT, teamName );
    }

    /** 
     * Set name of left team
     * @param teamName name of team
     */
    public void setRightTeamName( String teamName )
    {
	setTeamName( SIDE_RIGHT, teamName );
    }

    /** 
     * Set our team's name
     * @param teamName name of team
     */
    public void setOurTeamName( String teamName )
    {
	setTeamName( getOurSide(), teamName );
    }

    /** 
     * Set opponent team's name
     * @param teamName name of team
     */
    public void setOppTeamName( String teamName )
    {
	setTeamName( getOppSide(), teamName );
    }

    /** 
     * Get our team name for given side
     * @param side side of team
     * @return name of team
     */
    public String getTeamName( int side )
    {
	return teamNames[ side ];
    }

    /** 
     * Get left team's name
     * @return name of team
     */
    public String getLeftTeamName()
    {
	return getTeamName( SIDE_LEFT );
    }

    /** 
     * Get right team's name
     * @return name of team
     */
    public String getRightTeamName()
    {
	return getTeamName( SIDE_RIGHT );
    }

    /** 
     * Get our team's name
     * @return name of team
     */
    public String getOurTeamName()
    {
	return getTeamName( getOurSide() );
    }

    /** 
     * Get opponent team's name
     * @return name of team
     */
    public String getOppTeamName()
    {
	return getTeamName( getOppSide() );
    }

    /** 
     * Get team name of given player
     * @param id player ID
     * @return name of team
     */
    public String getTeamNameFromPlayer( int id )
    {
	return getTeamName( Utils.getSideFromID( id ) );
    }
    
    /** 
     * Set score of given team
     * @param side side of team
     * @param score new score
     */
    public void setScore( int side, int score )
    {
	scores[ side ] = score;
    }

    /** 
     * Set score of left team
     * @param score new score
     */
    public void setLeftScore( int score )
    {
	setScore( SIDE_LEFT, score );
    }

    /** 
     * Set score of right team
     * @param score new score
     */
    public void setRightScore( int score )
    {
	setScore( SIDE_RIGHT, score );
    }
    
    /** 
     * Set score of our team
     * @param score new score
     */
    public void setOurScore( int score )
    {
	setScore( getOurSide(), score );
    }

    /** 
     * Set score of opponent team
     * @param score new score
     */
    public void setOppScore( int score )
    {
	setScore( getOppSide(), score );
    }

    /** 
     * Get score of given team
     * @param side side of team
     * @return score in goals
     */
    public int getScore( int side )
    {
	return scores[ side ];
    }

    /** 
     * Get score of left team
     * @return score in goals
     */
    public int getLeftScore()
    {
	return getScore( SIDE_LEFT );
    }

    /** 
     * Get score of right team
     * @return score in goals
     */
    public int getRightScore()
    {
	return getScore( SIDE_RIGHT );
    }

    /** 
     * Get score of our team
     * @return score in goals
     */
    public int getOurScore()
    {
	return getScore( getOurSide() );
    }

    /** 
     * Get score of opponent team
     * @return score in goals
     */
    public int getOppScore()
    {
	return getScore( getOppSide() );
    }

    /** 
     * Get difference between our score and opponent's score
     * @return score difference in goals
     */
    public int getGoalDifference()
    {
	return getOurScore() - getOppScore();
    }

    /**
     * Is player our teammate?
     * @param id player ID
     * @return <code>true</code> if player is on our team
     */
    public boolean isTeammate( int id )
    {
	return Utils.isPlayer( id ) &&
	    Utils.getSideFromID( id ) == getOurSide();
    }

    /**
     * Is player our opponent?
     * @param id player ID
     * @return <code>true</code> if player is on opponent team
     */
    public boolean isOpponent( int id )
    {
	return Utils.isPlayer( id ) &&
	    Utils.getSideFromID( id ) == getOppSide();
    }

    /**
     * Get player ID of teammate with given uniform number
     * @param unum uniform number 1-11
     * @return playerID
     */
    public int getTeammateID( int unum )
    {
	return Utils.getPlayerID( getOurSide(), unum );
    }

    /**
     * Get player ID of opponent with given uniform number
     * @param unum uniform number 1-11
     * @return playerID
     */
    public int getOpponentID( int unum )
    {
	return Utils.getPlayerID( getOppSide(), unum );
    }

    /**
     * Is player "dead" or waiting on sidelines?
     * @param id player ID
     * @return <code>true</code> if player is at y coordinate for sidelines
     */
    public boolean isOnSidelines( int id )
    {
	return getPlayerPosition( id ).getY() == SIDELINES_Y;
    }
    
    /**
     * Update ball information
     * @param pos new ball position
     * @param vel new ball velocity
     */
    public void updateBall( VecPosition pos, VecPosition vel )
    {
	ball.setTimeLastSeen( getTime() );
	ball.setPosition( pos );
	ball.setVelocity( vel );
    }

    /**
     * Update player information for given player
     * @param id player ID
     * @param pos new player position
     * @param vel new player velocity
     * @param angBody new body angle
     * @param angNeck new neck angle
     */
    public void updatePlayer( int id, 
			      VecPosition pos, VecPosition vel, 
			      double angBody, double angNeck )
    {
	PlayerObject player = players[ id ];

	player.setTimeLastSeen( getTime() );
	player.setPosition( pos );
	player.setVelocity( vel );
	player.setBodyAngle( angBody );
	player.setNeckAngle( angNeck );
    }    

    /**
     * Get position of the ball
     * @return position of the ball
     */
    public VecPosition getBallPosition()
    {
	return ball.getPosition();
    }

    /**
     * Get number of cycles that have passed since the ball
     * information was updated
     * @return cycles
     */
    public int getTimeSinceSeenBall()
    {
	return getTime() - ball.getTimeLastSeen();
    }

    /**
     * Get position of given player
     * @param id player ID
     * @return position of player
     */
    public VecPosition getPlayerPosition( int id )
    {
	return players[ id ].getPosition();
    }

    /**
     * Get number of cycles that have passed since the
     * information for the given player was updated
     * @param id player ID
     * @return cycles
     */
    public int getTimeSinceSeenPlayer( int id )
    {
	return getTime() - players[ id ].getTimeLastSeen();
    }

    /**
     * Get position of given object
     * @param id object ID
     * @return position of object
     */
    public VecPosition getPosition( int id )
    {
	if ( Utils.isBall( id ) ) 
	    return getBallPosition();
	if ( Utils.isPlayer( id ) )
	    return getPlayerPosition( id );
	if ( Utils.isLeftGoal( id ) )
	    return new VecPosition( -PITCH_LENGTH / 2, 0 );
	if ( Utils.isRightGoal( id ) )
	    return new VecPosition(  PITCH_LENGTH / 2, 0 );
	return null;
    }

    /**
     * Get velocity of the ball
     * @return velocity of the ball
     */
    public VecPosition getBallVelocity()
    {
	return ball.getVelocity();
    }

    /**
     * Get velocity of the given player
     * @param id player ID
     * @return velocity of player
     */
    public VecPosition getPlayerVelocity( int id )
    {
	return players[ id ].getVelocity();
    }

    /**
     * Get velocity of the given object
     * @param id object ID
     * @return velocity of object
     */
    public VecPosition getVelocity( int id )
    {
	if ( Utils.isBall( id ) ) 
	    return getBallVelocity();
	if ( Utils.isPlayer( id ) )
	    return getPlayerVelocity( id );
	if ( Utils.isGoal( id ) )
	    return new VecPosition();
	return null;
    }

    /**
     * Get body angle of the given player
     * @param id player ID
     * @return angle in degrees
     */
    public double getPlayerBodyAngle( int id )
    {
	return players[ id ].getBodyAngle();
    }

    /**
     * Get neck angle of the given player
     * @param id player ID
     * @return angle in degrees
     */
    public double getPlayerNeckAngle( int id )
    {
	return players[ id ].getNeckAngle();
    }

    /**
     * Get heterogenous player type of the given player
     * @param id player ID
     * @return heterogenous player type
     */
    public int getHeteroPlayerType( int id )
    {
	return players[ id ].getHeteroPlayerType();
    }

    /**
     * Is the ball close enough to the given player to be kicked?
     * @param id player ID
     * @return <code>true</code> if ball is kickable
     */
    public boolean isBallKickableBy( int id )
    {
	return getBallPosition().getDistanceTo( getPlayerPosition( id ) ) <
	    SP.kickable_margin + SP.player_size + SP.ball_size;
    }

}

//////////////////
// OBJECTS
//////////////////

abstract class DynamicObject implements Serializable
{
    protected VecPosition pos;
    protected VecPosition vel;
    protected int timeLastSeen;
    
    public DynamicObject()
    {
	pos = new VecPosition();
	vel = new VecPosition();
	timeLastSeen = -1;
    }

    public void copy( DynamicObject obj )
    {
	setPosition( obj.pos );
	setVelocity( obj.vel );
	setTimeLastSeen( obj.timeLastSeen );
    }

    public void setPosition( VecPosition pos )
    {
	this.pos.copy( pos );
    }

    public VecPosition getPosition()
    {
	return new VecPosition( pos );
    }

    public void setVelocity( VecPosition vel )
    {
	this.vel.copy( vel );
    }

    public VecPosition getVelocity()
    {
	return new VecPosition( vel );
    }

    public int getTimeLastSeen()
    {
	return timeLastSeen;
    }

    public void setTimeLastSeen( int time )
    {
	timeLastSeen = time;
    }
}

class BallObject 
    extends DynamicObject 
    implements Serializable
{
    public String toString()
    {
	return "(ball pos:" + getPosition() +
	    " vel: " + getVelocity() + ")";
    }
}

class PlayerObject 
    extends DynamicObject 
    implements Serializable
{
    private boolean isGoalie;
    private int heteroPlayerType;
    private double angBody;
    private double angNeck;

    public void copy( PlayerObject obj )
    {
	super.copy( obj );
	setIsGoalie( obj.getIsGoalie() );
	setHeteroPlayerType( obj.getHeteroPlayerType() );
	setBodyAngle( obj.getBodyAngle() );
	setNeckAngle( obj.getNeckAngle() );
    }

    public void setIsGoalie( boolean isGoalie )
    {
	this.isGoalie = isGoalie;
    }

    public boolean getIsGoalie()
    {
	return isGoalie;
    }

    public void setHeteroPlayerType( int index )
    {
	heteroPlayerType = index;
    }

    public int getHeteroPlayerType()
    {
	return heteroPlayerType;
    }

    public void setBodyAngle( double angBody )
    {
	this.angBody = angBody;
    }

    public double getBodyAngle()
    {
	return angBody;
    }

    public void setNeckAngle( double angNeck )
    {
	this.angNeck = angNeck;
    }

    public double getNeckAngle()
    {
	return angNeck;
    }
}

