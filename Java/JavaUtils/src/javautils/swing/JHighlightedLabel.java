package javautils.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*******************************************************************************
 * Adds highlighting functionality to a standard JLabel.
 ******************************************************************************/
public class JHighlightedLabel extends JLabel
{
    private Color highlightColor = Color.cyan;
    private int highlightOffset;
    private int highlightLength;
    private FontMetrics fontMetrics;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public JHighlightedLabel()
    {
        this( "" );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param f
     **************************************************************************/
    public JHighlightedLabel( Font font )
    {
        this();
        setFont( font );
    }

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public JHighlightedLabel( String text )
    {
        super( text );
        highlightOffset = 0;
        highlightLength = 0;
        fontMetrics = getFontMetrics( getFont() );
    }

    /***************************************************************************
     * Sets the highlighted region of this label.
     * 
     * @param offset
     *            start offset
     * @param length
     *            length of highlight
     **************************************************************************/
    public void setHighlight( int offset, int length )
    {
        int len = getText().length();
        if( offset > len )
        {
            highlightOffset = 0;
            highlightLength = 0;
            return;
        }

        highlightOffset = offset;
        highlightLength = Math.min( len - offset, length );
        repaint();
    }

    /***************************************************************************
     * Clears the highlighted region.
     **************************************************************************/
    public void clearHighlight()
    {
        setHighlight( 0, 0 );
    }

    /***************************************************************************
     * Sets the color used to highlight.
     * 
     * @param color
     **************************************************************************/
    public void setHighlightColor( Color color )
    {
        highlightColor = color;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#setFont(java.awt.Font)
     */
    @Override
    public void setFont( Font font )
    {
        super.setFont( font );
        fontMetrics = getFontMetrics( getFont() );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent( Graphics g )
    {
        if( highlightLength != 0 )
        {
            // TODO Fix for different alignments
            int startOffset = fontMetrics.stringWidth( getText().substring( 0,
                    highlightOffset ) );
            int width = fontMetrics.stringWidth( getText().substring(
                    highlightOffset, highlightOffset + highlightLength ) );

            g.setColor( highlightColor );
            g.fillRect( startOffset, 0, width, getHeight() );
        }
        super.paintComponent( g );
    }

    /***************************************************************************
     * JHighlightedLabel demo
     * 
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        class DemoRunner extends FrameRunner
        {
            @Override
            protected JFrame createFrame()
            {
                JFrame frame = new JFrame();
                JPanel panel = new JPanel();

                JHighlightedLabel label1 = new JHighlightedLabel( new Font(
                        "Monospaced", Font.PLAIN, 12 ) );
                label1.setText( "Sample un-highlighted" );
                JHighlightedLabel label2 = new JHighlightedLabel( new Font(
                        "Sans Serif", Font.BOLD, 16 ) );
                label2.setText( "Sample with highlight" );
                label2.setHighlight( 12, 9 );
                JHighlightedLabel label3 = new JHighlightedLabel(
                        "Another sample" );
                label3.setHighlightColor( Color.magenta );
                label3.setHighlight( 8, 3 );

                panel.setLayout( new GridBagLayout() );
                panel.add( label1, new GridBagConstraints( 0, 0, 1, 1, 1.0,
                        0.33, GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( label2, new GridBagConstraints( 0, 1, 1, 1, 1.0,
                        0.33, GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( label3, new GridBagConstraints( 0, 2, 1, 1, 1.0,
                        0.33, GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );

                frame.setTitle( "JHighlightedLabel demo" );
                frame.setContentPane( panel );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setPreferredSize( new Dimension( 220, 100 ) );
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );

                return frame;
            }

            @Override
            protected boolean validate()
            {
                return false;
            }
        }

        SwingUtilities.invokeLater( new DemoRunner() );
    }
}
