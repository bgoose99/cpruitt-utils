package javautils.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface IMessageHeader
{
    /***************************************************************************
     * Returns a hash of the sender.
     * 
     * @return
     **************************************************************************/
    public int getSenderHash();

    /***************************************************************************
     * Sets the hash of the sender.
     * 
     * @param hash
     **************************************************************************/
    public void setSenderHash( int hash );

    /***************************************************************************
     * Returns the message type.
     * 
     * @return
     **************************************************************************/
    public MessageType getMessageType();

    /***************************************************************************
     * Sets the message type.
     * 
     * @param type
     **************************************************************************/
    public void setMessageType( MessageType type );

    /***************************************************************************
     * Returns the message length (including the length of the header).
     * 
     * @return
     **************************************************************************/
    public long getMessageLength();

    /***************************************************************************
     * Sets the message length.
     * 
     * @param length
     **************************************************************************/
    public void setMessageLength( long length );

    /***************************************************************************
     * Writes this header to a {@link DataOutputStream}.
     * 
     * @param stream
     * @throws Exception
     **************************************************************************/
    public void headerToBinaryStream( DataOutputStream stream )
            throws Exception;

    /***************************************************************************
     * Reads this message from the supplied {@link DataInputStream}.
     * 
     * @param stream
     * @return
     * @throws Exception
     **************************************************************************/
    public IMessageHeader binaryStreamToHeader( DataInputStream stream )
            throws Exception;
}
