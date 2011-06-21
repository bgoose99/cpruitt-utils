package ui;

import javautils.swing.FrameRunner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main extends FrameRunner
{
    /**
     * @param args
     */
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Main() );
    }

    @Override
    protected JFrame createFrame()
    {
        JFrame frame = new ProjectEulerFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        return frame;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }

}
