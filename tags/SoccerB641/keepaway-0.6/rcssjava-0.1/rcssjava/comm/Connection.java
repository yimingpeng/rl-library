package rcssjava.comm;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Handles UDP communication with the Soccer Server
 * @author Gregory Kuhlmann, Yaxin Liu
 */
public class Connection
{
    /** Maximum message size in bytes */
    public final static int MAX_MSG = 4096;

    private DatagramSocket socket;
    private InetAddress hostIP;
    private int hostPort;

    /**
     * Creates a socket, but doesn't connect to anything
     */
    public Connection()
    {
	try {
	    socket = new DatagramSocket();
	    // five seconds timeout for checking server: YL
	    socket.setSoTimeout( 5000 );  
	}
	catch ( SocketException e ) {
	    System.err.println( "Connection: Could not bind local UDP socket" );
	    System.exit( 1 );
	}
    }

    /**
     * Creates a socket and connects to host
     * @param hostName name or IP address of host
     * @param hostPort port number on host
     */
    public Connection( String hostName, int hostPort )
    {
	this();
	if ( !connect( hostName, hostPort ) ) {
	    System.err.println( "Connection: Could not create connection with " +
				hostName + ":" + hostPort );
	    System.exit( 1 );
	}
    }

    /**
     * Connect to remote host
     * @param hostName name or IP address of host
     * @param hostPort port number on host
     * @return <code>true</code> if successful
     */
    public boolean connect( String hostName, int hostPort )
    {
	try {
	    hostIP = InetAddress.getByName( hostName );
	    this.hostPort = hostPort;
	}
	catch ( UnknownHostException e ) {
	    System.err.println( "Connection: Unknown host: " + hostName );
	    return false;
	}
	return true;
    }

    /**
     * Disconnect from server
     */
    public void disconnect()
    {
	socket.close();
    }

    /**
     * Is the socket connected?
     */
    public boolean isConnected()
    {
	return ( hostIP != null );
    }

    /**
     * Receive data from the server
     * @return data received as String
     */
    public String receive()
    {       
	byte[] inBuffer = new byte[ MAX_MSG ];
        DatagramPacket inPacket =
            new DatagramPacket( inBuffer, MAX_MSG );
	try {
	    socket.receive( inPacket );
	    hostPort = inPacket.getPort();
	    return new String( inPacket.getData(),
			       inPacket.getOffset(),
			       inPacket.getLength() - 1 );
	}
	catch ( SocketTimeoutException e ) { // YL: check for server disconnect
	    System.err.println( "Connection: server timeout: " + e );
	    System.exit( -1 );
	    return null;  // dummy
	}
	catch ( IOException e ) {
	    System.err.println( "Connection: receive error: " + e );
	    return null;
	}
    }

    /** 
     * Send data to server
     * @param msg String to send
     * @return <code>true</code> if successful
     */
    public boolean send( String msg )
    {
	//System.out.println( "Sending: " + msg );
	msg += '\0';
	DatagramPacket outPacket = 
	    new DatagramPacket( msg.getBytes(), msg.length(),
				hostIP, hostPort );
	try {
	    socket.send( outPacket );
        } 
	catch ( IOException e ) {
	    System.err.println( "Connection: send error: " + e );
	    return false;
        }
	return true;
    }
}
