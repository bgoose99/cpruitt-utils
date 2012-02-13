using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace StreamView.View
{
    interface IByteStreamView
    {
        #region Methods

        /// <summary>
        /// Sets the list of bytes that are displayed in this view.
        /// </summary>
        void setBytes( Byte[] bytes );

        /// <summary>
        /// Sets the list of bytes that are displayed in this view. The
        /// "offset" parameter can be used to indicate the offset in the
        /// underlying data where these bytes belong.
        /// </summary>
        /// <param name="bytes"></param>
        /// <param name="offset"></param>
        void setBytes( Byte[] bytes, long offset );

        /// <summary>
        /// Highlight one or more bits (and their corresponding bytes)
        /// in this view.
        /// </summary>
        /// <param name="bitOffset"></param>
        /// <param name="bitLength"></param>
        void highlightSelection( int bitOffset, int bitLength );

        #endregion
    }
}
