package model;

import java.io.File;
import java.util.List;

/*******************************************************************************
 * Represents an arbitrary {@link java.io.File} used as a source of binary data.
 * Provides all necessary navigation and editing capability to the underlying
 * source.
 ******************************************************************************/
public interface HexTableData
{
    /***************************************************************************
     * Sets the input file that contains the underlying data. Upon calling this
     * method, any existing data is cleared.
     * 
     * @param f
     **************************************************************************/
    public void setInputFile( File f );

    /***************************************************************************
     * Writes out data to the specified file.
     **************************************************************************/
    public void saveData( File f );

    /***************************************************************************
     * Returns the total size of the underlying data this class represents. E.g.
     * the size of the input stream.
     * 
     * @return
     **************************************************************************/
    public long getTotalSize();

    /***************************************************************************
     * Returns the block size this model uses.
     * 
     * @return
     **************************************************************************/
    public int getBlockSize();

    /***************************************************************************
     * Returns the total number of block in the underlying data.
     * 
     * @return
     **************************************************************************/
    public int getBlockCount();

    /***************************************************************************
     * Returns the number of bytes in the current block.
     * 
     * @return
     **************************************************************************/
    public int getCurrentBlockSize();

    /***************************************************************************
     * Returns the byte at the specified index in the current block.
     * 
     * @param index
     * @return
     * @throws Exception
     **************************************************************************/
    public Byte getByte( int index ) throws Exception;

    /***************************************************************************
     * Sets the byte at the specified index in the current block.
     * 
     * @param index
     * @param b
     * @throws IndexOutOfBoundsException
     **************************************************************************/
    public void setByte( int index, Byte b ) throws Exception;

    /***************************************************************************
     * Returns the list of bytes associated with the current block in this data
     * model.
     * 
     * @return
     **************************************************************************/
    public List<Byte> getCurrentBlock();

    /***************************************************************************
     * Returns a 'row' in the current block of this data model, represented as a
     * string.
     * 
     * @param row
     * @return
     **************************************************************************/
    public String getRowAsString( int row );

    /***************************************************************************
     * Deletes a number of bytes from the current block of data, starting at the
     * specified offset.
     * 
     * @param offset
     * @param numBytesToDelete
     **************************************************************************/
    public void deleteBytes( int offset, int numBytesToDelete );

    /***************************************************************************
     * Adds a number of bytes to the current block of data, starting at the
     * specified offset.
     * 
     * @param offset
     * @param numBytesToAdd
     **************************************************************************/
    public void addBytes( int offset, int numBytesToAdd );

    /***************************************************************************
     * Clears all data.
     **************************************************************************/
    public void clear();

    /***************************************************************************
     * Returns true if this model has another block of data beyond the current,
     * false otherwise.
     * 
     * @return
     **************************************************************************/
    public boolean hasNextBlock();

    /***************************************************************************
     * Returns true if this model has another block of data preceding the
     * current, false otherwise.
     * 
     * @return
     **************************************************************************/
    public boolean hasPreviousBlock();

    /***************************************************************************
     * Advances this data model to the next block of data, if it exists.
     **************************************************************************/
    public void gotoNextBlock();

    /***************************************************************************
     * Backs up this data model to the previous block of data, if it exists.
     **************************************************************************/
    public void gotoPreviousBlock();

    /***************************************************************************
     * Moves the current data model to the specified block of data, if it
     * exists.
     * 
     * @param blockNum
     * @throws IndexOutOfBoundsException
     **************************************************************************/
    public boolean gotoBlock( int blockNum );

    /***************************************************************************
     * Returns the index of the current block.
     * 
     * @return
     **************************************************************************/
    public int getCurrentBlockIndex();
}
