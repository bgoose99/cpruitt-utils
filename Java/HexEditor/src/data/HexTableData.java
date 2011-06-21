package data;

import java.util.List;
import java.util.Vector;

import javautils.Utils;
import javautils.hex.HexUtils;
import javautils.hex.IHexTable;
import javautils.hex.IHexTableData;

/*******************************************************************************
 * This class represents all the data in a hex table.
 * 
 * @see IHexTableData
 * @see IHexTable
 ******************************************************************************/
public class HexTableData implements IHexTableData
{
    public static final int ROW_SIZE = 16;

    private List<Byte> bytes;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public HexTableData()
    {
        bytes = new Vector<Byte>();
    }

    /***************************************************************************
     * Constructor
     * 
     * @param bytes
     **************************************************************************/
    public HexTableData( List<Byte> bytes )
    {
        this();
        setBytes( bytes );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#getSize()
     */
    @Override
    public int getSize()
    {
        return bytes.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#getByte(int)
     */
    @Override
    public Byte getByte( int index ) throws IndexOutOfBoundsException
    {
        return bytes.get( index );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#setByte(int, java.lang.Byte)
     */
    @Override
    public void setByte( int index, Byte b ) throws IndexOutOfBoundsException
    {
        bytes.set( index, b );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#getBytes()
     */
    @Override
    public List<Byte> getBytes()
    {
        return bytes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#setBytes(java.util.List)
     */
    @Override
    public void setBytes( List<Byte> bytes )
    {
        this.bytes.clear();
        this.bytes = bytes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#getRowAsString(int)
     */
    @Override
    public String getRowAsString( int row )
    {
        String s = "";

        // calculate the indices
        int startIndex = row * ROW_SIZE;
        if( startIndex > bytes.size() )
        {
            return s;
        }

        int stopIndex = Math.min( startIndex + ROW_SIZE, bytes.size() );

        for( int i = startIndex; i < stopIndex; i++ )
        {
            Byte b = bytes.get( i );
            if( Utils.isPrintableChar( HexUtils.byteToUnsignedInt( b ) ) )
            {
                s += (char)b.byteValue();
            } else
            {
                s += ' ';
            }
        }

        return s;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#deleteBytes(int, int)
     */
    @Override
    public void deleteBytes( int offset, int numBytesToDelete )
    {
        if( offset > bytes.size() )
        {
            return;
        } else
        {
            int upperLimit = Math.min( offset + numBytesToDelete, bytes.size() );
            for( int i = offset; i < upperLimit; i++ )
            {
                bytes.remove( offset );
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#addBytes(int, int)
     */
    @Override
    public void addBytes( int offset, int numBytesToAdd )
    {
        if( offset > bytes.size() )
        {
            return;
        } else
        {
            for( int i = offset; i < ( offset + numBytesToAdd ); i++ )
            {
                bytes.add( i, new Byte( (byte)0 ) );
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#clear()
     */
    @Override
    public void clear()
    {
        bytes.clear();
    }
}
