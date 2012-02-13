using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace StreamView.Model
{
    /// <summary>
    /// This interface defines the properties and methods available for
    /// accessing parts of the underlying stream of data.
    /// </summary>
    interface IStreamModel
    {
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
        Byte[] BlockBytes { get; }

        #endregion

        #region Methods

        /// <summary>
        /// Reads the next block of data from the stream. If no
        /// further block exists, does nothing.
        /// </summary>
        void readNextBlock();

        /// <summary>
        /// Reads the previous block of data from the stream. If
        /// no previous block exists, does nothing.
        /// </summary>
        void readPrevBlock();

        /// <summary>
        /// Reads the specified block of data from the stream. If
        /// the specified block does not exist, does nothing.
        /// </summary>
        /// <param name="index"></param>
        void readBlock( uint index );

        /// <summary>
        /// Sets the stream used to read the underlying data.
        /// </summary>
        /// <param name="stream"></param>
        void setInputStream( Stream stream );

        /// <summary>
        /// Sets the stream used to read the underlying data, with
        /// the specified block size.
        /// </summary>
        /// <param name="stream"></param>
        /// <param name="blockSize"></param>
        void setInputStream( Stream stream, uint blockSize );

        #endregion
    }
}
