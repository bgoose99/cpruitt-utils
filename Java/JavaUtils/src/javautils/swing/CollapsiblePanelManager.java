package javautils.swing;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/*******************************************************************************
 * This class manages one or more {@link CollapsiblePanel}s in a single
 * {@link JPanel}.
 ******************************************************************************/
public class CollapsiblePanelManager extends JPanel
{
    private JPanel panel;
    private JScrollPane scrollPane;

    /***************************************************************************
     * Constructor
     * 
     * @param panels
     **************************************************************************/
    public CollapsiblePanelManager( CollapsiblePanel... panels )
    {
        this( false, "Desc", panels );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param drawBorder
     * @param description
     * @param panels
     **************************************************************************/
    public CollapsiblePanelManager( boolean drawBorder, String description,
            CollapsiblePanel... panels )
    {
        panel = new JPanel();
        panel.setLayout( new GridBagLayout() );

        scrollPane = new JScrollPane( panel );
        if( drawBorder )
            scrollPane.setBorder( new TitledBorder( description ) );
        else
            scrollPane.setBorder( null );

        setupPanels( panels );
        setLayout( new BorderLayout() );
        add( scrollPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * Sets up this panel.
     * 
     * @param panels
     **************************************************************************/
    private void setupPanels( CollapsiblePanel... panels )
    {
        for( int i = 0; i < panels.length; i++ )
        {
            panel.add( panels[i], new GridBagConstraints( 0, i, 1, 1, 1.0,
                    0.01, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                    new Insets( 1, 1, 1, 1 ), 0, 0 ) );
        }
        // add an empty panel to prevent last panel from filling empty space
        panel.add( new JPanel(), new GridBagConstraints( 0, panels.length, 1,
                1, 1.0, 0.99, GridBagConstraints.NORTH,
                GridBagConstraints.BOTH, new Insets( 1, 1, 1, 1 ), 0, 0 ) );
    }

    /***************************************************************************
     * CollapsiblePanelManager demo.
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

                JTextArea ta = new JTextArea();
                ta.setText( "This demo shows how one could use a\n"
                        + "CollapsiblePanelManager to set up a\n"
                        + "panel with various collapsible panels\n"
                        + "so that the UI does not get cluttered." );

                JPanel p1 = new JPanel();
                p1.setLayout( new GridBagLayout() );
                p1.add( new JButton( "Option 1" ),
                        new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                                GridBagConstraints.WEST,
                                GridBagConstraints.NONE,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                p1.add( new JButton( "Option 2" ),
                        new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                                GridBagConstraints.WEST,
                                GridBagConstraints.NONE,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                CollapsiblePanel cp1 = new CollapsiblePanel( p1, "Options 1",
                        false );

                JPanel p2 = new JPanel();
                p2.setLayout( new GridBagLayout() );
                p2.add( new JRadioButton( "Radio option" ),
                        new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                                GridBagConstraints.WEST,
                                GridBagConstraints.NONE,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                p2.add( new JToggleButton( "Toggle option" ),
                        new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                                GridBagConstraints.WEST,
                                GridBagConstraints.NONE,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                p2.add( new JCheckBox( "Checkbox option" ),
                        new GridBagConstraints( 0, 2, 1, 1, 1.0, 1.0,
                                GridBagConstraints.WEST,
                                GridBagConstraints.NONE,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                CollapsiblePanel cp2 = new CollapsiblePanel( p2, "Options 2",
                        false );

                CollapsiblePanelManager cpm = new CollapsiblePanelManager( cp1,
                        cp2 );

                frame.setLayout( new GridBagLayout() );
                frame.add( ta, new GridBagConstraints( 0, 0, 1, 1, 0.7, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                frame.add( cpm, new GridBagConstraints( 1, 0, 1, 1, 0.3, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );

                frame.setSize( 500, 300 );
                frame.setTitle( "CollapsiblePanelManager demo" );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
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
