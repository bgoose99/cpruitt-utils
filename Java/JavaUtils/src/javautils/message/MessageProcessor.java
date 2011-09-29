package javautils.message;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

/*******************************************************************************
 * This class handles the actual processing of incoming messages. Messages are
 * placed in a queue using the {@link #addMessage(DatagramPacket)} method. This
 * notifies the main thread function that a message needs to be processed.
 ******************************************************************************/
public class MessageProcessor extends Thread
{
    private List<DatagramPacket> messages;
    private boolean running = true;
    private IMessageDisplay display;
    private IHeartbeatListener heartbeatListener;

    /***************************************************************************
     * Constructor
     * 
     * @param display
     **************************************************************************/
    public MessageProcessor( IMessageDisplay display,
            IHeartbeatListener listener )
    {
        messages = new ArrayList<DatagramPacket>();
        this.display = display;
        this.heartbeatListener = listener;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run()
    {
        DatagramPacket packet;

        synchronized( this )
        {
            running = true;
        }

        while( ( packet = extractMessage() ) != null )
        {
            try
            {
                processMessage( MessageUtils.getMessage( packet ) );
            } catch( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    /***************************************************************************
     * Waits for a message to be received.
     * 
     * @return
     **************************************************************************/
    private synchronized DatagramPacket extractMessage()
    {
        while( running && messages.isEmpty() )
        {
            try
            {
                wait();
            } catch( Exception e )
            {
            }
        }

        return messages.isEmpty() ? null : (DatagramPacket)messages.remove( 0 );
    }

    /***************************************************************************
     * Stops this thread's execution.
     **************************************************************************/
    public synchronized void stopThread()
    {
        running = false;
        notify();
    }

    /***************************************************************************
     * Processes the message by updating the display.
     * 
     * @param msg
     **************************************************************************/
    public synchronized void processMessage( IMessage msg )
    {
        if( msg != null )
        {
            HandleMessageRunnable runnable = new HandleMessageRunnable( msg );
            SwingUtilities.invokeLater( runnable );
        }
    }

    /***************************************************************************
     * Adds a message to the queue.
     * 
     * @param msg
     **************************************************************************/
    public synchronized void addMessage( DatagramPacket msg )
    {
        if( running )
        {
            messages.add( msg );
            notify();
        }
    }

    /***************************************************************************
     * Convenience class that allows us to handle the message out of the way of
     * this thread's execution.
     **************************************************************************/
    private class HandleMessageRunnable implements Runnable
    {
        private IMessage msg;

        public HandleMessageRunnable( IMessage msg )
        {
            this.msg = msg;
        }

        @Override
        public void run()
        {
            switch( msg.getMessageHeader().getMessageType() )
            {
            case CHAT:
                if( MessageProcessor.this.display != null )
                {
                    MessageProcessor.this.display
                            .addMessage( (IChatMessage)msg );
                }
                break;
            case HEARTBEAT:
                if( MessageProcessor.this.heartbeatListener != null )
                {
                    MessageProcessor.this.heartbeatListener
                            .receiveHeartbeat( (IHeartbeatMessage)msg );
                }
                break;
            }
        }
    }
}
