package view;

import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javautils.swing.JHighlightedLabel;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import model.HexEditorTableModel;
import model.HexTable;
import model.HexTableModel;

/*******************************************************************************
 * Main {@link HexTable} of the application.
 ******************************************************************************/
public class HexEditorTable extends JTable implements HexTable
{
    public final String EDIT = "tableEdit";
    public final String SELECTION = "tableSelection";

    private HexEditorTableModel tableModel;
    private DefaultTableCellRenderer standardRenderer;
    private DefaultTableCellRenderer stringRenderer;

    private int selectStart;
    private int selectEnd;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public HexEditorTable()
    {
        super();
        clearSelection();

        tableModel = new HexEditorTableModel();
        tableModel.addTableModelListener( new HexTableListener() );
        setModel( tableModel );
        setFillsViewportHeight( false );
        setCellSelectionEnabled( true );
        setShowGrid( true );
        setupRenderers();
        setupColumns();
        getTableHeader().setResizingAllowed( false );
        getTableHeader().setReorderingAllowed( false );
        addKeyListener( new HexTableKeyListener() );
        updateUI();
    }

    /***************************************************************************
     * Sets up the renderers for this table.
     **************************************************************************/
    private void setupRenderers()
    {
        // renderer for most of the table
        standardRenderer = new DefaultTableCellRenderer()
        {
            @Override
            public Component getTableCellRendererComponent( JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column )
            {
                super.getTableCellRendererComponent( table, value, isSelected,
                        hasFocus, row, column );
                setFont( new Font( "Courier New", Font.PLAIN, 12 ) );
                setHorizontalAlignment( SwingConstants.CENTER );
                return this;
            }
        };

        // renderer for the ASCII portion of the table
        stringRenderer = new DefaultTableCellRenderer()
        {
            private JHighlightedLabel label = new JHighlightedLabel( new Font(
                    "Monospaced", Font.PLAIN, 12 ) );

            @Override
            public Component getTableCellRendererComponent( JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column )
            {
                label.setText( (String)value );
                label.setHorizontalAlignment( SwingConstants.LEFT );

                if( selectStart >= 0 )
                {
                    int min = Math.min( selectStart, selectEnd );
                    int max = Math.max( selectStart, selectEnd );
                    if( min / 16 < row && max / 16 > row )
                    {
                        label.setHighlight( 0, 16 );
                    } else if( min / 16 == row )
                    {
                        int off = min - ( row * 16 );
                        label.setHighlight( off, max - min + 1 );
                    } else if( max / 16 == row )
                    {
                        label.setHighlight( 0, max - ( row * 16 ) + 1 );
                    } else
                    {
                        label.clearHighlight();
                    }
                } else
                {
                    label.clearHighlight();
                }

                return label;
            }
        };
    }

    /***************************************************************************
     * Sets up the columns for this table.
     **************************************************************************/
    private void setupColumns()
    {
        for( int i = 0; i < getColumnCount(); i++ )
        {
            TableColumn c = getColumnModel().getColumn( i );
            if( i == 0 )
            {
                c.setCellRenderer( standardRenderer );
                c.setPreferredWidth( 50 );
            } else if( i == 17 )
            {
                c.setCellRenderer( stringRenderer );
                c.setPreferredWidth( 120 );
            } else
            {
                c.setCellRenderer( standardRenderer );
                c.setPreferredWidth( 20 );
            }
        }
    }

    /***************************************************************************
     * Calculate the offset into the data model's byte data for the specified
     * row and column.
     * 
     * @param row
     * @param column
     * @return
     **************************************************************************/
    private int calculateByteOffset( int row, int column )
    {
        if( row < 0 || row > getRowCount() || column < 1 || column > 16 )
        {
            return -1;
        }
        int offset = row * 16 + column - 1;
        return ( offset >= 0 && offset < tableModel.getCurrentBlockSize() ) ? offset
                : -1;
    }

    /***************************************************************************
     * Calculates the closest valid column to the one specified by row and
     * column. This allows the user to obtain a valid selection when dragging
     * onto the non-data columns (byte location and string representation), as
     * well as any empty cells that may exist at the end of a stream.
     * 
     * @param row
     * @param column
     * @return
     **************************************************************************/
    private int calculateClosestValidColumn( int row, int column )
    {
        if( column < 1 )
        {
            return 1;
        }

        if( row == getRowCount() - 1 )
        {
            // last row
            int i = tableModel.getCurrentBlockSize() % 16;
            if( i == 0 )
            {
                // very last column of last row
                i = 16;
            }
            return Math.min( column, i );
        }

        return Math.min( column, getColumnCount() - 2 );
    }

    /***************************************************************************
     * Sets a single selection at the specified byte offset.
     * 
     * @param byteOffset
     **************************************************************************/
    public void setSelection( int byteOffset )
    {
        if( byteOffset < 0 || byteOffset >= tableModel.getCurrentBlockSize() )
        {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        int row = (int)byteOffset / 16;
        int col = (int)byteOffset % 16 + 1;
        changeSelection( row, col, false, false );
    }

    @Override
    public boolean isCellSelected( int row, int column )
    {
        int offset = calculateByteOffset( row, column );
        if( offset == -1 )
        {
            return false;
        }
        int min = Math.min( selectStart, selectEnd );
        int max = Math.max( selectStart, selectEnd );
        return offset >= min && offset <= max;
    }

    @Override
    public boolean isCellEditable( int row, int column )
    {
        if( column == 0 || column == 17 )
        {
            return false;
        } else
        {
            return true;
        }
    }

    @Override
    public void changeSelection( int rowIndex, int columnIndex, boolean toggle,
            boolean extend )
    {
        columnIndex = calculateClosestValidColumn( rowIndex, columnIndex );

        selectEnd = calculateByteOffset( rowIndex, columnIndex );
        if( !extend )
        {
            selectStart = selectEnd;
        }

        firePropertyChange( SELECTION, false, true );
        repaint();
    }

    @Override
    public void clearSelection()
    {
        selectStart = -1;
        selectEnd = -1;
    }

    @Override
    public int find( int offset, byte searchKey )
    {
        int rval = tableModel.find( offset, searchKey );
        setSelection( rval % tableModel.getMaxBlockSize() );
        return rval;
    }

    @Override
    public int rfind( int offset, byte searchKey )
    {
        int rval = tableModel.rfind( offset, searchKey );
        setSelection( rval % tableModel.getMaxBlockSize() );
        return rval;
    }

    @Override
    public void gotoOffset( int offset )
    {
        setSelection( tableModel.gotoOffset( offset ) );
    }

    @Override
    public HexTableModel getHexTableModel()
    {
        return tableModel;
    }

    @Override
    public boolean addBytes( int numBytesToAdd )
    {
        if( selectStart != selectEnd )
        {
            JOptionPane.showMessageDialog( null, "Ambiguous command. "
                    + "Please select a single cell before trying to add.",
                    "Warning", JOptionPane.WARNING_MESSAGE );
            return false;
        } else if( selectStart == -1 )
        {
            tableModel.addBytes( 0, numBytesToAdd );
            return true;
        } else
        {
            tableModel.addBytes( selectStart, numBytesToAdd );
            return true;
        }
    }

    @Override
    public boolean deleteBytes( int numBytesToDelete )
    {
        if( selectStart == -1 )
        {
            JOptionPane.showMessageDialog( null,
                    "You must select a cell or range of cells\n"
                            + "you wish to delete.", "Warning",
                    JOptionPane.WARNING_MESSAGE );
            return false;
        } else if( numBytesToDelete == 1 )
        {
            tableModel.deleteBytes( Math.min( selectStart, selectEnd ),
                    ( Math.abs( selectStart - selectEnd ) + 1 ) );
            clearSelection();
            return true;
        } else
        {
            tableModel.deleteBytes( Math.min( selectStart, selectEnd ),
                    numBytesToDelete );
            clearSelection();
            return true;
        }
    }

    @Override
    public byte getFirstSelectedByte()
    {
        byte b = 0;
        try
        {
            b = tableModel.getByteAt( selectStart );
        } catch( Exception e )
        {
        }
        return b;
    }

    @Override
    public int getFirstSelectedOffset()
    {
        return ( ( tableModel.getCurrentBlockIndex() - 1 ) * tableModel
                .getCurrentBlockSize() ) + selectStart;
    }

    /***************************************************************************
     * Generic table listener to update the UI when a table change is made.
     **************************************************************************/
    private class HexTableListener implements TableModelListener
    {
        @Override
        public void tableChanged( TableModelEvent e )
        {
            repaint();
            firePropertyChange( EDIT, false, true );
        }
    }

    /***************************************************************************
     * Key listener for updating selections based on arrow keys.
     **************************************************************************/
    private class HexTableKeyListener extends KeyAdapter
    {
        @Override
        public void keyPressed( KeyEvent e )
        {
            int code = e.getKeyCode();
            int lowestSelection = -1;

            switch( code )
            {
            case KeyEvent.VK_LEFT:
                // move selection left
                lowestSelection = Math.min( selectStart, selectEnd );
                if( lowestSelection >= 0 )
                {
                    setSelection( lowestSelection - 1 );
                }
                e.consume();
                break;
            case KeyEvent.VK_UP:
                // move selection up
                lowestSelection = Math.min( selectStart, selectEnd );
                if( lowestSelection >= 0 )
                {
                    setSelection( lowestSelection - 16 );
                }
                e.consume();
                break;
            case KeyEvent.VK_RIGHT:
                // move selection right
                lowestSelection = Math.min( selectStart, selectEnd );
                if( lowestSelection >= 0 )
                {
                    setSelection( lowestSelection + 1 );
                }
                e.consume();
                break;
            case KeyEvent.VK_DOWN:
                // move selection down
                lowestSelection = Math.min( selectStart, selectEnd );
                if( lowestSelection >= 0 )
                {
                    setSelection( lowestSelection + 16 );
                }
                e.consume();
                break;
            }
        }
    }
}
