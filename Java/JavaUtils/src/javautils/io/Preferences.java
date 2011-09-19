package javautils.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javautils.FileUtils;

import javax.swing.JOptionPane;

/*******************************************************************************
 * This class handles preference I/O so that information can be saved between
 * runs of an application. Once initialized, parameters can be set and retrieved
 * from the preference {@link Map}.
 ******************************************************************************/
public final class Preferences
{
    private static Map<String, String> prefMap;

    private static File prefFile;

    private static boolean isInitialized = false;

    /***************************************************************************
     * Private constructor prevents instantiation.
     **************************************************************************/
    private Preferences()
    {
    }

    /***************************************************************************
     * Initializes a set of preferences. These do not have to be
     * all-encompassing; i.e. they can be added as needed.
     * 
     * @param prefFilename
     * @param prefNames
     **************************************************************************/
    public static void initialize( String prefFilename, String... prefNames )
    {
        prefFile = new File( prefFilename );
        prefMap = new HashMap<String, String>();

        for( String name : prefNames )
        {
            prefMap.put( name, "" );
        }

        isInitialized = true;
    }

    /***************************************************************************
     * Returns true if the preference file exists, false otherwise.
     * 
     * @return
     **************************************************************************/
    public static boolean exists()
    {
        if( !isInitialized )
        {
            JOptionPane.showMessageDialog( null,
                    "Preferences are un-initialized.\n"
                            + "Cannot determine if preference file exists.",
                    "Error", JOptionPane.ERROR_MESSAGE );
            return false;
        }

        return prefFile.exists();
    }

    /***************************************************************************
     * Reads the preference file, if it exists. It should be noted that if
     * values are found in the preference file that are not in the preference
     * map, they will simply be added.
     **************************************************************************/
    public static synchronized void readPreferences()
    {
        if( !isInitialized )
        {
            JOptionPane.showMessageDialog( null, "Cannot read preferences."
                    + "\nPreferences have not been initialized.", "Error",
                    JOptionPane.ERROR_MESSAGE );
            return;
        }

        if( !prefFile.exists() )
            return;

        BufferedReader in = null;

        try
        {
            in = new BufferedReader( new FileReader( prefFile ) );
            String line;

            // parameter=value
            line = FileUtils.getNextLine( in );
            while( line != null )
            {
                // split on '=' and verify
                String[] sArray = line.split( "[=]" );

                // put the pair in our map
                setPreference( sArray[0].trim(), ( sArray.length != 2 ? ""
                        : sArray[1].trim() ) );

                // read the next line
                line = FileUtils.getNextLine( in );
            }
        } catch( Exception e )
        {
            return;
        } finally
        {
            try
            {
                in.close();
            } catch( Exception e )
            {
                return;
            }
        }
    }

    /***************************************************************************
     * Writes out the preference file, in <code>parameter=value</code> format.
     **************************************************************************/
    public static synchronized void writePreferences()
    {
        if( !isInitialized )
        {
            JOptionPane.showMessageDialog( null, "Cannot write preferences."
                    + "\nPreferences have not been initialized.", "Error",
                    JOptionPane.ERROR_MESSAGE );
            return;
        }

        BufferedWriter out = null;

        try
        {
            out = new BufferedWriter( new FileWriter( prefFile ) );
            for( Map.Entry<String, String> entry : prefMap.entrySet() )
            {
                out.write( entry.getKey() + "=" + entry.getValue() + "\n" );
            }
        } catch( Exception e )
        {
            return;
        } finally
        {
            try
            {
                out.close();
            } catch( Exception e )
            {
                return;
            }
        }
    }

    /***************************************************************************
     * Returns the value associated with the supplied preference name, or
     * "Not found" if the key does not exist in the map.
     * 
     * @param prefName
     * @return
     **************************************************************************/
    public static String getPreference( String prefName )
    {
        if( prefMap.containsKey( prefName ) )
        {
            return prefMap.get( prefName );
        } else
        {
            return "Not found";
        }
    }

    /***************************************************************************
     * Sets the value associated with the supplied preference name. Note that
     * the supplied key will be added to the map if it does not exist.
     * 
     * @param prefName
     * @param prefValue
     **************************************************************************/
    public static void setPreference( String prefName, String prefValue )
    {
        prefMap.put( prefName, prefValue );
    }
}
