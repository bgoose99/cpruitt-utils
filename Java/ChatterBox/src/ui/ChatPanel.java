package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javautils.message.IChatMessage;
import javautils.message.IMessageDisplay;
import javautils.message.MessageFormatOption;
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

    /***************************************************************************
     * Adds a formatted message to the chat pane.
     * 
     * @param msg
     **************************************************************************/
    private void addFormattedMessage( IChatMessage msg )
    {
        List<MessageFormatOption> options = msg.getFormattingOptions();

        // if there is no formatting, append regular text and return
        if( options == null )
        {
            textPane.append( msg.getMessage() );
            return;
        }

        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setFontFamily( attr, "Dialog" );
        StyleConstants.setFontSize( attr, 12 );

        int index = 0;
        for( MessageFormatOption o : options )
        {
            if( index < o.getStartIndex() )
            {
                // append, no formatting
                textPane.append( msg.getMessage().substring( index,
                        o.getStartIndex() ) );
                index = o.getStartIndex();
            }

            StyleConstants.setBold( attr, false );
            StyleConstants.setItalic( attr, false );
            StyleConstants.setUnderline( attr, false );

            // append, with formatting
            switch( o.getType() )
            {
            case BOLD:
                StyleConstants.setBold( attr, true );
                break;
            case ITALIC:
                StyleConstants.setItalic( attr, true );
                break;
            case UNDERLINE:
                StyleConstants.setUnderline( attr, true );
                break;
            case BOLD_AND_ITALIC:
                StyleConstants.setBold( attr, true );
                StyleConstants.setItalic( attr, true );
                break;
            case BOLD_AND_UNDERLINE:
                StyleConstants.setBold( attr, true );
                StyleConstants.setUnderline( attr, true );
                break;
            case ITALIC_AND_UNDERLINE:
                StyleConstants.setItalic( attr, true );
                StyleConstants.setUnderline( attr, true );
                break;
            case BOLD_ITALIC_AND_UNDERLINE:
                StyleConstants.setBold( attr, true );
                StyleConstants.setItalic( attr, true );
                StyleConstants.setUnderline( attr, true );
                break;
            }

            textPane.append(
                    msg.getMessage().substring( index, index + o.getLength() ),
                    attr );
            index += o.getLength();
        }

        // print the rest of the string, if necessary
        if( index < msg.getMessage().length() )
            textPane.append( msg.getMessage().substring( index ) );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.message.IMessageDisplay#addMessage(javautils.message.IMessage)
     */
    @Override
    public void addMessage( IChatMessage msg )
    {
        SimpleAttributeSet attr = new SimpleAttributeSet();

        Color fg = msg.getDisplayColor();

        StyleConstants.setFontFamily( attr, "Dialog" );
        StyleConstants.setFontSize( attr, 12 );
        StyleConstants.setForeground( attr, fg );
        textPane.append( dateFormatter.format( msg.getDate() ), attr );

        StyleConstants.setBold( attr, true );
        textPane.append( msg.getSender(), attr );
        textPane.append( ": " );

        addFormattedMessage( msg );
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
