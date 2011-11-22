using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;

namespace ChatterBox
{
    interface IChatMessage
    {
        /// <summary>
        /// Returns the message contents of this message.
        /// </summary>
        /// <returns></returns>
        string getMessage();

        /// <summary>
        /// Returns the display name of the user who sent this message.
        /// </summary>
        /// <returns></returns>
        string getUserDisplayName();

        /// <summary>
        /// Returns the time this message was sent.
        /// </summary>
        /// <returns></returns>
        DateTime getSendTime();

        /// <summary>
        /// Returns this user's preferred display color.
        /// </summary>
        /// <returns></returns>
        Color getDisplayColor();
    }
}
