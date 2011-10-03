package javautils.message;

/*******************************************************************************
 * Message interface.
 ******************************************************************************/
public interface IMessage extends IMessageSerializer<IMessage>
{
    /***************************************************************************
     * Returns the message header.
     * 
     * @return
     **************************************************************************/
    public IMessageHeader getMessageHeader();
}
