using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Runtime.InteropServices;

namespace StreamView.View
{
    public partial class ByteStreamView : UserControl, IByteStreamView
    {
        /// <summary>
        /// Width of a single line, in bytes.
        /// </summary>
        private static readonly int LINE_BYTE_WIDTH = 16;

        /// <summary>
        /// Width of new line character(s).
        /// </summary>
        private static readonly int NEW_LINE_LENGTH = Environment.NewLine.Length;

        /// <summary>
        /// Prefix for a hex string.
        /// </summary>
        private static readonly string HEX_PREFIX = "0x";

        /// <summary>
        /// Prefix for a line.
        /// </summary>
        private static readonly string LINE_PREFIX = "  ";

        private Font baseFont;
        private Font highlightFont;
        private Color highlighColor;
        private int prefixLength;

        #region Properties

        /// <summary>
        /// Line length, not including new line character(s).
        /// </summary>
        private int LineWidth
        {
            get
            {
                return 
                    LINE_BYTE_WIDTH * 8 +     // bits/byte
                    ( LINE_BYTE_WIDTH - 1 ) + // spaces between bytes
                    prefixLength;             // prefix
            }
        }

        #endregion

        #region Constructors

        /// <summary>
        /// Constructor
        /// </summary>
        public ByteStreamView()
        {
            InitializeComponent();

            baseFont = richTextBox.Font;
            highlightFont = new Font( baseFont, FontStyle.Underline );
            highlighColor = Color.Blue;
        }

        #endregion

        #region Methods

        /// <summary>
        /// Highlights one or more bytes, and the corresponding bits.
        /// </summary>
        /// <param name="bitOffset"></param>
        /// <param name="bitLength"></param>
        private void highlightBytes( int bitOffset, int bitLength )
        {
            int byteOffset = bitOffset / 8;
            int numBytes = ( ( bitOffset + bitLength - 1 ) / 8 ) - ( bitOffset / 8 ) + 1;

            int bitInByteOffset = 0;
            int numBitsInByte = 0;

            for( int i = byteOffset; i < ( byteOffset + numBytes ); i++ )
            {
                bitInByteOffset = ( i == byteOffset ) ? ( bitOffset % 8 ) : 0;
                numBitsInByte = Math.Min( 8 - bitInByteOffset, bitLength );
                bitLength -= numBitsInByte;
                highlightByte( i, bitInByteOffset, numBitsInByte );
            }
        }

        /// <summary>
        /// Highlights a single byte, and the corresponding bits.
        /// </summary>
        /// <param name="byteOffset"></param>
        /// <param name="bitOffset"></param>
        /// <param name="numBits"></param>
        private void highlightByte( int byteOffset, int bitOffset, int numBits )
        {
            int byteLineIndex = ( byteOffset ) / LINE_BYTE_WIDTH;
            int byteSelectionStart = 
                byteLineIndex * ( LineWidth * 2 + ( NEW_LINE_LENGTH ) ) + // lines * lineWidth
                prefixLength +                                            // prefix at beginning of line
                ( ( byteOffset % LINE_BYTE_WIDTH ) * 9 );                 // individual byte
            int bitLineIndex = byteLineIndex + 1;
            int bitSelectionStart = byteSelectionStart + ( LineWidth + 1 ) + bitOffset;

            richTextBox.SelectionStart = byteSelectionStart;
            richTextBox.SelectionLength = 2;
            richTextBox.SelectionFont = highlightFont;
            richTextBox.SelectionColor = highlighColor;

            richTextBox.SelectionStart = bitSelectionStart;
            richTextBox.SelectionLength = numBits;
            richTextBox.SelectionFont = highlightFont;
            richTextBox.SelectionColor = highlighColor;
        }

        #endregion

        #region IByteStreamView Methods

        /// <summary cref="IByteStreamView.setBytes">
        /// <see cref="IByteStreamView.setBytes"/>
        /// </summary>
        /// <param name="bytes"></param>
        public void setBytes( Byte[] bytes )
        {
            setBytes( bytes, 0 );
        }

        /// <summary cref="IByteStreamView.setBytes">
        /// <see cref="IByteStreamView.setBytes"/>
        /// </summary>
        /// <param name="bytes"></param>
        /// <param name="offset"></param>
        public void setBytes( Byte[] bytes, long offset )
        {
            // get necessary padding width for hex-formatting
            int padding = String.Format( "{0,0:X}", ( offset + bytes.Length - 1 ) ).Length;

            // set overall prefix length, so our byte/bit location calculations are correct
            prefixLength = HEX_PREFIX.Length + padding + LINE_PREFIX.Length;

            DrawingControl.suspendDrawing( richTextBox );
            richTextBox.Clear();

            // convert the bytes to a '-' delimited string (- is the default)
            string hexString = BitConverter.ToString( bytes );

            // save our byte/bit prefixes
            long byteOffset = offset;
            string bitPrefix = String.Format( "{0}{1,0:X" + padding + "}{2}", HEX_PREFIX, byteOffset, LINE_PREFIX );
            string bytePrefix = String.Format( "{0," + ( HEX_PREFIX.Length + padding ) + "}{1}", String.Empty, LINE_PREFIX );

            // init our lines
            string hexLine = bytePrefix;
            string binLine = bitPrefix;

            string[] sArray = hexString.Split( '-' );
            int byteCounter = 0;
            foreach( string s in sArray )
            {
                hexLine += s;
                binLine += Convert.ToString( Convert.ToInt32( s, 16 ), 2 ).PadLeft( 8, '0' );
                byteCounter++;
                if( byteCounter > ( LINE_BYTE_WIDTH - 1 ) )
                {
                    byteCounter = 0;
                    richTextBox.AppendText( hexLine.PadRight( LineWidth ) + Environment.NewLine );
                    richTextBox.AppendText( binLine.PadRight( LineWidth ) + Environment.NewLine );
                    hexLine = bytePrefix;
                    byteOffset += LINE_BYTE_WIDTH;
                    bitPrefix = String.Format( "{0}{1,0:X" + padding + "}{2}", HEX_PREFIX, byteOffset, LINE_PREFIX );
                    binLine = bitPrefix;
                }
                else
                {
                    // spacing between bytes
                    hexLine += "       ";
                    binLine += " ";
                }
            }

            // don't add empty lines
            if( hexLine.Length != prefixLength && binLine.Length != prefixLength )
            {
                richTextBox.AppendText( hexLine.PadRight( LineWidth ) + Environment.NewLine );
                richTextBox.AppendText( binLine.PadRight( LineWidth ) + Environment.NewLine );
            }

            // hack to go to the top
            richTextBox.SelectionStart = 0;
            richTextBox.SelectionLength = 0;
            richTextBox.ScrollToCaret();
            DrawingControl.resumeDrawing( richTextBox );
        }

        /// <summary cref="IByteStreamView.highlightSelection">
        /// <see cref="IByteStreamView.highlightSelection"/>
        /// </summary>
        public void highlightSelection( int bitOffset, int bitLength )
        {
            if( bitLength < 1 )
                return;

            DrawingControl.suspendDrawing( richTextBox );
            richTextBox.SelectAll();
            richTextBox.SelectionFont = baseFont;
            richTextBox.SelectionColor = this.ForeColor;
            highlightBytes( bitOffset, bitLength );
            richTextBox.SelectionStart = 0;
            richTextBox.SelectionLength = 0;
            DrawingControl.resumeDrawing( richTextBox );
            richTextBox.Refresh();
        }

        #endregion
    }
}
