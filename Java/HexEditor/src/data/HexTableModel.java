package data;

import io.HexDataSerializer;

import java.io.File;
import java.util.List;
import java.util.Vector;

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
    private static final int BLOCK_SIZE = 512;

    private IHexTableData data;

    private int currentBlockIndex;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public HexTableModel()
    {
        super();

        data = new HexTableData();
        currentBlockIndex = 0;
    }

    /***************************************************************************
     * Calculates the "row" into the actual data, using the row of the currently
     * displayed block of data.
     * 
     * @param rowIndex
     * @return
     **************************************************************************/
    private int getModifiedRow( int rowIndex )
    {
        return ( currentBlockIndex * ( BLOCK_SIZE / HexTableData.ROW_SIZE ) )
                + rowIndex;
    }

    /***************************************************************************
     * Returns the label associated with the supplied row index.
     * 
     * @param row
     * @return
     **************************************************************************/
    private String getRowLabel( int row )
    {
        int len = Integer.toHexString( getTotalBytes() ).length();
        String s = Integer.toHexString(
                currentBlockIndex * BLOCK_SIZE + row * HexTableData.ROW_SIZE )
                .toUpperCase();
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
        return ( data.getSize() == 0 ? "" : data
                .getRowAsString( getModifiedRow( row ) ) );
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
        int index = ( getModifiedRow( row ) * HexTableData.ROW_SIZE ) + col - 1;
        if( index < data.getSize() )
        {
            return HexUtils.BYTES[HexUtils.byteToUnsignedInt( data
                    .getByte( index ) )];
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
        if( data.getSize() == 0 )
        {
            return 0;
        } else
        {
            int sizeLeft = data.getSize() - currentBlockIndex * BLOCK_SIZE;
            if( sizeLeft > BLOCK_SIZE )
            {
                return ( BLOCK_SIZE / HexTableData.ROW_SIZE );
            } else
            {
                return ( ( ( sizeLeft - 1 ) / HexTableData.ROW_SIZE ) + 1 );
            }
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

            int index = ( getModifiedRow( rowIndex ) * HexTableData.ROW_SIZE )
                    + columnIndex - 1;
            if( index < data.getSize() )
            {
                data.setByte( index, new Byte( (byte)newVal ) );
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
    public int getTotalBytes()
    {
        return data.getSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#getBlockCount()
     */
    @Override
    public int getBlockCount()
    {
        return ( data.getSize() < 1 ? 0
                : ( ( data.getSize() - 1 ) / BLOCK_SIZE ) ) + 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#getCurrentBlockIndex()
     */
    @Override
    public int getCurrentBlockIndex()
    {
        return currentBlockIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#getCurrentBlockSize()
     */
    @Override
    public int getCurrentBlockSize()
    {
        return Math.min( BLOCK_SIZE, data.getSize()
                - ( currentBlockIndex * BLOCK_SIZE ) );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#getMaxBlockSize()
     */
    @Override
    public int getMaxBlockSize()
    {
        return BLOCK_SIZE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#find(int, byte)
     */
    @Override
    public int find( int offset, byte searchKey )
    {
        for( int i = offset; i < data.getSize(); i++ )
        {
            if( data.getByte( i ).byteValue() == searchKey )
            {
                // update current block index
                currentBlockIndex = i / BLOCK_SIZE;
                return i;
            }
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
        for( int i = offset; i >= 0; i-- )
        {
            if( data.getByte( i ).byteValue() == searchKey )
            {
                // update current block index
                currentBlockIndex = i / BLOCK_SIZE;
                return i;
            }
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
        currentBlockIndex = offset / BLOCK_SIZE;
        return offset % BLOCK_SIZE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#hasNext()
     */
    @Override
    public boolean hasNext()
    {
        return data.getSize() > ( ( currentBlockIndex + 1 ) * BLOCK_SIZE );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#hasPrevious()
     */
    @Override
    public boolean hasPrevious()
    {
        return currentBlockIndex > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#next()
     */
    @Override
    public void next()
    {
        if( hasNext() )
        {
            currentBlockIndex++;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#previous()
     */
    @Override
    public void previous()
    {
        if( hasPrevious() )
        {
            currentBlockIndex--;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#gotoBlock(int)
     */
    @Override
    public boolean gotoBlock( int block )
    {
        if( block >= 0 && ( block * BLOCK_SIZE ) < data.getSize() )
        {
            currentBlockIndex = block;
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#deleteBytes(int, int)
     */
    @Override
    public void deleteBytes( int offset, int numBytesToDelete )
    {
        data.deleteBytes( ( currentBlockIndex * BLOCK_SIZE ) + offset,
                numBytesToDelete );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#addBytes(int, int)
     */
    @Override
    public void addBytes( int offset, int numBytesToAdd )
    {
        data.addBytes( ( currentBlockIndex * BLOCK_SIZE ) + offset,
                numBytesToAdd );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#readData(java.io.File)
     */
    @Override
    public void readData( File f ) throws Exception
    {
        HexDataSerializer serializer = new HexDataSerializer( f );
        data = serializer.readItem();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableModel#writeData(java.io.File)
     */
    @Override
    public void writeData( File f ) throws Exception
    {
        HexDataSerializer serializer = new HexDataSerializer( f );
        serializer.writeItem( data );
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
        currentBlockIndex = 0;
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
        currentBlockIndex = 0;
        List<Byte> bytes = new Vector<Byte>();
        bytes.add( new Byte( (byte)0 ) );
        data.setBytes( bytes );
    }
}
