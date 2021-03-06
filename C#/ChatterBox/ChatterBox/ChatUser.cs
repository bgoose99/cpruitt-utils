﻿using System.Security.Principal;
using System.Drawing;

namespace ChatterBox
{
    /// <summary>
    /// This class represents a chat user.
    /// </summary>
    class ChatUser : IUser
    {
        private string name;
        private string displayName;
        private Color preferredColor;
        private bool available;

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="displayName"></param>
        public ChatUser( string displayName ) : this( displayName, Color.Black )
        {
        }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="displayName"></param>
        /// <param name="preferredColor"></param>
        public ChatUser( string displayName, Color preferredColor )
        {
            this.name = WindowsIdentity.GetCurrent().Name; ;
            this.displayName = displayName;
            this.preferredColor = preferredColor;
            available = true;
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

        /// <summary cref="IUser.isAvailable">
        /// <see cref="IUser.isAvailable"/>
        /// </summary>
        /// <returns></returns>
        public bool isAvailable()
        {
            return available;
        }

        /// <summary cref="IUser.setAvailable">
        /// <see cref="IUser.setAvailable"/>
        /// </summary>
        /// <param name="available"></param>
        public void setAvailable( bool available )
        {
            this.available = available;
        }

        /// <summary>
        /// Returns this user's preferred color.
        /// </summary>
        /// <returns></returns>
        public Color getPreferredColor()
        {
            return preferredColor;
        }

        /// <summary>
        /// Sets this user's preferred color.
        /// </summary>
        /// <param name="color"></param>
        public void setPreferredColor( Color color )
        {
            preferredColor = color;
        }
    }
}
