package javautils.message;

public enum MessageFormatType
{
    BOLD( 0 ), ITALIC( 1 ), UNDERLINE( 2 ), BOLD_AND_ITALIC( 3 ), BOLD_AND_UNDERLINE(
            4 ), ITALIC_AND_UNDERLINE( 5 ), BOLD_ITALIC_AND_UNDERLINE( 6 );

    /** This value facilitates serialization. */
    private final int value;

    /***************************************************************************
     * Constructor
     * 
     * @param value
     **************************************************************************/
    private MessageFormatType( int value )
    {
        this.value = value;
    }

    /***************************************************************************
     * Returns the value of this message format type.
     * 
     * @return
     **************************************************************************/
    public int toValue()
    {
        return value;
    }

    /***************************************************************************
     * Returns the message format type associated with the given value.
     * 
     * @param value
     * @return
     * @throws IllegalArgumentException
     **************************************************************************/
    public static MessageFormatType fromValue( int value )
            throws IllegalArgumentException
    {
        for( MessageFormatType t : values() )
        {
            if( t.value == value )
            {
                return t;
            }
        }

        throw new IllegalArgumentException( "Invalid format type: " + value );
    }
}
