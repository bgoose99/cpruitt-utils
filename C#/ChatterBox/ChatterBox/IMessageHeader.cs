using System.IO;

namespace ChatterBox
{
    interface IMessageHeader
    {
        int getSenderHash();
        void setSenderHash( int hash );
        MessageUtils.MessageType getMessageType();
        void setMessageType( MessageUtils.MessageType type );
        int getMessageLength();
        void setMessageLength( int length );
        void headerToBinaryStream( BinaryWriter writer );
        IMessageHeader headerFromBinaryStream( BinaryReader reader );
    }
}
