package javautils.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/*******************************************************************************
 * Generic message serializer interface.
 * 
 * @param <E>
 ******************************************************************************/
public interface IMessageSerializer<E>
{
    /***************************************************************************
     * Writes this class to the supplied stream.
     * 
     * @param stream
     * @throws Exception
     **************************************************************************/
    public void toBinaryStream( DataOutputStream stream ) throws Exception;

    /***************************************************************************
     * Reads and constructs a new object of type E from the supplied stream.
     * 
     * @param stream
     * @return
     * @throws Exception
     **************************************************************************/
    public E fromBinaryStream( DataInputStream stream ) throws Exception;
}
