using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Chex.Data
{
    interface IStreamModel
    {
        #region EventHandlers

        event EventHandler BlockChanged;
        
        #endregion

        #region Properties

        /// <summary>
        /// The current stream.
        /// </summary>
        Stream CurrentStream { get; }

        /// <summary>
        /// The block size.
        /// </summary>
        uint BlockSize { get; }

        /// <summary>
        /// The number of blocks in the current stream.
        /// </summary>
        uint BlockCount { get; }

        /// <summary>
        /// The index of the current block.
        /// </summary>
        uint BlockIndex { get; }

        /// <summary>
        /// The current block of bytes.
        /// </summary>
        IList<Byte> BlockBytes { get; }

        /// <summary>
        /// Previous block available?
        /// </summary>
        bool HasPrevBlock { get; }

        /// <summary>
        /// Next block available?
        /// </summary>
        bool HasNextBlock { get; }

        #endregion

        #region Methods

        /// <summary>
        /// Reads the next block of data from the underlying stream.
        /// Does nothing if the next block does not exist.
        /// </summary>
        void readNextBlock();

        /// <summary>
        /// Reads the previous block of data from the underlying stream.
        /// Does nothing if the previous block does not exist.
        /// </summary>
        void readPrevBlock();

        /// <summary>
        /// Reads the specified block of data from the stream.
        /// Does nothing if the block does not exist.
        /// </summary>
        /// <param name="index"></param>
        void readBlock( uint index );

        /// <summary>
        /// Sets the underlying stream from which data is read.
        /// </summary>
        /// <param name="stream"></param>
        void setInputStream( Stream stream );

        /// <summary>
        /// Writes the underlying stream.
        /// </summary>
        void writeStream();

        /// <summary>
        /// Write the current stream to <code>newStream</code>, then changes
        /// the current stream to <code>newStream</code>.
        /// </summary>
        /// <param name="stream"></param>
        void writeStream( Stream newStream );

        /// <summary>
        /// Returns true if the block index is valid.
        /// </summary>
        /// <returns></returns>
        bool isValidBlock( uint block );

        /// <summary>
        /// Finds the next byte that matches the search key.
        /// </summary>
        /// <param name="offset"></param>
        /// <param name="searchKey"></param>
        /// <returns>The index of the next byte after <code>offset</code> that
        /// matches <code>searchKey</code>, or -1 if no match is found.</returns>
        int find( uint offset, byte searchKey );

        /// <summary>
        /// Finds the previous byte that matches the search key.
        /// </summary>
        /// <param name="offset"></param>
        /// <param name="searchKey"></param>
        /// <returns>The index of the next byte before <code>offset</code> that
        /// matches <code>searchKey</code>, or -1 if no match is found.</returns>
        int rfind( uint offset, byte searchKey );

        /// <summary>
        /// Loads the block that contains <code>offset</code>.
        /// </summary>
        /// <param name="offset"></param>
        /// <returns>The index of the desired offset in the current block,
        /// or -1 if <code>offset</code> is invalid.</returns>
        int gotoOffset( uint offset );

        #endregion
    }
}
