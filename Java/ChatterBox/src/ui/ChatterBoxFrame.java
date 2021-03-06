package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;

import javautils.IconManager;
import javautils.IconManager.IconSize;
import javautils.io.Preferences;
import javautils.message.IHeartbeatListener;
import javautils.message.MessageHandler;
import javautils.task.ICompletable;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import data.ChatUser;
import data.HeartbeatListener;
import data.HeartbeatTask;
import data.IUser;
import data.UserActivityMonitor;

/*******************************************************************************
 * This class contains the main UI elements of the ChatterBox application.
 ******************************************************************************/
public class ChatterBoxFrame extends JFrame
{
    private JToolBar toolbar;
    private JButton connectButton;
    private JButton disconnectButton;
    private JButton availableButton;
    private JButton trashButton;
    private JButton colorButton;
    private ChatPanel chatPanel;
    private UserPanel userPanel;
    private JSplitPane splitPane;
    private MessagePanel messagePanel;
    private MessageHandler messageHandler = null;
    private IHeartbeatListener heartbeatListener;
    private HeartbeatTask heartbeatTask;
    private IUser user;
    private UserActivityMonitor activityMonitor;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public ChatterBoxFrame()
    {
        chatPanel = new ChatPanel();
        userPanel = new UserPanel();
        Preferences.initialize( System.getProperty( "user.home" )
                + "/.ChatterBoxPrefs", "user", "autoconnect", "host", "port",
                "color" );
        if( !Preferences.exists() )
        {
            showPreferenceDialog();
        }
        Preferences.readPreferences();
        user = new ChatUser( Preferences.getPreference( "user" ),
                Preferences.getPreference( "color" ), true );

        activityMonitor = new UserActivityMonitor( user, this );

        heartbeatListener = new HeartbeatListener( userPanel, user );
        heartbeatTask = new HeartbeatTask( user );

        messagePanel = new MessagePanel( user );
        messagePanel.setActivityMonitor( activityMonitor );

        splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, chatPanel,
                userPanel );

        setupMenu();
        setupToolbar();
        setupFrame();

        try
        {
            boolean autoConnect = Boolean.parseBoolean( Preferences
                    .getPreference( "autoconnect" ) );
            if( autoConnect )
            {
                connect();
            } else
            {
                disconnect();
            }
        } catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    /***************************************************************************
     * Sets up the menu for this frame.
     **************************************************************************/
    private void setupMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu( "File" );
        JMenuItem exitMenuItem = new JMenuItem( "Exit", IconManager.getIcon(
                IconManager.DOOR_IN, IconSize.X16 ) );
        exitMenuItem.setMnemonic( 'x' );
        exitMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Q,
                java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        exitMenuItem.addActionListener( new ExitActionListener() );

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
        connectButton.setToolTipText( "Connect to the address specified"
                + " by your preferences" );

        disconnectButton = new JButton( IconManager.getIcon(
                IconManager.DISCONNECT, IconSize.X16 ) );
        disconnectButton.setFocusable( false );
        disconnectButton.addActionListener( new DisconnectListener() );
        disconnectButton.setToolTipText( "Disconnect from the current address" );

        availableButton = new JButton( IconManager.getIcon(
                IconManager.USER_SILHOUETTE, IconSize.X16 ) );
        availableButton.setFocusable( false );
        availableButton.addActionListener( new AvailabilityListener() );
        availableButton.setToolTipText( "Go unavailable" );

        trashButton = new JButton( IconManager.getIcon( IconManager.MAIL_TRASH,
                IconSize.X16 ) );
        trashButton.setFocusable( false );
        trashButton.addActionListener( new TrashListener() );
        trashButton.setToolTipText( "Clear the current conversation" );

        colorButton = new JButton( IconManager.getIcon(
                IconManager.COLOR_WHEEL, IconSize.X16 ) );
        colorButton.setFocusable( false );
        colorButton.addActionListener( new ColorListener() );
        colorButton.setToolTipText( "Choose a display color for your messages" );

        toolbar = new JToolBar();
        toolbar.setFloatable( false );
        toolbar.setBorderPainted( false );
        toolbar.add( connectButton );
        toolbar.add( disconnectButton );
        toolbar.add( availableButton );
        toolbar.addSeparator();
        toolbar.add( trashButton );
        toolbar.addSeparator();
        toolbar.add( colorButton );
    }

    /***************************************************************************
     * Sets up this frame.
     **************************************************************************/
    private void setupFrame()
    {
        setTitle( "ChatterBox" );
        setSize( 800, 400 );
        setLayout( new GridBagLayout() );
        add( toolbar, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.01,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( splitPane, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.80,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        add( messagePanel, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.20,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );

        chatPanel.setMinimumSize( new Dimension( 600, 100 ) );

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

        try
        {
            port = Integer.parseInt( Preferences.getPreference( "port" ) );

            messageHandler = new MessageHandler( host, port, chatPanel,
                    heartbeatListener );
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
        heartbeatTask.setMessageHandler( messageHandler );
    }

    /***************************************************************************
     * Connects the message handler on the specified address.
     **************************************************************************/
    private void connect()
    {
        setupMessageHandler();
        if( messageHandler != null )
            messageHandler.start();
        connectButton.setEnabled( false );
        disconnectButton.setEnabled( true );
        heartbeatTask.startTask();
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
        heartbeatTask.stopTask();
    }

    /***************************************************************************
     * Switches the user's availability.
     **************************************************************************/
    private void switchAvailability()
    {
        user.setAvailable( !user.isAvailable() );
        if( user.isAvailable() )
        {
            availableButton.setIcon( IconManager.getIcon(
                    IconManager.USER_SILHOUETTE, IconSize.X16 ) );
            availableButton.setToolTipText( "Go unavailable" );
        } else
        {
            availableButton.setIcon( IconManager.getIcon( IconManager.USER,
                    IconSize.X16 ) );
            availableButton.setToolTipText( "Go available" );
        }
        userPanel.updateDisplay();
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
     * Presents the user with a dialog allowing them to select a custom color.
     **************************************************************************/
    private void chooseDisplayColor()
    {
        Color c = JColorChooser.showDialog( this, "Choose custom color",
                user.getDisplayColor() );
        if( c != null )
        {
            user.setDisplayColor( c );
            Preferences.setPreference( "color", new String( "" + c.getRGB() ) );
        }
    }

    /***************************************************************************
     * Exits this application.
     **************************************************************************/
    private void exit()
    {
        disconnect();
        Preferences.writePreferences();
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
            user.setDisplayName( Preferences.getPreference( "user" ) );
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

    /***************************************************************************
     * 
     **************************************************************************/
    private class AvailabilityListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            switchAvailability();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class TrashListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            chatPanel.clear();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ColorListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            chooseDisplayColor();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ExitActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            exit();
        }
    }
}
