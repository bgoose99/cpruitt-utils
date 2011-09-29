package data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javautils.message.DefaultHeartbeatMessage;
import javautils.message.IMessageHandler;

import javax.swing.Timer;

/*******************************************************************************
 * This class sends a heartbeat message every second.
 ******************************************************************************/
public class HeartbeatTask implements ActionListener
{
    private IUser localUser;
    private Timer timer;
    private IMessageHandler messageHandler = null;

    /***************************************************************************
     * Constructor
     * 
     * @param localUser
     **************************************************************************/
    public HeartbeatTask( IUser localUser )
    {
        this.localUser = localUser;

        // set up our timer to go off every second
        timer = new Timer( 2000, this );
        timer.setInitialDelay( 100 );
    }

    /***************************************************************************
     * Sets the message handler used to send messages.
     * 
     * @param messageHandler
     **************************************************************************/
    public void setMessageHandler( IMessageHandler messageHandler )
    {
        this.messageHandler = messageHandler;
    }

    /***************************************************************************
     * Start this task.
     **************************************************************************/
    public void startTask()
    {
        timer.start();
    }

    /***************************************************************************
     * Stop this task.
     **************************************************************************/
    public void stopTask()
    {
        timer.stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed( ActionEvent event )
    {
        if( messageHandler != null )
        {
            try
            {
                DefaultHeartbeatMessage msg = new DefaultHeartbeatMessage(
                        localUser.getName(), localUser.getDisplayName(),
                        localUser.isAvailable() );
                messageHandler.sendMessage( msg );
            } catch( Exception e )
            {
                e.printStackTrace();
            }
        }
    }
}
