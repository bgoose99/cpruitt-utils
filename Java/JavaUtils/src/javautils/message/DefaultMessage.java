package javautils.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

public class DefaultMessage implements IMessage
{
    private String sender;
    private Date date;
    private String message;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public DefaultMessage() throws Exception
    {
        this( "", "" );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param sender
     * @param message
     **************************************************************************/
    public DefaultMessage( String sender, String message ) throws Exception
    {
        if( sender.length() > MessageUtils.MAX_MESSAGE_SIZE )
        {
            throw new Exception( "Sender ID contains too many characters" );
        }

        if( ( sender.length() + message.length() ) > ( MessageUtils.MAX_MESSAGE_SIZE + 8 ) )
        {
            throw new Exception( "Message too long" );
        }

        this.sender = sender;
        this.message = message;
        date = new Date();
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
        return sender;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IMessage#getDate()
     */
    @Override
    public Date getDate()
    {
        return date;
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
        stream.writeInt( sender.length() );
        stream.writeBytes( sender );

        // date
        stream.writeLong( date.getTime() );

        // message
        stream.writeInt( message.length() );
        stream.writeBytes( message );
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
        sender = new String( msg );

        // date
        date = new Date( stream.readLong() );

        // message
        len = stream.readInt();

        msg = new byte[len];
        stream.read( msg, 0, len );
        message = new String( msg );

        return this;
    }
}
