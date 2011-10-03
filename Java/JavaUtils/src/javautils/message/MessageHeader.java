package javautils.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/*******************************************************************************
 * This class represents a simple message header.
 ******************************************************************************/
public class MessageHeader implements IMessageHeader
{
    public static final long SIZE = 16;

    private int senderHash;
    private MessageType messageType;
    private long messageLength;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public MessageHeader()
    {
        this( 0, MessageType.CHAT, 0 );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param sender
     * @param type
     * @param length
     **************************************************************************/
    public MessageHeader( int sender, MessageType type, long length )
    {
        this.senderHash = sender;
        this.messageType = type;
        this.messageLength = length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IMessageHeader#getSenderHash()
     */
    @Override
    public int getSenderHash()
    {
        return senderHash;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IMessageHeader#setSenderHash(int)
     */
    @Override
    public void setSenderHash( int hash )
    {
        senderHash = hash;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IMessageHeader#getMessageType()
     */
    @Override
    public MessageType getMessageType()
    {
        return messageType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.message.IMessageHeader#setMessageType(javautils.message.MessageType
     * )
     */
    @Override
    public void setMessageType( MessageType type )
    {
        messageType = type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IMessageHeader#getMessageLength()
     */
    @Override
    public long getMessageLength()
    {
        return messageLength;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IMessageHeader#setMessageLength(long)
     */
    @Override
    public void setMessageLength( long length )
    {
        messageLength = length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IMessageHeader#headerToBinaryStream(java.io.
     * DataOutputStream)
     */
    @Override
    public void headerToBinaryStream( DataOutputStream stream )
            throws Exception
    {
        // sender hash code
        stream.writeInt( senderHash );

        // type
        stream.writeInt( messageType.toValue() );

        // message length
        stream.writeLong( messageLength );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.message.IMessageHeader#binaryStreamToHeader(java.io.DataInputStream
     * )
     */
    @Override
    public IMessageHeader binaryStreamToHeader( DataInputStream stream )
            throws Exception
    {
        setSenderHash( stream.readInt() );
        setMessageType( MessageType.fromValue( stream.readInt() ) );
        setMessageLength( stream.readLong() );

        return this;
    }
}
