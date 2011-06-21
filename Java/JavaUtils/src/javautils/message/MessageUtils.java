package javautils.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;

/*******************************************************************************
 * Message utility methods.
 ******************************************************************************/
public final class MessageUtils
{
    public static final int MAX_MESSAGE_SIZE = 65535;

    /***************************************************************************
     * Returns an {@link IMessage} that corresponds to the supplied
     * {@link DatagramPacket} .
     * 
     * @param packet
     * @return
     * @throws Exception
     **************************************************************************/
    public static synchronized IMessage getMessage( DatagramPacket packet )
            throws Exception
    {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(
                packet.getData(), 0, packet.getLength() );
        DataInputStream dataStream = new DataInputStream( byteStream );

        DefaultMessage msg = new DefaultMessage();

        return msg.binaryStreamToMessage( dataStream );
    }

    /***************************************************************************
     * Returns a {@link DatagramPacket} that corresponds to the supplied
     * {@link IMessage}.
     * 
     * @param msg
     * @return
     * @throws Exception
     **************************************************************************/
    public static synchronized DatagramPacket encode( IMessage msg )
            throws Exception
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream( byteStream );

        msg.messageToBinaryStream( dataStream );

        return new DatagramPacket( byteStream.toByteArray(), byteStream.size() );
    }
}
