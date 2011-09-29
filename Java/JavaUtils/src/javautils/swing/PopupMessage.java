package javautils.swing;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/*******************************************************************************
 * This class provides a convenient way to show a popup with a message. This is
 * ideal for situations where you want to alert the user of something, but don't
 * require any interaction.
 ******************************************************************************/
public class PopupMessage
{
    private Popup popup;
    private Timer hideTimer;
    private Timer fadeTimer;
    private float alpha;
    private Component comp;
    private final Color background;

    /***************************************************************************
     * Constructor
     * 
     * @param message
     **************************************************************************/
    public PopupMessage( String message )
    {
        this( message, 1000 );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param message
     * @param lifespan
     **************************************************************************/
    public PopupMessage( String message, int lifespan )
    {
        this( message, lifespan, new Point( 0, 0 ) );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param message
     * @param lifespan
     * @param location
     **************************************************************************/
    public PopupMessage( String message, int lifespan, Point location )
    {
        this( message, lifespan, location, Color.yellow );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param message
     * @param lifespan
     * @param location
     * @param bgColor
     **************************************************************************/
    public PopupMessage( String message, int lifespan, Point location,
            Color bgColor )
    {
        background = bgColor;

        comp = new PopupComponent( message );

        popup = PopupFactory.getSharedInstance().getPopup( null, comp,
                location.x, location.y );
        popup.show();
        hideTimer = new Timer( lifespan, new HideTimerTask() );
        hideTimer.setInitialDelay( lifespan );

        // set up the fade timer for a total operation of 1/2 second or
        // lifespan, whichever is smaller
        int fadeDuration = Math.min( 500, lifespan );
        fadeTimer = new Timer( fadeDuration / 10, new FadeTimerTask() );
        fadeTimer.setInitialDelay( lifespan - fadeDuration );

        // set initial alpha at 100%
        alpha = 1.0f;
    }

    /***************************************************************************
     * Shows this popup.
     **************************************************************************/
    public void showPopup()
    {
        hideTimer.start();
        fadeTimer.start();
    }

    /***************************************************************************
     * Hiding timer utility class.
     **************************************************************************/
    private class HideTimerTask implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            popup.hide();
            hideTimer.stop();
            fadeTimer.stop();
        }
    }

    /***************************************************************************
     * Fade timer utility class.
     **************************************************************************/
    private class FadeTimerTask implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            alpha -= 0.1f;
            if( alpha < 0.0 )
                alpha = 0.0f;
            comp.repaint();
        }
    }

    /***************************************************************************
     * The component that will be used to display our message.
     **************************************************************************/
    private class PopupComponent extends JLabel
    {
        public PopupComponent( String message )
        {
            super();
            setText( "<html><b>" + message + "</b></html>" );
            setOpaque( true );
            setBackground( background );
            setBorder( BorderFactory.createRaisedBevelBorder() );
        }

        @Override
        public void paint( Graphics g )
        {
            ( (Graphics2D)g ).setComposite( AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, alpha ) );
            super.paint( g );
        }
    }

    /***************************************************************************
     * Demo
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
                frame.setLayout( new FlowLayout() );

                JButton b1 = new JButton( "Standard popup" );
                b1.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        Point p = MouseInfo.getPointerInfo().getLocation();
                        p.y -= 20;
                        PopupMessage msg = new PopupMessage( "I am a popup",
                                2000, p );
                        msg.showPopup();
                    }
                } );

                JButton b2 = new JButton( "Custom color" );
                b2.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        Point p = MouseInfo.getPointerInfo().getLocation();
                        p.y -= 20;
                        PopupMessage msg = new PopupMessage(
                                "I am a popup with a custom color", 2000, p,
                                Color.green );
                        msg.showPopup();
                    }
                } );

                frame.setTitle( "PopupMessage Demo" );
                frame.add( b1 );
                frame.add( b2 );

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
