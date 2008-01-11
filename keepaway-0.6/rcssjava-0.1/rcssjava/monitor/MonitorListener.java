package rcssjava.monitor;

import java.util.*;
import java.awt.event.*;

/**
 * Listens to user events like mouse and keyboard input and 
 * updates monitor accordingly.  This version doesn't do
 * a whole lot, but it can be subclassed to add functionality
 * @author Gregory Kuhlmann
 */
public class MonitorListener 
    implements ActionListener,
	       ItemListener,
	       MouseListener,
	       MouseMotionListener,
	       WindowStateListener
{
    /**
     * Monitor connected to this listener
     */
    protected Monitor monitor;

    /**
     * Connect this listener to the given monitor
     * @param monitor Monitor to be connected to this listener
     */
    public MonitorListener( Monitor monitor )
    {
	this.monitor = monitor;
    }

    public void windowStateChanged( WindowEvent e )
    {
	monitor.repaint();
    }

    public void actionPerformed( ActionEvent e ) 
    {
	String cmd = e.getActionCommand();

	if ( cmd.equals( "Quit" ) ) {
	    monitor.quit();
	}
    }

    public void itemStateChanged( ItemEvent e ) 
    {
    }


    public void mouseClicked( MouseEvent e ) 
    {
	int x = e.getX();
	int y = e.getY();
    }

    public void mouseEntered( MouseEvent e ) 
    {
    }

    public void mouseExited( MouseEvent e ) 
    {
    }

    public void mousePressed( MouseEvent e ) 
    {
	if ( e.getButton() == MouseEvent.BUTTON3 ) {

	    int x = e.getX();
	    int y = e.getY();
	}
    }

    public void mouseReleased( MouseEvent e ) 
    {
	int x = e.getX();
	int y = e.getY();
    }

    public void mouseDragged( MouseEvent e ) 
    {
	    int x = e.getX();
	    int y = e.getY();
    }

    public void mouseMoved( MouseEvent e ) 
    {
	int x = e.getX();
	int y = e.getY();
    }
}
