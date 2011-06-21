package javautils;

import java.awt.Component;
import java.awt.Dimension;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;

/*******************************************************************************
 * Contains some useful general purpose functions.
 ******************************************************************************/
public final class Utils
{
    /***************************************************************************
     * Returns true if the supplied int is a printable character, false
     * otherwise.
     * 
     * @param i
     * @return
     **************************************************************************/
    public static boolean isPrintableChar( int i )
    {
        if( i >= 32 && i < 127 )
            return true;
        else
            return false;
        // return !Character.isISOControl( i );
        // Character.UnicodeBlock block = Character.UnicodeBlock.of( i );
        // return ( !Character.isISOControl( i ) ) && i !=
        // KeyEvent.CHAR_UNDEFINED
        // && block != null && block != Character.UnicodeBlock.SPECIALS;
    }

    /***************************************************************************
     * Returns an ASCII representation of a character. This allows the display
     * of common control codes (e.g. null and delete).
     * 
     * @param i
     * @return
     **************************************************************************/
    public static String getPrintableChar( int i )
    {
        if( isPrintableChar( i ) )
        {
            String s = "" + (char)i;
            return s;
        } else
        {
            switch( i )
            {
            case 0:
                return "NUL (null)";
            case 1:
                return "SOH (start of heading)";
            case 2:
                return "STX (start of text)";
            case 3:
                return "ETX (end of text)";
            case 4:
                return "EOT (end of translation)";
            case 5:
                return "ENQ (enquiry)";
            case 6:
                return "ACK (acknowledge)";
            case 7:
                return "BEL (bell)";
            case 8:
                return "BS (backspace)";
            case 9:
                return "TAB (horizontal tab)";
            case 10:
                return "LF (line feed, new line)";
            case 11:
                return "VT (vertical tab)";
            case 12:
                return "FF (form feed, new page)";
            case 13:
                return "CR (carriage return)";
            case 14:
                return "SO (shift out)";
            case 15:
                return "SI (shift in)";
            case 16:
                return "DLE (data link escape)";
            case 17:
                return "DC1 (device control 1)";
            case 18:
                return "DC2 (device control 2)";
            case 19:
                return "DC3 (device control 3)";
            case 20:
                return "DC4 (device control 4)";
            case 21:
                return "NAK (negative ack)";
            case 22:
                return "SYN (synchronous idle)";
            case 23:
                return "ETB (end of trans. block)";
            case 24:
                return "CAN (cancel)";
            case 25:
                return "EM (end of medium)";
            case 26:
                return "SUB (substitute)";
            case 27:
                return "ESC (escape)";
            case 28:
                return "FS (file separator)";
            case 29:
                return "GS (group separator)";
            case 30:
                return "RS (record separator)";
            case 31:
                return "US (unit separator)";
            case 127:
                return "DEL";
            default:
                return "Unknown";
            }
        }
    }

    /***************************************************************************
     * Returns the dimensions of the largest of a set of components.
     * 
     * @param components
     * @return
     **************************************************************************/
    public static Dimension getMaxComponentSize( Component... components )
    {
        return getMaxComponentSize( Arrays.asList( components ) );
    }

    /***************************************************************************
     * Returns the dimensions of the largest of a set of components.
     * 
     * @param components
     * @return
     **************************************************************************/
    public static Dimension getMaxComponentSize(
            List<? extends Component> components )
    {
        Dimension max = new Dimension( 0, 0 );
        Dimension dim;

        for( Component c : components )
        {
            dim = c.getPreferredSize();
            max.width = Math.max( max.width, dim.width );
            max.height = Math.max( max.height, dim.height );
        }

        return max;
    }

    /***************************************************************************
     * Returns a space-padded string of the specified length. If the string is
     * already longer than the specified length, the given string is returned.
     * 
     * @param s
     * @param width
     * @return
     **************************************************************************/
    public static String getFixedWidthString( String s, int width )
    {
        return getFixedWidthString( s, width, ' ' );
    }

    /***************************************************************************
     * Returns a padded string of the specified length. If the string is already
     * longer than the specified length, the given string is returned.
     * 
     * @param s
     * @param width
     * @param pad
     * @return
     **************************************************************************/
    public static String getFixedWidthString( String s, int width, char pad )
    {
        return getPaddedString( s, width, pad, true );
    }

    /***************************************************************************
     * Returns a padded string. The pad can either come before or after the
     * supplied string. If the string is already longer than the specified
     * length, the given string is returned.
     * 
     * @param s
     * @param width
     * @param pad
     * @param pre
     * @return
     **************************************************************************/
    public static String getPaddedString( String s, int width, char pad,
            boolean pre )
    {
        if( s == null )
            s = "";
        if( s.length() >= width )
            return s;

        return String.format( "%" + ( pre ? "" : "-" ) + width + "s", s )
                .replace( ' ', pad );
    }

    /***************************************************************************
     * Returns a URL pointing to the supplied resource name.
     * 
     * @param resource
     * @return
     **************************************************************************/
    public static URL loadResourceURL( String resource )
    {
        return loadResourceURL( Utils.class, resource );
    }

    /***************************************************************************
     * Returns a URL pointing to the supplied resource name.
     * 
     * @param c
     * @param resource
     * @return
     **************************************************************************/
    public static URL loadResourceURL( Class<?> c, String resource )
    {
        URL url = c.getResource( resource );
        return url;
    }

    /***************************************************************************
     * Formats a number with commas as group separators.
     * 
     * @param i
     * @return
     **************************************************************************/
    public static String formatNumber( int i )
    {
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator( ',' );
        df.setDecimalFormatSymbols( dfs );
        return df.format( i );
    }

    /***************************************************************************
     * Formats a number with commas as group separators.
     * 
     * @param d
     * @return
     **************************************************************************/
    public static String formatNumber( double d )
    {
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator( ',' );
        df.setDecimalFormatSymbols( dfs );
        return df.format( d );
    }
}
