package rcssjava.comm;

import rcssjava.*;
import static rcssjava.SoccerTypes.*;

/**
 * Parses incoming server messages and uses them to update the
 * supplied world state. Contains private methods to handle each
 * type of message.
 * @author Gregory Kuhlmann
 */
public class RCSSParser
{
    private WorldState WS;
    private ServerParams SP;

    /**
     * Constructs a parser using the given world state and
     * server params
     * @param WS world state connected to this parser
     * @param SP server parameters
     */
    public RCSSParser( WorldState WS, ServerParams SP )
    {
	this.WS = WS;
	this.SP = SP;
    }

    /**
     * Parse a server message and update the world state
     * @param msg server message
     * @return <code>true</code> if successful
     */
    public boolean analyzeMessage( String msg )
    {
	if ( msg == null || msg.length() < 2 )
	    return false;

	switch ( msg.charAt( 1 ) ) {
	case 'c':
	    return analyzeCLangVersionMessage( msg );
	case 'o':
	    // ok
	    return true;
	case 's':
	    switch( msg.charAt( 3 ) ) {
	    case 'e':
		return analyzeSeeGlobalMessage( msg );
	    case 'r': 
		return analyzeServerParamMessage( msg );
	    default: 
		break;
	    }
	case 'i':     
	    return analyzeInitMessage( msg );
	case 'h':     
	    return analyzeHearMessage( msg );
	case 'p':    
	    if ( msg.charAt( 8 ) == 't' )
		return analyzePlayerTypeMessage( msg );
	    else
		return analyzePlayerParamMessage( msg );
	case 'e':     
	    System.err.println( msg );
	    break;
	case 't':  //think - don't need to do anything
	    break;
	default:
	    System.err.println( "Ignored message: " + msg ); 
	    return false;
	}
	return true;
    }

    private boolean analyzeCLangVersionMessage( String msg )
    {
	// ignore
	return true;
    }

    private boolean analyzeSeeGlobalMessage( String msg )
    {
	int pos, next;

	pos = 12;
	next = msg.indexOf( ' ', pos );
	int time = Integer.parseInt( msg.substring( pos, next ) );
	pos = next;
	WS.updateTime( time );
	
	double x, y, vx, vy, angBody, angNeck;
	while ( msg.charAt( pos ) != ')' ) {
	    pos += 3;
	    switch ( msg.charAt( pos ) ) {
	    case 'b':
		pos += 3;
		next = msg.indexOf( ' ', pos );
		x = Double.parseDouble( msg.substring( pos, next ) );
		pos = next + 1;
		next = msg.indexOf( ' ', pos );
		y = Double.parseDouble( msg.substring( pos, next ) );
		pos = next + 1;
		next = msg.indexOf( ' ', pos );
		vx = Double.parseDouble( msg.substring( pos, next ) );
		pos = next + 1;
		next = msg.indexOf( ')', pos );
		vy = Double.parseDouble( msg.substring( pos, next ) );
		pos = next + 1;
		WS.updateBall( new VecPosition( x, y ),
			       new VecPosition( vx, vy ) );
		break;
	    case 'p':
		pos += 3;
		next = msg.indexOf( '\"', pos );
		String team = msg.substring( pos, next );
		int side = WS.getSideFromTeamName( team );
		if ( side == SIDE_ILLEGAL ) {
		    if ( WS.getTeamName( SIDE_LEFT ).equals( "" ) ) {
			side = SIDE_LEFT;
		    }
		    else if ( WS.getTeamName( SIDE_RIGHT ).equals( "" ) ) {
			side = SIDE_RIGHT;
		    }
		    WS.setTeamName( side, team );
		}
		pos = next + 2;
		int space = msg.indexOf( ' ', pos );
		int paren = msg.indexOf( ')', pos );
		int unum;
		boolean goalie;
		if ( space != -1 && space < paren ) {
		    unum = Integer.parseInt( msg.substring( pos, space ) );
		    pos = msg.indexOf( ')', space ) + 2;
		    goalie = true;
		}
		else {
		    unum = Integer.parseInt( msg.substring( pos, paren ) );
		    pos = paren + 2;
		    goalie = false;
		}
		next = msg.indexOf( ' ', pos );
		x = Double.parseDouble( msg.substring( pos, next ) );
		pos = next + 1;
		next = msg.indexOf( ' ', pos );
		y = Double.parseDouble( msg.substring( pos, next ) );
		pos = next + 1;
		next = msg.indexOf( ' ', pos );
		vx = Double.parseDouble( msg.substring( pos, next ) );
		pos = next + 1;
		next = msg.indexOf( ' ', pos );
		vy = Double.parseDouble( msg.substring( pos, next ) );
		pos = next + 1;
		next = msg.indexOf( ' ', pos );
		angBody = Double.parseDouble( msg.substring( pos, next ) );
		pos = next + 1;
		next = msg.indexOf( ')', pos );
		angNeck = Double.parseDouble( msg.substring( pos, next ) );
		pos = next + 1;
		//gjk add isGoalie
		WS.updatePlayer( Utils.getPlayerID( side, unum ), new VecPosition( x, y ),
				 new VecPosition( vx, vy ),
				 angBody, angNeck );
		break;
	    case 'g':
		// ignore
		pos = msg.indexOf( ')', pos );
		pos = msg.indexOf( ')', pos + 1 );
		pos++;
		break;
	    default:
		System.err.println( "Error parsing see global message at: " +
				    msg.substring( pos, msg.length() ) );
		return false;
	    }
	}
	return true;
    }

    private boolean analyzeServerParamMessage( String msg )
    {
	int pos, next;

	pos = 14;
	while ( msg.charAt( pos ) != ')' ) {
	    pos++;
	    next = msg.indexOf( ' ', pos );
	    String param = msg.substring( pos, next );
	    pos = next + 1;
	    next = msg.indexOf( ')', pos );
	    String value = msg.substring( pos, next );
	    pos = next + 1;
	    SP.setParam( param, value );
	    //System.out.println( "SP: " + param + " = " + 
	    //			SP.getParam( param ) );
	}
	return true;
    }
    
    private boolean analyzeInitMessage( String msg )
    {
	if ( msg.charAt( 6 ) == 'l' )
	    WS.setSide( SIDE_LEFT );
	else if ( msg.charAt( 6 ) == 'r' )
	    WS.setSide( SIDE_RIGHT );
	else {
	    WS.setSide( SIDE_ILLEGAL );
	    return false;
	}
	return true;
    }

    private boolean analyzeHearMessage( String msg )
    {
	int pos, next;

	pos = 6;
	next = msg.indexOf( ' ', pos );
	int time = Integer.parseInt( msg.substring( pos, next ) );
	pos = next + 1;
	//WS.updateTime( time );

	//System.out.println( msg );
	switch ( msg.charAt( pos ) ) {
	case '(': 
	    //ignore player messages
	    break;
	case 'r':
	    pos = msg.indexOf( ' ', pos ) + 1;
	    next = msg.indexOf( ')', pos );
	    String pmode = msg.substring( pos, next );
	    WS.setPlayMode( getPlayModeFromString( pmode ) );
	    break;
	default:
	    System.err.println( "Error parsing hear message: " + msg );
	    return false;
	}

	return true;
    }

    private int getPlayModeFromString( String s )
    {
	for ( int i = 0; i < PM_MAX; i++ ) {
	    if ( s.equals( PLAYMODE_STRINGS[ i ] ) )
		return i;
	}
	return PM_Null;
    }

    private boolean analyzePlayerTypeMessage( String msg )
    {
	// ignore
	return true;
    }

    private boolean analyzePlayerParamMessage( String msg )
    {
	int pos, next;

	pos = 14;
	while ( msg.charAt( pos ) != ')' ) {
	    pos ++;
	    next = msg.indexOf( ' ', pos );
	    String param = msg.substring( pos, next );
	    pos = next + 1;
	    next = msg.indexOf( ')', pos );
	    String value = msg.substring( pos, next );
	    pos = next + 1;
	    SP.setParam( param, value );
	    //System.out.println( "PP: " + param + " = " + 
	    //			SP.getParam( param ) );
	}
	return true;
    }
}

