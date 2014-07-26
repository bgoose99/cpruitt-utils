package data;

import java.io.File;

import javautils.Utils;
import javautils.hex.HexUtils;
import javautils.hex.IHexTable;
import javautils.hex.IHexTableData;
import javautils.hex.IHexTableModel;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/*******************************************************************************
 * This is the table model that is used in a hex table.
 * 
 * @see IHexTableModel
 * @see AbstractTableModel
 * @see IHexTable
 ******************************************************************************/
public class HexTableModel extends AbstractTableModel implements IHexTableModel
{
    private IHexTableData data;
    private HexTableViewMode mode;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public HexTableModel()
    {
        super();

        data = new HexTableData();
        mode = HexTableViewMode.HEX;
    }

    /***************************************************************************
     * Returns the label associated with the supplied row index.
     * 
     * @param row
     * @return
     **************************************************************************/
    private String getRowLabel( int row )
    {
        int len = Long.toHexString( getTotalBytes() ).length();
        String s = Integer.toHexString(
                ( data.getCurrentBlockIndex() - 1 ) * HexTableData.BLOCK_SIZE
                        + row * HexTableData.ROW_SIZE ).toUpperCase();
        return "0x" + Utils.getPaddedString( s, len, '0', true );
    }

    /***************************************************************************
     * Returns the ASCII representation of the supplied row index.
     * 
     * @param row
     * @return
     **************************************************************************/
    private String getRowString( int row )
    {
        return data.getRowAsString( row );
    }

    /***************************************************************************
     * Returns the value of the given row/column index.
     * 
     * @param row
     * @param col
     * @return
     **************************************************************************/
    private String getValue( int row, int col )
    {
        int index = ( row * HexTableData.ROW_SIZE ) + col - 1;
        if( index < data.getCurrentBlockSize() )
        {
            String s = "";
            try
            {
                switch( mode )
                {
                case HEX:
                    s = HexUtils.HEX_BYTES[0xFF & data.getByte( index )];
                    break;
                case DECIMAL:
                    s = HexUtils.DEC_BYTES[0xFF & data.getByte( index )];
                    break;
                case OCTAL:
                    s = HexUtils.OCT_BYTES[0xFF & data.getByte( index )];
                    break;
                case BINARY:
                    s = HexUtils.BIN_BYTES[0xFF & data.getByte( index )];
                    break;
                }
            } catch( Exception e )
            {
            }
            return s;
        } else
        {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount()
    {
        return 18;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount()
    {
        if( data.getCurrentBlockSize() == 0 )
        {
            return 0;
        } else
        {
            return ( ( ( data.getCurrentBlockSize() - 1 ) / HexTableData.ROW_SIZE ) + 1 );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName( int column )
    {
        if( column == 0 )
        {
            return "";
        } else if( column == 17 )
        {
            return "ASCII String";
        } else
        {
            return Integer.toHexString( column - 1 ).toUpperCase();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt( int rowIndex, int columnIndex )
    {
        if( columnIndex == 0 )
        {
            return getRowLabel( rowIndex );
        } else if( columnIndex == 17 )
        {
            return getRowString( rowIndex );
        } else
        {
            return getValue( rowIndex, columnIndex );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
     * int, int)
     */
    @Override
    public void setValueAt( Object val, int rowIndex, int columnIndex )
    {
        if( columnIndex == 0 || columnIndex == 17 )
        {
            return;
        } else
        {
            int newVal = 0;

            try
            {
                newVal = HexUtils.parseHexString( ( val.toString().startsWith(
                        "0x" ) ? "" : "0x" )
                        + val.toString() );
                if( newVal < 0 || newVal > 255 )
                    throw new Exception();
            } catch( Exception e )
            {
                JOptionPane.showMessageDialog( null,
                        "Error converting " + val.toString()
                                + " to a valid hex/byte value.\n"
                                + "You must input a valid hex value.",
                        "Warning", JOptionPane.WARNING_MESSAGE );
                return;
            }

            int index = ( rowIndex * HexTableData.ROW_SIZE ) + columnIndex - 1;
            if( index < data.getCurrentBlockSize() )
            {
                try
                {
                    data.setByte( index, new Byte( (byte)newVal ) );
                } catch( Exception e )
                {
                    JOptionPane.showMessageDialog( null, "Error setting index "
                            + index + " to " + newVal + "." + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE );
                    return;
                }
            }
            fireTableCellUpdated( rowIndex, columnIndex );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#getTotalBytes()
     */
    @Override
    public long getTotalBytes()
    {
        return data.getTotalSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#getBlockCount()
     */
    @Override
    public int getBlockCount()
    {
        return data.getBlockCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#getCurrentBlockIndex()
     */
    @Override
    public int getCurrentBlockIndex()
    {
        return data.getCurrentBlockIndex();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#getCurrentBlockSize()
     */
    @Override
    public int getCurrentBlockSize()
    {
        return data.getCurrentBlockSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#getMaxBlockSize()
     */
    @Override
    public int getMaxBlockSize()
    {
        return HexTableData.BLOCK_SIZE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#find(int, byte)
     */
    @Override
    public int find( int offset, byte searchKey )
    {
        try
        {
            for( int i = offset; i < data.getBlockSize(); i++ )
            {
                if( data.getByte( i ).byteValue() == searchKey )
                {
                    return i;
                }
            }
        } catch( Exception e )
        {
        }
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#rfind(int, byte)
     */
    @Override
    public int rfind( int offset, byte searchKey )
    {
        try
        {
            for( int i = offset; i >= 0; i-- )
            {
                if( data.getByte( i ).byteValue() == searchKey )
                {
                    return i;
                }
            }
        } catch( Exception e )
        {
        }

        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#gotoOffset(int)
     */
    @Override
    public int gotoOffset( int offset )
    {
        int blockIndex = offset / HexTableData.BLOCK_SIZE + 1;
        data.gotoBlock( blockIndex );
        return offset % HexTableData.BLOCK_SIZE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#hasNext()
     */
    @Override
    public boolean hasNext()
    {
        return data.hasNextBlock();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#hasPrevious()
     */
    @Override
    public boolean hasPrevious()
    {
        return data.hasPreviousBlock();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#next()
     */
    @Override
    public void next()
    {
        data.gotoNextBlock();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#previous()
     */
    @Override
    public void previous()
    {
        data.gotoPreviousBlock();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#gotoBlock(int)
     */
    @Override
    public boolean gotoBlock( int block )
    {
        return data.gotoBlock( block );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#deleteBytes(int, int)
     */
    @Override
    public void deleteBytes( int offset, int numBytesToDelete )
    {
        data.deleteBytes( offset, numBytesToDelete );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#addBytes(int, int)
     */
    @Override
    public void addBytes( int offset, int numBytesToAdd )
    {
        data.addBytes( offset, numBytesToAdd );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#readData(java.io.File)
     */
    @Override
    public void readData( File f ) throws Exception
    {
        data.setInputFile( f );
        gotoBlock( 1 );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#writeData(java.io.File)
     */
    @Override
    public void writeData( File f ) throws Exception
    {
        data.saveData( f );
        gotoBlock( data.getCurrentBlockIndex() );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#clear()
     */
    @Override
    public void clear()
    {
        data.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#newData()
     */
    @Override
    public void newData()
    {
        data.clear();
        data.addBytes( 0, 1 );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.hex.IHexTableModel#setViewMode(javautils.hex.IHexTableModel
     * .HexTableViewMode)
     */
    @Override
    public void setViewMode( HexTableViewMode mode )
    {
        this.mode = mode;
    }
}
