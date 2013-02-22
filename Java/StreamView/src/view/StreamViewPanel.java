package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;

import presenter.IStreamViewPresenter;
import presenter.IViewUpdater;
import presenter.StreamViewPresenter;

/*******************************************************************************
 * Simple {@link IStreamView}.
 ******************************************************************************/
public class StreamViewPanel extends JPanel implements IStreamView
{
    private IStreamViewPresenter presenter;
    private IViewUpdater viewUpdater;
    private NavigatorPanel navigator;
    private ByteStreamView byteStreamView;

    /***************************************************************************
     * Constructor
     * 
     * @throws Exception
     **************************************************************************/
    public StreamViewPanel() throws Exception
    {
        this( 128 );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param blockSize
     * @throws Exception
     **************************************************************************/
    public StreamViewPanel( int blockSize ) throws Exception
    {
        viewUpdater = new ViewUpdater();
        presenter = new StreamViewPresenter( blockSize, null, viewUpdater );
        navigator = new NavigatorPanel( new PrevActionListener(),
                new NextActionListener(), new GotoActionListener() );
        byteStreamView = new ByteStreamView();

        setupPanel();

        updateNavigatorControls();
    }

    /***************************************************************************
     * Sets up this panel.
     **************************************************************************/
    private void setupPanel()
    {
        setLayout( new GridBagLayout() );
        add( navigator, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.01,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( byteStreamView, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.99,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
    }

    /***************************************************************************
     * Updates all navigator controls.
     **************************************************************************/
    private void updateNavigatorControls()
    {
        navigator.setPrevEnabled( presenter.hasPrev() );
        navigator.setNextEnabled( presenter.hasNext() );
        navigator.setGotoEnabled( true );
        navigator.setLabelText( "File: " + presenter.getCurrentFilename()
                + " | " + "Size: " + presenter.getCurrentFilesize()
                + " bytes | " + "Showing block " + presenter.getBlockIndex()
                + " of " + presenter.getBlockCount() );
        navigator.setGotoText( "" + presenter.getBlockIndex() );
        navigator
                .setProgress( (int)( ( ( presenter.getBlockIndex() * 1.0 ) / ( presenter
                        .getBlockCount() == 0 ? 1 : presenter.getBlockCount() ) ) * 100.0 ) );
    }

    /***************************************************************************
     * Updates the displayed byte stream.
     **************************************************************************/
    private void updateByteStreamView()
    {
        byteStreamView
                .setBytes( presenter.getCurrentBlock(), ( ( presenter
                        .getBlockIndex() - 1 ) * presenter.getBlockSize() ) );
    }

    /***************************************************************************
     * Updates the display.
     **************************************************************************/
    private void updateView()
    {
        updateNavigatorControls();
        updateByteStreamView();
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.IStreamView#nextPressed()
     */
    @Override
    public void nextPressed()
    {
        try
        {
            presenter.gotoNextBlock();
        } catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.IStreamView#prevPressed()
     */
    @Override
    public void prevPressed()
    {
        try
        {
            presenter.gotoPrevBlock();
        } catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.IStreamView#gotoPressed()
     */
    @Override
    public void gotoPressed()
    {
        int block = 0;
        try
        {
            block = Integer.parseInt( navigator.getGotoText() );
            presenter.gotoBlock( block );
        } catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.IStreamView#highlightSelection(int, int)
     */
    @Override
    public void highlightSelection( int bitOffset, int bitLength )
    {
        byteStreamView.highlightSelection( bitOffset, bitLength );
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.IStreamView#openFile(java.io.File)
     */
    @Override
    public void openFile( File f )
    {
        try
        {
            presenter.setInputFile( f );
            presenter.gotoBlock( 1 );
        } catch( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class ViewUpdater implements IViewUpdater
    {
        @Override
        public void updateView()
        {
            StreamViewPanel.this.updateView();
        }
    }

    private class PrevActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            StreamViewPanel.this.prevPressed();
        }
    }

    private class NextActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            StreamViewPanel.this.nextPressed();
        }
    }

    private class GotoActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            StreamViewPanel.this.gotoPressed();
        }
    }
}
