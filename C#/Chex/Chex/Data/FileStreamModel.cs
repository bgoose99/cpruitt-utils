using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Chex.Data
{
    class FileStreamModel : IStreamModel, IDisposable
    {
        #region Members

        private readonly uint blockSize = 256;
        private BinaryReader  reader;
        private uint          blockCount;
        private uint          blockIndex;
        private IList<Byte>   blockBytes;

        #endregion

        #region Constructors

        /// <summary>
        /// Default constructor.
        /// </summary>
        public FileStreamModel()
            : this( null )
        {
        }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="stream"></param>
        public FileStreamModel( Stream stream )
        {
            setInputStream( stream );
        }

        #endregion

        #region IStreamModel Members

        public event EventHandler BlockChanged;

        /// <summary cref="IStreamModel.CurrentStream">
        /// <see cref="IStreamModel.CurrentStream"/>
        /// </summary>
        public System.IO.Stream CurrentStream
        {
            get { return reader == null ? null : reader.BaseStream; }
        }

        /// <summary cref="IStreamModel.BlockSize">
        /// <see cref="IStreamModel.BlockSize"/>
        /// </summary>
        public uint BlockSize
        {
            get { return blockSize; }
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
        public IList<byte> BlockBytes
        {
            get { return blockBytes; }
            private set { blockBytes = value; }
        }

        /// <summary cref="IStreamModel.HasPrevBlock">
        /// <see cref="IStreamModel.HasPrevBlock"/>
        /// </summary>
        public bool HasPrevBlock
        {
            get { return blockIndex > 0; }
        }

        /// <summary cref="IStreamModel.HasNextBlock">
        /// <see cref="IStreamModel.HasNextBlock"/>
        /// </summary>
        public bool HasNextBlock
        {
            get { return blockIndex < blockCount; }
        }

        /// <summary cref="IStreamModel.readNextBlock">
        /// <see cref="IStreamModel.readNextBlock"/>
        /// </summary>
        public void readNextBlock()
        {
            if ( blockIndex < blockCount ) readBlock( blockIndex + 1 );
        }

        /// <summary cref="IStreamModel.readPrevBlock">
        /// <see cref="IStreamModel.readPrevBlock"/>
        /// </summary>
        public void readPrevBlock()
        {
            if ( blockIndex > 1 ) readBlock( blockIndex - 1 );
        }

        /// <summary cref="IStreamModel.readBlock">
        /// <see cref="IStreamModel.readBlock"/>
        /// </summary>
        public void readBlock( uint index )
        {
            if ( index > 0 && index <= blockCount )
            {
                reader.BaseStream.Seek( ( ( index - 1 ) * blockSize ), SeekOrigin.Begin );
                blockBytes = reader.ReadBytes( (int)blockSize );
                blockIndex = index;

                if ( BlockChanged != null ) BlockChanged( this, null );
            }
        }

        /// <summary cref="IStreamModel.setInputStream">
        /// <see cref="IStreamModel.setInputStream"/>
        /// </summary>
        public void setInputStream( System.IO.Stream stream )
        {
            if ( reader != null )
            {
                reader.Close();
                reader = null;
            }

            if ( stream == null ) reader = null;
            else reader = new BinaryReader( stream );

            if ( reader != null )
            {
                blockCount = (uint)( stream.Length / blockSize ) + (uint)( stream.Length % blockSize == 0 ? 0 : 1 );
            }
            else
            {
                blockCount = 0;
                blockIndex = 0;
            }
        }

        /// <summary cref="IStreamModel.writeStream">
        /// <see cref="IStreamModel.writeStream"/>
        /// </summary>
        public void writeStream()
        {

            throw new NotImplementedException();
        }

        /// <summary cref="IStreamModel.writeStream">
        /// <see cref="IStreamModel.writeStream"/>
        /// </summary>
        public void writeStream( Stream newStream )
        {

            throw new NotImplementedException();
        }

        /// <summary cref="IStreamModel.isValidBlock">
        /// <see cref="IStreamModel.isValidBlock"/>
        /// </summary>
        public bool isValidBlock( uint block )
        {
            return ( block > 0 && block <= blockCount );
        }

        /// <summary cref="IStreamModel.find">
        /// <see cref="IStreamModel.find"/>
        /// </summary>
        public int find( uint offset, byte searchKey )
        {
            try
            {
                for ( int i = (int)offset; i < blockBytes.Count; ++i )
                    if ( blockBytes[i] == searchKey ) return i;
            }
            catch ( Exception ) { }
            return -1;
        }

        /// <summary cref="IStreamModel.rfind">
        /// <see cref="IStreamModel.rfind"/>
        /// </summary>
        public int rfind( uint offset, byte searchKey )
        {
            try
            {
                for ( int i = (int)offset; i >= 0; --i )
                    if ( blockBytes[i] == searchKey ) return i;
            }
            catch ( Exception ) { }
            return -1;
        }

        /// <summary cref="IStreamModel.gotoOffset">
        /// <see cref="IStreamModel.gotoOffset"/>
        /// </summary>
        public int gotoOffset( uint offset )
        {
            uint index = offset / blockSize + 1;
            if ( index < 1 || index > blockCount )
                return -1;

            if( index != blockIndex ) readBlock( index );
            return (int)( offset % blockSize );
        }

        #endregion

        #region IDisposable Members

        /// <summary cref="IDisposable.Dispose">
        /// <see cref="IDisposable.Dispose"/>
        /// </summary>
        public void Dispose()
        {
            if ( reader != null ) reader.Close();
        }

        #endregion
    }
}
