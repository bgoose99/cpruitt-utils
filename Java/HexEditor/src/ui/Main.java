package ui;

import javautils.swing.FrameRunner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Main extends FrameRunner
{
    /***************************************************************************
     * Program entry point.
     * 
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Main() );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.swing.FrameRunner#createFrame()
     */
    @Override
    protected JFrame createFrame()
    {
        JFrame.setDefaultLookAndFeelDecorated( true );
        JFrame frame = new HexEditorFrame();
        return frame;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.swing.FrameRunner#validate()
     */
    @Override
    protected boolean validate()
    {
        return true;
    }
}
