using System;

namespace ChatterBox
{
    interface IMessage : IMessageSerializer<IMessage>
    {
        /// <summary>
        /// Returns the message header for this message.
        /// </summary>
        /// <returns></returns>
        IMessageHeader getMessageHeader();
    }
}
