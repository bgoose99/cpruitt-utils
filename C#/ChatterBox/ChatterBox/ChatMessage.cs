using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace ChatterBox
{
    class ChatMessage : IMessage
    {
        private IMessageHeader header;

        private string displayName;
        private DateTime sendTime;
        private string message;
        private int messageLength;

        public ChatMessage() : this( "", "", "" )
        {
        }

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

        public ChatMessage( IMessageHeader header ) : this()
        {
            this.header = header;
        }

        private void calculateMessageLength()
        {
            messageLength = MessageHeader.SIZE + 1 + displayName.Length + sizeof( long ) + 1 + message.Length;
        }

        public IMessageHeader getMessageHeader()
        {
            return header;
        }

        public string getMessage()
        {
            return message;
        }

        public string getSender()
        {
            return displayName;
        }

        public DateTime getDateTime()
        {
            return sendTime;
        }

        public void toBinaryStream( BinaryWriter writer )
        {
            writer.Write( displayName );
            writer.Write( sendTime.ToBinary() );
            writer.Write( message );
        }

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
