package javautils.swing;

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
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.plaf.LayerUI;

public class BlurLayerUI extends LayerUI<JComponent>
{
    private BufferedImage offScreenImage;
    private BufferedImageOp imageOp;
    private boolean blur;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public BlurLayerUI()
    {
        blur = false;
        float[] f = { 0.1f, 0.1f, 0.1f, 0.1f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f };
        imageOp = new ConvolveOp( new Kernel( 3, 3, f ), ConvolveOp.EDGE_NO_OP,
                null );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.LayerUI#paint(java.awt.Graphics,
     * javax.swing.JComponent)
     */
    @Override
    public void paint( Graphics g, JComponent component )
    {
        if( blur )
        {
            int width = component.getWidth();
            int height = component.getHeight();

            if( width == 0 || height == 0 )
                return;

            if( offScreenImage == null || offScreenImage.getWidth() != width
                    || offScreenImage.getHeight() != height )
            {
                offScreenImage = new BufferedImage( width, height,
                        BufferedImage.TYPE_INT_RGB );
            }

            Graphics2D tempGraphics = offScreenImage.createGraphics();
            tempGraphics.setClip( g.getClip() );
            super.paint( tempGraphics, component );
            tempGraphics.dispose();

            Graphics2D g2d = (Graphics2D)g;
            g2d.drawImage( offScreenImage, imageOp, 0, 0 );
        } else
        {
            super.paint( g, component );
        }
    }

    /***************************************************************************
     * Toggles the blurry.
     **************************************************************************/
    public void toggleBlur()
    {
        blur = !blur;
    }

    /***************************************************************************
     * BlurLayerUI demo frame.
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
                        "Click to toggle blurry layer" );
                tb.setFocusable( false );

                JButton b1 = new JButton( "Dummy 1" );
                b1.setFocusable( false );
                JButton b2 = new JButton( "Dummy 2" );
                b2.setFocusable( false );

                JPanel panel = new JPanel();
                final BlurLayerUI layerUI = new BlurLayerUI();
                final JLayer<JComponent> layer = new JLayer<JComponent>( panel,
                        layerUI );

                tb.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent arg0 )
                    {
                        layerUI.toggleBlur();
                        layer.repaint();
                    }
                } );

                panel.setLayout( new GridBagLayout() );

                panel.add( tb, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.01,
                        GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( b1, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.01,
                        GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( b2, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.98,
                        GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );

                frame.add( layer );
                frame.setTitle( "BlurLayerUI Demo" );
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
