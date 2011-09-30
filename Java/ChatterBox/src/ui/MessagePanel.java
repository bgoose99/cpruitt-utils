package ui;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javautils.message.DefaultChatMessage;
import javautils.message.IMessageHandler;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import data.IUser;
import data.IUserActivityMonitor;

/*******************************************************************************
 * This panel provides a place for the user to type a message.
 **************************************************************************/
public class MessagePanel extends JPanel
{
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private IMessageHandler messageHandler;
    private IUser localUser;
    private IUserActivityMonitor activityMonitor = null;

    /***************************************************************************
     * Constructor
     * 
     * @param localUser
     **************************************************************************/
    public MessagePanel( IUser localUser )
    {
        this.localUser = localUser;
        textArea = new JTextArea();
        textArea.addKeyListener( new MessagePanelKeyAdapter() );
        scrollPane = new JScrollPane( textArea );
        setupPanel();
    }

    /***************************************************************************
     * Sets up this panel.
     **************************************************************************/
    private void setupPanel()
    {
        setLayout( new BorderLayout() );
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
                        localUser.getDisplayColor(), s );
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
