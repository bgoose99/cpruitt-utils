package javautils.hex;

import javautils.hex.IHexTableModel.HexTableViewMode;

/*******************************************************************************
 * Hex table interface.
 ******************************************************************************/
public interface IHexTable
{
    /***************************************************************************
     * Searches for the supplied key, starting at the given offset. NOTE: This
     * function will switch blocks if the search key is found in a block after
     * the current block.
     * 
     * @param offset
     *            Offset from whence to start the search.
     * @param searchKey
     *            Key to search for.
     * 
     * @return The offset, into the current block, where <code>searchKey</code>
     *         was found, or -1 if it was not found.
     **************************************************************************/
    public int find( int offset, byte searchKey );

    /***************************************************************************
     * Searches backwards for the supplied key, starting at the given offset.
     * NOTE: This function will switch blocks if the search key is found in a
     * block before the current block.
     * 
     * @param offset
     *            Offset from whence to start the search.
     * @param searchKey
     *            Key to search for.
     * 
     * @return The offset, into the current block, where <code>searchKey</code>
     *         was found, or -1 if it was not found.
     **************************************************************************/
    public int rfind( int offset, byte searchKey );

    /***************************************************************************
     * Goes to the specified offset, switching blocks if necessary.
     * 
     * @param offset
     **************************************************************************/
    public void gotoOffset( int offset );

    /***************************************************************************
     * Returns the {@link IHexTableModel} associated with this table.
     * 
     * @return
     **************************************************************************/
    public IHexTableModel getHexTableModel();

    /***************************************************************************
     * Adds bytes at the current selection, or at the beginning of the data if
     * no selection is made.
     * 
     * @param numBytesToAdd
     * @return <b>true</b> if the add was successful, <b>false</b> otherwise
     **************************************************************************/
    public boolean addBytes( int numBytesToAdd );

    /***************************************************************************
     * Deletes bytes at the current selection. If <code>numBytesToDelete</code>
     * is 1, and there are one or more selections, deletes the current selection
     * only.
     * 
     * @param numBytesToDelete
     * @return <b>true</b> if the remove was successful, <b>false</b> otherwise
     **************************************************************************/
    public boolean deleteBytes( int numBytesToDelete );

    /***************************************************************************
     * Sets the view mode for this table.
     * 
     * @param mode
     **************************************************************************/
    public void setViewMode( HexTableViewMode mode );
}
