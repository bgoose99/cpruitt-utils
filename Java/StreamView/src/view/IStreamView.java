package view;

import java.io.File;

/*******************************************************************************
 * Simple interface for navigating a file stream.
 ******************************************************************************/
public interface IStreamView
{
    /***************************************************************************
     * Displays the next block of data.
     **************************************************************************/
    void nextPressed();

    /***************************************************************************
     * Displays the previous block of data.
     **************************************************************************/
    void prevPressed();

    /***************************************************************************
     * Displays an individual block of data.
     **************************************************************************/
    void gotoPressed();

    /***************************************************************************
     * Highlights a selection in the block of data.
     * 
     * @param bitOffset
     * @param bitLength
     **************************************************************************/
    void highlightSelection( int bitOffset, int bitLength );

    /***************************************************************************
     * Sets the input file for the underlying data.
     * 
     * @param f
     **************************************************************************/
    void openFile( File f );
}
