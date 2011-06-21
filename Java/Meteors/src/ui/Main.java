package ui;

import javax.swing.JFrame;

public class Main
{
    public static void main( String[] args )
    {
        GameFrame frame = new GameFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
        frame.run();
    }
}
