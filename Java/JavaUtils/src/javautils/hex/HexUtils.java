package javautils.hex;

import javautils.Utils;

public class HexUtils
{
    public static final String[] BYTES;

    /***************************************************************************
     * Static initializer.
     **************************************************************************/
    static
    {
        BYTES = new String[256];
        for( int i = 0; i < 256; i++ )
        {
            BYTES[i] = Utils.getPaddedString( Integer.toHexString( i )
                    .toUpperCase(), 2, '0', true );
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
