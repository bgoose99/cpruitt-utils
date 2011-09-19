package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javautils.message.MessageHandler;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import data.ChatUser;

public class ChatterBoxFrame extends JFrame
{
    private JToolBar toolbar;
    private ChatPanel chatPanel;
    private UserPanel userPanel;
    private MessagePanel messagePanel;
    private MessageHandler messageHandler;

    public ChatterBoxFrame()
    {
        chatPanel = new ChatPanel();
        userPanel = new UserPanel();

        try
        {
            messageHandler = new MessageHandler( "PC895.arc.army.mil", 6969,
                    chatPanel );
        } catch( Exception e )
        {
            JOptionPane.showMessageDialog( this, "Error",
                    "Error: " + e.getMessage(), JOptionPane.ERROR_MESSAGE );
        }

        messageHandler.start();
        messagePanel = new MessagePanel( messageHandler );

        userPanel.addUser( new ChatUser( "Big Long Name", true ) );
        userPanel.addUser( new ChatUser( "User 2", false ) );

        setupMenu();
        setupToolbar();
        setupFrame();
    }

    private void setupMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu( "File" );
        JMenuItem exitMenuItem = new JMenuItem( "Exit" );

        fileMenu.add( exitMenuItem );

        menuBar.add( fileMenu );

        setJMenuBar( menuBar );
    }

    private void setupToolbar()
    {
        toolbar = new JToolBar();
        toolbar.setFloatable( false );
        toolbar.setBorderPainted( false );
        toolbar.add( new JButton( "Dummy" ) );
    }

    private void setupFrame()
    {
        setTitle( "ChatterBox" );
        setSize( 800, 400 );
        setLayout( new GridBagLayout() );
        add( toolbar, new GridBagConstraints( 0, 0, 2, 1, 1.0, 0.01,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( chatPanel, new GridBagConstraints( 0, 1, 1, 1, 0.85, 0.80,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        add( userPanel, new GridBagConstraints( 1, 1, 1, 1, 0.15, 0.80,
                GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        add( messagePanel, new GridBagConstraints( 0, 2, 2, 1, 0.9, 0.20,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );

        addWindowListener( new ChatterBoxWindowAdapter() );
    }

    private void exit()
    {
        if( messageHandler != null )
        {
            messageHandler.stopThread();
        }
        System.out.println( "Exiting" );
        System.exit( 0 );
    }

    private class ChatterBoxWindowAdapter extends WindowAdapter
    {
        @Override
        public void windowClosed( WindowEvent e )
        {
            exit();
        }

        @Override
        public void windowClosing( WindowEvent e )
        {
            exit();
        }
    }
}
