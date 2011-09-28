package javautils.message;

public enum MessageType
{
    HEARTBEAT( 0 ), CHAT( 1 );

    private final int value;

    private MessageType( int value )
    {
        this.value = value;
    }

    public int toValue()
    {
        return value;
    }

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
