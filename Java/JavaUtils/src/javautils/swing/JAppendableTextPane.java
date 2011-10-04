package javautils.swing;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;

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
        insert( text, getDocument().getLength(), attr );
    }

    /***************************************************************************
     * Inserts the supplied text at the supplied location.
     * 
     * @param text
     * @param location
     **************************************************************************/
    public void insert( String text, int location )
    {
        insert( text, location, null );
    }

    /***************************************************************************
     * Inserts the supplied text at the supplied location, using the supplied
     * attributes.
     * 
     * @param text
     * @param location
     * @param attr
     **************************************************************************/
    public void insert( String text, int location, AttributeSet attr )
    {
        try
        {
            getDocument().insertString( location, text, attr );
        } catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    /***************************************************************************
     * Replaces the text from startOffset to (startOffset + length) with the
     * supplied text.
     * 
     * @param text
     * @param startOffset
     * @param length
     **************************************************************************/
    public void replace( String text, int startOffset, int length )
    {
        replace( text, startOffset, length, null );
    }

    /***************************************************************************
     * Replaces the text from startOffset to (startOffset + length) with the
     * supplied text, using the supplied attributes.
     * 
     * @param text
     * @param location
     * @param length
     * @param attr
     **************************************************************************/
    public void replace( String text, int location, int length,
            AttributeSet attr )
    {
        try
        {
            getDocument().remove( location, length );
            insert( text, location, attr );
        } catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
