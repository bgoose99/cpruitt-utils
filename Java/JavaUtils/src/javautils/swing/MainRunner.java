package javautils.swing;

import javax.swing.UIManager;

/*******************************************************************************
 * Simple class that handles common swing application initialization.
 * 
 * @author Joseph Gordon
 ******************************************************************************/
public abstract class MainRunner implements Runnable
{
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        // set up the Nimbus L&F
        try
        {
            UIManager
                    .setLookAndFeel( "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel" );
        } catch( Exception e )
        {
            e.printStackTrace();
            System.out
                    .println( "Could not load Nimbus L&F, going on without it." );
        }

        createAndShowGui();
    }

    /***************************************************************************
     * Creates and shows the UI
     **************************************************************************/
    protected abstract void createAndShowGui();
}
