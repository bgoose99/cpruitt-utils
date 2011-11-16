using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace ChatterBox
{
    sealed class MessageUtils
    {
        public delegate void ReceiveMessageDelegate( IMessage message );

        public enum MessageType { CHAT, HEARTBEAT };

        public static int MAX_MESSAGE_SIZE = 65535;

        private static Object EncodeLock = new Object();
        private static Object DecodeLock = new Object();

        public static byte[] encodeMessage( IMessage msg )
        {
            lock( EncodeLock )
            {
                byte[] bytes = new byte[msg.getMessageHeader().getMessageLength()];
                MemoryStream stream = new MemoryStream( bytes );

                using( BinaryWriter writer = new BinaryWriter( stream ) )
                {
                    msg.getMessageHeader().headerToBinaryStream( writer );
                    msg.toBinaryStream( writer );
                }

                return bytes;
            }
        }

        public static IMessage decodeMessage( byte[] msg )
        {
            lock( DecodeLock )
            {
                MemoryStream stream = new MemoryStream( msg );
                MessageHeader header = new MessageHeader();

                using( BinaryReader reader = new BinaryReader( stream ) )
                {
                    header.headerFromBinaryStream( reader );

                    switch( header.getMessageType() )
                    {
                        case MessageUtils.MessageType.CHAT:
                            ChatMessage chatMessage = new ChatMessage( header );
                            return chatMessage.fromBinaryStream( reader );
                        case MessageUtils.MessageType.HEARTBEAT:
                            // TODO: fix
                            return null;
                        default:
                            throw new Exception( "Invalid message type: " + header.getMessageType() );
                    }
                }
            }
        }
    }
}
