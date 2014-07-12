using Chex.Data;
using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Effects;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Chex
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        #region Members

        private IStreamModel streamModel;
        private bool unsavedEdits;
        private bool findNext;
        private byte searchKey;

        #endregion

        #region Constructor(s)

        /// <summary>
        /// Default constructor.
        /// </summary>
        public MainWindow()
        {
            // NOTE: initialize stream model before calling InitializeComponent()
            streamModel = new FileStreamModel();
            streamModel.BlockChanged += new EventHandler( blockChangedEvent );

            InitializeComponent();

            hexDataGrid.FileByteOffset = getFileByteOffset;

            unsavedEdits = false;
            findNext = false;
        }

        #endregion

        #region Commands

        private void CanExecuteExitCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = true;
        }

        private void ExecuteExitCommand( object sender, ExecutedRoutedEventArgs e )
        {
            Application.Current.Shutdown();
        }

        private void CanExecuteOpenCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = true;
        }

        private void ExecuteOpenCommand( object sender, ExecutedRoutedEventArgs e )
        {
            if ( verifyAction() )
            {
                OpenFileDialog dialog = new OpenFileDialog();
                dialog.CheckFileExists = true;
                dialog.CheckPathExists = true;

                Nullable<bool> result = dialog.ShowDialog();
                if ( result == true )
                {
                    FileInfo info = new FileInfo( dialog.FileName );
                    filesizeTextBlock.Text = String.Format( "{0:n0}", info.Length ) + " bytes";

                    streamModel.setInputStream( File.Open( info.FullName, FileMode.Open ) );
                    streamModel.readBlock( 1 );

                    filenameTextBlock.Text = info.Name;

                    fileProgressBar.Value = 0;
                    fileProgressBar.Maximum = streamModel.BlockCount;
                }
            }
        }

        private void CanExecuteCloseCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = true;
        }

        private void ExecuteCloseCommand( object sender, ExecutedRoutedEventArgs e )
        {
            if ( verifyAction() )
            {
                ExecuteNewCommand( this, null );
            }
        }

        private void CanExecuteNewCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = true;
        }

        private void ExecuteNewCommand( object sender, ExecutedRoutedEventArgs e )
        {
            if ( verifyAction() )
            {
                FileInfo info = new FileInfo( System.IO.Path.GetTempFileName() );
                filesizeTextBlock.Text = String.Format( "{0:n0}", info.Length ) + " bytes";

                streamModel.setInputStream( File.Open( info.FullName, FileMode.Open ) );
                streamModel.readBlock( 1 );

                filenameTextBlock.Text = info.Name;

                fileProgressBar.Value = 0;
                fileProgressBar.Maximum = streamModel.BlockCount;

                unsavedEdits = true;
            }
        }

        private void CanExecuteGotoBlockCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            uint block = 0;
            try
            {
                block = Convert.ToUInt32( gotoTextBox.Text );
            }
            catch ( Exception )
            {
                e.CanExecute = false;
            }
            e.CanExecute = streamModel.isValidBlock( block );
        }

        private void ExecuteGotoBlockCommand( object sender, ExecutedRoutedEventArgs e )
        {
            if ( verifyAction() )
            {
                uint block = Convert.ToUInt32( gotoTextBox.Text );
                streamModel.readBlock( block );
                if ( gotoTextBox.IsFocused ) gotoTextBox.MoveFocus( new TraversalRequest( FocusNavigationDirection.Next ) );
            }
        }

        private void CanExecutePrevBlockCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = streamModel.HasPrevBlock;
        }

        private void ExecutePrevBlockCommand( object sender, ExecutedRoutedEventArgs e )
        {
            if ( verifyAction() ) streamModel.readPrevBlock();
        }

        private void CanExecuteNextBlockCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = streamModel.HasNextBlock;
        }

        private void ExecuteNextBlockCommand( object sender, ExecutedRoutedEventArgs e )
        {
            if ( verifyAction() ) streamModel.readNextBlock();
        }

        private void CanExecuteSaveCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = false;
        }

        private void ExecuteSaveCommand( object sender, ExecutedRoutedEventArgs e )
        {
            if ( unsavedEdits )
            {
                streamModel.writeStream();
            }
        }

        private void CanExecuteSaveAsCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = false;
        }

        private void ExecuteSaveAsCommand( object sender, ExecutedRoutedEventArgs e )
        {
            if ( unsavedEdits )
            {
                Console.WriteLine( "ExecuteSaveAsCommand Not implemented" );
                SaveFileDialog dialog = new SaveFileDialog();

                Nullable<bool> result = dialog.ShowDialog();
                if ( result == true )
                {
                    FileInfo info = new FileInfo( dialog.FileName );

                    filesizeTextBlock.Text = String.Format( "{0:n0}", info.Length ) + " bytes";

                    // TODO: fix
                    streamModel.writeStream();

                    filenameTextBlock.Text = info.Name;

                    fileProgressBar.Value = 0;
                    fileProgressBar.Maximum = streamModel.BlockCount;
                }
            }
        }

        private void CanExecuteGetGotoOffsetInputCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = true;
        }

        private void ExecuteGetGotoOffsetInputCommand( object sender, ExecutedRoutedEventArgs e )
        {
            if ( verifyAction() )
            {
                gotoOffsetInputBox.Visibility = System.Windows.Visibility.Visible;
                gotoOffsetButton.IsDefault = true;
                gotoOffsetTextBox.Text = String.Empty;
                gotoOffsetTextBox.Focus();

                toggleUI( false );
            }
        }

        private void CanExecuteGotoOffsetCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = true;
        }

        private void ExecuteGotoOffsetCommand( object sender, ExecutedRoutedEventArgs e )
        {
            gotoOffsetInputBox.Visibility = System.Windows.Visibility.Collapsed;
            gotoOffsetButton.IsDefault = false;

            toggleUI( true );

            bool success;
            uint offset;
            string input = gotoOffsetTextBox.Text.Trim();
            if ( input.StartsWith( "0x" ) )
            {
                input = input.Substring( 2 );
                success = UInt32.TryParse( input, NumberStyles.HexNumber, null, out offset );
            }
            else
            {
                success = UInt32.TryParse( input, out offset );
            }

            if ( success )
            {
                int blockOffset = streamModel.gotoOffset( offset );
                // TODO: Why does a block change affect how the offset is selected?
                Console.WriteLine( "yay" );
                if ( blockOffset < 0 ) System.Media.SystemSounds.Beep.Play();
                hexDataGrid.selectOffset( blockOffset );
            }
            else
            {
                MessageBox.Show( "Error parsing input.", "Invalid input", MessageBoxButton.OK, MessageBoxImage.Exclamation );
            }

            gotoOffsetTextBox.Text = String.Empty;
        }

        private void CanExecuteGetFindInputCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = true;
        }

        private void ExecuteGetFindInputCommand( object sender, ExecutedRoutedEventArgs e )
        {
            findInputBox.Visibility = System.Windows.Visibility.Visible;
            findByteButton.IsDefault = true;
            findByteTextBox.Text = hexDataGrid.CurrentSelectionString;
            findByteTextBox.SelectAll();
            findByteTextBox.Focus();

            toggleUI( false );
        }

        private void CanExecuteFindNextCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = findNext;
        }

        private void ExecuteFindNextCommand( object sender, ExecutedRoutedEventArgs e )
        {
            int offset = streamModel.find( hexDataGrid.CurrentSelectionOffset + 1, searchKey );
            if ( offset < 0 ) System.Media.SystemSounds.Beep.Play();
            hexDataGrid.selectOffset( offset );
        }

        private void CanExecuteFindPrevCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = findNext;
        }

        private void ExecuteFindPrevCommand( object sender, ExecutedRoutedEventArgs e )
        {
            int offset = streamModel.rfind( hexDataGrid.CurrentSelectionOffset - 1, searchKey );
            if ( offset < 0 ) System.Media.SystemSounds.Beep.Play();
            hexDataGrid.selectOffset( offset );
        }

        private void CanExecuteAddBytesCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = false;
        }

        private void ExecuteAddBytesCommand( object sender, ExecutedRoutedEventArgs e )
        {
            Console.WriteLine( "ExecuteAddBytesCommand Not implemented" );
        }

        private void CanExecuteDeleteBytesCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = false;
        }

        private void ExecuteDeleteBytesCommand( object sender, ExecutedRoutedEventArgs e )
        {
            Console.WriteLine( "ExecuteDeleteBytesCommand Not implemented" );
        }

        private void CanExecuteAboutCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = false;
        }

        private void ExecuteAboutCommand( object sender, ExecutedRoutedEventArgs e )
        {
            Console.WriteLine( "ExecuteAboutCommand Not implemented" );
        }

        private void CanExecuteAsciiCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = false;
        }

        private void ExecuteAsciiCommand( object sender, ExecutedRoutedEventArgs e )
        {
            Console.WriteLine( "ExecuteAsciiCommand Not implemented" );
        }

        private void CanExecuteCancelCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = true;
        }

        private void ExecuteCancelCommand( object sender, ExecutedRoutedEventArgs e )
        {
            if ( findInputBox.Visibility == System.Windows.Visibility.Visible ||
                gotoOffsetInputBox.Visibility == System.Windows.Visibility.Visible )
            {
                gotoOffsetInputBox.Visibility = System.Windows.Visibility.Collapsed;
                gotoOffsetButton.IsDefault = false;
                gotoOffsetTextBox.Text = String.Empty;

                findInputBox.Visibility = System.Windows.Visibility.Collapsed;
                findByteButton.IsDefault = false;
                findByteTextBox.Text = String.Empty;

                toggleUI( true );
            }
        }

        private void CanExecuteFindByteCommand( object sender, CanExecuteRoutedEventArgs e )
        {
            e.CanExecute = true;
        }

        private void ExecuteFindByteCommand( object sender, ExecutedRoutedEventArgs e )
        {
            findInputBox.Visibility = System.Windows.Visibility.Collapsed;
            findByteButton.IsDefault = false;

            toggleUI( true );

            string input = findByteTextBox.Text.Trim();
            if ( input.StartsWith( "0x" ) ) input = input.Substring( 2 );
            bool success = Byte.TryParse( input, NumberStyles.HexNumber, null, out searchKey );
            if ( success )
            {
                int offset = streamModel.find( 0, searchKey );
                if ( offset < 0 ) System.Media.SystemSounds.Beep.Play();
                hexDataGrid.selectOffset( offset );
                findNext = true;
            }
            else
            {
                MessageBox.Show( "Error parsing input.", "Invalid input", MessageBoxButton.OK, MessageBoxImage.Exclamation );
            }

            findByteTextBox.Text = String.Empty;
        }


        #endregion

        #region Properties

        #endregion

        #region Methods

        /// <summary>
        /// Called when the current block of bytes changes.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        public void blockChangedEvent( object sender, EventArgs e )
        {
            hexDataGrid.setBlockData( streamModel.BlockBytes );
            gotoTextBox.Text = "" + streamModel.BlockIndex;
            fileProgressBar.Value = streamModel.BlockIndex;
            blockNumberTextBlock.Text = "Block " + streamModel.BlockIndex + " of " + streamModel.BlockCount;
        }

        /// <summary>
        /// Returns the byte offset of the current block.
        /// </summary>
        /// <returns></returns>
        public uint getFileByteOffset()
        {
            return streamModel.BlockCount == 0 ? 0 : ( streamModel.BlockIndex - 1 ) * streamModel.BlockSize;
        }

        /// <summary>
        /// Overridden to allow user to cancel closing event, if necessary.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnClosing( System.ComponentModel.CancelEventArgs e )
        {
            e.Cancel = !verifyAction();
            base.OnClosing( e );
        }

        /// <summary>
        /// Returns true if the user wishes to continue action.
        /// </summary>
        /// <returns></returns>
        private bool verifyAction()
        {
            if ( unsavedEdits )
            {
                MessageBoxResult result = MessageBox.Show(
                    "There are unsaved edits in the current document." +
                    Environment.NewLine + "Do you wish to continue?", "Continue?",
                    MessageBoxButton.YesNo, MessageBoxImage.Question );
                return result == MessageBoxResult.Yes;
            }

            return true;
        }

        /// <summary>
        /// Toggles the main portions of the UI for displaying one of the custom input
        /// dialogs.
        /// </summary>
        /// <param name="enabled"></param>
        private void toggleUI( bool enabled )
        {
            gotoBlockButton.IsDefault = enabled;
            mainMenu.IsEnabled = enabled;
            toolBarTray.IsEnabled = enabled;
            statusBar1.IsEnabled = enabled;
            statusBar2.IsEnabled = enabled;
            hexDataGrid.IsEnabled = enabled;
            hexDataGridBlurEffect.Radius = ( enabled ? 0 : 3 );
        }

        #endregion
    }
}
