package javautils.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javautils.IconManager;
import javautils.IconManager.IconSize;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/*******************************************************************************
 * This class represents a JPanel that can be collapsed or expanded by pushing a
 * button.
 ******************************************************************************/
public class CollapsiblePanel extends JPanel
{
    private static final String EXPAND = IconManager.BULLET_ADD;
    private static final String COLLAPSE = IconManager.BULLET_DELETE;
    private JPanel innerPanel;
    private JToggleButton button;

    /***************************************************************************
     * Constructor
     * 
     * @param innerPanel
     *            - the panel that is hidden/shown
     * @param desc
     *            - description of the contained panel
     * @param expanded
     *            - whether this panel should be expanded by default
     **************************************************************************/
    public CollapsiblePanel( JPanel innerPanel, String desc, boolean expanded )
    {
        super();

        setLayout( new GridBagLayout() );

        this.innerPanel = innerPanel;
        button = new JToggleButton( desc );
        button.setFocusable( false );
        button.setHorizontalAlignment( SwingConstants.LEFT );
        button.setSelected( expanded );
        button.addActionListener( new ToggleListener() );

        if( expanded )
            expand();
        else
            collapse();
    }

    /***************************************************************************
     * Collapse the inner panel.
     **************************************************************************/
    private void collapse()
    {
        setVisible( false );
        removeAll();
        button.setIcon( IconManager.getIcon( EXPAND, IconSize.X16 ) );
        add( button, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.01,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 1, 1, 1, 1 ), 0, 0 ) );
        setVisible( true );
    }

    /***************************************************************************
     * Expand the inner panel.
     **************************************************************************/
    private void expand()
    {
        setVisible( false );
        removeAll();
        button.setIcon( IconManager.getIcon( COLLAPSE, IconSize.X16 ) );
        add( button, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.01,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 1, 1, 1, 1 ), 0, 0 ) );
        add( innerPanel, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.99,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
                        1, 1, 1, 1 ), 0, 0 ) );
        setVisible( true );
    }

    /***************************************************************************
     * {@link ActionListener} for the toggle button.
     **************************************************************************/
    private class ToggleListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            Object src = e.getSource();
            if( src instanceof JToggleButton )
            {
                if( ( (JToggleButton)src ).isSelected() )
                {
                    expand();
                } else
                {
                    collapse();
                }
            }
        }
    }

    /***************************************************************************
     * CollapsiblePanel demo.
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
                panel.add( new JButton( "A button" ) );
                panel.add( new JButton( "Another button" ) );
                panel.add( new JTextField( "A text field" ) );

                CollapsiblePanel cp = new CollapsiblePanel( panel,
                        "Demo panel", true );
                frame.add( cp );

                frame.setTitle( "CollapsiblePanel demo" );
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
