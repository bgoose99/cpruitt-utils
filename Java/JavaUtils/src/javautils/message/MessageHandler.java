package javautils.message;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import javax.swing.JOptionPane;

/*******************************************************************************
 * This class handles incoming and outgoing messages. It listens on the
 * specified multicast socket for message, shipping them off to the
 * {@link MessageProcessor} upon receipt. Messages can be sent using the
 * {@link #sendMessage(IMessage)} method.
 ******************************************************************************/
public class MessageHandler extends Thread implements IMessageHandler
{
    private static final int BUFFER_SIZE = 10000;

    private InetAddress multicastAddress;
    private MulticastSocket socket;
    private int port;
    private boolean connected;
    private int msSocketTimeout;
    private short timeToLive;
    private int largestMessage;
    private boolean running;
    private IMessageDisplay display;
    private MessageProcessor processor;

    /***************************************************************************
     * Constructor
     * 
     * @param hostName
     * @param port
     * @param display
     * @throws Exception
     **************************************************************************/
    public MessageHandler( String hostName, int port, IMessageDisplay display )
            throws Exception
    {
        multicastAddress = InetAddress.getByName( hostName );
        this.port = port;
        this.display = display;
        connected = false;
        msSocketTimeout = 1000;
        timeToLive = 20;
        largestMessage = MessageUtils.MAX_MESSAGE_SIZE;
        running = true;
        processor = new MessageProcessor( display );
    }

    /***************************************************************************
     * Attempts to connect on the specified multicast address, if necessary.
     * 
     * @return
     **************************************************************************/
    public synchronized boolean connect()
    {
        if( !connected )
        {
            try
            {
                SocketAddress socketAddress = new InetSocketAddress(
                        multicastAddress, port );
                socket = new MulticastSocket( socketAddress );
                socket.setSendBufferSize( BUFFER_SIZE );
                socket.setReceiveBufferSize( BUFFER_SIZE );
                socket.joinGroup( multicastAddress );
                socket.setSoTimeout( msSocketTimeout );
                socket.setTimeToLive( timeToLive );
                socket.setBroadcast( false );
                connected = true;
            } catch( BindException be )
            {
                try
                {
                    socket = new MulticastSocket( port );
                    socket.setSendBufferSize( BUFFER_SIZE );
                    socket.setReceiveBufferSize( BUFFER_SIZE );
                    socket.joinGroup( multicastAddress );
                    socket.setSoTimeout( msSocketTimeout );
                    socket.setTimeToLive( timeToLive );
                    socket.setBroadcast( false );
                    connected = true;
                } catch( Exception e )
                {
                    JOptionPane.showMessageDialog( null, "Unable to open IP "
                            + multicastAddress.getHostAddress() + ", port "
                            + port, "Socket Error", JOptionPane.ERROR_MESSAGE );
                    e.printStackTrace();
                }
            } catch( Exception e )
            {
                JOptionPane.showMessageDialog( null, "Unable to open IP "
                        + multicastAddress.getHostAddress() + ", port " + port,
                        "Socket Error", JOptionPane.ERROR_MESSAGE );
                e.printStackTrace();
            }
        }

        return connected;
    }

    /***************************************************************************
     * If connected, this function disconnects from the multicast socket.
     * 
     * @return
     **************************************************************************/
    public synchronized boolean disconnect()
    {
        if( connected )
        {
            try
            {
                socket.leaveGroup( multicastAddress );
                connected = false;
            } catch( IOException e )
            {
                JOptionPane.showMessageDialog( null,
                        "Unable to disconnect from socket", "Socket Error",
                        JOptionPane.ERROR_MESSAGE );
                e.printStackTrace();
            }
        }

        return connected;
    }

    /***************************************************************************
     * Sends a message across the multicast socket.
     * 
     * @param msg
     * @throws Exception
     **************************************************************************/
    @Override
    public synchronized void sendMessage( IMessage msg ) throws Exception
    {
        if( connect() )
        {
            DatagramPacket packet = MessageUtils.encode( msg );
            packet.setAddress( multicastAddress );
            packet.setPort( port );
            socket.send( packet );
            display.addMessage( msg );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run()
    {
        running = true;

        if( connect() )
        {
            while( running )
            {
                try
                {
                    byte[] buffer = new byte[largestMessage];
                    DatagramPacket packet = new DatagramPacket( buffer,
                            buffer.length );

                    socket.receive( packet );

                    if( processor != null )
                    {
                        if( !processor.isAlive() )
                        {
                            processor.start();
                        }
                        processor.addMessage( packet );
                    }
                } catch( SocketTimeoutException e )
                {
                    // expected
                } catch( Exception e )
                {
                    JOptionPane.showMessageDialog( null,
                            "Error receiving message", "Receive Error",
                            JOptionPane.ERROR_MESSAGE );
                    e.printStackTrace();
                }
            }

            disconnect();
        }
    }

    /***************************************************************************
     * Stops this thread's execution.
     **************************************************************************/
    public void stopThread()
    {
        running = false;

        if( processor != null )
        {
            processor.stopThread();
            processor = null;
        }

        connected = false;
    }
}
