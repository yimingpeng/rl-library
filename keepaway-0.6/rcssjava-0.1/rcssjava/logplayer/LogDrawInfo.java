package rcssjava.logplayer;

import java.io.*;
import java.util.*;
import rcssjava.monitor.*;

/**
 * Container for logdraw shapes
 * @author Gregory Kuhlmann
 */
class LogDrawInfo
{
    private Vector<Map<String, Vector<FieldShape>>> frames;
    private Set<String> idents;

    public LogDrawInfo( int numCycles )
    {
	frames = new Vector<Map<String, Vector<FieldShape>>>( numCycles );
	for ( int i = 0; i < numCycles; i++ ) {
	    frames.add( new HashMap<String, Vector<FieldShape>>() );
	}
	idents = new HashSet<String>();
    }

    public void add( int cycle, String ident, FieldShape fs )
    {
	idents.add( ident );
	Vector<FieldShape> info = getInfo( cycle, ident );
	if ( info == null ) {
	    info = new Vector<FieldShape>();
	}
	info.add( fs );
	frames.elementAt( cycle ).put( ident, info );
    }

    public String[] getIdents()
    {
	return idents.toArray( new String[ 0 ] );
    }

    public Vector<FieldShape> getInfo( int cycle, String ident )
    {
	return frames.elementAt( cycle ).get( ident );
    }
}
