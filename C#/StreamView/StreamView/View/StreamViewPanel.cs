using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using StreamView.Presenter;

namespace StreamView.View
{
    public partial class StreamViewPanel : UserControl, IStreamView
    {
        private IStreamViewPresenter presenter;

        #region Constructors

        /// <summary>
        /// Constructor
        /// </summary>
        public StreamViewPanel()
            : this( 128 )
        {
        }

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="blockSize"></param>
        public StreamViewPanel( uint blockSize )
        {
            InitializeComponent();

            presenter = new StreamViewPresenter( blockSize, null, new StreamViewPresenter.UpdateView( updateView ) );

            navigatorPanel.PrevButtonPressed += new EventHandler( prevPressed );
            navigatorPanel.NextButtonPressed += new EventHandler( nextPressed );
            navigatorPanel.GotoButtonPressed += new EventHandler( gotoPressed );

            updateNavigatorControls();
        }

        #endregion

        #region Methods

        /// <summary>
        /// Updates the navigator panel's controls and labels with current
        /// data from the Presenter.
        /// </summary>
        private void updateNavigatorControls()
        {
            navigatorPanel.PrevEnabled = presenter.HasPrev;
            navigatorPanel.NextEnabled = presenter.HasNext;
            navigatorPanel.GotoEnabled = true;
            navigatorPanel.LabelText =
                "File: " + presenter.CurrentFileName + " | " +
                "Size: " + presenter.CurrentFileSize + " bytes | " +
                "Showing block " + presenter.BlockIndex + " of " + presenter.BlockCount;
            navigatorPanel.GotoText = "" + presenter.BlockIndex;
            navigatorPanel.Progress = (int)( ( ( presenter.BlockIndex * 1.0 ) / ( presenter.BlockCount == 0 ? 1 : presenter.BlockCount ) ) * 100.0 );
        }

        /// <summary>
        /// Updates the byte stream view with the current block of data.
        /// </summary>
        private void updateByteStreamView()
        {
            byteStreamView.setBytes( presenter.CurrentBlock,
                ( ( presenter.BlockIndex - 1 ) * presenter.BlockSize ) );
        }

        /// <summary>
        /// Updates the view.
        /// </summary>
        private void updateView()
        {
            updateNavigatorControls();
            updateByteStreamView();
        }

        #endregion

        #region IStreamView Methods

        /// <summary cref="IStreamView.nextPressed">
        /// <see cref="IStreamView.nextPressed"/>
        /// </summary>
        public void nextPressed( object sender, EventArgs e )
        {
            presenter.gotoNextBlock();
        }

        /// <summary cref="IStreamView.prevPressed">
        /// <see cref="IStreamView.prevPressed"/>
        /// </summary>
        public void prevPressed( object sender, EventArgs e )
        {
            presenter.gotoPrevBlock();
        }

        /// <summary cref="IStreamView.gotoPressed">
        /// <see cref="IStreamView.gotoPressed"/>
        /// </summary>
        public void gotoPressed( object sender, EventArgs e )
        {
            uint block = 0;
            try
            {
                block = UInt32.Parse( navigatorPanel.GotoText );
            }
            catch( Exception )
            {
                MessageBox.Show( "Invalid entry: " + navigatorPanel.GotoText, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error );
                return;
            }
            presenter.gotoBlock( block );
        }

        /// <summary cref="IStreamView.highlightSelection">
        /// <see cref="IStreamView.highlightSelection"/>
        /// </summary>
        public void highlightSelection( int bitOffset, int bitLength )
        {
            byteStreamView.highlightSelection( bitOffset, bitLength );
        }

        /// <summary cref="IStreamView.openFile">
        /// <see cref="IStreamView.openFile"/>
        /// </summary>
        /// <param name="filename"></param>
        public void openFile( string filename )
        {
            presenter.setInputFile( filename );
            presenter.gotoBlock( 1 );
        }

        #endregion
    }
}
