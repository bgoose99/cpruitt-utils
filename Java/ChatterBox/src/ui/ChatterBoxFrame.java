package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;

import javautils.IconManager;
import javautils.IconManager.IconSize;
import javautils.io.Preferences;
import javautils.message.MessageHandler;
import javautils.task.ICompletable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import data.ChatUser;

/*******************************************************************************
 * This class contains the main UI elements of the ChatterBox application.
 ******************************************************************************/
public class ChatterBoxFrame extends JFrame
{
    private JToolBar toolbar;
    private JButton connectButton;
    private JButton disconnectButton;
    private ChatPanel chatPanel;
    private UserPanel userPanel;
    private MessagePanel messagePanel;
    private MessageHandler messageHandler = null;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public ChatterBoxFrame()
    {
        chatPanel = new ChatPanel();
        userPanel = new UserPanel();

        Preferences.initialize( System.getProperty( "user.home" )
                + "/.ChatterBoxPrefs", "user", "autoconnect", "host", "port" );
        if( !Preferences.exists() )
        {
            showPreferenceDialog();
        }
        Preferences.readPreferences();

        messagePanel = new MessagePanel();

        userPanel.addUser( new ChatUser( "Big Long Name", true ) );
        userPanel.addUser( new ChatUser( "User 2", false ) );

        setupMenu();
        setupToolbar();
        setupFrame();
        setupMessageHandler();
    }

    /***************************************************************************
     * Sets up the menu for this frame.
     **************************************************************************/
    private void setupMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu( "File" );
        JMenuItem exitMenuItem = new JMenuItem( "Exit" );

        JMenu editMenu = new JMenu( "Edit" );
        JMenuItem preferencesMenuItem = new JMenuItem( "Preferences" );
        preferencesMenuItem.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                showPreferenceDialog();
            }
        } );

        fileMenu.add( exitMenuItem );
        editMenu.add( preferencesMenuItem );

        menuBar.add( fileMenu );
        menuBar.add( editMenu );

        setJMenuBar( menuBar );
    }

    /***************************************************************************
     * Sets up the toolbar for this frame.
     **************************************************************************/
    private void setupToolbar()
    {
        connectButton = new JButton( IconManager.getIcon( IconManager.CONNECT,
                IconSize.X16 ) );
        connectButton.setFocusable( false );
        connectButton.addActionListener( new ConnectListener() );
        disconnectButton = new JButton( IconManager.getIcon(
                IconManager.DISCONNECT, IconSize.X16 ) );
        disconnectButton.setFocusable( false );
        disconnectButton.addActionListener( new DisconnectListener() );

        toolbar = new JToolBar();
        toolbar.setFloatable( false );
        toolbar.setBorderPainted( false );
        toolbar.add( connectButton );
        toolbar.add( disconnectButton );
    }

    /***************************************************************************
     * Sets up this frame.
     **************************************************************************/
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

    /***************************************************************************
     * Sets up the message handler based on user preferences.
     **************************************************************************/
    private void setupMessageHandler()
    {
        disconnect();

        String host = Preferences.getPreference( "host" );
        int port;
        boolean autoConnect;

        try
        {
            port = Integer.parseInt( Preferences.getPreference( "port" ) );
            autoConnect = Boolean.parseBoolean( Preferences
                    .getPreference( "autoconnect" ) );
            messageHandler = new MessageHandler( host, port, chatPanel );
            if( autoConnect )
                connect();
        } catch( UnknownHostException e )
        {
            JOptionPane.showMessageDialog( this, "Unknown host: " + host,
                    "Error", JOptionPane.ERROR_MESSAGE );
            messageHandler = null;
            return;
        } catch( Exception e )
        {
            JOptionPane.showMessageDialog( this, "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE );
            messageHandler = null;
            return;
        }

        messagePanel.setMessageHandler( messageHandler );
    }

    /***************************************************************************
     * Connects the message handler on the specified address.
     **************************************************************************/
    private void connect()
    {
        if( messageHandler != null )
            messageHandler.start();
        connectButton.setEnabled( false );
        disconnectButton.setEnabled( true );
    }

    /***************************************************************************
     * Disconnects the message handler from its specified address.
     **************************************************************************/
    private void disconnect()
    {
        if( messageHandler != null )
            messageHandler.stopThread();
        connectButton.setEnabled( true );
        disconnectButton.setEnabled( false );
    }

    /***************************************************************************
     * Presents the user with a dialog they can use to change their preferences.
     **************************************************************************/
    private void showPreferenceDialog()
    {
        PreferenceDialog dialog = new PreferenceDialog( new Completable() );
        dialog.setVisible( true );
    }

    /***************************************************************************
     * Exits this application.
     **************************************************************************/
    private void exit()
    {
        disconnect();
        System.out.println( "Exiting" );
        System.exit( 0 );
    }

    /***************************************************************************
     * Custom window adapter so we don't exit without cleaning up.
     **************************************************************************/
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

    /***************************************************************************
     * 
     **************************************************************************/
    private class Completable implements ICompletable
    {
        @Override
        public void notifyComplete()
        {
            disconnect();
            setupMessageHandler();
        }

        @Override
        public void notifyCancelled()
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ConnectListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            connect();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class DisconnectListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            disconnect();
        }
    }
}
