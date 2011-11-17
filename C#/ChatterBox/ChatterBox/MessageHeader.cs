using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace ChatterBox
{
    /// <summary>
    /// This class represents the header of a chat message.
    /// </summary>
    class MessageHeader : IMessageHeader
    {
        /// <summary>
        /// Size of a header.
        /// </summary>
        public static int SIZE = 12;

        private int senderHash;
        private MessageUtils.MessageType messageType;
        private int messageLength;

        /// <summary>
        /// Constructor
        /// </summary>
        public MessageHeader()
            : this( 0, MessageUtils.MessageType.CHAT, 0 )
        {
        }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="senderHash"></param>
        /// <param name="type"></param>
        /// <param name="length"></param>
        public MessageHeader( int senderHash, MessageUtils.MessageType type, int length )
        {
            this.senderHash = senderHash;
            this.messageType = type;
            this.messageLength = length;
        }

        /// <summary cref="IMessageHeader.getSenderHash">
        /// <see cref="IMessageHeader.getSenderHash"/>
        /// </summary>
        /// <returns></returns>
        public int getSenderHash()
        {
            return senderHash;
        }

        /// <summary cref="IMessageHeader.setSenderHash">
        /// <see cref="IMessageHeader.setSenderHash"/>
        /// </summary>
        /// <param name="hash"></param>
        public void setSenderHash( int hash )
        {
            senderHash = hash;
        }

        /// <summary cref="IMessageHeader.getMessageType">
        /// <see cref="IMessageHeader.getMessageType"/>
        /// </summary>
        /// <returns></returns>
        public MessageUtils.MessageType getMessageType()
        {
            return messageType;
        }

        /// <summary cref="IMessageHeader.setMessageType">
        /// <see cref="IMessageHeader.setMessageType"/>
        /// </summary>
        /// <param name="type"></param>
        public void setMessageType( MessageUtils.MessageType type )
        {
            messageType = type;
        }

        /// <summary cref="IMessageHeader.getMessageLength">
        /// <see cref="IMessageHeader.getMessageLength"/>
        /// </summary>
        /// <returns></returns>
        public int getMessageLength()
        {
            return messageLength;
        }

        /// <summary cref="IMessageHeader.setMessageLength">
        /// <see cref="IMessageHeader.setMessageLength"/>
        /// </summary>
        /// <param name="length"></param>
        public void setMessageLength( int length )
        {
            messageLength = length;
        }

        /// <summary cref="IMessageHeader.headerToBinaryStream">
        /// <see cref="IMessageHeader.headerToBinaryStream"/>
        /// </summary>
        /// <param name="writer"></param>
        public void headerToBinaryStream( BinaryWriter writer )
        {
            writer.Write( senderHash );
            writer.Write( (int)messageType );
            writer.Write( messageLength );
        }

        /// <summary cref="IMessageHeader.headerFromBinaryStream">
        /// <see cref="IMessageHeader.headerFromBinaryStream"/>
        /// </summary>
        /// <param name="reader"></param>
        /// <returns></returns>
        public IMessageHeader headerFromBinaryStream( BinaryReader reader )
        {
            setSenderHash( reader.ReadInt32() );
            setMessageType( (MessageUtils.MessageType)reader.ReadInt32() );
            setMessageLength( reader.ReadInt32() );
            return this;
        }
    }
}
