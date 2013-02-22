package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseMotionListener;

import javautils.Utils;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;

/*******************************************************************************
 * Simple {@link IByteStreamView}.
 ******************************************************************************/
public class ByteStreamView extends JPanel implements IByteStreamView
{
    private static final Dimension PREFERRED_SIZE = new Dimension( 930, 260 );

    private static final int LINE_BYTE_WIDTH = 16;
    private static final String NEW_LINE = System
            .getProperty( "line.separator" );
    private static final int NEW_LINE_LENGTH = NEW_LINE.length();
    private static final String HEX_PREFIX = "0x";
    private static final String LINE_PREFIX = "  ";

    private JTextPane textPane;
    private JPanel textPanel;
    private JScrollPane scrollPane;
    private DefaultHighlighter.DefaultHighlightPainter highlightPainter;
    private int prefixLength;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public ByteStreamView()
    {
        textPane = new JTextPane()
        {
            @Override
            public void addMouseMotionListener( MouseMotionListener l )
            {
                // don't allow user selection
            }
        };
        textPane.setFont( new Font( Font.MONOSPACED, Font.PLAIN, 10 ) );
        textPane.setEditable( false );

        // simple workaround for disabling text pane line wrapping
        textPanel = new JPanel();
        textPanel.setLayout( new BorderLayout() );
        textPanel.add( textPane, BorderLayout.CENTER );
        scrollPane = new JScrollPane( textPanel );

        highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(
                Color.YELLOW );

        setupPanel();
    }

    /***************************************************************************
     * Sets up this panel.
     **************************************************************************/
    private void setupPanel()
    {
        setLayout( new BorderLayout() );
        add( scrollPane, BorderLayout.CENTER );

        setPreferredSize( PREFERRED_SIZE );
    }

    /***************************************************************************
     * Returns the width of a line, not including new line characters.
     * 
     * @return
     **************************************************************************/
    private int getLineWidth()
    {
        return LINE_BYTE_WIDTH * 8 + ( LINE_BYTE_WIDTH - 1 ) + prefixLength;
    }

    /***************************************************************************
     * Highlights one or more bytes and the corresponding bits.
     * 
     * @param bitOffset
     * @param bitLength
     **************************************************************************/
    private void highlightBytes( int bitOffset, int bitLength )
    {
        int byteOffset = bitOffset / 8;
        int numBytes = ( ( bitOffset + bitLength - 1 ) / 8 ) - ( bitOffset / 8 )
                + 1;

        int bitInByteOffset = 0;
        int numBitsInByte = 0;

        textPane.getHighlighter().removeAllHighlights();
        for( int i = byteOffset; i < ( byteOffset + numBytes ); i++ )
        {
            bitInByteOffset = ( i == byteOffset ) ? ( bitOffset % 8 ) : 0;
            numBitsInByte = Math.min( 8 - bitInByteOffset, bitLength );
            bitLength -= numBitsInByte;
            highlightByte( i, bitInByteOffset, numBitsInByte );
        }
    }

    /***************************************************************************
     * Highlights a single byte and the corresponding bits.
     * 
     * @param byteOffset
     * @param bitOffset
     * @param numBits
     **************************************************************************/
    private void highlightByte( int byteOffset, int bitOffset, int numBits )
    {
        int byteLineIndex = byteOffset / LINE_BYTE_WIDTH;
        int byteSelectionStart = byteLineIndex
                * ( getLineWidth() * 2 + ( NEW_LINE_LENGTH * 2 ) )
                + prefixLength + ( ( byteOffset % LINE_BYTE_WIDTH ) * 9 );
        int bitSelectionStart = byteSelectionStart
                + ( getLineWidth() + NEW_LINE_LENGTH ) + bitOffset;

        try
        {
            textPane.getHighlighter().addHighlight( byteSelectionStart,
                    byteSelectionStart + 2, highlightPainter );
            textPane.getHighlighter().addHighlight( bitSelectionStart,
                    bitSelectionStart + numBits, highlightPainter );
        } catch( BadLocationException e )
        {
            // do nothing
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.IByteStreamView#setBytes(byte[])
     */
    @Override
    public void setBytes( byte[] bytes )
    {
        setBytes( bytes, 0 );
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.IByteStreamView#setBytes(byte[], long)
     */
    @Override
    public void setBytes( byte[] bytes, long offset )
    {
        try
        {
            int padding = String.format( "%s", ( offset + bytes.length - 1 ) )
                    .length();

            prefixLength = HEX_PREFIX.length() + padding + LINE_PREFIX.length();

            textPane.setText( "" );

            StringBuffer buf = new StringBuffer();
            String hexByte;
            for( byte b : bytes )
            {
                hexByte = Integer.toHexString( 0xFF & b );
                if( hexByte.length() == 1 )
                    buf.append( '0' );
                buf.append( hexByte );
                buf.append( " " );
            }
            String hexString = buf.toString().toUpperCase();

            long byteOffset = offset;
            String bitPrefix = String.format( "%s%0" + padding + "x%s",
                    HEX_PREFIX, byteOffset, LINE_PREFIX );
            String bytePrefix = String.format( "%" + ( prefixLength ) + "s",
                    LINE_PREFIX );

            String hexLine = bytePrefix;
            String binLine = bitPrefix;

            String[] sArray = hexString.split( " " );
            int byteCounter = 0;
            for( String s : sArray )
            {
                hexLine += s;
                binLine += String.format( "%8s",
                        Integer.toBinaryString( Integer.parseInt( s, 16 ) ) )
                        .replace( ' ', '0' );

                byteCounter++;
                if( byteCounter > ( LINE_BYTE_WIDTH - 1 ) )
                {
                    byteCounter = 0;
                    hexLine = Utils.getPaddedString( hexLine, getLineWidth(),
                            ' ', false );
                    binLine = Utils.getPaddedString( binLine, getLineWidth(),
                            ' ', false );
                    textPane.getDocument().insertString(
                            textPane.getDocument().getLength(),
                            hexLine + NEW_LINE, null );
                    textPane.getDocument().insertString(
                            textPane.getDocument().getLength(),
                            binLine + NEW_LINE, null );
                    hexLine = bytePrefix;
                    byteOffset += LINE_BYTE_WIDTH;
                    bitPrefix = String.format( "%s%0" + padding + "x%s",
                            HEX_PREFIX, byteOffset, LINE_PREFIX );
                    binLine = bitPrefix;
                } else
                {
                    hexLine += "       ";
                    binLine += " ";
                }
            }

            if( hexLine.length() != prefixLength
                    && binLine.length() != prefixLength )
            {
                hexLine = Utils.getPaddedString( hexLine, getLineWidth(), ' ',
                        false );
                binLine = Utils.getPaddedString( binLine, getLineWidth(), ' ',
                        false );
                textPane.getDocument().insertString(
                        textPane.getDocument().getLength(), hexLine + NEW_LINE,
                        null );
                textPane.getDocument().insertString(
                        textPane.getDocument().getLength(), binLine + NEW_LINE,
                        null );
            }

            textPane.getHighlighter().removeAllHighlights();
        } catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.IByteStreamView#highlightSelection(int, int)
     */
    @Override
    public void highlightSelection( int bitOffset, int bitLength )
    {
        if( bitLength < 1 )
            return;

        highlightBytes( bitOffset, bitLength );
        updateUI();
    }

}
