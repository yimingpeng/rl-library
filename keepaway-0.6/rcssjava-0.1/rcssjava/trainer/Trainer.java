package rcssjava.trainer;

import java.util.*;
import java.io.*;
import rcssjava.*;
import rcssjava.comm.*;
import static rcssjava.SoccerTypes.*;

/**
 * Trainer main execution loop.  Most of the work is actually
 * done outside this class by the specific Task that is 
 * chosen.
 * @author Gregory Kuhlmann
 */
public class Trainer
{
    final int queueCapacity = 1100;

    ServerParams SP;
    WorldState WS;
    TrainerCommandHandler CMD;
    Connection C;
    RCSSParser parser;
    String queueFile;
    Task task;
    LinkedList<WorldState> cycleQueue;

    Trainer( ServerParams sp, WorldState ws,
	     Connection c,
	     TrainerCommandHandler cmd,
	     RCSSParser parser,
	     String queueFile,
	     Task task )
    {
	SP = sp;
	WS = ws;
	C = c;
	CMD = cmd;
        this.parser = parser;
	this.queueFile = queueFile;
	this.task = task;

	cycleQueue = new LinkedList<WorldState>();
    }

    private void saveCycle()
    {
	WorldState tmp = new WorldState( SP );
	tmp.copy( WS );
	cycleQueue.addLast( tmp );
	if ( cycleQueue.size() > queueCapacity )
	    cycleQueue.removeFirst();
    }

    private void writeCycles()
    {
	try {
	    ObjectOutputStream oos = 
		new ObjectOutputStream( new FileOutputStream( queueFile ) );
	    oos.writeObject( cycleQueue );
	}
	catch ( Exception e ) {
	    System.err.println( "Unable to write World State queue: " + e );
	}
    }

    private void mainLoop() 
    {
	final double version = 9.0;
	String reply;

	CMD.init( version );
	reply = C.receive();
	if ( !reply.equals( "(init ok)" ) ) {
	    System.err.println( "Unable to init: " + reply );
	    System.exit( 1 );
	}
	
	CMD.eye( true );

	int time = 0;
	boolean firstSeen = true;
	while ( true ) {
	    reply = C.receive();
	    //System.out.println( reply );
	    parser.analyzeMessage( reply );
	    // Wait until at least one see message before initting
	    if ( firstSeen &&
		 !WS.getLeftTeamName().equals( "" ) ) {
		firstSeen = false;
		task.init();
	    }
	    if ( WS.getTime() > time ) {
		time = WS.getTime();
		saveCycle();
		if ( !task.processCycle() ) {
		    if ( queueFile != null )
			writeCycles();
		    System.exit( 1 );
		}
		if ( SP.synch_mode == 1 )
		    CMD.done();
	    }
	    if ( WS.getPlayMode() == PM_TimeOver )
		break;
	}

	C.disconnect();
	System.out.println( "Shutting down trainer." );
    }

    private static void printUsage()
    {
	System.out.println( "RCSSJava Trainer\n" );
	System.out.println( "Commandline options:" );
	System.out.println( "-help                  - Print this usage message" );
	System.out.println( "-host <hostname>       - Host name or IP of Soccer Server" );
	System.out.println( "-port <int>            - UDP port on host to connect to" );
	System.out.println( "-keepers <int>         - Number of keepers" );
	System.out.println( "-takers <int>          - Number of takers" );
	System.out.println( "-width <size in m>     - Width of play region" );
	System.out.println( "-length <size in m>    - Length of play region" );
	System.out.println( "-kwy <filename>        - Save episode durations to logfile" );
	System.out.println( "-queue_file <filename> - Save cycles to queue file" );
	System.out.println( "-monitor <0/1>         - Set to 1 to launch monitor" );
    }

    /**
     * Main method
     */
    public static void main( String[] args )
    {
	String hostName = "localhost";
	int trainerPort = 5801;
	boolean launchMonitor = false;
	String queueFile = null;

	int numKeepers = 3;
	int numTakers = 2;
	double kawayWidth = 20;
	double kawayLength = 20;
	String kwyFile = null;

	try {
	    for ( int i = 0; i < args.length; i += 2 ) {
		if ( args[ i ].equals( "-help" ) ) {
		    printUsage();
		    System.exit( 0 );
		}
		else if ( args[ i ].equals( "-host" ) ) {
		    hostName = args[ i + 1 ];
		}
		else if ( args[ i ].equals( "-port" ) ) {
		    trainerPort = Integer.parseInt( args[ i + 1 ] );
		}
		else if ( args[ i ].equals( "-monitor" ) ) {
		    launchMonitor = args[ i + 1 ].equals( "1" );
		}
		else if ( args[ i ].equals( "-queue_file" ) ) {
		    queueFile = args[ i + 1 ];
		}
		else if ( args[ i ].equals( "-keepers" ) ) {
		    numKeepers = Integer.parseInt( args[ i + 1 ] );
		}
		else if ( args[ i ].equals( "-takers" ) ) {
		    numTakers = Integer.parseInt( args[ i + 1 ] );
		}
		else if ( args[ i ].equals( "-width" ) ) {
		    kawayWidth = Double.parseDouble( args[ i + 1 ] );
		}
		else if ( args[ i ].equals( "-length" ) ) {
		    kawayLength = Double.parseDouble( args[ i + 1 ] );
		}
		else if ( args[ i ].equals( "-kwy" ) ) {
		    kwyFile = args[ i + 1 ];
		}
		else {
		    System.err.println( "Unknown option: " + args[ i ] );
		}
	    }
	}
	catch ( Exception e ) {
	    System.err.println( "Unable to parse commandline options: " + e );
	    System.exit( 1 );
	}

	ServerParams sp = new ServerParams();
	WorldState ws = new WorldState( sp );
	Connection c = new Connection( hostName, trainerPort );
	TrainerCommandHandler cmd = new TrainerCommandHandler( c, ws );
	RCSSParser parser = new RCSSParser( ws, sp );

	// Change the task here
	Task task = new KeepawayTask( sp, ws, cmd, launchMonitor,
				      numKeepers, numTakers,
				      kawayWidth, kawayLength,
				      kwyFile );
	Trainer trainer = new Trainer( sp, ws, c, cmd, parser, queueFile, task );
	trainer.mainLoop();
    }
}


