package model;

import java.io.File;

import javautils.Utils;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/*******************************************************************************
 * This table model is used in the main hex table.
 * 
 * @see HexTableModel
 ******************************************************************************/
public class HexEditorTableModel extends AbstractTableModel implements
        HexTableModel
{
    private HexTableData data;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public HexEditorTableModel()
    {
        super();
        data = new HexEditorTableData();
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
                0xFF & ( data.getCurrentBlockIndex() - 1 )
                        * HexEditorTableData.BLOCK_SIZE + row
                        * HexEditorTableData.ROW_SIZE ).toUpperCase();
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
        int index = ( row * HexEditorTableData.ROW_SIZE ) + col - 1;
        if( index < data.getCurrentBlockSize() )
        {
            String s = "";
            try
            {
                s = String
                        .format(
                                "%2s",
                                Integer.toHexString( 0xFF & data
                                        .getByte( index ) ) )
                        .replace( ' ', '0' ).toUpperCase();
            } catch( Exception e )
            {
            }
            return s;
        }
        return null;
    }

    @Override
    public int getColumnCount()
    {
        return 18;
    }

    @Override
    public int getRowCount()
    {
        if( data.getCurrentBlockSize() == 0 )
        {
            return 0;
        } else
        {
            return ( ( ( data.getCurrentBlockSize() - 1 ) / HexEditorTableData.ROW_SIZE ) + 1 );
        }
    }

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

            int index = ( rowIndex * HexEditorTableData.ROW_SIZE )
                    + columnIndex - 1;
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

    @Override
    public long getTotalBytes()
    {
        return data.getTotalSize();
    }

    @Override
    public int getBlockCount()
    {
        return data.getBlockCount();
    }

    @Override
    public int getCurrentBlockIndex()
    {
        return data.getCurrentBlockIndex();
    }

    @Override
    public int getCurrentBlockSize()
    {
        return data.getCurrentBlockSize();
    }

    @Override
    public int getMaxBlockSize()
    {
        return HexEditorTableData.BLOCK_SIZE;
    }

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

    @Override
    public int gotoOffset( int offset )
    {
        int blockIndex = offset / HexEditorTableData.BLOCK_SIZE + 1;
        data.gotoBlock( blockIndex );
        return offset % HexEditorTableData.BLOCK_SIZE;
    }

    @Override
    public boolean hasNext()
    {
        return data.hasNextBlock();
    }

    @Override
    public boolean hasPrevious()
    {
        return data.hasPreviousBlock();
    }

    @Override
    public void next()
    {
        data.gotoNextBlock();
    }

    @Override
    public void previous()
    {
        data.gotoPreviousBlock();
    }

    @Override
    public boolean gotoBlock( int block )
    {
        return data.gotoBlock( block );
    }

    @Override
    public void deleteBytes( int offset, int numBytesToDelete )
    {
        data.deleteBytes( offset, numBytesToDelete );
    }

    @Override
    public void addBytes( int offset, int numBytesToAdd )
    {
        data.addBytes( offset, numBytesToAdd );
    }

    @Override
    public void readData( File f ) throws Exception
    {
        data.setInputFile( f );
        gotoBlock( 1 );
    }

    @Override
    public void writeData( File f ) throws Exception
    {
        data.saveData( f );
        gotoBlock( data.getCurrentBlockIndex() );
    }

    @Override
    public void clear()
    {
        data.clear();
    }

    @Override
    public void newData()
    {
        data.clear();
        data.addBytes( 0, 1 );
    }

    @Override
    public byte getByteAt( int offset ) throws Exception
    {
        return data.getByte( offset );
    }

}
