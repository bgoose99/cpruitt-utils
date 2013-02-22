package presenter;

import java.io.File;
import java.io.IOException;

import model.IStreamModel;

/*******************************************************************************
 * This is the interface between an object and an {@link IStreamModel}.
 ******************************************************************************/
public interface IStreamViewPresenter
{
    /***************************************************************************
     * Returns the current filename.
     * 
     * @return
     **************************************************************************/
    String getCurrentFilename();

    /***************************************************************************
     * Returns the total size, in bytes, of the current file.
     * 
     * @return
     **************************************************************************/
    long getCurrentFilesize();

    /***************************************************************************
     * Returns true if the current file has another block of data, false
     * otherwise.
     * 
     * @return
     **************************************************************************/
    boolean hasNext();

    /***************************************************************************
     * Returns true if the current file has a previous block of data, false
     * otherwise.
     * 
     * @return
     **************************************************************************/
    boolean hasPrev();

    /***************************************************************************
     * Returns the block size.
     * 
     * @return
     **************************************************************************/
    int getBlockSize();

    /***************************************************************************
     * Returns the number of blocks in the current file.
     * 
     * @return
     **************************************************************************/
    int getBlockCount();

    /***************************************************************************
     * Returns the current block number.
     * 
     * @return
     **************************************************************************/
    int getBlockIndex();

    /***************************************************************************
     * Returns the current block of data as an array of bytes.
     * 
     * @return
     **************************************************************************/
    byte[] getCurrentBlock();

    /***************************************************************************
     * Advances to the next block of data.
     * 
     * @throws IOException
     **************************************************************************/
    void gotoNextBlock() throws IOException;

    /***************************************************************************
     * Goes to the previous block of data.
     * 
     * @throws IOException
     **************************************************************************/
    void gotoPrevBlock() throws IOException;

    /***************************************************************************
     * Goes to the specified block of data.
     * 
     * @param blockIndex
     * @throws IOException
     **************************************************************************/
    void gotoBlock( int blockIndex ) throws IOException;

    /***************************************************************************
     * Sets the current file and goes to the first block of data.
     * 
     * @param f
     * @throws Exception
     **************************************************************************/
    void setInputFile( File f ) throws Exception;
}
