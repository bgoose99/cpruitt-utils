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

        /// <summary>
        /// Returns the message contents of this message.
        /// </summary>
        /// <returns></returns>
        String getMessage();

        /// <summary>
        /// Returns the display name of the user who sent this message.
        /// </summary>
        /// <returns></returns>
        String getSender();

        /// <summary>
        /// Returns the time this message was sent.
        /// </summary>
        /// <returns></returns>
        DateTime getDateTime();
    }
}
