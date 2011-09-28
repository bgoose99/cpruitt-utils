package javautils.message;

/*******************************************************************************
 * Enum for different message types.
 ******************************************************************************/
public enum MessageType
{
    HEARTBEAT( 0 ), CHAT( 1 );

    /** This value facilitates serialization. */
    private final int value;

    /***************************************************************************
     * Constructor
     * 
     * @param value
     **************************************************************************/
    private MessageType( int value )
    {
        this.value = value;
    }

    /***************************************************************************
     * Returns the value of this message type.
     * 
     * @return
     **************************************************************************/
    public int toValue()
    {
        return value;
    }

    /***************************************************************************
     * Returns the message type associated with the given value.
     * 
     * @param value
     * @return
     * @throws IllegalArgumentException
     **************************************************************************/
    public static MessageType fromValue( int value )
            throws IllegalArgumentException
    {
        for( MessageType t : values() )
        {
            if( t.value == value )
            {
                return t;
            }
        }

        throw new IllegalArgumentException( "Invalid type: " + value );
    }
}
