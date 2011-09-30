package data;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Date;

import javax.swing.Timer;

/*******************************************************************************
 * This class will monitor the user's mouse movement, setting the user's
 * availability based on activity.
 ******************************************************************************/
public class UserActivityMonitor implements IUserActivityMonitor
{
    private static final long THRESHOLD = 60000; // 1 minute
    private IUser localUser;
    private Date lastActive;
    private Timer timer;

    /***************************************************************************
     * Constructor
     * 
     * @param localUser
     * @param parent
     **************************************************************************/
    public UserActivityMonitor( IUser localUser, Component parent )
    {
        this.localUser = localUser;

        // Create our timer to check activity every 5 seconds
        timer = new Timer( 5000, new TimerTask() );
        timer.start();

        EventMonitor monitor = new EventMonitor();
        parent.addMouseMotionListener( monitor );
    }

    /*
     * (non-Javadoc)
     * 
     * @see data.IUserActivityMonitor#updateActivity()
     */
    @Override
    public void updateActivity()
    {
        Date now = new Date();
        synchronized( this )
        {
            lastActive = now;
            if( !localUser.isAvailable() )
            {
                localUser.setAvailable( true );
            }
        }
    }

    /***************************************************************************
     * Timer task used to determine inactivity.
     **************************************************************************/
    private class TimerTask implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            Date now = new Date();
            long millisInactive = 0;
            synchronized( UserActivityMonitor.this )
            {
                millisInactive = now.getTime() - lastActive.getTime();
                if( millisInactive > THRESHOLD && localUser.isAvailable() )
                {
                    localUser.setAvailable( false );
                }
            }
        }
    }

    /***************************************************************************
     * Simple mouse listener.
     **************************************************************************/
    private class EventMonitor implements MouseMotionListener
    {
        @Override
        public void mouseDragged( MouseEvent e )
        {
            updateActivity();
        }

        @Override
        public void mouseMoved( MouseEvent e )
        {
            updateActivity();
        }

    }
}
