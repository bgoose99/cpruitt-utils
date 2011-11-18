using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChatterBox
{
    interface IHeartbeatMessage
    {
        /// <summary>
        /// Returns the time this message was sent.
        /// </summary>
        /// <returns></returns>
        DateTime getSendTime();

        /// <summary>
        /// Returns the availability status of the user who sent this message.
        /// </summary>
        /// <returns></returns>
        bool isUserAvailable();

        /// <summary>
        /// Returns the display name of this user who sent this message.
        /// </summary>
        /// <returns></returns>
        string getUserDisplayName();
    }
}
