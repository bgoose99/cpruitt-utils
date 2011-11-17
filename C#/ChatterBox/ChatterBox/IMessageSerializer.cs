using System.IO;

namespace ChatterBox
{
    interface IMessageSerializer<T>
    {
        /// <summary>
        /// Writes a message to the supplied BinaryWriter.
        /// </summary>
        /// <param name="writer"></param>
        void toBinaryStream( BinaryWriter writer );

        /// <summary>
        /// Reads a message from the supplied BinaryReader.
        /// </summary>
        /// <param name="reader"></param>
        /// <returns></returns>
        T fromBinaryStream( BinaryReader reader );
    }
}
