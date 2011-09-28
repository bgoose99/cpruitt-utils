package javautils.swing;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;

/*******************************************************************************
 * A {@link JTextPane} that allows the user to append text. Something you should
 * probably be able to do anyway.
 ******************************************************************************/
public class JAppendableTextPane extends JTextPane
{
    /***************************************************************************
     * Constructor
     **************************************************************************/
    public JAppendableTextPane()
    {
        super();
    }

    /***************************************************************************
     * Appends the supplied text to the text pane.
     * 
     * @param text
     **************************************************************************/
    public void append( String text )
    {
        append( text, null );
    }

    /***************************************************************************
     * Appends the supplied text to the text pane, using the supplied
     * attributes.
     * 
     * @param text
     * @param attr
     **************************************************************************/
    public void append( String text, AttributeSet attr )
    {
        Document doc = getDocument();
        try
        {
            doc.insertString( doc.getLength(), text, attr );
        } catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
