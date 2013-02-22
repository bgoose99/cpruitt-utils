package view;

/*******************************************************************************
 * Interface for showing and highlighting some bytes.
 ******************************************************************************/
public interface IByteStreamView
{
    /***************************************************************************
     * Sets the bytes to be displayed in this view.
     * 
     * @param bytes
     **************************************************************************/
    void setBytes( byte[] bytes );

    /***************************************************************************
     * Sets the bytes, and their offset in some stream, to be displayed in this
     * view.
     * 
     * @param bytes
     * @param offset
     **************************************************************************/
    void setBytes( byte[] bytes, long offset );

    /***************************************************************************
     * Highlights some bits in this view.
     * 
     * @param bitOffset
     * @param bitLength
     **************************************************************************/
    void highlightSelection( int bitOffset, int bitLength );
}
