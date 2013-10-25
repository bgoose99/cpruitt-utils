package javautils;

import java.io.BufferedReader;

/*******************************************************************************
 * Contains some useful file manipulation and I/O functions.
 ******************************************************************************/
public final class FileUtils
{
    /***************************************************************************
     * Returns the next non-comment, non-empty line from a reader. Comments are
     * assumed to start with '#'.
     * 
     * @param reader
     * @return
     * @throws Exception
     **************************************************************************/
    public static String getNextLine( BufferedReader reader ) throws Exception
    {
        return getNextLine( reader, "#" );
    }

    /***************************************************************************
     * Returns the next non-comment, non-empty line from a reader.
     * 
     * @param reader
     * @param comment
     * @return
     * @throws Exception
     **************************************************************************/
    public static String getNextLine( BufferedReader reader, String comment )
            throws Exception
    {
        String s;

        while( true )
        {
            // get the next line
            s = reader.readLine();

            // if it's null, we've reached the end of the file
            if( s == null )
                return null;

            // trim it
            s = s.trim();

            // if it starts with a comment, go on to the next line
            if( s.startsWith( comment ) )
                continue;

            // if it still has a length after trimming, it's a good line
            if( s.length() > 0 )
                return s;
        }
    }

    /***************************************************************************
     * Returns the next line from a reader that begins with a specified string,
     * or null if one is not found.
     * 
     * @param reader
     * @param startsWith
     * @return
     * @throws Exception
     **************************************************************************/
    public static String getLine( BufferedReader reader, String startsWith )
            throws Exception
    {
        String s;

        while( true )
        {
            // get the next line
            s = reader.readLine();

            // if it's null, we've reached the end of the file
            if( s == null )
                return null;

            // trim it
            s = s.trim();

            // if it starts with our search string, return it
            if( s.startsWith( startsWith ) )
                return s;
        }
    }
}
