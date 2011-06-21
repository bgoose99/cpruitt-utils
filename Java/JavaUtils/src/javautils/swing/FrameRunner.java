package javautils.swing;

import javax.swing.JFrame;

/*******************************************************************************
 * This class handles common initialization for applications.
 * 
 * @author Joseph Gordon
 ******************************************************************************/
public abstract class FrameRunner extends MainRunner
{
    private JFrame frame;

    /*
     * (non-Javadoc)
     * 
     * @see javautils.swing.MainRunner#createAndShowGui()
     */
    @Override
    protected void createAndShowGui()
    {
        frame = createFrame();

        // ---------------------------------------------------------------------
        // Validate frames that have preset sizes. Pack frames that have
        // useful preferred size info, e.g. from their layout.
        // ---------------------------------------------------------------------
        if( validate() )
        {
            frame.validate();
        } else
        {
            frame.pack();
        }

        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }

    /***************************************************************************
     * Returns the frame associated with this object.
     * 
     * @return
     **************************************************************************/
    public JFrame getFrame()
    {
        return frame;
    }

    /***************************************************************************
     * Creates and returns the {@link JFrame} associated with this object.
     * 
     * @return
     **************************************************************************/
    protected abstract JFrame createFrame();

    /***************************************************************************
     * Returns <code>true</code> if this object has a preset size,
     * <code>false</code> if it should be packed.
     * 
     * @return
     **************************************************************************/
    protected abstract boolean validate();
}
