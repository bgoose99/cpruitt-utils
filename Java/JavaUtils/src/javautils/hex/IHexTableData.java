package javautils.hex;

import java.util.List;

/*******************************************************************************
 * Generic hex table data interface.
 ******************************************************************************/
public interface IHexTableData
{
    /***************************************************************************
     * Returns the number of bytes in this data model.
     * 
     * @return
     **************************************************************************/
    public int getSize();

    /***************************************************************************
     * Returns the byte at the specified index.
     * 
     * @param index
     * @return
     * @throws IndexOutOfBoundsException
     **************************************************************************/
    public Byte getByte( int index ) throws IndexOutOfBoundsException;

    /***************************************************************************
     * Sets the byte at the specified index.
     * 
     * @param index
     * @param b
     * @throws IndexOutOfBoundsException
     **************************************************************************/
    public void setByte( int index, Byte b ) throws IndexOutOfBoundsException;

    /***************************************************************************
     * Returns the list of bytes associated with this data model.
     * 
     * @return
     **************************************************************************/
    public List<Byte> getBytes();

    /***************************************************************************
     * Sets the list of bytes to be used with this data model.
     * 
     * @param bytes
     **************************************************************************/
    public void setBytes( List<Byte> bytes );

    /***************************************************************************
     * Returns a 'row' of this data model, represented as a string.
     * 
     * @param row
     * @return
     **************************************************************************/
    public String getRowAsString( int row );

    /***************************************************************************
     * Deletes a number of bytes from the underlying data, starting at the
     * specified offset.
     * 
     * @param offset
     * @param numBytesToDelete
     **************************************************************************/
    public void deleteBytes( int offset, int numBytesToDelete );

    /***************************************************************************
     * Adds a number of bytes to the underlying data, starting at the specified
     * offset.
     * 
     * @param offset
     * @param numBytesToAdd
     **************************************************************************/
    public void addBytes( int offset, int numBytesToAdd );

    /***************************************************************************
     * Clears all data.
     **************************************************************************/
    public void clear();
}
