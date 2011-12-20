package javautils.hex;

import java.io.File;

/*******************************************************************************
 * Hex table model interface. Any data model implementing this interface is
 * intended to split up its data in arbitrarily sized chunks, making the display
 * of the data much easier. These data chunks are designated blocks.
 ******************************************************************************/
public interface IHexTableModel
{
    /***************************************************************************
     * Returns the total number of bytes in this model.
     * 
     * @return
     **************************************************************************/
    public long getTotalBytes();

    /***************************************************************************
     * Returns the number of blocks in this model.
     * 
     * @return
     **************************************************************************/
    public int getBlockCount();

    /***************************************************************************
     * Returns the current block index.
     * 
     * @return
     **************************************************************************/
    public int getCurrentBlockIndex();

    /***************************************************************************
     * Returns the number of bytes in the current block.
     * 
     * @return
     **************************************************************************/
    public int getCurrentBlockSize();

    /***************************************************************************
     * Returns the maximum block size for this model.
     * 
     * @return
     **************************************************************************/
    public int getMaxBlockSize();

    /***************************************************************************
     * Searches for the supplied key, starting at the given offset.
     * 
     * @param offset
     *            Offset from whence to start the search
     * @param searchKey
     *            Key to search for.
     * @return The offset where <code>searchKey</code> was found, or -1 if it
     *         was not found.
     **************************************************************************/
    public int find( int offset, byte searchKey );

    /***************************************************************************
     * Searches backwards for the supplied key, starting at the given offset.
     * 
     * @param offset
     *            Offset from whence to start the search
     * @param searchKey
     *            Key to search for.
     * @return The offset where <code>searchKey</code> was found, or -1 if it
     *         was not found.
     **************************************************************************/
    public int rfind( int offset, byte searchKey );

    /***************************************************************************
     * Goes to the specified offset.
     * 
     * @param offset
     * @return The offset into the current block, or -1 if the given offset is
     *         not valid.
     **************************************************************************/
    public int gotoOffset( int offset );

    /***************************************************************************
     * Returns true if this model has another data block, false otherwise.
     * 
     * @return
     **************************************************************************/
    public boolean hasNext();

    /***************************************************************************
     * Returns true if this model has a previous data block, false otherwise.
     * 
     * @return
     **************************************************************************/
    public boolean hasPrevious();

    /***************************************************************************
     * Advances to the next data block.
     **************************************************************************/
    public void next();

    /***************************************************************************
     * Goes back to the previous data block.
     **************************************************************************/
    public void previous();

    /***************************************************************************
     * Goes to the supplied block index, if it exists.
     * 
     * @param block
     **************************************************************************/
    public boolean gotoBlock( int block );

    /***************************************************************************
     * Deletes a number of bytes from the underlying data, starting at the
     * specified offset in the current block.
     * 
     * @param offset
     * @param numBytesToDelete
     **************************************************************************/
    public void deleteBytes( int offset, int numBytesToDelete );

    /***************************************************************************
     * Adds a number of bytes to the underlying data, starting at the specified
     * offset in the current block.
     * 
     * @param offset
     * @param numBytesToAdd
     **************************************************************************/
    public void addBytes( int offset, int numBytesToAdd );

    /***************************************************************************
     * Reads data from the given file.
     * 
     * @param f
     * @throws Exception
     **************************************************************************/
    public void readData( File f ) throws Exception;

    /***************************************************************************
     * Writes data to the given file.
     * 
     * @param f
     * @throws Exception
     **************************************************************************/
    public void writeData( File f ) throws Exception;

    /***************************************************************************
     * Clears the data in this model.
     **************************************************************************/
    public void clear();

    /***************************************************************************
     * Sets up a blank data structure to be edited by the user.
     **************************************************************************/
    public void newData();
}
