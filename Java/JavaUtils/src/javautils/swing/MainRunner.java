package javautils.swing;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

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
            for( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() )
            {
                if( "Nimbus".equals( info.getName() ) )
                {
                    UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
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
