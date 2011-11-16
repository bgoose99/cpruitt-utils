using System;

namespace ChatterBox
{
    interface IMessage : IMessageSerializer<IMessage>
    {
        IMessageHeader getMessageHeader();
        String getMessage();
        String getSender();
        DateTime getDateTime();
    }
}
