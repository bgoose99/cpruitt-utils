package javautils.swing;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javautils.IconManager;
import javautils.IconManager.IconSize;
import javautils.swing.BusyBar.BarColor;
import javautils.task.GenericWorker;
import javautils.task.ICompletable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/*******************************************************************************
 * This glass pane places a {@link BusyBar} in the middle of the pane, alerting
 * the user that some unknown amount of processing is going on behind the
 * scenes.
 ******************************************************************************/
public class BusyGlassPane extends BlurryGlassPane implements ICompletable
{
    private BusyBar busyBar;
    private GenericWorker task;

    /***************************************************************************
     * Constructor
     * 
     * @param underPane
     **************************************************************************/
    public BusyGlassPane( Component underPane )
    {
        this( underPane, BarColor.BLUE );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param underPane
     * @param color
     **************************************************************************/
    public BusyGlassPane( Component underPane, BarColor color )
    {
        this( underPane, color, "Processing" );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param underPane
     * @param color
     * @param message
     **************************************************************************/
    public BusyGlassPane( Component underPane, BarColor color, String message )
    {
        this( underPane, color, message, false );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param underPane
     * @param color
     * @param message
     * @param cancelable
     **************************************************************************/
    public BusyGlassPane( Component underPane, BarColor color, String message,
            boolean cancelable )
    {
        super( underPane );

        task = null;

        busyBar = new BusyBar( 25, color );

        JLabel label = new JLabel( message, IconManager.getIcon(
                IconManager.CLOCK, IconSize.X16 ), SwingConstants.CENTER );

        JButton button = new JButton( "Cancel" );
        button.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                if( task != null )
                {
                    task.notifyCancelled();
                }
                setVisible( false );
            }
        } );
        button.setFocusable( false );
        button.setEnabled( cancelable );

        MouseEventGobbler listener = new MouseEventGobbler();
        addMouseListener( listener );
        addMouseMotionListener( listener );

        setLayout( new GridBagLayout() );

        add( label, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.49,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( busyBar, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.01,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        add( button, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.49,
                GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
    }

    /***************************************************************************
     * Execute a worker task.
     * 
     * @param worker
     * @throws Exception
     **************************************************************************/
    public void executeTask( GenericWorker worker ) throws Exception
    {
        if( task != null )
            throw new Exception( "Worker task already running!" );

        task = worker;
        task.addCompleteListener( this );
        task.execute();
        setVisible( true );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#setVisible(boolean)
     */
    @Override
    public void setVisible( boolean visible )
    {
        super.setVisible( visible );
        busyBar.setBusy( visible );

        if( visible )
        {
            this.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        } else
        {
            this.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.task.ICompletable#notifyComplete()
     */
    @Override
    public void notifyComplete()
    {
        task = null;
        setVisible( false );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.task.ICompletable#notifyCancelled()
     */
    @Override
    public void notifyCancelled()
    {
        notifyComplete();
    }

    /***************************************************************************
     * BlurryGlassPane demo frame.
     * 
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        class DemoRunner extends FrameRunner
        {
            @Override
            protected JFrame createFrame()
            {
                JFrame frame = new JFrame();

                JButton b1 = new JButton( "Click to do dummy task" );
                b1.setFocusable( false );
                JButton b2 = new JButton( "Dummy task 2" );
                b2.setFocusable( false );
                JButton b3 = new JButton( "Dummy task 3" );
                b3.setFocusable( false );

                final BusyGlassPane glassPane = new BusyGlassPane(
                        frame.getContentPane(), BarColor.GREEN,
                        "<html><b><i>Performing random task</i></b></html>",
                        true );

                class DummyTask extends GenericWorker
                {
                    @Override
                    protected void doWorkerTask() throws Exception
                    {
                        try
                        {
                            for( int i = 0; i < 100; i++ )
                            {
                                Thread.sleep( 20 );
                            }
                        } catch( Exception e )
                        {
                        }
                    }
                }

                ActionListener dummyAL = new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent arg0 )
                    {
                        DummyTask task = new DummyTask();
                        try
                        {
                            glassPane.executeTask( task );
                        } catch( Exception e )
                        {
                            e.printStackTrace();
                        }
                    }
                };

                b1.addActionListener( dummyAL );
                b2.addActionListener( dummyAL );
                b3.addActionListener( dummyAL );

                frame.setGlassPane( glassPane );

                frame.setLayout( new GridBagLayout() );

                frame.add( b1, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.01,
                        GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                frame.add( b2, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.01,
                        GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                frame.add( b3, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.98,
                        GridBagConstraints.NORTH,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );

                frame.setTitle( "BusyGlassPane demo" );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( 300, 150 );
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );

                return frame;
            }

            @Override
            protected boolean validate()
            {
                return true;
            }

        }

        SwingUtilities.invokeLater( new DemoRunner() );
    }
}
