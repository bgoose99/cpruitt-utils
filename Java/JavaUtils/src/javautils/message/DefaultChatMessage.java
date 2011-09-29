package javautils.message;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

public class DefaultChatMessage implements IChatMessage
{
    private IMessageHeader header;

    private String displayName;
    private Color displayColor;
    private Date sendTime;
    private String message;
    private long messageLength;

    /***************************************************************************
     * Default Constructor
     * 
     * @throws Exception
     **************************************************************************/
    public DefaultChatMessage() throws Exception
    {
        this( "", "", "" );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param sender
     * @param displayName
     * @param message
     * @throws Exception
     **************************************************************************/
    public DefaultChatMessage( String sender, String displayName, String message )
            throws Exception
    {
        this( sender, displayName, Color.black, message );
    }

    /***************************************************************************
     * Constructor used when writing message out to stream.
     * 
     * @param sender
     * @param displayName
     * @param displayColor
     * @param message
     * @throws Exception
     **************************************************************************/
    public DefaultChatMessage( String sender, String displayName,
            Color displayColor, String message ) throws Exception
    {

        this.displayName = displayName;
        this.displayColor = displayColor;
        this.sendTime = new Date();
        this.message = message;

        calculateMessageLength();

        if( messageLength > MessageUtils.MAX_MESSAGE_SIZE )
        {
            throw new Exception( "Message too long" );
        }

        header = new MessageHeader( sender.hashCode(), MessageType.CHAT,
                messageLength );
    }

    /***************************************************************************
     * Constructor used when reading messages from a stream.
     * 
     * @param header
     * @throws Exception
     **************************************************************************/
    public DefaultChatMessage( IMessageHeader header ) throws Exception
    {
        this();
        this.header = header;
    }

    /***************************************************************************
     * Calculates the message length.
     **************************************************************************/
    private void calculateMessageLength()
    {
        messageLength = MessageHeader.LENGTH + 4 + displayName.length() + 8 + 4
                + message.length();
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
     * @see javautils.chat.IMessage#getMessage()
     */
    @Override
    public String getMessage()
    {
        return message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.chat.IMessage#getSender()
     */
    @Override
    public String getSender()
    {
        return displayName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IMessage#getDate()
     */
    @Override
    public Date getDate()
    {
        return sendTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IMessage#getColor()
     */
    @Override
    public Color getDisplayColor()
    {
        return displayColor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.chat.IMessage#messageToBinaryStream(java.io.DataOutputStream)
     */
    @Override
    public void messageToBinaryStream( DataOutputStream stream )
            throws Exception
    {
        // sender
        stream.writeInt( displayName.length() );
        stream.writeBytes( displayName );

        // date
        stream.writeLong( sendTime.getTime() );

        // message
        stream.writeInt( message.length() );
        stream.writeBytes( message );

        // color
        stream.writeInt( displayColor.getRGB() );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.chat.IMessage#binaryStreamToMessage(java.io.DataInputStream)
     */
    @Override
    public IMessage binaryStreamToMessage( DataInputStream stream )
            throws Exception
    {
        // sender
        int len = stream.readInt();

        byte[] msg = new byte[len];
        stream.read( msg, 0, len );
        displayName = new String( msg );

        // date
        sendTime = new Date( stream.readLong() );

        // message
        len = stream.readInt();

        msg = new byte[len];
        stream.read( msg, 0, len );
        message = new String( msg );

        // color
        displayColor = new Color( stream.readInt() );

        // re-calculate message length and assign to header
        calculateMessageLength();
        header.setMessageLength( messageLength );

        return this;
    }
}
