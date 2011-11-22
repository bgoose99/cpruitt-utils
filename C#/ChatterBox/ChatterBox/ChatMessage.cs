using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Drawing;

namespace ChatterBox
{
    /// <summary>
    /// This class represents a single chat message.
    /// </summary>
    class ChatMessage : IMessage, IChatMessage
    {
        private IMessageHeader header;

        private string displayName;
        private Color displayColor;
        private DateTime sendTime;
        private string message;
        private int messageLength;

        /// <summary>
        /// Default constructor
        /// </summary>
        public ChatMessage() : this( "", "", "" )
        {
        }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="displayName"></param>
        /// <param name="message"></param>
        public ChatMessage( string sender, string displayName, string message ) : this( sender, displayName, Color.Black, message )
        {
        }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="displayName"></param>
        /// <param name="displayColor"></param>
        /// <param name="message"></param>
        public ChatMessage( string sender, string displayName, Color displayColor, string message )
        {
            this.displayName = displayName;
            this.displayColor = displayColor;
            this.sendTime = DateTime.Now;
            this.message = message;

            calculateMessageLength();

            if( messageLength > MessageUtils.MAX_MESSAGE_SIZE )
            {
                throw new Exception( "Message is too long. (" + 
                                     messageLength + " bytes, max is " + 
                                     MessageUtils.MAX_MESSAGE_SIZE + " bytes)" );
            }

            header = new MessageHeader( sender.GetHashCode(), MessageUtils.MessageType.CHAT, messageLength );
        }

        /// <summary>
        /// Constructor used for receiving messages. This one is necessary
        /// because we don't know the message contents yet, as they haven't
        /// been parsed from the raw binary message.
        /// </summary>
        /// <param name="header"></param>
        public ChatMessage( IMessageHeader header ) : this()
        {
            this.header = header;
        }

        /// <summary>
        /// Calculates the message length.
        /// </summary>
        private void calculateMessageLength()
        {
            messageLength = MessageHeader.SIZE;      // header
            messageLength += 1 + displayName.Length; // display name
            messageLength += sizeof( int );          // color
            messageLength += sizeof( long );         // send time
            messageLength += 1 + message.Length;     // message
        }

        /// <summary cref="IMessage.getMessageHeader">
        /// <see cref="IMessage.getMessageHeader"/>
        /// </summary>
        public IMessageHeader getMessageHeader()
        {
            return header;
        }

        /// <summary cref="IChatMessage.getMessage">
        /// <see cref="IChatMessage.getMessage"/>
        /// </summary>
        /// <returns></returns>
        public string getMessage()
        {
            return message;
        }

        /// <summary cref="IChatMessage.getUserDisplayName">
        /// <see cref="IChatMessage.getUserDisplayName"/>
        /// </summary>
        /// <returns></returns>
        public string getUserDisplayName()
        {
            return displayName;
        }

        /// <summary cref="IChatMessage.getSendTime">
        /// <see cref="IChatMessage.getSendTime"/>
        /// </summary>
        /// <returns></returns>
        public DateTime getSendTime()
        {
            return sendTime;
        }

        /// <summary cref="IChatMessage.getDisplayColor">
        /// <see cref="IChatMessage.getDisplayColor"/>
        /// </summary>
        /// <returns></returns>
        public Color getDisplayColor()
        {
            return displayColor;
        }

        /// <summary cref="IMessageSerializer.toBinaryStream">
        /// <see cref="IMessageSerializer.toBinaryStream"/>
        /// </summary>
        /// <param name="writer"></param>
        public void toBinaryStream( BinaryWriter writer )
        {
            writer.Write( displayName );
            writer.Write( displayColor.ToArgb() );
            writer.Write( sendTime.ToBinary() );
            writer.Write( message );
        }

        /// <summary cref="IMessageSerializer.fromBinaryStream">
        /// <see cref="IMessageSerializer.fromBinaryStream"/>
        /// </summary>
        /// <param name="reader"></param>
        /// <returns></returns>
        public IMessage fromBinaryStream( BinaryReader reader )
        {
            displayName = reader.ReadString();
            displayColor = Color.FromArgb( reader.ReadInt32() );
            sendTime = DateTime.FromBinary( reader.ReadInt64() );
            message = reader.ReadString();
            
            calculateMessageLength();
            header.setMessageLength( messageLength );
            return this;
        }
    }
}
