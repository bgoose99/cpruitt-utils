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

public class MessagePanel extends JPanel
{
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private IMessageHandler messageHandler;
    private IUser localUser;

    public MessagePanel( IUser localUser )
    {
        this.localUser = localUser;
        textArea = new JTextArea();
        textArea.addKeyListener( new MessagePanelKeyAdapter() );
        scrollPane = new JScrollPane( textArea );
        setupPanel();
    }

    private void setupPanel()
    {
        setLayout( new BorderLayout() );
        add( scrollPane, BorderLayout.CENTER );
    }

    public String getMessage()
    {
        return textArea.getText();
    }

    public void clearMessage()
    {
        textArea.setText( "" );
    }

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

    public void setMessageHandler( IMessageHandler handler )
    {
        this.messageHandler = handler;
    }

    private class MessagePanelKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyReleased( KeyEvent e )
        {
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
