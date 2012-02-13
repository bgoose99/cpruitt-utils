using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace StreamView.Presenter
{
    /// <summary>
    /// This defines the interface between the view and model.
    /// </summary>
    interface IStreamViewPresenter
    {
        #region Properties

        /// <summary>
        /// Name of the current file, or null if one is not defined.
        /// </summary>
        string CurrentFileName { get; }

        /// <summary>
        /// Size of the current file, or zero if one is not defined.
        /// </summary>
        long CurrentFileSize { get; }

        /// <summary>
        /// True if the underlying model has another block
        /// beyond the current block, false otherwise.
        /// </summary>
        bool HasNext { get; }

        /// <summary>
        /// True if the underlying model has another block
        /// before the current block, false otherwise.
        /// </summary>
        bool HasPrev { get; }

        /// <summary>
        /// Block size, in bytes.
        /// </summary>
        uint BlockSize { get; }

        /// <summary>
        /// Number of blocks in the underlying data.
        /// </summary>
        uint BlockCount { get; }

        /// <summary>
        /// Index of the current block.
        /// </summary>
        uint BlockIndex { get; }

        /// <summary>
        /// Byte array containing the current block of data.
        /// </summary>
        Byte[] CurrentBlock { get; }

        #endregion

        #region Methods

        /// <summary>
        /// Advances the underlying model to the next block. If there
        /// is not another block, does nothing.
        /// </summary>
        void gotoNextBlock();

        /// <summary>
        /// Backs up the underlying model to the previous block. If there
        /// is not a previous block, does nothing.
        /// </summary>
        void gotoPrevBlock();

        /// <summary>
        /// Goes to the block at the specified index. If the supplied index
        /// does not exist, does nothing.
        /// </summary>
        /// <param name="blockIndex"></param>
        void gotoBlock( uint blockIndex );

        /// <summary>
        /// Sets the file to be used for the underlying data model.
        /// </summary>
        /// <param name="filename"></param>
        void setInputFile( string filename );

        #endregion
    }
}
