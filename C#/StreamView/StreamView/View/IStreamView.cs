using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace StreamView.View
{
    interface IStreamView
    {
        #region Properties
        #endregion

        #region Methods

        /// <summary>
        /// Event handler for when the user presses the "next" button.
        /// </summary>
        void nextPressed( object sender, EventArgs e );

        /// <summary>
        /// Event handler for when the user presses the "previous" button.
        /// </summary>
        void prevPressed( object sender, EventArgs e );

        /// <summary>
        /// Event handler for when the user presses the "goto" button.
        /// </summary>
        void gotoPressed( object sender, EventArgs e );

        /// <summary>
        /// Highlights a selection in the current block of data.
        /// </summary>
        /// <param name="bitOffset"></param>
        /// <param name="bitLength"></param>
        void highlightSelection( int bitOffset, int bitLength );

        /// <summary>
        /// Opens a file for viewing.
        /// </summary>
        /// <param name="filename"></param>
        void openFile( string filename );

        #endregion
    }
}
