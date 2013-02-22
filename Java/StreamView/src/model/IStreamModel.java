package model;

import java.io.File;
import java.io.IOException;

/*******************************************************************************
 * This model represents a file stream that is read and presented in blocks of
 * bytes.
 ******************************************************************************/
public interface IStreamModel
{
    /***************************************************************************
     * Returns the block size of this model.
     * 
     * @return
     **************************************************************************/
    int getBlockSize();

    /***************************************************************************
     * Returns the number of blocks in this model.
     * 
     * @return
     **************************************************************************/
    int getBlockCount();

    /***************************************************************************
     * Returns the current block index.
     * 
     * @return
     **************************************************************************/
    int getBlockIndex();

    /***************************************************************************
     * Returns the current block as an array of bytes.
     * 
     * @return
     **************************************************************************/
    byte[] getBlockBytes();

    /***************************************************************************
     * Returns the total size of this model.
     * 
     * @return
     **************************************************************************/
    long getTotalSize();

    /***************************************************************************
     * Reads the next block of data from the model.
     * 
     * @throws IOException
     **************************************************************************/
    void readNextBlock() throws IOException;

    /***************************************************************************
     * Reads the previous block of data from the model.
     * 
     * @throws IOException
     **************************************************************************/
    void readPrevBlock() throws IOException;

    /***************************************************************************
     * Reads the specified block of data from the model.
     * 
     * @param index
     * @throws IOException
     **************************************************************************/
    void readBlock( int index ) throws IOException;

    /***************************************************************************
     * Sets the input file to use as the source of data for this model.
     * 
     * @param in
     * @throws Exception
     **************************************************************************/
    void setInputFile( File in ) throws Exception;

    /***************************************************************************
     * Sets the input file and block size to use as the source of data for this
     * model.
     * 
     * @param in
     * @param blockSize
     * @throws Exception
     **************************************************************************/
    void setInputFile( File in, int blockSize ) throws Exception;
}
