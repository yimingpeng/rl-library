package rcssjava.monitor;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import rcssjava.*;
import static rcssjava.SoccerTypes.*;

/**
 * Graphical visualization of the world state
 * @author Gregory Kuhlmann
 */
public class Monitor
{
    private MonitorParams MP;
    private ServerParams SP;
    private MonitorListener listener;
    private FieldCanvas field;
    private JFrame frame;

    /**
     * Construct monitor with default listener and parameters
     */
    public Monitor()
    {
	this( null );
    }

    /**
     * Construct monitor with default listener and server parameters
     * but with the monitor parameters specified
     * @param MP monitor parameters
     */
    public Monitor( MonitorParams MP )
    {
	this( MP, null );
    }

    /**
     * Construct monitor with the given parameters and the default listener
     * @param MP monitor parameters
     * @param SP server parameters
     */
    public Monitor( MonitorParams MP, ServerParams SP )
    {
	this( MP, SP, null );
    }

    /**
     * Construct monitor with everything specified
     * @param MP monitor parameters
     * @param SP server parameters
     * @param listener class the listens to user input from mouse or keyboard
     */
    public Monitor( MonitorParams MP, ServerParams SP,
		    MonitorListener listener )
    {
	if ( MP == null )
	    this.MP = new MonitorParams();
	else
	    this.MP = MP;

	if ( SP == null )
	    this.SP = new ServerParams();
	else
	    this.SP = SP;

	if ( listener == null ) 
	    this.listener = new MonitorListener( this );
	else
	    this.listener = listener;

	initializeFrame();
    }

    private void createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque( true );

	createFileMenu( menuBar );

        frame.setJMenuBar(menuBar);
    }

    private void createFileMenu( JMenuBar menuBar )
    {
	// file menu
	JMenu menu = new JMenu( "File" );
	menu.setMnemonic( KeyEvent.VK_F );
	menu.getAccessibleContext().setAccessibleDescription( "The File Menu" );
	menuBar.add( menu );

	// file->quit
	JMenuItem menuItem = new JMenuItem( "Quit", KeyEvent.VK_Q );
	menuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Q, 
							 ActionEvent.ALT_MASK ) );
	menuItem.getAccessibleContext().setAccessibleDescription( "Quit the program" );
	menuItem.addActionListener( listener );
	menu.add( menuItem );
    }

    private void initializeFrame()
    {
	// disable eye candy around the window
        JFrame.setDefaultLookAndFeelDecorated( false );

        // create and set up the window
        frame = new JFrame( MP.title );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

	// create menu
	createMenuBar();

	// create field canvas
	field = new FieldCanvas( frame, this );
	field.setPreferredSize( new Dimension( MP.window_size_x, MP.window_size_y ) );
	field.addMouseListener( listener );
	field.addMouseMotionListener( listener );
	frame.getContentPane().add( field );
	field.setVisible( true );

        // display the main frame
        frame.pack();
	frame.addWindowStateListener( listener );
        frame.setVisible( true );

	update( null );
    }

    private Vector<FieldShape> getBallShape( WorldState ws )
    {
	Vector<FieldShape> shapes = new Vector<FieldShape>();

	double radius = 1.3;
	shapes.add( new FieldCircle( MP.color_ball, true,
				     ws.getBallPosition(),
				     MP.ball_radius, MP.depth_ball ) );
	shapes.add( new FieldCircle( MP.color_ball, false,
				     ws.getBallPosition(),
				     radius, MP.depth_ball ) );
	return shapes;
    }

    private Vector<FieldShape> getPlayerShape( WorldState ws, int id )
    {
	Vector<FieldShape> shapes = new Vector<FieldShape>();
	
	if ( !ws.isOnSidelines( id ) || MP.show_sideline_players ) { 
	    VecPosition pos = ws.getPlayerPosition( id );
	    double radius = 1.1;
	    double inner = 0.3;
	    Color body_color = 
		Utils.isLeftPlayer( id ) ? MP.color_team_l : MP.color_team_r;
	    Color unum_color = 
		Utils.isLeftPlayer( id ) ? MP.color_unum_l : MP.color_unum_r;
	    shapes.add( new FieldCircle( body_color, true, 
					 pos, radius,
					 MP.depth_player_body ) );
	    shapes.add( new FieldCircle( MP.color_player_outline, false, 
					 pos, radius,
					 MP.depth_player_outline ) );
	    shapes.add( new FieldCircle( MP.color_player_outline, false, 
					 pos, inner,
					 MP.depth_player_outline ) );
	    VecPosition bodyVector = 
		new VecPosition( radius, ws.getPlayerBodyAngle( id ), true );
	    VecPosition neckVector = 
		new VecPosition( radius, ws.getPlayerNeckAngle( id ) + 
				 ws.getPlayerBodyAngle( id ), true );
	    shapes.add( new FieldLine( MP.color_body_ang,
				       pos, pos.add( bodyVector ),
				       MP.depth_player_decorations ) );
	    shapes.add( new FieldLine( MP.color_neck_ang,
				       pos, pos.add( neckVector ),
				       MP.depth_player_decorations ) );
	    shapes.add( new FieldText( unum_color, pos, 
				       Utils.getUnumFromID( id ) + "",
				       MP.depth_player_unum ) );
	}
	
	return shapes;
    }

    private Vector<FieldShape> getWorldStateShapes( WorldState ws )
    {
	Vector<FieldShape> shapes = new Vector<FieldShape>();

	if ( ws != null ) {
	    if ( MP.show_ball ) {
		shapes.addAll( getBallShape( ws ) );
	    }

	    if ( MP.show_players ) {
		for ( int i = 0; i < MAX_PLAYERS; i++ ) {
		    shapes.addAll( getPlayerShape( ws, i ) );
		}
	    }
	}

	return shapes;
    }

    /**
     * Update visualization to display given world state.  This method
     * must be called every time the objects in the world state
     * move and need to be redrawn.
     */
    public void update( WorldState ws )
    {
	update( ws, null );
    }

    /**
     * Update visualization to display given world state as well
     * as some additional graphics.  This method
     * must be called every time the objects in the world state
     * move and need to be redrawn.
     */
    public void update( WorldState ws, Vector<FieldShape> shapes )
    {
	List<FieldShape> toSend = getWorldStateShapes( ws );
	if ( shapes != null )
	    toSend.addAll( shapes );
	Collections.sort( toSend );

	field.updateCanvasSize();
	field.drawField( MP );
	field.drawShapes( toSend );
	repaint();
    }

    /**
     * Refresh the display using the previously specified world state, if any.
     */
    public void repaint()
    {
	field.repaint();
    }

    /**
     * Terminate monitor 
     */
    public void quit()
    {
	System.exit( 0 );
    }
}

