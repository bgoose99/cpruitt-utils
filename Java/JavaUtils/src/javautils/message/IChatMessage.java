package javautils.message;

import java.awt.Color;
import java.util.Date;

/*******************************************************************************
 * Chat message interface.
 ******************************************************************************/
public interface IChatMessage extends IMessage
{
    /***************************************************************************
     * Returns a string representation of this message.
     * 
     * @return
     **************************************************************************/
    public String getMessage();

    /***************************************************************************
     * Returns the sender of this message.
     * 
     * @return
     **************************************************************************/
    public String getSender();

    /***************************************************************************
     * Returns the time this message was created.
     * 
     * @return
     **************************************************************************/
    public Date getDate();

    /***************************************************************************
     * Returns the color associated with this message.
     * 
     * @return
     **************************************************************************/
    public Color getDisplayColor();

}
