using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace ChatterBox
{
    /// <summary>
    /// This class represents a single chat message.
    /// </summary>
    class ChatMessage : IMessage
    {
        private IMessageHeader header;

        private string displayName;
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
        /// Constructor used for sending messages.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="displayName"></param>
        /// <param name="message"></param>
        public ChatMessage( string sender, string displayName, string message )
        {
            this.displayName = displayName;
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
            messageLength = MessageHeader.SIZE + 1 + displayName.Length + sizeof( long ) + 1 + message.Length;
        }

        /// <summary cref="IMessage.getMessageHeader">
        /// <see cref="IMessage.getMessageHeader"/>
        /// </summary>
        public IMessageHeader getMessageHeader()
        {
            return header;
        }

        /// <summary cref="IMessage.getMessage">
        /// <see cref="IMessage.getMessage"/>
        /// </summary>
        /// <returns></returns>
        public string getMessage()
        {
            return message;
        }

        /// <summary cref="IMessage.getSender">
        /// <see cref="IMessage.getSender"/>
        /// </summary>
        /// <returns></returns>
        public string getSender()
        {
            return displayName;
        }

        /// <summary cref="IMessage.getDateTime">
        /// <see cref="IMessage.getDateTime"/>
        /// </summary>
        /// <returns></returns>
        public DateTime getDateTime()
        {
            return sendTime;
        }

        /// <summary cref="IMessageSerializer.toBinaryStream">
        /// <see cref="IMessageSerializer.toBinaryStream"/>
        /// </summary>
        /// <param name="writer"></param>
        public void toBinaryStream( BinaryWriter writer )
        {
            writer.Write( displayName );
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
            sendTime = DateTime.FromBinary( reader.ReadInt64() );
            message = reader.ReadString();
            
            calculateMessageLength();
            header.setMessageLength( messageLength );
            return this;
        }
    }
}
