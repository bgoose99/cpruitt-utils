package ui;

import javautils.game.GameFrame;

import javax.swing.JFrame;

public class Main
{
    /**
     * @param args
     */
    public static void main( String[] args )
    {
        GameFrame frame = new BlueFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
        frame.run();
    }

}
