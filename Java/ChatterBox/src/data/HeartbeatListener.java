package data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javautils.message.IHeartbeatListener;
import javautils.message.IHeartbeatMessage;
import javautils.message.IMessageHeader;

import javax.swing.Timer;

public class HeartbeatListener implements IHeartbeatListener
{
    private List<UserActivity> users;
    private IUserDisplay display;
    private IUser localUser;
    private Timer timer;

    /***************************************************************************
     * Constructor
     * 
     * @param display
     * @param localUser
     **************************************************************************/
    public HeartbeatListener( IUserDisplay display, IUser localUser )
    {
        this.display = display;
        this.localUser = localUser;
        users = new ArrayList<UserActivity>();
        timer = new Timer( 1000, new TimerTask() );
        timer.setInitialDelay( 0 );
        timer.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.message.IHeartbeatListener#receiveHeartbeat(javautils.message
     * .IMessage)
     */
    @Override
    public void receiveHeartbeat( IHeartbeatMessage msg )
    {
        IMessageHeader header = msg.getMessageHeader();
        if( header.getSenderHash() != localUser.getName().hashCode() )
        {
            synchronized( this )
            {
                boolean found = false;
                for( UserActivity u : users )
                {
                    // TODO: replace the following check with the real one after
                    // debugging
                    // if( header.getSenderHash() == u.user.getName().hashCode()
                    // )
                    if( msg.getUserDisplayName().equals(
                            u.user.getDisplayName() ) )
                    {
                        // same sender, update and break
                        if( msg.isUserAvailable() != u.user.isAvailable() )
                        {
                            u.user.setAvailable( msg.isUserAvailable() );
                            if( display != null )
                                display.updateDisplay();
                        }
                        u.lastUpdate = msg.getSendTime();
                        found = true;
                        break;
                    }
                }

                if( !found )
                {
                    IUser user = new ChatUser( msg.getUserDisplayName(), true );
                    users.add( new UserActivity( user, msg.getSendTime() ) );
                    if( display != null )
                    {
                        display.addUser( user );
                    }
                }
            }
        }
    }

    /***************************************************************************
     * Utility class.
     **************************************************************************/
    private class UserActivity
    {
        public IUser user;
        public Date lastUpdate;

        public UserActivity( IUser user, Date lastUpdate )
        {
            this.user = user;
            this.lastUpdate = lastUpdate;
        }
    }

    /***************************************************************************
     * This class monitors all users for inactivity. Users who have not sent a
     * heartbeat within a certain threshold are removed.
     **************************************************************************/
    private class TimerTask implements ActionListener
    {
        private static final long THRESHOLD = 6000;

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            synchronized( HeartbeatListener.this )
            {
                Date now = new Date();
                Iterator<UserActivity> it = users.iterator();
                while( it.hasNext() )
                {
                    UserActivity u = it.next();
                    if( now.getTime() - u.lastUpdate.getTime() > THRESHOLD )
                    {
                        it.remove();
                        if( display != null )
                        {
                            display.removeUser( u.user );
                        }
                    }
                }
            }
        }
    }
}
