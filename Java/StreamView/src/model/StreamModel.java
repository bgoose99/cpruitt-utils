package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/*******************************************************************************
 * Simple {@link IStreamModel}.
 ******************************************************************************/
public class StreamModel implements IStreamModel
{
    public static final int DEFAULT_BLOCK_SIZE = 256;
    public static final int MINIMUM_BLOCK_SIZE = 16;
    public static final int MAXIMUM_BLOCK_SIZE = 1024;

    private int blockSize;
    private FileInputStream input;
    private int blockCount;
    private int blockIndex;
    private ByteBuffer blockBytes;
    private long totalSize;

    /***************************************************************************
     * Constructor
     * 
     * @throws Exception
     **************************************************************************/
    public StreamModel() throws Exception
    {
        this( DEFAULT_BLOCK_SIZE );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param blockSize
     * @throws Exception
     **************************************************************************/
    public StreamModel( int blockSize ) throws Exception
    {
        this( blockSize, null );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param blockSize
     * @param file
     * @throws Exception
     **************************************************************************/
    public StreamModel( int blockSize, File file ) throws Exception
    {
        setInputFile( file, blockSize );
    }

    /***************************************************************************
     * Sets the block size for this model.
     * 
     * @param size
     * @throws Exception
     **************************************************************************/
    private void setBlockSize( int size ) throws Exception
    {
        if( size < MINIMUM_BLOCK_SIZE || size > MAXIMUM_BLOCK_SIZE )
        {
            throw new Exception( "Block size must be between "
                    + MINIMUM_BLOCK_SIZE + " and " + MAXIMUM_BLOCK_SIZE
                    + " bytes." );
        }
        blockSize = size;
        if( blockBytes != null )
            blockBytes.clear();
        blockBytes = ByteBuffer.allocate( blockSize );
    }

    /*
     * (non-Javadoc)
     * 
     * @see model.IStreamModel#getBlockSize()
     */
    @Override
    public int getBlockSize()
    {
        return blockSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see model.IStreamModel#getBlockCount()
     */
    @Override
    public int getBlockCount()
    {
        return blockCount;
    }

    /*
     * (non-Javadoc)
     * 
     * @see model.IStreamModel#getBlockIndex()
     */
    @Override
    public int getBlockIndex()
    {
        return blockIndex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see model.IStreamModel#getBlockBytes()
     */
    @Override
    public byte[] getBlockBytes()
    {
        return blockBytes.array();
    }

    /*
     * (non-Javadoc)
     * 
     * @see model.IStreamModel#getTotalSize()
     */
    @Override
    public long getTotalSize()
    {
        return totalSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see model.IStreamModel#readNextBlock()
     */
    @Override
    public void readNextBlock() throws IOException
    {
        if( blockIndex < blockCount )
            readBlock( blockIndex + 1 );
    }

    /*
     * (non-Javadoc)
     * 
     * @see model.IStreamModel#readPrevBlock()
     */
    @Override
    public void readPrevBlock() throws IOException
    {
        if( blockIndex > 1 )
            readBlock( blockIndex - 1 );
    }

    /*
     * (non-Javadoc)
     * 
     * @see model.IStreamModel#readBlock(int)
     */
    @Override
    public void readBlock( int index ) throws IOException
    {
        if( index > 0 && index <= blockCount && input != null )
        {
            int bytesToRead = ( index < blockCount ) ? blockSize
                    : (int)( totalSize % blockSize );

            // clear and resize byte buffer
            blockBytes.clear();
            blockBytes = ByteBuffer.allocate( bytesToRead );

            // read bytes
            input.getChannel().position( ( index - 1 ) * blockSize );
            int bytesRead = input.getChannel().read( blockBytes );

            // make sure we read what we expected to read
            if( bytesRead != bytesToRead )
                throw new IOException( "Expected to read " + bytesToRead
                        + " bytes. Read " + bytesRead + " bytes." );

            blockIndex = index;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see model.IStreamModel#setInputFile(java.io.File)
     */
    @Override
    public void setInputFile( File in ) throws Exception
    {
        setInputFile( in, blockSize );
    }

    /*
     * (non-Javadoc)
     * 
     * @see model.IStreamModel#setInputFile(java.io.File, int)
     */
    @Override
    public void setInputFile( File in, int blockSize ) throws Exception
    {
        setBlockSize( blockSize );

        if( input != null )
            input.close();

        if( in == null )
            input = null;
        else
            input = new FileInputStream( in );

        if( input != null )
        {
            totalSize = input.getChannel().size();
            blockCount = (int)( totalSize / blockSize )
                    + ( totalSize % blockSize == 0 ? 0 : 1 );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#finalize()
     */
    @Override
    public void finalize() throws Throwable
    {
        super.finalize();
        if( input != null )
            input.close();
    }
}
