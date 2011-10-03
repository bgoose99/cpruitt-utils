package javautils.message;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DefaultChatMessage implements IChatMessage
{
    private IMessageHeader header;

    private String displayName;
    private Color displayColor;
    private Date sendTime;
    private String message;
    private List<MessageFormatOption> options;
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
        this( sender, displayName, Color.black, message, null );
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
            Color displayColor, String message,
            List<MessageFormatOption> options ) throws Exception
    {

        this.displayName = displayName;
        this.displayColor = displayColor;
        this.sendTime = new Date();
        this.message = message;
        this.options = options;

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

        messageLength = MessageHeader.SIZE + 4 + displayName.length() + 8 + 4
                + message.length() + 4;

        if( options != null )
        {
            messageLength += options.size() * MessageFormatOption.SIZE;
        }
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
     * @see javautils.message.IChatMessage#getFormattingOptions()
     */
    @Override
    public List<MessageFormatOption> getFormattingOptions()
    {
        return options;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.message.IMessageSerializer#toBinaryStream(java.io.DataOutputStream
     * )
     */
    @Override
    public void toBinaryStream( DataOutputStream stream ) throws Exception
    {
        // sender
        stream.writeInt( displayName.length() );
        stream.writeBytes( displayName );

        // color
        stream.writeInt( displayColor.getRGB() );

        // date
        stream.writeLong( sendTime.getTime() );

        // message
        stream.writeInt( message.length() );
        stream.writeBytes( message );

        // options
        if( options != null )
        {
            stream.writeInt( options.size() );
            Collections.sort( options );
            for( MessageFormatOption o : options )
            {
                o.toBinaryStream( stream );
            }
        } else
        {
            stream.writeInt( 0 );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.message.IMessageSerializer#fromBinaryStream(java.io.DataInputStream
     * )
     */
    @Override
    public IMessage fromBinaryStream( DataInputStream stream ) throws Exception
    {
        // sender
        int len = stream.readInt();

        byte[] msg = new byte[len];
        stream.read( msg, 0, len );
        displayName = new String( msg );

        // color
        displayColor = new Color( stream.readInt() );

        // date
        sendTime = new Date( stream.readLong() );

        // message
        len = stream.readInt();

        msg = new byte[len];
        stream.read( msg, 0, len );
        message = new String( msg );

        // options
        options = new ArrayList<MessageFormatOption>();
        int numOptions = stream.readInt();
        for( int i = 0; i < numOptions; i++ )
        {
            MessageFormatOption o = new MessageFormatOption();
            o.fromBinaryStream( stream );
            options.add( o );
        }

        // re-calculate message length and assign to header
        calculateMessageLength();
        header.setMessageLength( messageLength );

        return this;
    }
}
