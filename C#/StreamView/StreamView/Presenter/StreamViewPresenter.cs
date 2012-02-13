using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using StreamView.Model;
using System.IO;

namespace StreamView.Presenter
{
    class StreamViewPresenter : IStreamViewPresenter, IDisposable
    {
        /// <summary>
        /// Delegate used any time the view should be updated.
        /// </summary>
        public delegate void UpdateView();

        private IStreamModel model;
        private Stream currentStream;
        private string currentFileName;
        private long currentFileSize;
        private UpdateView updateViewDelegate;

        #region Constructors

        public StreamViewPresenter()
            : this( StreamModel.DEFAULT_BLOCK_SIZE )
        {
        }

        public StreamViewPresenter( uint blockSize )
            : this( blockSize, null )
        {
        }

        public StreamViewPresenter( uint blockSize, string inputFile )
            : this( blockSize, inputFile, null )
        {
        }

        public StreamViewPresenter( uint blockSize, string inputFile, UpdateView updateViewDelegate )
        {
            currentFileSize = 0;
            model = new StreamModel( blockSize );
            setInputFile( inputFile );
            this.updateViewDelegate = updateViewDelegate;
        }

        #endregion

        #region Methods
        #endregion

        #region IStreamViewPresenter Properties

        /// <summary cref="IStreamViewPresenter.CurrentFileName">
        /// <see cref="IStreamViewPresenter.CurrentFileName"/>
        /// </summary>
        public string CurrentFileName
        {
            get { return currentFileName; }
        }

        /// <summary cref="IStreamViewPresenter.CurrentFileSize">
        /// <see cref="IStreamViewPresenter.CurrentFileSize"/>
        /// </summary>
        public long CurrentFileSize
        {
            get { return currentFileSize; }
        }

        /// <summary cref="IStreamViewPresenter.HasNext">
        /// <see cref="IStreamViewPresenter.HasNext"/>
        /// </summary>
        public bool HasNext
        {
            get { return ( BlockIndex < BlockCount ); }
        }

        /// <summary cref="IStreamViewPresenter.HasPrev">
        /// <see cref="IStreamViewPresenter.HasPrev"/>
        /// </summary>
        public bool HasPrev
        {
            get { return ( BlockIndex > 1 ); }
        }

        /// <summary cref="IStreamViewPresenter.BlockSize">
        /// <see cref="IStreamViewPresenter.BlockSize"/>
        /// </summary>
        public uint BlockSize
        {
            get { return model.BlockSize; }
        }

        /// <summary cref="IStreamViewPresenter.BlockCount">
        /// <see cref="IStreamViewPresenter.BlockCount"/>
        /// </summary>
        public uint BlockCount
        {
            get { return model.BlockCount; }
        }

        /// <summary cref="IStreamViewPresenter.BlockIndex">
        /// <see cref="IStreamViewPresenter.BlockIndex"/>
        /// </summary>
        public uint BlockIndex
        {
            get { return model.BlockIndex; }
        }

        /// <summary cref="IStreamViewPresenter.CurrentBlock">
        /// <see cref="IStreamViewPresenter.CurrentBlock"/>
        /// </summary>
        public Byte[] CurrentBlock
        {
            get { return model.BlockBytes; }
        }

        #endregion

        #region IStreamViewPresenter Methods

        /// <summary cref="IStreamViewPresenter.gotoNextBlock">
        /// <see cref="IStreamViewPresenter.gotoNextBlock"/>
        /// </summary>
        public void gotoNextBlock()
        {
            model.readNextBlock();
            if( updateViewDelegate != null )
                updateViewDelegate();
        }

        /// <summary cref="IStreamViewPresenter.gotoPrevBlock">
        /// <see cref="IStreamViewPresenter.gotoPrevBlock"/>
        /// </summary>
        public void gotoPrevBlock()
        {
            model.readPrevBlock();
            if( updateViewDelegate != null )
                updateViewDelegate();
        }

        /// <summary cref="IStreamViewPresenter.gotoBlock">
        /// <see cref="IStreamViewPresenter.gotoBlock"/>
        /// </summary>
        /// <param name="blockIndex"></param>
        public void gotoBlock( uint blockIndex )
        {
            model.readBlock( blockIndex );
            if( updateViewDelegate != null )
                updateViewDelegate();
        }

        /// <summary cref="IStreamViewPresenter.setInputFile">
        /// <see cref="IStreamViewPresenter.setInputFile"/>
        /// </summary>
        /// <param name="filename"></param>
        public void setInputFile( string filename )
        {
            if( filename == null )
            {
                currentFileName = null;
                currentFileSize = 0;
                model.setInputStream( null );
            }
            else
            {
                if( !File.Exists( filename ) )
                    throw new Exception( "Supplied filename does not exist: " + Environment.NewLine +
                        filename );

                FileInfo info = new FileInfo( filename );
                currentFileName = info.Name;
                currentFileSize = info.Length;

                currentStream = File.Open( filename, FileMode.Open );
                model.setInputStream( currentStream );
            }
        }

        #endregion

        #region IDisposable Methods

        public void Dispose()
        {
            if( currentStream != null )
                currentStream.Close();
        }

        #endregion
    }
}
