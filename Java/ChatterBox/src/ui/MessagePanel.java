package ui;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javautils.IconManager;
import javautils.IconManager.IconSize;
import javautils.message.DefaultChatMessage;
import javautils.message.IMessageHandler;
import javautils.message.MessageFormatOption;
import javautils.swing.JAppendableTextPane;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import data.IUser;
import data.IUserActivityMonitor;

/*******************************************************************************
 * This panel provides a place for the user to type a message.
 ******************************************************************************/
public class MessagePanel extends JPanel
{
    private JToolBar toolbar;
    private JToggleButton boldButton;
    private JToggleButton italicButton;
    private JToggleButton underlineButton;
    private JButton lowercaseButton;
    private JButton uppercaseButton;
    private JAppendableTextPane textArea;
    private JScrollPane scrollPane;
    private IMessageHandler messageHandler;
    private IUser localUser;
    private IUserActivityMonitor activityMonitor = null;
    private List<MessageFormatOption> currentOptions;

    /***************************************************************************
     * Constructor
     * 
     * @param localUser
     **************************************************************************/
    public MessagePanel( IUser localUser )
    {
        this.localUser = localUser;
        currentOptions = new ArrayList<MessageFormatOption>();
        textArea = new JAppendableTextPane();
        textArea.addKeyListener( new MessagePanelKeyAdapter() );
        scrollPane = new JScrollPane( textArea );
        setupToolbar();
        setupPanel();
    }

    /***************************************************************************
     * Sets up the toolbar for this panel.
     **************************************************************************/
    private void setupToolbar()
    {
        boldButton = new JToggleButton( IconManager.getIcon(
                IconManager.TEXT_BOLD, IconSize.X16 ) );
        boldButton.setFocusable( false );

        italicButton = new JToggleButton( IconManager.getIcon(
                IconManager.TEXT_ITALIC, IconSize.X16 ) );
        italicButton.setFocusable( false );

        underlineButton = new JToggleButton( IconManager.getIcon(
                IconManager.TEXT_UNDERLINE, IconSize.X16 ) );
        underlineButton.setFocusable( false );

        lowercaseButton = new JButton( IconManager.getIcon(
                IconManager.TEXT_LOWERCASE, IconSize.X16 ) );
        lowercaseButton.setFocusable( false );

        uppercaseButton = new JButton( IconManager.getIcon(
                IconManager.TEXT_UPPERCASE, IconSize.X16 ) );
        uppercaseButton.setFocusable( false );

        toolbar = new JToolBar();
        toolbar.setFloatable( false );
        toolbar.setBorderPainted( false );
        toolbar.add( boldButton );
        toolbar.add( italicButton );
        toolbar.add( underlineButton );
        toolbar.addSeparator();
        toolbar.add( lowercaseButton );
        toolbar.add( uppercaseButton );
    }

    /***************************************************************************
     * Sets up this panel.
     **************************************************************************/
    private void setupPanel()
    {
        setLayout( new BorderLayout() );
        add( toolbar, BorderLayout.PAGE_START );
        add( scrollPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * Sets the activity monitor for this panel.
     * 
     * @param monitor
     **************************************************************************/
    public void setActivityMonitor( IUserActivityMonitor monitor )
    {
        this.activityMonitor = monitor;
    }

    /***************************************************************************
     * Returns the user's message.
     * 
     * @return
     **************************************************************************/
    public String getMessage()
    {
        return textArea.getText();
    }

    /***************************************************************************
     * Clears the message field.
     **************************************************************************/
    public void clearMessage()
    {
        textArea.setText( "" );
    }

    /***************************************************************************
     * Sends the user's message.
     **************************************************************************/
    private void sendMessage()
    {
        String s = getMessage();
        clearMessage();
        if( messageHandler != null )
        {
            try
            {
                DefaultChatMessage msg = new DefaultChatMessage(
                        localUser.getName(), localUser.getDisplayName(),
                        localUser.getDisplayColor(), s, currentOptions );
                messageHandler.sendMessage( msg );
            } catch( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

    /***************************************************************************
     * Sets the message handler associated with this panel.
     * 
     * @param handler
     **************************************************************************/
    public void setMessageHandler( IMessageHandler handler )
    {
        this.messageHandler = handler;
    }

    /***************************************************************************
     * Custom {@link KeyAdapter} used to alter the behavior of the Enter key.
     **************************************************************************/
    private class MessagePanelKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyReleased( KeyEvent e )
        {
            if( activityMonitor != null )
                activityMonitor.updateActivity();

            if( e.getKeyCode() == KeyEvent.VK_ENTER )
            {
                if( e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK )
                {
                    textArea.append( "\n" );
                } else
                {
                    sendMessage();
                }
            }
        }
    }
}
