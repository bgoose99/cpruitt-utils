package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import presenter.IViewUpdater;

/*******************************************************************************
 * Main UI.
 ******************************************************************************/
public class MainFrame extends JFrame
{
    private StreamViewPanel view;

    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem openMenuItem;
    private JMenuItem hlMenuItem;

    private HighlightDialog hlDialog;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public MainFrame()
    {
        setupFrame();
    }

    /***************************************************************************
     * Sets up this frame.
     **************************************************************************/
    private void setupFrame()
    {
        setTitle( "StreamView" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        try
        {
            view = new StreamViewPanel();
        } catch( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        menuBar = new JMenuBar();
        menu = new JMenu( "File" );
        openMenuItem = new JMenuItem( "Open" );
        openMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O,
                java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        openMenuItem.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                openFile();
            }
        } );

        hlMenuItem = new JMenuItem( "Highlight" );
        hlMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_H,
                java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        hlMenuItem.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                highlightBits();
            }
        } );

        menu.add( openMenuItem );
        menu.add( hlMenuItem );
        menuBar.add( menu );
        setJMenuBar( menuBar );

        hlDialog = new HighlightDialog( new IViewUpdater()
        {
            @Override
            public void updateView()
            {
                view.highlightSelection( hlDialog.getStartBit(),
                        hlDialog.getBitLength() );
            }
        } );

        setLayout( new GridBagLayout() );
        add( view, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
    }

    /***************************************************************************
     * Presents an open dialog for selecting a file.
     **************************************************************************/
    private void openFile()
    {
        JFileChooser chooser = new JFileChooser( "." );
        if( chooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION )
            view.openFile( chooser.getSelectedFile() );
    }

    /***************************************************************************
     * Presents the highlight dialog.
     **************************************************************************/
    private void highlightBits()
    {
        hlDialog.setVisible( true );
    }
}
