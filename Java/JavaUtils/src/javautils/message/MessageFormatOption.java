package javautils.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/*******************************************************************************
 * This class represents a single formatting option to be applied to an
 * arbitrary message.
 ******************************************************************************/
public class MessageFormatOption implements
        IMessageSerializer<MessageFormatOption>,
        Comparable<MessageFormatOption>
{
    public static final int SIZE = 4 + 4 + 4;

    private MessageFormatType type;
    private int startIndex;
    private int length;

    /***************************************************************************
     * Default constructor
     **************************************************************************/
    public MessageFormatOption()
    {
        this( MessageFormatType.BOLD, 0, 0 );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param type
     * @param startIndex
     * @param length
     **************************************************************************/
    public MessageFormatOption( MessageFormatType type, int startIndex,
            int length )
    {
        this.type = type;
        this.startIndex = startIndex;
        this.length = length;
    }

    /***************************************************************************
     * Returns the format type of this option.
     * 
     * @return
     **************************************************************************/
    public MessageFormatType getType()
    {
        return type;
    }

    /***************************************************************************
     * Returns the starting index (into the message) for this formatting option.
     * 
     * @return
     **************************************************************************/
    public int getStartIndex()
    {
        return startIndex;
    }

    /***************************************************************************
     * Returns the length of this formatting option.
     * 
     * @return
     **************************************************************************/
    public int getLength()
    {
        return length;
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
        stream.writeInt( type.toValue() );
        stream.writeInt( startIndex );
        stream.writeInt( length );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.message.IMessageSerializer#fromBinaryStream(java.io.DataInputStream
     * )
     */
    @Override
    public MessageFormatOption fromBinaryStream( DataInputStream stream )
            throws Exception
    {
        type = MessageFormatType.fromValue( stream.readInt() );
        startIndex = stream.readInt();
        length = stream.readInt();
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo( MessageFormatOption o )
    {
        return this.startIndex - o.startIndex;
    }
}
