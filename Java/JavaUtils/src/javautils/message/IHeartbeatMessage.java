package javautils.message;

import java.util.Date;

/*******************************************************************************
 * Heartbeat message interface.
 ******************************************************************************/
public interface IHeartbeatMessage extends IMessage
{
    /***************************************************************************
     * Returns the time this message was sent.
     * 
     * @return
     **************************************************************************/
    public Date getSendTime();

    /***************************************************************************
     * Returns the availability status of the user who sent this heartbeat.
     * 
     * @return
     **************************************************************************/
    public boolean isUserAvailable();

    /***************************************************************************
     * Returns the display name of the user who sent this heartbeat.
     * 
     * @return
     **************************************************************************/
    public String getUserDisplayName();
}
