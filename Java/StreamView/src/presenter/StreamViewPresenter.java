package presenter;

import java.io.File;
import java.io.IOException;

import model.IStreamModel;
import model.StreamModel;

/*******************************************************************************
 * Simple {@link IStreamViewPresenter}.
 ******************************************************************************/
public class StreamViewPresenter implements IStreamViewPresenter
{
    private IViewUpdater viewUpdater;
    private IStreamModel model;
    private File currentFile;

    /***************************************************************************
     * Constructor
     * 
     * @throws Exception
     **************************************************************************/
    public StreamViewPresenter() throws Exception
    {
        this( StreamModel.DEFAULT_BLOCK_SIZE );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param blockSize
     * @throws Exception
     **************************************************************************/
    public StreamViewPresenter( int blockSize ) throws Exception
    {
        this( blockSize, null );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param blockSize
     * @param inputFile
     * @throws Exception
     **************************************************************************/
    public StreamViewPresenter( int blockSize, File inputFile )
            throws Exception
    {
        this( blockSize, inputFile, null );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param blockSize
     * @param inputFile
     * @param viewUpdater
     * @throws Exception
     **************************************************************************/
    public StreamViewPresenter( int blockSize, File inputFile,
            IViewUpdater viewUpdater ) throws Exception
    {
        model = new StreamModel( blockSize );
        setInputFile( inputFile );
        this.viewUpdater = viewUpdater;
    }

    /*
     * (non-Javadoc)
     * 
     * @see presenter.IStreamViewPresenter#getCurrentFilename()
     */
    @Override
    public String getCurrentFilename()
    {
        return currentFile == null ? "N/A" : currentFile.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see presenter.IStreamViewPresenter#getCurrentFilesize()
     */
    @Override
    public long getCurrentFilesize()
    {
        return model.getTotalSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see presenter.IStreamViewPresenter#hasNext()
     */
    @Override
    public boolean hasNext()
    {
        return getBlockIndex() < getBlockCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see presenter.IStreamViewPresenter#hasPrev()
     */
    @Override
    public boolean hasPrev()
    {
        return getBlockIndex() > 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see presenter.IStreamViewPresenter#getBlockSize()
     */
    @Override
    public int getBlockSize()
    {
        return model.getBlockSize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see presenter.IStreamViewPresenter#getBlockCount()
     */
    @Override
    public int getBlockCount()
    {
        return model.getBlockCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see presenter.IStreamViewPresenter#getBlockIndex()
     */
    @Override
    public int getBlockIndex()
    {
        return model.getBlockIndex();
    }

    /*
     * (non-Javadoc)
     * 
     * @see presenter.IStreamViewPresenter#getCurrentBlock()
     */
    @Override
    public byte[] getCurrentBlock()
    {
        return model.getBlockBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see presenter.IStreamViewPresenter#gotoNextBlock()
     */
    @Override
    public void gotoNextBlock() throws IOException
    {
        model.readNextBlock();
        if( viewUpdater != null )
            viewUpdater.updateView();
    }

    /*
     * (non-Javadoc)
     * 
     * @see presenter.IStreamViewPresenter#gotoPrevBlock()
     */
    @Override
    public void gotoPrevBlock() throws IOException
    {
        model.readPrevBlock();
        if( viewUpdater != null )
            viewUpdater.updateView();
    }

    /*
     * (non-Javadoc)
     * 
     * @see presenter.IStreamViewPresenter#gotoBlock(int)
     */
    @Override
    public void gotoBlock( int blockIndex ) throws IOException
    {
        model.readBlock( blockIndex );
        if( viewUpdater != null )
            viewUpdater.updateView();
    }

    /*
     * (non-Javadoc)
     * 
     * @see presenter.IStreamViewPresenter#setInputFile(java.io.File)
     */
    @Override
    public void setInputFile( File f ) throws Exception
    {
        currentFile = f;
        model.setInputFile( currentFile );
    }

}
