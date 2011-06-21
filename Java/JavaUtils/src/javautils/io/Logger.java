package javautils.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javautils.DateUtils;

import javax.swing.JOptionPane;

/*******************************************************************************
 * Logger is a generic class that can be used by applications to log messages to
 * a single log file. Once initialized, it will create a sessions folder (of
 * format YYYY_MM_DD_Sessions) where log files will be stored per run.
 ******************************************************************************/
public final class Logger
{
    private static boolean isInitialized = false;
    private static File logFile = null;
    private static BufferedWriter out = null;

    /***************************************************************************
     * Private constructor prevents instantiation.
     **************************************************************************/
    private Logger()
    {
    }

    /***************************************************************************
     * Initializes the log, creating the daily session folder if necessary. Once
     * initialized, the <code>write()</code> function can be called from any
     * number of threads to write messages to the log file.
     * 
     * @param basePath
     **************************************************************************/
    public static void initialize( String basePath )
    {
        if( isInitialized )
        {
            JOptionPane.showMessageDialog( null, "Cannot initialize Logger."
                    + "\nIt has already been initialized.", "Error",
                    JOptionPane.ERROR_MESSAGE );
            return;
        }
        String dirname = basePath + DateUtils.getUnderscoreSeparatedDate()
                + "_Sessions/";
        String filename = dirname + DateUtils.getUnderscoreSeparatedTime()
                + ".log";

        try
        {
            logFile = new File( dirname );
            logFile.mkdirs();
            logFile = new File( filename );
            out = new BufferedWriter( new FileWriter( logFile ) );
        } catch( Exception e )
        {
            e.printStackTrace();
            return;
        }

        Runtime.getRuntime().addShutdownHook( new LoggerShutdownThread() );

        isInitialized = true;
    }

    /***************************************************************************
     * Writes a string to the log file.
     * 
     * @param s
     **************************************************************************/
    public static synchronized void write( String s )
    {
        if( !isInitialized )
        {
            JOptionPane.showMessageDialog( null, "Cannot write to log file."
                    + "\nLogger has not been initialized.", "Error",
                    JOptionPane.ERROR_MESSAGE );
            return;
        }
        try
        {
            out.write( s + "\n" );
        } catch( Exception e )
        {
            ;
        }
    }

    /***************************************************************************
     * Returns the full name of the log file.
     * 
     * @return
     **************************************************************************/
    public static String getLogFileName()
    {
        if( isInitialized && logFile != null )
        {
            return logFile.getAbsolutePath();
        } else
        {
            return "Logger not initialized";
        }
    }

    /***************************************************************************
     * Prepares this object for destruction, including closing the log.
     **************************************************************************/
    private static void close()
    {
        if( isInitialized )
        {
            isInitialized = false;
            try
            {
                out.close();
            } catch( Exception e )
            {
                ;
            }
        }
    }

    /***************************************************************************
     * This class allows us to close the log file when the application using it
     * exits.
     **************************************************************************/
    private static class LoggerShutdownThread extends Thread
    {
        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Thread#run()
         */
        @Override
        public void run()
        {
            Logger.close();
        }
    }
}
