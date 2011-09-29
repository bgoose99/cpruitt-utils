package javautils.message;

/*******************************************************************************
 * Heartbeat listener interface.
 ******************************************************************************/
public interface IHeartbeatListener
{
    /***************************************************************************
     * Receives a heartbeat message.
     * 
     * @param msg
     **************************************************************************/
    public void receiveHeartbeat( IHeartbeatMessage msg );
}
