package data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Vector;

import javautils.FileUtils;
import javautils.Utils;
import javautils.hex.HexUtils;
import javautils.hex.IHexTable;
import javautils.hex.IHexTableData;

import javax.swing.JOptionPane;

/*******************************************************************************
 * This class represents all the data in a hex table.
 * 
 * @see IHexTableData
 * @see IHexTable
 ******************************************************************************/
public class HexTableData implements IHexTableData
{
    public static final int BLOCK_SIZE = 512;
    public static final int ROW_SIZE = 16;

    private RandomAccessFile inputFile;
    private int currentBlockIndex;
    private int totalNumBlocks;
    private long totalSize;
    private List<Byte> currentBlock;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public HexTableData()
    {
        this( null );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param inputFile
     **************************************************************************/
    public HexTableData( File inputFile )
    {
        currentBlock = new Vector<Byte>();
        setInputFile( inputFile );
    }

    /***************************************************************************
     * Closes the current input file.
     **************************************************************************/
    private void closeInputFile()
    {
        if( inputFile != null )
        {
            try
            {
                inputFile.close();
            } catch( Exception e )
            {
            }
            inputFile = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#setInputFile(java.io.File)
     */
    @Override
    public void setInputFile( File f )
    {
        // clear any previous data
        clear();

        // init file/stream
        if( f != null )
        {
            // set up our stream
            try
            {
                inputFile = new RandomAccessFile( f, "rwd" );
                currentBlockIndex = 1;
                totalSize = inputFile.length();
                totalNumBlocks = (int)( totalSize / BLOCK_SIZE )
                        + ( totalSize % BLOCK_SIZE == 0 ? 0 : 1 );
            } catch( Exception e )
            {
                inputFile = null;
            }
        }

        if( inputFile == null )
        {
            currentBlockIndex = 0;
            totalNumBlocks = 0;
            totalSize = 0;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#saveData(java.io.File)
     */
    @Override
    public void saveData( File f )
    {
        FileOutputStream out = null;
        try
        {
            File tempOut = File.createTempFile( "hexEditorTemp", null );
            out = new FileOutputStream( tempOut );
            boolean done = false;
            int block = 1;
            while( !done )
            {
                if( block == currentBlockIndex )
                {
                    for( Byte b : currentBlock )
                    {
                        out.write( b.intValue() );
                    }
                } else
                {
                    long offset = ( block - 1 ) * BLOCK_SIZE;
                    inputFile.seek( offset );
                    for( int i = 0; i < BLOCK_SIZE; i++ )
                    {
                        int val = inputFile.read();
                        if( val == -1 )
                        {
                            done = true;
                            continue;
                        } else
                        {
                            out.write( val );
                        }
                    }
                }
                block++;
            }

            out.close();
            FileUtils.moveFile( tempOut, f );
            int blockIndex = currentBlockIndex;
            setInputFile( f );
            gotoBlock( blockIndex );
        } catch( Exception e )
        {
            JOptionPane.showMessageDialog( null,
                    "Error encountered while saving data:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE );

        } finally
        {
            try
            {
                if( out != null )
                    out.close();
            } catch( Exception x )
            {
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#getTotalSize()
     */
    @Override
    public long getTotalSize()
    {
        return totalSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#getBlockSize()
     */
    @Override
    public int getBlockSize()
    {
        return BLOCK_SIZE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#getBlockCount()
     */
    @Override
    public int getBlockCount()
    {
        return totalNumBlocks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#getCurrentBlockSize()
     */
    @Override
    public int getCurrentBlockSize()
    {
        return currentBlock.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#getByte(int)
     */
    @Override
    public Byte getByte( int index ) throws Exception
    {
        return currentBlock.get( index );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#setByte(int, java.lang.Byte)
     */
    @Override
    public void setByte( int index, Byte b ) throws Exception
    {
        currentBlock.set( index, b );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#getCurrentBlock()
     */
    @Override
    public List<Byte> getCurrentBlock()
    {
        return currentBlock;
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
        if( startIndex > currentBlock.size() )
        {
            return s;
        }

        int stopIndex = Math.min( startIndex + ROW_SIZE, currentBlock.size() );

        for( int i = startIndex; i < stopIndex; i++ )
        {
            Byte b = currentBlock.get( i );
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
        if( offset > currentBlock.size() )
        {
            return;
        } else
        {
            int upperLimit = Math.min( offset + numBytesToDelete,
                    currentBlock.size() );
            for( int i = offset; i < upperLimit; i++ )
            {
                currentBlock.remove( offset );
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
        if( offset > currentBlock.size() )
        {
            return;
        } else
        {
            for( int i = offset; i < ( offset + numBytesToAdd ); i++ )
            {
                currentBlock.add( i, new Byte( (byte)0 ) );
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
        currentBlock.clear();
        closeInputFile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#hasNextBlock()
     */
    @Override
    public boolean hasNextBlock()
    {
        return currentBlockIndex < totalNumBlocks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#hasPreviousBlock()
     */
    @Override
    public boolean hasPreviousBlock()
    {
        return currentBlockIndex > 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#gotoNextBlock()
     */
    @Override
    public void gotoNextBlock()
    {
        if( hasNextBlock() )
        {
            gotoBlock( currentBlockIndex + 1 );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#gotoPreviousBlock()
     */
    @Override
    public void gotoPreviousBlock()
    {
        if( hasPreviousBlock() )
        {
            gotoBlock( currentBlockIndex - 1 );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#gotoBlock(int)
     */
    @Override
    public boolean gotoBlock( int blockNum )
    {
        if( blockNum < 0 || blockNum > totalNumBlocks )
        {
            return false;
        }

        currentBlockIndex = blockNum;
        if( inputFile != null )
        {
            currentBlock.clear();
            long offset = ( blockNum - 1 ) * BLOCK_SIZE;
            try
            {
                inputFile.seek( offset );
                for( int i = 0; i < BLOCK_SIZE; i++ )
                {

                    int val = inputFile.read();
                    if( val == -1 )
                    {
                        return true;
                    } else
                    {
                        currentBlock.add( new Byte( (byte)val ) );
                    }
                }
            } catch( Exception e )
            {
                e.printStackTrace();
            }
            return true;
        } else
        {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.hex.IHexTableData#getCurrentBlockIndex()
     */
    @Override
    public int getCurrentBlockIndex()
    {
        return currentBlockIndex;
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
        closeInputFile();
    }
}
