using System.IO;

namespace ChatterBox
{
    interface IMessageHeader
    {
        /// <summary>
        /// Returns the hash code representing the sender of this message.
        /// </summary>
        /// <returns></returns>
        int getSenderHash();

        /// <summary>
        /// Sets the hash code representing the sender of this message.
        /// </summary>
        /// <param name="hash"></param>
        void setSenderHash( int hash );

        /// <summary>
        /// Returns the type of message.
        /// </summary>
        /// <returns></returns>
        MessageUtils.MessageType getMessageType();

        /// <summary>
        /// Sets the type of message.
        /// </summary>
        /// <param name="type"></param>
        void setMessageType( MessageUtils.MessageType type );

        /// <summary>
        /// Returns this message's length.
        /// </summary>
        /// <returns></returns>
        int getMessageLength();

        /// <summary>
        /// Sets this message's length.
        /// </summary>
        /// <param name="length"></param>
        void setMessageLength( int length );

        /// <summary>
        /// Writes this message header to the supplied BinaryWriter.
        /// </summary>
        /// <param name="writer"></param>
        void headerToBinaryStream( BinaryWriter writer );

        /// <summary>
        /// Reads a message header from the supplied BinaryReader.
        /// </summary>
        /// <param name="reader"></param>
        /// <returns></returns>
        IMessageHeader headerFromBinaryStream( BinaryReader reader );
    }
}
