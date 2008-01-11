package rcssjava.trainer;

import java.io.*;
import java.util.*;
import java.awt.Color;
import rcssjava.*;
import rcssjava.comm.*;
import rcssjava.geom.*;
import rcssjava.monitor.*;
import static rcssjava.SoccerTypes.*;

/**
 * Enforces the keepaway rules and monitors/logs episode durations
 * @author Gregory Kuhlman
 */
public class KeepawayTask 
    implements Task
{
    // This string is sent to the players at the end of each episode
    final String trainingMsg = "ka ";
    // Number of cycles the takers must maintain the ball for a
    // turnover to be called
    final int TURNOVER_TIME = 4;

    ServerParams SP;
    WorldState WS;
    TrainerCommandHandler CMD;
    int numKeepers;
    int numTakers;
    double kawayWidth;
    double kawayLength;
    BufferedWriter bw;
    int epoch;
    Random rand;
    Rectangle region;
    int takeTime;
    int startTime;
    Monitor monitor;
    Vector<FieldShape> shapes;
    Set<Integer> keepers;
    Set<Integer> takers;
    Set<Integer> players;

    /**
     * Constructs a keepaway task with the given options.  
     */
    public KeepawayTask( ServerParams sp,
			 WorldState ws,
			 TrainerCommandHandler cmd,
			 boolean launchMonitor,
			 int numKeepers,
			 int numTakers,
			 double kawayWidth,
			 double kawayLength,
			 String kwyFile )
    {
	SP = sp;
	WS = ws;
	CMD = cmd;

	this.numKeepers = numKeepers;
	this.numTakers = numTakers;
	this.kawayWidth = kawayWidth;
	this.kawayLength = kawayLength;

	if ( kwyFile != null ) {
	    try {
		bw = new BufferedWriter( new FileWriter( kwyFile ) );
	    }
	    catch ( Exception e ) {
		System.err.println( "Unable to create .kwy file: " + e );
		bw = null;
	    }
	}

	epoch = 0;
	rand = new Random();
	region = new Rectangle( kawayWidth, kawayLength );
	takeTime = 0;

	keepers = new HashSet<Integer>();
	for ( int i = 1; i <= numKeepers; i++ )
	    keepers.add( Utils.getPlayerID( SIDE_LEFT, i ) );
	takers = new HashSet<Integer>();
	for ( int i = 1; i <= numTakers; i++ )
	    takers.add( Utils.getPlayerID( SIDE_RIGHT, i ) );
	players = new HashSet<Integer>();
	players.addAll( keepers );
	players.addAll( takers );

	monitor = null;
	shapes = null;
	if ( launchMonitor ) {
	    MonitorParams mp = new MonitorParams();
	    mp.show_center_circle = false;
	    mp.show_middle_line = false;
	    monitor = new Monitor( mp, sp );
	    // draw keepaway region in monitor
	    shapes = new Vector<FieldShape>();
	    shapes.add( new FieldRect( Color.WHITE, false,
				       region.getTopLeft(), region.getBottomRight(), 0 ) );
	}
    }

    /**
     * Initialize task by resetting the field setting playmode to PlayOn
     */
    public void init()
    {
	if ( bw != null )
	    kwyHeader();
	resetField();
	CMD.changeMode( PM_PlayOn );
    }

    /**
     * Enforce rules for the previous cycle
     * @return <code>false</code> if an error occurs
     */
    public boolean processCycle()
    {
	if ( monitor != null ) {
	    monitor.update( WS, shapes );
	}

	if ( !region.isInside( WS.getBallPosition() ) ) {
	    if ( bw != null )
		kwyLogEpisode( 'o' );
	    resetField();
	}
	else if ( takeTime >= TURNOVER_TIME ) {
	    if ( bw != null )
		kwyLogEpisode( 't' );
	    resetField();
	}
	else {
	    boolean keeperPoss = false;
	    for ( int i = 1; i <= numKeepers; i++ ) {
		int id = Utils.getLeftPlayerID( i );
		if ( WS.isBallKickableBy( id ) ) {
		    keeperPoss = true;
		    break;
		}
	    }
	    
	    boolean takerPoss = false;
	    for ( int i = 1; i <= numTakers; i++ ) {
		int id = Utils.getRightPlayerID( i );
		if ( WS.isBallKickableBy( id ) ) {
		    takerPoss = true;
		    break;
		}
	    }
	    
	    if ( takerPoss && !keeperPoss ) {
		    takeTime++;
	    }
	    else {
		takeTime = 0;
	    }
	}	    

	// If I haven't seen one of the players in 50 cycles, quit.
	for ( int id : players )
	    if ( WS.getTimeSinceSeenPlayer( id ) > 50 ) {
		System.err.println( "Haven't seen player " + id + 
				    " in over 50 cycles." );
		return false;
	    }

	// If an episode lasts longer than 1:45, quit.
	if ( WS.getTime() - startTime > 1050 ) {
	    System.err.println( "Episode lasted longer than 1:45--" +
				" that can't be right." );

	    return false;
	}

	return true;
    }

    private void resetField()
    {
	VecPosition pos;
	double buffer = 2;
	double ballBuffer = 4;
	int keeperPos = rand.nextInt( numKeepers );

	region = new Rectangle( region.getWidth(), region.getLength() );
	if ( shapes != null ) {
	    shapes.clear();
	    shapes.add( new FieldRect( Color.WHITE, false,
				       region.getTopLeft(), region.getBottomRight(), 0 ) );
	}
	for ( int i = 1; i <= numKeepers; i++ ) {
	    switch( keeperPos ) {
	    case 0:
		pos = region.getBottomLeft().add( buffer );
		break;
	    case 1:
		pos = region.getTopLeft().add( new VecPosition( -buffer, buffer ) );
		break;
	    case 2:
		pos = region.getTopRight().subtract( buffer );
		break;
	    default:
		pos = region.getCenter().add( rand.nextGaussian() );
		break;
	    }
	    
	    CMD.move( Utils.getLeftPlayerID( i ), pos );
	    keeperPos = ( keeperPos + 1 ) % numKeepers;
	}

	for ( int i = 1; i <= numTakers; i++ ) {
	    pos = region.getBottomRight().add( new VecPosition( buffer, -buffer ) );
	    pos = pos.add( rand.nextGaussian() );
	    CMD.move( Utils.getRightPlayerID( i ), pos );
	}

	pos = region.getBottomLeft().add( ballBuffer );
	CMD.move( ID_BALL, pos, new VecPosition( 0, 0 ) );

	takeTime = 0;
	startTime = WS.getTime();
	epoch++;

	CMD.say( trainingMsg );
    }

    private void kwyHeader()
    {
	try {
	    bw.write( "# Keepers: " + numKeepers + "\n" +
		      "# Takers:  " + numTakers + "\n" +
		      "# Region:  " + kawayLength +
		      " x " + kawayWidth + "\n" );
	    
	    bw.write( "#\n");
	    
	    bw.write( "# Description of Fields:\n" +
		      "# 1) Episode number\n" +
		      "# 2) Start time in simulator steps (100ms)\n" +
		      "# 3) End time in simulator steps (100ms)\n" +
		      "# 4) Duration in simulator steps (100ms)\n" +
		      "# 5) (o)ut of bounds / (t)aken away\n" );

	    bw.write( "#\n" );

	    bw.flush();
	}
	catch ( Exception e ) {
	    System.err.println( "Unable to write to kwy file: " + e );
	}
    }

    private void kwyLogEpisode( char endCond )
    {
	try {
	    bw.write( epoch + "\t" + 
		      startTime + "\t" +
		      WS.getTime() + "\t" +
		      ( WS.getTime() - startTime ) + "\t" +
		      endCond + "\n" );

	    bw.flush();
	}
	catch ( Exception e ) {
	    System.err.println( "Unable to write to kwy file: " + e );
	}
    }
}

