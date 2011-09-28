package javautils.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

/*******************************************************************************
 * Message interface.
 ******************************************************************************/
public interface IMessage
{
    /***************************************************************************
     * Returns a string representation of this message.
     * 
     * @return
     **************************************************************************/
    public String getMessage();

    /***************************************************************************
     * Returns the sender of this message.
     * 
     * @return
     **************************************************************************/
    public String getSender();

    /***************************************************************************
     * Returns the time this message was created.
     * 
     * @return
     **************************************************************************/
    public Date getDate();

    /***************************************************************************
     * Writes this message to a {@link DataOutputStream}.
     * 
     * @param stream
     * @throws Exception
     **************************************************************************/
    public void messageToBinaryStream( DataOutputStream stream )
            throws Exception;

    /***************************************************************************
     * Reads this message from the supplied {@link DataInputStream}.
     * 
     * @param stream
     * @return
     * @throws Exception
     **************************************************************************/
    public IMessage binaryStreamToMessage( DataInputStream stream )
            throws Exception;
}
