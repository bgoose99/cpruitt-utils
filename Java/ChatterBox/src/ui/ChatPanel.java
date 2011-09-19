package ui;

import java.awt.BorderLayout;

import javautils.message.IMessage;
import javautils.message.IMessageDisplay;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ChatPanel extends JPanel implements IMessageDisplay
{
    private JEditorPane editorPane;
    private JScrollPane scrollPane;

    public ChatPanel()
    {
        editorPane = new JEditorPane();
        editorPane.setEditable( false );
        scrollPane = new JScrollPane( editorPane );

        setupPanel();
    }

    private void setupPanel()
    {
        setLayout( new BorderLayout() );
        add( scrollPane, BorderLayout.CENTER );
    }

    @Override
    public void addMessage( IMessage msg )
    {
        String s = editorPane.getText();
        s += msg.getSender() + " : " + msg.getMessage();
        editorPane.setText( s );
    }

    @Override
    public void clear()
    {
        editorPane.setText( "" );
    }

}
