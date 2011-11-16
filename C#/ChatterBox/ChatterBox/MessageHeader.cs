using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace ChatterBox
{
    class MessageHeader : IMessageHeader
    {
        public static int SIZE = 12;

        private int senderHash;
        private MessageUtils.MessageType messageType;
        private int messageLength;

        public MessageHeader()
            : this( 0, MessageUtils.MessageType.CHAT, 0 )
        {
        }

        public MessageHeader( int senderHash, MessageUtils.MessageType type, int length )
        {
            this.senderHash = senderHash;
            this.messageType = type;
            this.messageLength = length;
        }

        public int getSenderHash()
        {
            return senderHash;
        }

        public void setSenderHash( int hash )
        {
            senderHash = hash;
        }

        public MessageUtils.MessageType getMessageType()
        {
            return messageType;
        }

        public void setMessageType( MessageUtils.MessageType type )
        {
            messageType = type;
        }

        public int getMessageLength()
        {
            return messageLength;
        }

        public void setMessageLength( int length )
        {
            messageLength = length;
        }

        public void headerToBinaryStream( BinaryWriter writer )
        {
            writer.Write( senderHash );
            writer.Write( (int)messageType );
            writer.Write( messageLength );
        }

        public IMessageHeader headerFromBinaryStream( BinaryReader reader )
        {
            setSenderHash( reader.ReadInt32() );
            setMessageType( (MessageUtils.MessageType)reader.ReadInt32() );
            setMessageLength( reader.ReadInt32() );
            return this;
        }
    }
}
