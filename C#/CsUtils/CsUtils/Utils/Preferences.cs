using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace CsUtils.Utils
{
    /// <summary>
    /// This class handles preference I/O so that information can be saved between
    /// runs of an application. Once initialized, parameters can be set and retrieved
    /// from the preference map.
    /// </summary>
    public sealed class Preferences
    {
        private static Dictionary<string, string> prefDict;

        private static string prefFilename;

        private static bool isInitialized = false;

        private static Object lockObject;

        /// <summary>
        /// Private constructor prevents instantiation.
        /// </summary>
        private Preferences() { }

        public static void initialize( string prefFilename, string[] prefNames )
        {
            Preferences.prefFilename = prefFilename;
            prefDict = new Dictionary<string, string>();

            foreach( string s in prefNames )
            {
                prefDict.Add( s, "" );
            }

            lockObject = new Object();

            isInitialized = true;
        }

        /// <summary>
        /// Returns true if the preference file exists, false otherwise.
        /// </summary>
        /// <returns></returns>
        public static bool exists()
        {
            if( !isInitialized )
            {
                MessageBox.Show( "Preferences are un-initialized.\n" +
                    "Cannot determine if preference file exists.", "Warning",
                    MessageBoxButtons.OK, MessageBoxIcon.Warning );
                return false;
            }

            return File.Exists( prefFilename );
        }

        /// <summary>
        /// Reads the preference file, if it exists.
        /// <remarks>
        /// It should be noted that if 
        /// values are found in the preference file that are not in the preference
        /// map, they will simply be added.
        /// </remarks>
        /// </summary>
        public static void readPreferences()
        {
            lock( lockObject )
            {
                if( !isInitialized )
                {
                    MessageBox.Show( "Cannot read preferences.\n" +
                        "Preferences have not been initialized.", "Error",
                        MessageBoxButtons.OK, MessageBoxIcon.Error );
                    return;
                }

                if( !File.Exists( prefFilename ) )
                    return;

                using( StreamReader reader = new StreamReader( prefFilename ) )
                {
                    string line;

                    // FORMAT: parameter=value
                    while( ( line = reader.ReadLine() ) != null )
                    {
                        string[] sArray = line.Split( '=' );
                        setPreference( sArray[0].Trim(),
                            ( sArray.Length != 2 ? "" : sArray[1].Trim() ) );
                    }
                }
            }
        }

        /// <summary>
        /// Writes out the preference file.
        /// </summary>
        public static void writePreferences()
        {
            lock( lockObject )
            {
                if( !isInitialized )
                {
                    MessageBox.Show( "Cannot write preferences.\n" +
                        "Preferences have not been initialized.", "Error",
                        MessageBoxButtons.OK, MessageBoxIcon.Error );
                    return;
                }

                using( StreamWriter writer = new StreamWriter( prefFilename ) )
                {
                    foreach( KeyValuePair<string, string> p in prefDict )
                    {
                        writer.WriteLine( p.Key + "=" + p.Value );
                    }
                }
            }
        }

        /// <summary>
        /// Returns the value associated with the supplied preference name, or
        /// "Not found" if the key does not exit in the dictionary.
        /// </summary>
        /// <param name="prefName"></param>
        /// <returns></returns>
        public static string getPreference( string prefName )
        {
            string value = "";
            if( prefDict.TryGetValue( prefName, out value ) )
            {
                return value;
            }
            else
            {
                return "Not found";
            }
        }

        /// <summary>
        /// Sets the value associated with the supplied preference name.
        /// <remarks>
        /// Note that the supplied key will be added to the map if it does not exist.
        /// </remarks>
        /// </summary>
        /// <param name="prefName"></param>
        /// <param name="prefValue"></param>
        public static void setPreference( string prefName, string prefValue )
        {
            prefDict[prefName] = prefValue;
        }
    }
}
