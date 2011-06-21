package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Vector;

import javautils.hex.IHexTableData;
import javautils.io.ISerializer;
import data.HexTableData;

/***************************************************************************
 * Hex data serializer that operates on {@link IHexTableData}.
 **************************************************************************/
public class HexDataSerializer implements ISerializer<IHexTableData>
{
    private File file;

    /***************************************************************************
     * Constructor
     * 
     * @param f
     **************************************************************************/
    public HexDataSerializer( File f )
    {
        file = f;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.io.ISerializer#readItem()
     */
    @Override
    public IHexTableData readItem() throws Exception
    {
        FileInputStream stream = null;
        List<Byte> bytes = new Vector<Byte>();

        try
        {
            stream = new FileInputStream( file );
            boolean done = false;

            while( !done )
            {
                int i = stream.read();
                if( i == -1 )
                {
                    done = true;
                } else
                {
                    bytes.add( new Byte( (byte)i ) );
                }
            }
        } finally
        {
            if( stream != null )
            {
                stream.close();
            }
        }

        return new HexTableData( bytes );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.io.ISerializer#writeItem(java.lang.Object)
     */
    @Override
    public void writeItem( IHexTableData item ) throws Exception
    {
        FileOutputStream stream = null;

        try
        {
            stream = new FileOutputStream( file );

            for( Byte b : item.getBytes() )
            {
                stream.write( b.byteValue() );
            }
        } finally
        {
            if( stream != null )
            {
                stream.close();
            }
        }
    }
}
