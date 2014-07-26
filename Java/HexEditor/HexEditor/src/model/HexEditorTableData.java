package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Vector;

import javautils.Utils;
import javautils.hex.HexUtils;

import javax.swing.JOptionPane;

/*******************************************************************************
 * This class represents all the data in the hex table.
 * 
 * @see HexTableData
 ******************************************************************************/
public class HexEditorTableData implements HexTableData
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
    public HexEditorTableData()
    {
        this( null );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param inputFile
     **************************************************************************/
    public HexEditorTableData( File inputFile )
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
            currentBlockIndex = 0;
            totalNumBlocks = 0;
            totalSize = 0;
        }
    }

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
            Files.move( tempOut.toPath(), f.toPath(),
                    StandardCopyOption.REPLACE_EXISTING );
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

    @Override
    public long getTotalSize()
    {
        return totalSize;
    }

    @Override
    public int getBlockSize()
    {
        return BLOCK_SIZE;
    }

    @Override
    public int getBlockCount()
    {
        return totalNumBlocks;
    }

    @Override
    public int getCurrentBlockSize()
    {
        return currentBlock.size();
    }

    @Override
    public Byte getByte( int index ) throws Exception
    {
        return currentBlock.get( index );
    }

    @Override
    public void setByte( int index, Byte b ) throws Exception
    {
        currentBlock.set( index, b );
    }

    @Override
    public List<Byte> getCurrentBlock()
    {
        return currentBlock;
    }

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

    @Override
    public void clear()
    {
        currentBlock.clear();
        closeInputFile();
    }

    @Override
    public boolean hasNextBlock()
    {
        return currentBlockIndex < totalNumBlocks;
    }

    @Override
    public boolean hasPreviousBlock()
    {
        return currentBlockIndex > 1;
    }

    @Override
    public void gotoNextBlock()
    {
        if( hasNextBlock() )
        {
            gotoBlock( currentBlockIndex + 1 );
        }
    }

    @Override
    public void gotoPreviousBlock()
    {
        if( hasPreviousBlock() )
        {
            gotoBlock( currentBlockIndex - 1 );
        }
    }

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

    @Override
    public int getCurrentBlockIndex()
    {
        return currentBlockIndex;
    }

    @Override
    public void finalize() throws Throwable
    {
        super.finalize();
        closeInputFile();
    }
}
