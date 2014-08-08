package javautils.swing;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javautils.IconManager;
import javautils.IconManager.IconSize;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/*******************************************************************************
 * This class manages a group of {@link JPanel}s. This class provides methods
 * for easily switching between panels, and provides a fading animation while
 * switching.
 ******************************************************************************/
public class JFadePanel extends JPanel
{
    private CardLayout layout;
    private Timer fadeTimer;
    private float alpha;
    private BufferedImage offScreenImage;

    protected List<? extends JPanel> panels;

    private boolean fading = false;

    /***************************************************************************
     * Constructor
     * 
     * @param components
     **************************************************************************/
    public JFadePanel( JPanel... components )
    {
        this( Arrays.asList( components ) );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param components
     **************************************************************************/
    public JFadePanel( List<? extends JPanel> components )
    {
        int prefWidth = 200;
        int prefHeight = 200;
        panels = new ArrayList<JPanel>( components );
        layout = new CardLayout();

        setLayout( layout );

        for( JComponent comp : this.panels )
        {
            add( comp, comp.toString() );
            prefWidth = Math.max( prefWidth, comp.getWidth() );
            prefHeight = Math.max( prefHeight, comp.getHeight() );
        }

        setPreferredSize( new Dimension( prefWidth, prefHeight ) );

        fadeTimer = new Timer( 75, new FadeListener() );

        alpha = 1.0f;
    }

    /***************************************************************************
     * Returns the total number of panels.
     * 
     * @return
     **************************************************************************/
    public int getPanelCount()
    {
        return panels.size();
    }

    /***************************************************************************
     * Shows the next panel.
     **************************************************************************/
    public void next()
    {
        layout.next( this );
        fade();
    }

    /***************************************************************************
     * Shows the last panel in the list.
     **************************************************************************/
    public void last()
    {
        layout.last( this );
        fade();
    }

    /***************************************************************************
     * Shows the first panel in the list.
     **************************************************************************/
    public void first()
    {
        layout.first( this );
        fade();
    }

    /***************************************************************************
     * Shows the previous panel.
     **************************************************************************/
    public void previous()
    {
        layout.previous( this );
        fade();
    }

    /***************************************************************************
     * Fades in the current panel.
     **************************************************************************/
    private void fade()
    {
        alpha = 0.0f;
        fading = true;
        fadeTimer.start();
    }

    @Override
    public void paintComponent( Graphics g )
    {
        System.out.println( "Huh?" );
        super.paintComponent( g );
        if( fading )
        {
            System.out.println( "Painting faded panel, alpha = " + alpha );
            int width = this.getWidth();
            int height = this.getHeight();

            if( width == 0 || height == 0 )
                return;

            if( offScreenImage == null || offScreenImage.getWidth() != width
                    || offScreenImage.getHeight() != height )
            {
                offScreenImage = new BufferedImage( width, height,
                        BufferedImage.TYPE_INT_RGB );
            }

            super.paintComponent( offScreenImage.getGraphics() );
            Graphics2D g2d = (Graphics2D)g;
            g2d.setComposite( AlphaComposite.SrcOver.derive( alpha ) );
            g2d.drawImage( offScreenImage, null, 0, 0 );
            g2d.dispose();
        } else
        {
            System.out.println( "Painting stock panel" );
            super.paintComponent( g );
        }
    }

    // Utility classes

    /***************************************************************************
     * 
     **************************************************************************/
    private class FadeListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            alpha += 0.05f;
            repaint();
            if( alpha >= 1.0f )
            {
                alpha = 1.0f;
                fadeTimer.stop();
                fading = false;
            }
        }
    }

    /***************************************************************************
     * JFadePanel demo
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
                frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
                JPanel panel1 = new JPanel();
                panel1.add( new JButton( "A button" ) );
                panel1.add( new JTextField( "a text field" ) );
                JPanel panel2 = new JPanel();
                panel2.add( new JCheckBox( "A check box" ) );
                panel2.add( new JButton( "another button" ) );
                JPanel panel3 = new JPanel();
                panel3.add( new JRadioButton( "a radio button" ) );
                panel3.add( new JLabel( "a label" ) );
                JPanel panel4 = new JPanel();
                panel4.add( new JTextArea( "an editor pane" ) );

                final JFadePanel fp = new JFadePanel( panel1, panel2, panel3,
                        panel4 );
                JButton first = new JButton( IconManager.getIcon(
                        IconManager.RESULTSET_FIRST, IconSize.X16 ) );
                first.setFocusable( false );
                first.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        fp.first();
                    }
                } );

                JButton last = new JButton( IconManager.getIcon(
                        IconManager.RESULTSET_LAST, IconSize.X16 ) );
                last.setFocusable( false );
                last.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        fp.last();
                    }
                } );

                JButton next = new JButton( IconManager.getIcon(
                        IconManager.RESULTSET_NEXT, IconSize.X16 ) );
                next.setFocusable( false );
                next.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        fp.next();
                    }
                } );

                JButton prev = new JButton( IconManager.getIcon(
                        IconManager.RESULTSET_PREVIOUS, IconSize.X16 ) );
                prev.setFocusable( false );
                prev.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        fp.previous();
                    }
                } );

                JToolBar toolbar = new JToolBar();
                toolbar.setFloatable( false );
                toolbar.setFocusable( false );
                toolbar.add( first );
                toolbar.add( prev );
                toolbar.add( next );
                toolbar.add( last );

                frame.setTitle( "JFadePanel Demo" );
                frame.setSize( 350, 400 );
                frame.setLayout( new BorderLayout() );
                frame.add( toolbar, BorderLayout.PAGE_START );
                frame.add( fp, BorderLayout.CENTER );

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
