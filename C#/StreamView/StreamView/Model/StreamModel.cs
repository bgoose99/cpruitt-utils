using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace StreamView.Model
{
    class StreamModel : IStreamModel, IDisposable
    {
        public static readonly uint DEFAULT_BLOCK_SIZE = 256;
        public static readonly uint MINIMUM_BLOCK_SIZE = 16;
        public static readonly uint MAXIMUM_BLOCK_SIZE = 1024;

        private uint blockSize;
        private BinaryReader reader;
        private uint blockCount;
        private uint blockIndex;
        private Byte[] blockBytes;

        #region Constructors

        public StreamModel()
            : this( DEFAULT_BLOCK_SIZE )
        {
        }

        public StreamModel( uint blockSize )
            : this( blockSize, null )
        {
        }

        public StreamModel( uint blockSize, Stream stream )
        {
            setInputStream( stream, blockSize );
        }

        #endregion

        #region Methods

        /// <summary>
        /// Sets the block size to the supplied value.
        /// </summary>
        /// <param name="blockSize"></param>
        private void setBlockSize( uint blockSize )
        {
            if( blockSize < MINIMUM_BLOCK_SIZE || blockSize > MAXIMUM_BLOCK_SIZE )
            {
                throw new Exception( "Block size must be between " + MINIMUM_BLOCK_SIZE +
                    " and " + MAXIMUM_BLOCK_SIZE + " bytes." );
            }
            BlockSize = blockSize;
        }

        #endregion

        #region IStreamModel Properties

        /// <summary cref="IStreamModel.CurrentStream">
        /// <see cref="IStreamModel.CurrentStream"/>
        /// </summary>
        public Stream CurrentStream
        {
            get { return reader == null ? null : reader.BaseStream; }
        }

        /// <summary cref="IStreamModel.BlockSize">
        /// <see cref="IStreamModel.BlockSize"/>
        /// </summary>
        public uint BlockSize
        {
            get { return blockSize; }
            private set { blockSize = value; }
        }

        /// <summary cref="IStreamModel.BlockCount">
        /// <see cref="IStreamModel.BlockCount"/>
        /// </summary>
        public uint BlockCount
        {
            get { return blockCount; }
            private set { blockCount = value; }
        }

        /// <summary cref="IStreamModel.BlockIndex">
        /// <see cref="IStreamModel.BlockIndex"/>
        /// </summary>
        public uint BlockIndex
        {
            get { return blockIndex; }
            private set { blockIndex = value; }
        }

        /// <summary cref="IStreamModel.BlockBytes">
        /// <see cref="IStreamModel.BlockBytes"/>
        /// </summary>
        public Byte[] BlockBytes
        {
            get { return blockBytes; }
            private set { blockBytes = value; }
        }

        #endregion

        #region IStreamModel Methods

        /// <summary cref="IStreamModel.readNextBlock">
        /// <see cref="IStreamModel.readNextBlock"/>
        /// </summary>
        public void readNextBlock()
        {
            if( blockIndex < blockCount )
                readBlock( blockIndex + 1 );
        }

        /// <summary cref="IStreamModel.readPrevBlock">
        /// <see cref="IStreamModel.readPrevBlock"/>
        /// </summary>
        public void readPrevBlock()
        {
            if( blockIndex > 1 )
                readBlock( blockIndex - 1 );
        }

        /// <summary cref="IStreamModel.readBlock">
        /// <see cref="IStreamModel.readBlock"/>
        /// </summary>
        /// <param name="index"></param>
        public void readBlock( uint index )
        {
            if( index > 0 && index <= blockCount )
            {
                reader.BaseStream.Seek( ( ( index - 1 ) * blockSize ), SeekOrigin.Begin );
                blockBytes = reader.ReadBytes( (int)blockSize );
                blockIndex = index;
            }
        }

        /// <summary cref="IStreamModel.setInputStream">
        /// <see cref="IStreamModel.setInputStream"/>
        /// </summary>
        /// <param name="stream"></param>
        public void setInputStream( Stream stream )
        {
            setInputStream( stream, blockSize );
        }

        /// <summary cref="IStreamModel.setInputStream">
        /// <see cref="IStreamModel.setInputStream"/>
        /// </summary>
        /// <param name="stream"></param>
        /// <param name="blockSize"></param>
        public void setInputStream( Stream stream, uint blockSize )
        {
            setBlockSize( blockSize );

            // close previous reader, if necessary
            if( reader != null )
            {
                reader.Close();
                reader = null;
            }

            // set new reader
            if( stream == null )
                reader = null;
            else
                reader = new BinaryReader( stream );

            // get block count
            if( reader != null )
            {
                blockCount = (uint)( stream.Length / blockSize ) + (uint)( stream.Length % blockSize == 0 ? 0 : 1 );
            }
        }

        #endregion

        #region IDisposable Methods

        public void Dispose()
        {
            if( reader != null )
                reader.Close();
        }

        #endregion
    }
}
