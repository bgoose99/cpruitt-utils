using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Chex.Data
{
    public class HexDataRow : INotifyPropertyChanged
    {
        #region Members

        private IList<Byte>   bytes;
        private IList<string> strings;
        private ASCIIEncoding asciiEncoder;
        private int           beginHighlight;
        private int           endHighlight;
        private string        preHighlightString;
        private string        highlightString;
        private string        postHighlightString;

        public event PropertyChangedEventHandler PropertyChanged;

        #endregion

        #region Properties

        /// <summary>
        /// The bytes in this row.
        /// </summary>
        public IList<Byte> Bytes
        {
            get { return bytes; }
            set
            {
                bytes.Clear();
                foreach ( Byte b in value ) bytes.Add( b );
                initDisplayStrings();
                clearHighlight();
            }
        }

        public string x0 { get { return strings[0]; } }
        public string x1 { get { return strings[1]; } }
        public string x2 { get { return strings[2]; } }
        public string x3 { get { return strings[3]; } }
        public string x4 { get { return strings[4]; } }
        public string x5 { get { return strings[5]; } }
        public string x6 { get { return strings[6]; } }
        public string x7 { get { return strings[7]; } }
        public string x8 { get { return strings[8]; } }
        public string x9 { get { return strings[9]; } }
        public string xa { get { return strings[10]; } }
        public string xb { get { return strings[11]; } }
        public string xc { get { return strings[12]; } }
        public string xd { get { return strings[13]; } }
        public string xe { get { return strings[14]; } }
        public string xf { get { return strings[15]; } }
        public string AsciiPreHighlight { get { return preHighlightString; } }
        public string AsciiHighlight { get { return highlightString; } }
        public string AsciiPostHighlight { get { return postHighlightString; } }

        #endregion

        #region Constructors

        /// <summary>
        /// Default constructor.
        /// </summary>
        public HexDataRow()
            : this( null )
        {
        }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="bytes"></param>
        public HexDataRow( Byte[] bytes )
        {
            this.bytes = new List<Byte>();
            if ( bytes != null )
                foreach ( Byte b in bytes )
                    this.bytes.Add( b );

            this.strings = new List<string>();
            for ( int i = 0; i < 16; ++i )
                this.strings.Add( "" );

            this.asciiEncoder = new ASCIIEncoding();

            clearHighlight();
            initDisplayStrings();
        }

        #endregion

        #region Methods

        /// <summary>
        /// Highlights specified bytes in this row.
        /// </summary>
        /// <param name="beginIndex"></param>
        /// <param name="endIndex"></param>
        public void highlightBytes( int beginIndex, int endIndex )
        {
            // bound and swap begin/end index, if necessary
            beginHighlight = Math.Min( beginIndex, endIndex );
            endHighlight = Math.Min( Math.Max( beginIndex, endIndex ), 15 );

            // bound to byte count
            beginHighlight = Math.Min( beginHighlight, bytes.Count );
            endHighlight = Math.Min( endHighlight, bytes.Count );

            if ( bytes.Count == 0 )
            {
                preHighlightString = "";
                highlightString = "";
                postHighlightString = "";
            }
            else
            {
                preHighlightString = replaceNonPrintableChars( ( beginHighlight < 0 ) ? asciiEncoder.GetString( bytes.ToArray<Byte>() ) : asciiEncoder.GetString( bytes.ToArray<Byte>(), 0, beginHighlight ) );
                highlightString = replaceNonPrintableChars( ( beginHighlight < 0 ) ? "" : asciiEncoder.GetString( bytes.ToArray<Byte>(), beginHighlight, endHighlight - beginHighlight + 1 ) );
                postHighlightString = replaceNonPrintableChars( ( beginHighlight < 0 ) ? "" : asciiEncoder.GetString( bytes.ToArray<Byte>(), endHighlight + 1, bytes.Count - endHighlight - 1 ) );
            }

            onPropertyChanged( "AsciiPreHighlight" );
            onPropertyChanged( "AsciiHighlight" );
            onPropertyChanged( "AsciiPostHighlight" );
        }

        /// <summary>
        /// Clears all highlights from this row.
        /// </summary>
        public void clearHighlight()
        {
            highlightBytes( -1, -1 );
        }

        /// <summary>
        /// Returns the string used to display the byte at the supplied index.
        /// </summary>
        /// <param name="index"></param>
        /// <returns></returns>
        public string getDisplayString( int index )
        {
            return ( index >= 0 && index < 16 ) ? strings[index] : "Invalid";
        }

        /// <summary>
        /// Property change method.
        /// </summary>
        /// <param name="name"></param>
        protected void onPropertyChanged( string name )
        {
            PropertyChangedEventHandler handler = PropertyChanged;
            if ( handler != null )
            {
                handler( this, new PropertyChangedEventArgs( name ) );
            }
        }

        /// <summary>
        /// Initializes the display strings for all bytes in this row.
        /// </summary>
        private void initDisplayStrings()
        {
            strings[0] = bytes.Count > 0 ? Convert.ToString( bytes[0], 16 ).PadLeft( 2, '0' ) : "";
            strings[1] = bytes.Count > 1 ? Convert.ToString( bytes[1], 16 ).PadLeft( 2, '0' ) : "";
            strings[2] = bytes.Count > 2 ? Convert.ToString( bytes[2], 16 ).PadLeft( 2, '0' ) : "";
            strings[3] = bytes.Count > 3 ? Convert.ToString( bytes[3], 16 ).PadLeft( 2, '0' ) : "";
            strings[4] = bytes.Count > 4 ? Convert.ToString( bytes[4], 16 ).PadLeft( 2, '0' ) : "";
            strings[5] = bytes.Count > 5 ? Convert.ToString( bytes[5], 16 ).PadLeft( 2, '0' ) : "";
            strings[6] = bytes.Count > 6 ? Convert.ToString( bytes[6], 16 ).PadLeft( 2, '0' ) : "";
            strings[7] = bytes.Count > 7 ? Convert.ToString( bytes[7], 16 ).PadLeft( 2, '0' ) : "";
            strings[8] = bytes.Count > 8 ? Convert.ToString( bytes[8], 16 ).PadLeft( 2, '0' ) : "";
            strings[9] = bytes.Count > 9 ? Convert.ToString( bytes[9], 16 ).PadLeft( 2, '0' ) : "";
            strings[10] = bytes.Count > 10 ? Convert.ToString( bytes[10], 16 ).PadLeft( 2, '0' ) : "";
            strings[11] = bytes.Count > 11 ? Convert.ToString( bytes[11], 16 ).PadLeft( 2, '0' ) : "";
            strings[12] = bytes.Count > 12 ? Convert.ToString( bytes[12], 16 ).PadLeft( 2, '0' ) : "";
            strings[13] = bytes.Count > 13 ? Convert.ToString( bytes[13], 16 ).PadLeft( 2, '0' ) : "";
            strings[14] = bytes.Count > 14 ? Convert.ToString( bytes[14], 16 ).PadLeft( 2, '0' ) : "";
            strings[15] = bytes.Count > 15 ? Convert.ToString( bytes[15], 16 ).PadLeft( 2, '0' ) : "";
        }

        /// <summary>
        /// Replaces all non-printable characters with a space.
        /// </summary>
        /// <param name="value"></param>
        /// <returns></returns>
        private static string replaceNonPrintableChars( string value )
        {
            string pattern = "[^ -~]";
            Regex regex = new Regex( pattern );
            return regex.Replace( value, " " );
        }

        #endregion
    }
}
