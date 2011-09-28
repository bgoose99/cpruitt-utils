package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javautils.message.IMessage;
import javautils.message.IMessageDisplay;
import javautils.swing.JAppendableTextPane;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/*******************************************************************************
 * This class represents the panel containing the current chat conversation.
 ******************************************************************************/
public class ChatPanel extends JPanel implements IMessageDisplay
{
    private DateFormat dateFormatter;
    private JAppendableTextPane textPane;
    private JScrollPane scrollPane;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public ChatPanel()
    {
        dateFormatter = new SimpleDateFormat( "(MM-dd-yy HH:mm:ss) " );

        textPane = new JAppendableTextPane();
        textPane.setEditable( false );
        scrollPane = new JScrollPane( textPane );

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.message.IMessageDisplay#addMessage(javautils.message.IMessage)
     */
    @Override
    public void addMessage( IMessage msg )
    {
        SimpleAttributeSet attr = new SimpleAttributeSet();

        Color fg = msg.getDisplayColor();

        StyleConstants.setFontFamily( attr, "Dialog" );
        StyleConstants.setFontSize( attr, 12 );
        StyleConstants.setForeground( attr, fg );
        textPane.append( dateFormatter.format( msg.getDate() ), attr );

        StyleConstants.setBold( attr, true );
        textPane.append( msg.getSender(), attr );

        textPane.append( ": " + msg.getMessage() );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.message.IMessageDisplay#clear()
     */
    @Override
    public void clear()
    {
        textPane.setText( "" );
    }

}
