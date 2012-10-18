package javautils.hex;

public class HexUtils
{
    public static final String[] HEX_BYTES;
    public static final String[] DEC_BYTES;
    public static final String[] OCT_BYTES;
    public static final String[] BIN_BYTES;

    /***************************************************************************
     * Static initializer.
     **************************************************************************/
    static
    {
        HEX_BYTES = new String[256];
        DEC_BYTES = new String[256];
        OCT_BYTES = new String[256];
        BIN_BYTES = new String[256];
        for( int i = 0; i < 256; i++ )
        {
            HEX_BYTES[i] = String.format( "%2s", Integer.toHexString( i ) )
                    .replace( ' ', '0' ).toUpperCase();
            DEC_BYTES[i] = String.format( "%3s", Integer.toString( i ) )
                    .replace( ' ', '0' );
            OCT_BYTES[i] = String.format( "%3s", Integer.toOctalString( i ) )
                    .replace( ' ', '0' );
            BIN_BYTES[i] = String.format( "%8s", Integer.toBinaryString( i ) )
                    .replace( ' ', '0' );
        }
    }

    /***************************************************************************
     * Converts a byte to an unsigned integer.
     * 
     * @param b
     * @return
     **************************************************************************/
    public static int byteToUnsignedInt( byte b )
    {
        if( b < 0 )
        {
            return (int)( b + 256 );
        }

        return b;
    }

    /***************************************************************************
     * Converts an integer to a signed byte.
     * 
     * @param i
     * @return
     **************************************************************************/
    public static byte intToSignedByte( int i )
    {
        if( i > 127 )
        {
            return (byte)( i - 256 );
        }

        return (byte)i;
    }

    /***************************************************************************
     * Parses an integer value from the given string. This string can either be
     * a typical integer format or a hex-formatted string.
     * 
     * @param s
     * @return
     * @throws Exception
     **************************************************************************/
    public static int parseHexString( String s ) throws Exception
    {
        int rval;
        if( s.indexOf( "0x" ) != -1 )
        {
            rval = Integer.parseInt( s.substring( s.indexOf( "0x" ) + 2 ), 16 );
        } else
        {
            rval = Integer.parseInt( s );
        }

        return rval;
    }
}
