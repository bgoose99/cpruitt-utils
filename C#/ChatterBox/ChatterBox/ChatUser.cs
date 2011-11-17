﻿using System.Security.Principal;

namespace ChatterBox
{
    /// <summary>
    /// This class represents a chat user.
    /// </summary>
    class ChatUser : IUser
    {
        private string name;
        private string displayName;

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="displayName"></param>
        public ChatUser( string displayName )
        {
            this.name = WindowsIdentity.GetCurrent().Name; ;
            this.displayName = displayName;
        }

        /// <summary cref="IUser.getName">
        /// <see cref="IUser.getName"/>
        /// </summary>
        /// <returns></returns>
        public string getName()
        {
            return name;
        }

        /// <summary cref="IUser.getDisplayName">
        /// <see cref="IUser.getDisplayName"/>
        /// </summary>
        /// <returns></returns>
        public string getDisplayName()
        {
            return displayName;
        }

        /// <summary cref="IUser.setDisplayName">
        /// <see cref="IUser.setDisplayName"/>
        /// </summary>
        /// <param name="displayName"></param>
        public void setDisplayName( string displayName )
        {
            this.displayName = displayName;
        }
    }
}