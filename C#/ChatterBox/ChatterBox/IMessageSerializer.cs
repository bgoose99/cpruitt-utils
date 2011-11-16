using System.IO;

namespace ChatterBox
{
    interface IMessageSerializer<T>
    {
        void toBinaryStream( BinaryWriter writer );

        T fromBinaryStream( BinaryReader reader );
    }
}
