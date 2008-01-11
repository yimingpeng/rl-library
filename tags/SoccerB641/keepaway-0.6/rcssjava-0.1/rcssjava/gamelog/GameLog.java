package rcssjava.gamelog;

import java.io.*;
import rcssjava.*;
import static rcssjava.SoccerTypes.*;

/**
 * Parses a game log (rcg file) and uses it to update a world state
 */
public class GameLog 
{
    final int REC_VERSION_NONE = 0;
    final int REC_VERSION_OLD = 1;
    final int REC_VERSION_2 = 2;
    final int REC_VERSION_3 = 3;

    final int NO_INFO = 0;
    final int SHOW_MODE = 1;
    final int MSG_MODE = 2;
    final int DRAW_MODE = 3;
    final int BLANK_MODE = 4;
    final int PM_MODE = 5;
    final int TEAM_MODE = 6;
    final int PT_MODE = 7;
    final int PARAM_MODE = 8;
    final int PPARAM_MODE = 9;

    private WorldState WS;
    private InputStream logFile;
    private int logVersion;

    private BallT ballT;
    private PlayerParamsT playerParamsT;
    private PlayerT playerT;
    private PlayerTypeT playerTypeT;
    private ServerParamsT serverParamsT; 
    private ShortShowinfoT2 shortShowinfoT2;
    private TeamT[] teams;

    /**
     * Open the given game log and construct a parser for it
     * @param filename name of .rcg file to open
     */
    public GameLog( String filename )   
    {
	try {
	    logFile = new FileInputStream( filename );
	}
	catch( FileNotFoundException e ) {
	    System.err.println( "GameLog: File not found: " + filename );
	    System.exit( 1 );
	}

	logVersion = REC_VERSION_NONE;
	readVersion();

	ballT = new BallT();
	playerParamsT = new PlayerParamsT();
	playerT = new PlayerT();
	playerTypeT = new PlayerTypeT();
	serverParamsT = new ServerParamsT(); 
	shortShowinfoT2 = new ShortShowinfoT2();
	teams = new TeamT[ 2 ];
	for ( int i = 0; i < 2; i++ ) {
	    teams[ i ] = new TeamT();
	}
    }

    /**
     * Set the world state.  The world state must be set before
     * you start reading a log file.
     * @param WS world state
     */
    public void setWorldState( WorldState WS )
    {
	this.WS = WS;
    }

    private int s2i( short s )
    {
	return (int) s;
    }

    private int l2i( long l )
    {
	return (int) Math.round( l2d( l ) );
    }

    private double l2d( long l )
    {
	return l / 65536.0;
    }

    private void readVersion()
    {
	byte[] buffer = new byte[128];
	
	try {
	    logFile.read( buffer, 0, 4 );
	}
	catch ( IOException e ) {
	    System.err.println( "GameLog: Unable to read version number" );
	    System.exit( 1 );
	}

	String s = new String( buffer, 0, 4 );
	if ( s.substring( 0, 3 ).equals( "ULG" ) ) {
	    logVersion = (int) buffer[ 3 ];
	}
	else {
	    logVersion = REC_VERSION_OLD;
	}
    }

    private void readPlayMode()
    {
	byte pmode = Struct.readByte( logFile );
	updatePlayMode( pmode );
    }

    private void readTeams()
    {
	for ( int i = 0; i < 2; i++ ) {
	    teams[ i ].read( logFile );
	}
	updateTeams( teams );
    }

    private void readShowInfo()
    {
	shortShowinfoT2.read( logFile );
	updateShowInfo( shortShowinfoT2 );
    }

    private void readMessage()
    {
	short board = Struct.readShort( logFile, false );
	short len = Struct.readShort( logFile, false );
	byte[] msg = new byte[ 2048 ];
	try {
	    logFile.read( msg, 0, s2i( len ) );
	}
	catch ( IOException e ) {
	    System.err.println( e );
	    return;
	}
	updateMessage( board, msg, len );
    }

    private void readServerParams()
    {
	serverParamsT.read( logFile );
	updateServerParams( serverParamsT );
    }

    private void readPlayerParams()
    {
	playerParamsT.read( logFile );
	updatePlayerParams( playerParamsT );
    }

    private void readHeteroPlayerType()
    {
	playerTypeT.read( logFile );
	// Kludge to eat up extra byte in struct
	Struct.readShort( logFile, false );	
	updateHeteroPlayerType( playerTypeT );
    }

    /**
     * Read next record from log file
     * @return <code>true</code> if successful
     */
    public boolean readNext()
    {
	if ( logVersion == REC_VERSION_3 ) {
	    short mode = Struct.readShort( logFile, false );
	    if ( mode == 0 )
		mode = Struct.readShort( logFile, false );

	    switch ( s2i( mode ) ) {
	    case PM_MODE:
		readPlayMode();
		break;
	    case TEAM_MODE:
		readTeams();
		break;
	    case SHOW_MODE:
		readShowInfo();
		break;
	    case MSG_MODE:
		readMessage();
		break;
	    case PARAM_MODE:
		readServerParams();
		break;
	    case PPARAM_MODE:
		readPlayerParams();
		break;
	    case PT_MODE:
		readHeteroPlayerType();
		break;
	    default:
		if ( s2i( mode ) != WS.getTime() )
		    System.err.println( "GameLog: Invalid mode: " + 
					s2i( mode ) );
		return false;
	    }
	}
	else { // logVersion != REC_VERSION_3
	    System.err.println( "GameLog: Bad log version: " + logVersion );
	    return false;
	}
	
	return true;
    }

    private void updateServerParams( ServerParamsT sp )
    {
    }

    private void updatePlayerParams( PlayerParamsT pp )
    {
    }

    private void updateHeteroPlayerType( PlayerTypeT pt )
    {
    }

    private void updatePlayMode( byte pm )
    {
	WS.setPlayMode( pm );
    }

    private void updateTeams( TeamT[] teams )
    {
	WS.setLeftTeamName(  new String( teams[ SIDE_LEFT  ].name ).trim() );
	WS.setRightTeamName( new String( teams[ SIDE_RIGHT ].name ).trim() );
	WS.setLeftScore(  s2i( teams[ SIDE_LEFT  ].score ) );
	WS.setRightScore( s2i( teams[ SIDE_RIGHT ].score ) );
    }

    private void updateShowInfo( ShortShowinfoT2 si )
    {
	WS.updateTime( s2i( si.time ) );

	// Get ball info
	BallT b = si.ball;
	VecPosition bpos = new VecPosition( l2d( b.x ), l2d( b.y ) );
	VecPosition bvel = new VecPosition( l2d( b.deltax ), l2d( b.deltay ) );

// 	if ( WS.getSide() == SIDE_RIGHT ) {
// 	    bpos = bpos.negate();
// 	    bvel = bvel.negate();
// 	}
	
	WS.updateBall( bpos, bvel );

	// Get player info
	for ( int i = 0; i < MAX_PLAYERS; i++ ) {
	    PlayerT p = si.pos[ i ];
	    VecPosition ppos = new VecPosition( l2d( p.x ), l2d( p.y ) );
	    VecPosition pvel = new VecPosition( l2d( p.deltax ), l2d( p.deltay ) );
	    double angBody = Math.toDegrees( l2d( p.body_angle ) );
	    double angNeck = Math.toDegrees( l2d( p.head_angle ) );

// 	    if ( WS.getSide() == SIDE_RIGHT ) {
// 		ppos = ppos.negate();
// 		pvel = pvel.negate();
// 		angBody = Utils.normalizeAngle( angBody + 180 );
// 		angNeck = Utils.normalizeAngle( angNeck + 180 );
// 	    }

	    WS.updatePlayer( i, ppos, pvel, angBody, angNeck );
	}
    }

    private void updateMessage( short board, byte[] msg, short len )
    {
	/* Ignore */
    }
}
