﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChatterBox
{
    interface IUser
    {
        /// <summary>
        /// Returns the machine name of this user.
        /// </summary>
        /// <returns></returns>
        string getName();

        /// <summary>
        /// Returns the displya name of this user.
        /// </summary>
        /// <returns></returns>
        string getDisplayName();

        /// <summary>
        /// Sets the display name of this user.
        /// </summary>
        /// <param name="displayName"></param>
        void setDisplayName( string displayName );

        /// <summary>
        /// Returns true if this user is available, false otherwise.
        /// </summary>
        /// <returns></returns>
        bool isAvailable();

        /// <summary>
        /// Sets the availability of this user.
        /// </summary>
        /// <param name="available"></param>
        void setAvailable( bool available );
    }
}
