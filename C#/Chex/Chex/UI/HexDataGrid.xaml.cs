using Chex.Data;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Controls.Primitives;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Chex.UI
{
    /// <summary>
    /// Interaction logic for HexDataGrid.xaml
    /// </summary>
    public partial class HexDataGrid : DataGrid, INotifyPropertyChanged
    {
        #region Members

        public delegate uint getCurrentFileByteOffset();

        private ObservableCollection<HexDataRow> hexDataRows;
        private int currentRowSelection;
        private int currentColSelection;
        private getCurrentFileByteOffset fileByteOffset;

        #endregion

        #region INotifyPropertyChanged Members

        public event PropertyChangedEventHandler PropertyChanged;

        #endregion

        #region Constructor(s)

        /// <summary>
        /// Default constructor.
        /// </summary>
        public HexDataGrid()
        {
            InitializeComponent();

            CurrentCellChanged += new EventHandler<EventArgs>( customCurrentCellChangedHandler );
            SelectedCellsChanged += new SelectedCellsChangedEventHandler( customSelectedCellsChangedHandler );

            AutoGenerateColumns = false;
            AlternatingRowBackground = Brushes.Azure;
            CanUserAddRows = false;
            CanUserDeleteRows = false;
            CanUserReorderColumns = false;
            CanUserResizeColumns = false;
            CanUserResizeRows = false;
            CanUserSortColumns = false;
            LoadingRow += loadingRow;
            IsReadOnly = true;
            SelectionMode = DataGridSelectionMode.Extended;
            SelectionUnit = DataGridSelectionUnit.Cell;

            hexDataRows = new ObservableCollection<HexDataRow>();
            Items.Clear();
            ItemsSource = hexDataRows;

            for ( int i = 0; i < 16; ++i )
                hexDataRows.Add( new HexDataRow() );

            currentRowSelection = -1;
            currentColSelection = -1;
        }

        #endregion

        #region Properties

        public getCurrentFileByteOffset FileByteOffset
        {
            get { return fileByteOffset; }
            set { fileByteOffset = value; }
        }

        /// <summary>
        /// The actual HexDataRows that make up this grid.
        /// </summary>
        public ObservableCollection<HexDataRow> Rows
        {
            get { return hexDataRows; }
        }

        /// <summary>
        /// The offset of the current selection, formatted for display.
        /// </summary>
        public string CurrentSelectionOffsetString
        {
            get
            {
                uint i = ( fileByteOffset == null ) ? 0 : fileByteOffset();
                return "Offset: " + String.Format( "{0:n0}", i + CurrentSelectionOffset );
            }
        }

        /// <summary>
        /// String representing the current selection.
        /// </summary>
        public string CurrentSelectionString
        {
            get
            {
                return ( currentRowSelection < 0 || currentColSelection < 0 || Rows[currentRowSelection].getDisplayString( currentColSelection ).Length == 0 ) ?
                        "" :
                        Rows[currentRowSelection].getDisplayString( currentColSelection );
            }
        }

        /// <summary>
        /// The offset of the current selection.
        /// </summary>
        public uint CurrentSelectionOffset
        {
            get
            {
                return ( currentRowSelection < 0 || currentColSelection < 0 ) ?
                    0 : (uint)( currentRowSelection * 16 + currentColSelection );
            }
        }

        /// <summary>
        /// The current byte selection, in binary, formatted for display.
        /// </summary>
        public string CurrentSelectionBinary
        {
            get
            {
                return "Bin: " + ( String.IsNullOrEmpty( CurrentSelectionString ) ?
                    "" : Convert.ToString( Convert.ToInt32( CurrentSelectionString, 16 ), 2 ).PadLeft( 8, '0' ) );
            }
        }

        /// <summary>
        /// The current byte selection, in octal, formatted for display.
        /// </summary>
        public string CurrentSelectionOctal
        {
            get
            {
                return "Oct: " + ( String.IsNullOrEmpty( CurrentSelectionString ) ?
                    "" : Convert.ToString( Convert.ToInt32( CurrentSelectionString, 16 ), 8 ).PadLeft( 3, '0' ) );
            }
        }

        /// <summary>
        /// The current byte selection, in decimal, formatted for display.
        /// </summary>
        public string CurrentSelectionDecimal
        {
            get
            {
                return "Dec: " + ( String.IsNullOrEmpty( CurrentSelectionString ) ?
                    "" : Convert.ToString( Convert.ToInt32( CurrentSelectionString, 16 ), 10 ) );
            }
        }

        #endregion

        #region Methods

        /// <summary>
        /// Sets the block of data that makes up this grid.
        /// </summary>
        /// <param name="blockBytes"></param>
        public void setBlockData( IList<Byte> blockBytes )
        {
            IList<Byte> rowBytes = new List<Byte>();
            int rowIndex = 0;

            foreach ( Byte b in blockBytes )
            {
                rowBytes.Add( b );

                if ( rowBytes.Count == 16 )
                {
                    Rows[rowIndex].Bytes = rowBytes;
                    rowBytes.Clear();
                    ++rowIndex;
                    if ( rowIndex >= Rows.Count ) break;
                }
            }

            if ( rowBytes.Count > 0 )
                Rows[rowIndex].Bytes = rowBytes;

            Items.Refresh();
        }

        /// <summary>
        /// Sets the selection to the given index, if possible.
        /// </summary>
        /// <param name="offset"></param>
        public void selectOffset( int offset )
        {
            if ( offset >= 0 )
            {
                Console.WriteLine( "Selecting offset: {0}", offset );
                // calculate row/col
                currentRowSelection = offset / 16;
                currentColSelection = offset % 16;

                try
                {
                    DataGridCellInfo info = new DataGridCellInfo( Rows[currentRowSelection], Columns[currentColSelection] );
                    IList<DataGridCellInfo> addedCells = new List<DataGridCellInfo>();
                    IList<DataGridCellInfo> removedCells = new List<DataGridCellInfo>( SelectedCells );

                    {
                        SelectedCellsChanged -= new SelectedCellsChangedEventHandler( customSelectedCellsChangedHandler );

                        SelectedCells.Clear();
                        SelectedCells.Add( info );

                        SelectedCellsChanged += new SelectedCellsChangedEventHandler( customSelectedCellsChangedHandler );
                    }

                    CurrentCell = info;
                    SelectedItem = info;

                    DataGridCell cell = getDataGridCell(info );
                    if( cell != null ) cell.Focus();

                    addedCells.Add( info );
                    SelectedCellsChangedEventArgs args = 
                        new SelectedCellsChangedEventArgs( new ReadOnlyCollection<DataGridCellInfo>( addedCells ),
                            new ReadOnlyCollection<DataGridCellInfo>( removedCells ) );
                    customSelectedCellsChangedHandler( this, args );
                }
                catch ( Exception e ) { Console.WriteLine( e.Message ); }
            }
        }

        /// <summary>
        /// Custom handler for current cell changes.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void customCurrentCellChangedHandler( object sender, EventArgs e )
        {
            try
            {
                if ( CurrentItem != null && CurrentColumn != null )
                {
                    currentRowSelection = Items.IndexOf( CurrentItem );
                    currentColSelection = Math.Min( CurrentColumn.DisplayIndex, 15 );
                }
            }
            catch ( Exception )
            {
                currentRowSelection = -1;
                currentColSelection = -1;
            }

            onPropertyChanged( "CurrentSelectionOffsetString" );
            onPropertyChanged( "CurrentSelectionBinary" );
            onPropertyChanged( "CurrentSelectionOctal" );
            onPropertyChanged( "CurrentSelectionDecimal" );
        }

        /// <summary>
        /// Custom handler for selection changes.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void customSelectedCellsChangedHandler( object sender, SelectedCellsChangedEventArgs e )
        {
            if ( SelectedCells.Count == 0 )
                return;

            int rowStart, colStart;
            int rowEnd, colEnd;

            // get rowStart, colStart (first selected item)
            rowStart = Items.IndexOf( SelectedCells[0].Item );
            colStart = Math.Min( SelectedCells[0].Column.DisplayIndex, 15 );

            // get rowEnd, colEnd (last selected item)
            rowEnd = Items.IndexOf( SelectedCells[SelectedCells.Count - 1].Item );
            colEnd = Math.Min( SelectedCells[SelectedCells.Count - 1].Column.DisplayIndex, 15 );

            if ( colStart > colEnd )
            {
                int i = colStart;
                colStart = colEnd;
                colEnd = i;
            }

            if ( rowStart > rowEnd )
            {
                int i = rowStart;
                rowStart = rowEnd;
                rowEnd = i;
            }

            {
                IList<int> rows = new List<int>();

                foreach ( DataGridCellInfo i in e.RemovedCells )
                {
                    int row = Items.IndexOf( i.Item );

                    if ( row >= 0 && ( row < rowStart || row > rowEnd ) && !rows.Contains( row ) )
                    {
                        Rows[row].clearHighlight();
                        rows.Add( row );
                    }
                }
            }

            if ( SelectedCells.Count > 1 )
            {
                if ( rowEnd != rowStart )
                {
                    // clear selection
                    // un-hook this listener while we're tinkering with the selected cells
                    SelectedCellsChanged -= new SelectedCellsChangedEventHandler( customSelectedCellsChangedHandler );
                    SelectedCells.Clear();

                    Rows[rowStart].highlightBytes( colStart, 15 );

                    // select first row
                    for ( int i = colStart; i < 16; ++i )
                        SelectedCells.Add( new DataGridCellInfo( Rows[rowStart], Columns[i] ) );

                    // select middle row(s)
                    for ( int i = rowStart + 1; i < rowEnd; ++i )
                    {
                        Rows[i].highlightBytes( 0, 15 );
                        for ( int j = 0; j < 16; ++j )
                            SelectedCells.Add( new DataGridCellInfo( Rows[i], Columns[j] ) );
                    }

                    // select last row
                    Rows[rowEnd].highlightBytes( 0, colEnd );
                    for ( int i = 0; i <= colEnd; ++i )
                        SelectedCells.Add( new DataGridCellInfo( Rows[rowEnd], Columns[i] ) );

                    // hook up listener again
                    SelectedCellsChanged += new SelectedCellsChangedEventHandler( customSelectedCellsChangedHandler );
                }
                else
                {
                    SelectedCellsChanged -= new SelectedCellsChangedEventHandler( customSelectedCellsChangedHandler );
                    SelectedCells.Clear();

                    Rows[rowStart].highlightBytes( colStart, colEnd );

                    for ( int i = colStart; i <= colEnd; ++i )
                        SelectedCells.Add( new DataGridCellInfo( Rows[rowStart], Columns[i] ) );

                    SelectedCellsChanged += new SelectedCellsChangedEventHandler( customSelectedCellsChangedHandler );
                }


            }
            else
            {
                // single cell selected, highlight
                Rows[rowStart].highlightBytes( SelectedCells[0].Column.DisplayIndex, SelectedCells[SelectedCells.Count - 1].Column.DisplayIndex );
            }
        }

        /// <summary>
        /// Sets up the row headers.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void loadingRow( object sender, DataGridRowEventArgs e )
        {
            uint i = ( fileByteOffset == null ) ? 0 : fileByteOffset();
            e.Row.Header = "0x" + Convert.ToString( i + ( e.Row.GetIndex() * 16 ), 16 );
        }

        /// <summary>
        /// Property change method.
        /// </summary>
        /// <param name="name"></param>
        protected void onPropertyChanged( string name )
        {
            PropertyChangedEventHandler handler = PropertyChanged;
            if ( handler != null )
            {
                handler( this, new PropertyChangedEventArgs( name ) );
            }
        }

        /// <summary>
        /// Returns <code>DataGridCell</code> given <code>DataGridCellInfo</code>.
        /// </summary>
        /// <param name="cellInfo"></param>
        /// <returns></returns>
        public DataGridCell getDataGridCell( DataGridCellInfo cellInfo )
        {
            var cellContent = cellInfo.Column.GetCellContent( cellInfo.Item );
            if ( cellContent != null )
                return (DataGridCell)cellContent.Parent;

            return null;
        }

        #endregion
    }
}
