using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace ChatterBox
{
    /// <summary>
    /// This class represents a single heartbeat message.
    /// </summary>
    class HeartbeatMessage : IMessage, IHeartbeatMessage
    {
        private IMessageHeader header;

        private DateTime sendTime;
        private bool available;
        private string displayName;
        private int messageLength;

        /// <summary>
        /// Constructor
        /// </summary>
        public HeartbeatMessage() : this( "Unknown", "Unknown", false )
        {
        }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="displayName"></param>
        /// <param name="available"></param>
        public HeartbeatMessage( string sender, string displayName, bool available )
        {
            this.displayName = displayName;
            this.available = available;
            sendTime = DateTime.Now;
            calculateMessageLength();

            if( messageLength > MessageUtils.MAX_MESSAGE_SIZE )
            {
                throw new Exception( "Message is too long. (" +
                                     messageLength + " bytes, max is " +
                                     MessageUtils.MAX_MESSAGE_SIZE + " bytes)" );
            }

            header = new MessageHeader( sender.GetHashCode(), MessageUtils.MessageType.HEARTBEAT, messageLength );
        }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="header"></param>
        public HeartbeatMessage( IMessageHeader header ) : this()
        {
            this.header = header;
        }

        /// <summary>
        /// Calculates the length of this message.
        /// </summary>
        private void calculateMessageLength()
        {
            messageLength = MessageHeader.SIZE + sizeof( long ) + 1 + 1 + displayName.Length;
        }

        /// <summary cref="IMessage.getMessageHeader">
        /// <see cref="IMessage.getMessageHeader"/>
        /// </summary>
        /// <returns></returns>
        public IMessageHeader getMessageHeader()
        {
            return header;
        }

        /// <summary cref="IHeartbeatMessage.getSendTime">
        /// <see cref="IHeartbeatMessage.getSendTime"/>
        /// </summary>
        /// <returns></returns>
        public DateTime getSendTime()
        {
            return sendTime;
        }

        /// <summary cref="IHeartbeatMessage.isUserAvailable">
        /// <see cref="IHeartbeatMessage.isUserAvailable"/>
        /// </summary>
        /// <returns></returns>
        public bool isUserAvailable()
        {
            return available;
        }

        /// <summary cref="IHeartbeatMessage.getUserDisplayName">
        /// <see cref="IHeartbeatMessage.getUserDisplayName"/>
        /// </summary>
        /// <returns></returns>
        public string getUserDisplayName()
        {
            return displayName;
        }

        /// <summary cref="IMessageSerializer.toBinaryStream">
        /// <see cref="IMessageSerializer.toBinaryStream"/>
        /// </summary>
        /// <param name="writer"></param>
        public void toBinaryStream( BinaryWriter writer )
        {
            writer.Write( sendTime.ToBinary() );
            writer.Write( available );
            writer.Write( displayName );
        }

        /// <summary cref="IMessageSerializer.fromBinaryStream">
        /// <see cref="IMessageSerializer.fromBinaryStream"/>
        /// </summary>
        /// <param name="reader"></param>
        /// <returns></returns>
        public IMessage fromBinaryStream( BinaryReader reader )
        {
            sendTime = DateTime.FromBinary( reader.ReadInt64() );
            available = reader.ReadBoolean();
            displayName = reader.ReadString();
            calculateMessageLength();
            header.setMessageLength( messageLength );
            return this;
        }
    }
}
