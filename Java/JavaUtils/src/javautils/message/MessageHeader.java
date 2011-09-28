package javautils.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class MessageHeader implements IMessageHeader
{
    public static final long LENGTH = 16;

    private int senderHash;
    private MessageType messageType;
    private long messageLength;

    public MessageHeader()
    {
        this( 0, MessageType.CHAT, 0 );
    }

    public MessageHeader( int sender, MessageType type, long length )
    {
        this.senderHash = sender;
        this.messageType = type;
        this.messageLength = length;
    }

    @Override
    public int getSenderHash()
    {
        return senderHash;
    }

    @Override
    public void setSenderHash( int hash )
    {
        senderHash = hash;
    }

    @Override
    public MessageType getMessageType()
    {
        return messageType;
    }

    @Override
    public void setMessageType( MessageType type )
    {
        messageType = type;
    }

    @Override
    public long getMessageLength()
    {
        return messageLength;
    }

    @Override
    public void setMessageLength( long length )
    {
        messageLength = length;
    }

    @Override
    public void headerToBinaryStream( DataOutputStream stream )
            throws Exception
    {
        System.out.println( "DEBUG: header = [" + senderHash + ", "
                + messageType.name() + ", " + messageLength );
        // sender hash code
        stream.writeInt( senderHash );

        // type
        stream.writeInt( messageType.toValue() );

        // message length
        stream.writeLong( messageLength );
    }

    @Override
    public IMessageHeader binaryStreamToHeader( DataInputStream stream )
            throws Exception
    {
        int i = stream.readInt();
        MessageType t = MessageType.fromValue( stream.readInt() );
        long l = stream.readLong();

        return new MessageHeader( i, t, l );
    }
}
