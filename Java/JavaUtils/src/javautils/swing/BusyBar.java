package javautils.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/*******************************************************************************
 * This component is a bar signifying some indeterminate amount of processing is
 * taking place.
 ******************************************************************************/
public class BusyBar extends JComponent implements ActionListener
{
    public static enum BarColor
    {
        BLUE, RED, GREEN, PURPLE;
    }

    private static final int ALPHA = 200;

    private final int size;
    private LinkedList<Color> colors;
    private Timer timer;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public BusyBar()
    {
        this( 30 );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param size
     **************************************************************************/
    public BusyBar( int size )
    {
        this( size, BarColor.BLUE );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param size
     * @param color
     **************************************************************************/
    public BusyBar( int size, BarColor color )
    {
        this.size = size;

        setOpaque( false );
        setPreferredSize( new Dimension( ( size * 8 ) + 14, size ) );
        setMinimumSize( new Dimension( ( size * 8 ) + 14, size ) );

        colors = new LinkedList<Color>();
        setColors( color );

        timer = new Timer( 125, this );
    }

    /***************************************************************************
     * Starts/stops this component from updating.
     * 
     * @param busy
     **************************************************************************/
    public void setBusy( boolean busy )
    {
        if( busy )
            timer.start();
        else
            timer.stop();
    }

    /***************************************************************************
     * Sets the colors associated with this bar.
     * 
     * @param color
     **************************************************************************/
    private void setColors( BarColor color )
    {
        colors.clear();
        switch( color )
        {
        default:
        case BLUE:
            colors.add( new Color( 119, 119, 255, ALPHA ) );
            colors.add( new Color( 102, 102, 255, ALPHA ) );
            colors.add( new Color( 85, 85, 255, ALPHA ) );
            colors.add( new Color( 68, 68, 255, ALPHA ) );
            colors.add( new Color( 51, 51, 255, ALPHA ) );
            colors.add( new Color( 34, 34, 255, ALPHA ) );
            colors.add( new Color( 17, 17, 255, ALPHA ) );
            colors.add( new Color( 0, 0, 255, ALPHA ) );
            break;
        case RED:
            colors.add( new Color( 255, 119, 119, ALPHA ) );
            colors.add( new Color( 255, 102, 102, ALPHA ) );
            colors.add( new Color( 255, 85, 85, ALPHA ) );
            colors.add( new Color( 255, 68, 68, ALPHA ) );
            colors.add( new Color( 255, 51, 51, ALPHA ) );
            colors.add( new Color( 255, 34, 34, ALPHA ) );
            colors.add( new Color( 255, 17, 17, ALPHA ) );
            colors.add( new Color( 255, 0, 0, ALPHA ) );
            break;
        case GREEN:
            colors.add( new Color( 119, 255, 119, ALPHA ) );
            colors.add( new Color( 102, 255, 102, ALPHA ) );
            colors.add( new Color( 85, 255, 85, ALPHA ) );
            colors.add( new Color( 68, 255, 68, ALPHA ) );
            colors.add( new Color( 51, 255, 51, ALPHA ) );
            colors.add( new Color( 34, 255, 34, ALPHA ) );
            colors.add( new Color( 17, 255, 17, ALPHA ) );
            colors.add( new Color( 0, 255, 0, ALPHA ) );
            break;
        case PURPLE:
            colors.add( new Color( 255, 119, 255, ALPHA ) );
            colors.add( new Color( 255, 102, 255, ALPHA ) );
            colors.add( new Color( 255, 85, 255, ALPHA ) );
            colors.add( new Color( 255, 68, 255, ALPHA ) );
            colors.add( new Color( 255, 51, 255, ALPHA ) );
            colors.add( new Color( 255, 34, 255, ALPHA ) );
            colors.add( new Color( 255, 17, 255, ALPHA ) );
            colors.add( new Color( 255, 0, 255, ALPHA ) );
            break;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed( ActionEvent arg0 )
    {
        // "shift" color array
        colors.addFirst( colors.removeLast() );

        repaint();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        for( int i = 0; i < colors.size(); i++ )
        {
            g.setColor( colors.get( i ) );
            g.fill3DRect( ( i * size + i * 2 ), 0, size, size, true );
        }
    }

    /***************************************************************************
     * BusyBar demo
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

                BusyBar bar1 = new BusyBar( 20, BarColor.BLUE );
                BusyBar bar2 = new BusyBar( 30, BarColor.GREEN );
                BusyBar bar3 = new BusyBar( 40, BarColor.PURPLE );
                BusyBar bar4 = new BusyBar( 50, BarColor.RED );
                bar1.setBusy( true );
                bar2.setBusy( true );
                bar3.setBusy( true );
                bar4.setBusy( true );

                panel.setLayout( new GridBagLayout() );
                panel.add( bar1, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( bar2, new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( bar3, new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( bar4, new GridBagConstraints( 0, 3, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.NONE,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );

                frame.setTitle( "BusyBar demo" );
                frame.setContentPane( panel );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
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
