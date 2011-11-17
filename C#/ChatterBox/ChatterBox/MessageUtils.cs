using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace ChatterBox
{
    sealed class MessageUtils
    {
        /// <summary>
        /// This delegate is used to process incoming messages.
        /// </summary>
        /// <param name="message"></param>
        public delegate void ReceiveMessageDelegate( IMessage message );

        /// <summary>
        /// Message type enum.
        /// </summary>
        public enum MessageType { CHAT, HEARTBEAT };

        /// <summary>
        /// Maximum UDP packet size.
        /// </summary>
        public static int MAX_MESSAGE_SIZE = 65535;

        private static Object EncodeLock = new Object();
        private static Object DecodeLock = new Object();

        /// <summary>
        /// Encodes the supplied message to a raw byte array.
        /// </summary>
        /// <param name="msg"></param>
        /// <returns></returns>
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

        /// <summary>
        /// Decodes a raw byte array.
        /// </summary>
        /// <param name="msg"></param>
        /// <returns></returns>
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
