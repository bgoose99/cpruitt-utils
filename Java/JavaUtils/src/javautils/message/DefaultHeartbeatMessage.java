package javautils.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

public class DefaultHeartbeatMessage implements IHeartbeatMessage
{
    private IMessageHeader header;
    private Date sendTime;
    private boolean available;
    private String displayName;
    private long messageLength;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public DefaultHeartbeatMessage() throws Exception
    {
        this( "Unknown", "Unknown", false );
    }

    /***************************************************************************
     * Constructor used when sending a message.
     * 
     * @param sender
     * @param displayName
     * @param available
     * @throws Exception
     **************************************************************************/
    public DefaultHeartbeatMessage( String sender, String displayName,
            boolean available ) throws Exception
    {
        this.displayName = displayName;
        this.available = available;
        calculateMessageLength();
        sendTime = new Date();

        calculateMessageLength();

        if( messageLength > MessageUtils.MAX_MESSAGE_SIZE )
        {
            throw new Exception( "Message too long" );
        }

        header = new MessageHeader( sender.hashCode(), MessageType.HEARTBEAT,
                messageLength );
    }

    /***************************************************************************
     * Constructor used when reading a message from a stream.
     * 
     * @param header
     **************************************************************************/
    public DefaultHeartbeatMessage( IMessageHeader header ) throws Exception
    {
        this();
        this.header = header;
    }

    /***************************************************************************
     * Calculates message length.
     **************************************************************************/
    private void calculateMessageLength()
    {
        messageLength = MessageHeader.LENGTH + 8 + 1 + 4 + displayName.length();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IMessage#getMessageHeader()
     */
    @Override
    public IMessageHeader getMessageHeader()
    {
        return header;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IHeartbeatMessage#getSendTime()
     */
    @Override
    public Date getSendTime()
    {
        return sendTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IHeartbeatMessage#isAvailable()
     */
    @Override
    public boolean isUserAvailable()
    {
        return available;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IHeartbeatMessage#getDisplayName()
     */
    @Override
    public String getUserDisplayName()
    {
        return displayName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.message.IMessage#messageToBinaryStream(java.io.DataOutputStream
     * )
     */
    @Override
    public void messageToBinaryStream( DataOutputStream stream )
            throws Exception
    {
        stream.writeLong( sendTime.getTime() );

        stream.writeBoolean( available );

        stream.writeInt( displayName.length() );
        stream.writeBytes( displayName );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.message.IMessage#binaryStreamToMessage(java.io.DataInputStream)
     */
    @Override
    public IMessage binaryStreamToMessage( DataInputStream stream )
            throws Exception
    {
        sendTime = new Date( stream.readLong() );

        available = stream.readBoolean();

        int len = stream.readInt();
        byte[] msg = new byte[len];
        stream.read( msg, 0, len );
        displayName = new String( msg );

        calculateMessageLength();
        header.setMessageLength( messageLength );

        return this;
    }
}
