package javautils.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

/*******************************************************************************
 * This class is used to display a blurry glass pane over another component.
 ******************************************************************************/
public class BlurryGlassPane extends JComponent
{
    private Component underPane;
    private BufferedImage offScreenImage;
    private BufferedImageOp imageOp;

    /***************************************************************************
     * Constructor
     * 
     * @param underPane
     *            the component under this glass pane
     **************************************************************************/
    public BlurryGlassPane( Component underPane )
    {
        this.underPane = underPane;
        setOpaque( false );
        float[] f = { 0.1f, 0.1f, 0.1f, 0.1f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f };
        imageOp = new ConvolveOp( new Kernel( 3, 3, f ) );
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

        offScreenImage = new BufferedImage( underPane.getWidth(),
                underPane.getHeight(), BufferedImage.TYPE_INT_ARGB );
        Graphics2D tempGraphics = offScreenImage.createGraphics();
        underPane.paint( tempGraphics );
        tempGraphics.dispose();

        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage( offScreenImage, imageOp, 0, 0 );
    }

    /***************************************************************************
     * BlurryGlassPane demo frame.
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

                final JToggleButton tb = new JToggleButton(
                        "Click to toggle glass pane" );
                tb.setFocusable( false );

                JButton b1 = new JButton( "Dummy 1" );
                b1.setFocusable( false );
                JButton b2 = new JButton( "Dummy 2" );
                b2.setFocusable( false );

                final BlurryGlassPane glassPane = new BlurryGlassPane(
                        frame.getContentPane() );
                GlassPaneMouseListener listener = new GlassPaneMouseListener(
                        frame.getContentPane(), glassPane );
                listener.addLiveComponent( tb );
                glassPane.addMouseListener( listener );
                glassPane.addMouseMotionListener( listener );

                tb.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent arg0 )
                    {
                        glassPane.setVisible( tb.isSelected() );
                    }
                } );

                frame.setGlassPane( glassPane );

                frame.setLayout( new GridBagLayout() );

                frame.add( tb, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.01,
                        GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                frame.add( b1, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.01,
                        GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                frame.add( b2, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.98,
                        GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );

                frame.setTitle( "BlurryGlassPane Demo" );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( 300, 200 );
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );

                return frame;
            }

            @Override
            protected boolean validate()
            {
                return true;
            }
        }

        SwingUtilities.invokeLater( new DemoRunner() );
    }
}
