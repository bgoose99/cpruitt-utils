package javautils.task;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javautils.IconManager;
import javautils.IconManager.IconSize;
import javautils.swing.IToggleable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/*******************************************************************************
 * This is a dialog class that works hand-in-hand with a {@link WorkerThread}
 * object. These two classes allow a separate thread to do lengthy processing in
 * the background and inform the user of the progress of the processing. These
 * two classes facilitate the separation of lengthy processing from the EDT.
 * 
 * @see WorkerThread
 * 
 ******************************************************************************/
public class WorkerDialog extends JDialog
{
    /** Default width */
    private final static int DEFAULT_WIDTH = 350;

    /** Default height */
    private final static int DEFAULT_HEIGHT = 175;

    /** Frame that spawned this dialog */
    private Frame parent = null;

    /** Label associated with the total progress bar */
    private JLabel totalLabel;

    /** Total progress bar */
    private JProgressBar totalProgressBar;

    /** Label associated with the current progress bar */
    private JLabel currentLabel;

    /** Current progress bar */
    private JProgressBar currentProgressBar;

    /** Cancel/OK button */
    private JButton cancelButton;

    /** Worker associated with this dialog */
    private WorkerThread worker;

    /** Informer associated with this dialog */
    private Informer informer;

    /** Property change listener */
    private PropertyChangeListener propertyListener;

    /** Toggleable object associated with this dialog */
    private IToggleable toggle;

    /** Number of tasks the worker will perform */
    private int numTasks;

    /** Task the worker is currently processing */
    private int currentTask;

    /** Size of the total progress bar "bins" */
    private double binSize;

    /***************************************************************************
     * Default constructor
     * 
     * @param parent
     *            frame that spawned this dialog
     * @param worker
     *            worker associated with this dialog
     **************************************************************************/
    public WorkerDialog( Frame parent, WorkerThread worker )
    {
        this( parent, worker, "Processing" );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param parent
     *            frame that spawned this dialog
     * @param worker
     *            worker associated with this dialog
     * @param message
     *            default progress string to be displayed
     **************************************************************************/
    public WorkerDialog( Frame parent, WorkerThread worker, String message )
    {
        this( parent, worker, message, null );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param parent
     *            frame that spawned this dialog
     * @param worker
     *            worker associated with this dialog
     * @param message
     *            default progress string to be displayed
     * @param toggle
     *            object to toggle off during processing, and on when processing
     *            finishes
     **************************************************************************/
    public WorkerDialog( Frame parent, WorkerThread worker, String message,
            IToggleable toggle )
    {
        super( parent, message, false );

        this.parent = parent;
        this.worker = worker;
        this.toggle = toggle;

        totalLabel = new JLabel( "Progress" );
        totalProgressBar = new JProgressBar( 0, 100 );

        currentLabel = new JLabel();
        currentProgressBar = new JProgressBar( 0, 100 );

        cancelButton = new JButton( "Cancel" );
        cancelButton.setFocusable( false );
        cancelButton.setIcon( IconManager.getIcon( IconManager.STOP,
                IconSize.X16 ) );
        cancelButton.setHorizontalTextPosition( SwingConstants.RIGHT );
        cancelButton.setIconTextGap( 10 );
        cancelButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                dispose();
            }
        } );

        numTasks = 1;
        currentTask = 1;
        binSize = 1.0;

        setupFrame();

        this.informer = new Informer()
        {
            @Override
            public void messageChanged( String message )
            {
                currentLabel.setText( message );
            }
        };

        this.propertyListener = new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                if( "progress".equals( e.getPropertyName() ) )
                {
                    int progress = (Integer)e.getNewValue();
                    currentProgressBar.setValue( progress );

                    double percent = ( ( currentTask - 1 ) * binSize )
                            + ( ( progress / 100.0 ) * binSize );
                    progress = (int)( percent * 100.0 );
                    totalProgressBar.setValue( progress );
                }
            }
        };

        this.worker.addPropertyChangeListener( this.propertyListener );
        this.worker.setInformer( this.informer );
        this.worker.setDialog( this );
        currentLabel.setText( message );
    }

    /***************************************************************************
     * Sets up this dialog
     **************************************************************************/
    private void setupFrame()
    {
        setLayout( new GridBagLayout() );
        setResizable( false );

        add( totalLabel, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.05,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( totalProgressBar, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.05,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( currentLabel, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.05,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( currentProgressBar, new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.05,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( cancelButton, new GridBagConstraints( 0, 4, 1, 1, 1.0, 0.05,
                GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        setSize( DEFAULT_WIDTH, DEFAULT_HEIGHT );
        setLocationRelativeTo( parent );
        setVisible( true );
    }

    /***************************************************************************
     * Sets the number of tasks, or chunks of processing, the associated
     * <code>WorkerThread</code> will complete.
     * 
     * @param numTasks
     **************************************************************************/
    public void setNumTasks( int numTasks )
    {
        this.numTasks = numTasks;
        binSize = 1.0 / numTasks;
    }

    /***************************************************************************
     * Notifies this dialog that a chunk of processing has completed. The number
     * of times this function is called should correspond to the number of tasks
     * set in the <code>setNumTasks()</code> function.
     **************************************************************************/
    public void taskDone()
    {
        currentTask++;
        if( currentTask > numTasks )
            currentTask = numTasks;
    }

    /***************************************************************************
     * Returns the status of the current progress bar.
     * 
     * @return
     **************************************************************************/
    public boolean isCurrentIndeterminate()
    {
        return currentProgressBar.isIndeterminate();
    }

    /***************************************************************************
     * Sets the status of the current progress bar.
     * 
     * @param b
     **************************************************************************/
    public void setCurrentIndeterminate( boolean b )
    {
        SetCurrentIndeterminateRunnable runnable = new SetCurrentIndeterminateRunnable(
                b );
        SwingUtilities.invokeLater( runnable );
    }

    /***************************************************************************
     * Returns the status of the total progress bar.
     * 
     * @return
     **************************************************************************/
    public boolean isTotalIndeterminate()
    {
        return totalProgressBar.isIndeterminate();
    }

    /***************************************************************************
     * Sets the status of the total progress bar.
     * 
     * @param b
     **************************************************************************/
    public void setTotalIndeterminate( boolean b )
    {
        SetTotalIndeterminateRunnable runnable = new SetTotalIndeterminateRunnable(
                b );
        SwingUtilities.invokeLater( runnable );
    }

    /***************************************************************************
     * This function notifies the user that processing has completed, allowing
     * the user to dispose of this dialog by clicking the OK button.
     * 
     * @param message
     **************************************************************************/
    public void setFinished( String message )
    {
        Container contentPane = this.getContentPane();
        contentPane.setLayout( new GridBagLayout() );
        contentPane.removeAll();

        totalLabel.setText( message );

        cancelButton.setText( "OK" );
        cancelButton.setIcon( IconManager.getIcon( IconManager.ACCEPT,
                IconSize.X16 ) );
        cancelButton.setHorizontalTextPosition( SwingConstants.LEFT );
        cancelButton.setIconTextGap( 10 );

        this.add( totalLabel, new GridBagConstraints( 0, 0, 1, 1, 0.5, 0.5,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        this.add( cancelButton, new GridBagConstraints( 0, 1, 1, 1, 0.5, 0.5,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );

        contentPane.repaint();
    }

    /***************************************************************************
     * This method insures that we enable the parent, if applicable, before we
     * exit.
     **************************************************************************/
    @Override
    public void dispose()
    {
        if( worker != null )
        {
            boolean cancelled = true;

            if( !worker.isDone() )
                cancelled = worker.cancel( true );

            if( cancelled )
            {
                this.worker = null;
            }
        }

        toggleParent( true );

        super.dispose();
    }

    /***************************************************************************
     * Starts the {@link WorkerThread} associated with this dialog.
     **************************************************************************/
    public void startThread()
    {
        toggleParent( false );

        worker.execute();
    }

    /***************************************************************************
     * Toggles the parent of this dialog.
     * 
     * @param b
     **************************************************************************/
    private void toggleParent( boolean b )
    {
        ToggleParentRunnable runnable = new ToggleParentRunnable( toggle, b );
        SwingUtilities.invokeLater( runnable );
    }

    /***************************************************************************
     * Convenience class to toggle the parent.
     **************************************************************************/
    private class ToggleParentRunnable implements Runnable
    {
        private IToggleable toggle;
        private boolean b;

        public ToggleParentRunnable( IToggleable toggle, boolean b )
        {
            this.toggle = toggle;
            this.b = b;
        }

        @Override
        public void run()
        {
            if( toggle != null )
                toggle.toggle( b );
        }
    }

    /***************************************************************************
     * Convenience class to handle toggling the status of the current progress
     * bar.
     **************************************************************************/
    private class SetCurrentIndeterminateRunnable implements Runnable
    {
        private boolean b;

        public SetCurrentIndeterminateRunnable( boolean b )
        {
            this.b = b;
        }

        @Override
        public void run()
        {
            currentProgressBar.setIndeterminate( b );
        }
    }

    /***************************************************************************
     * Convenience class to handle toggling the status of the total progress
     * bar.
     **************************************************************************/
    private class SetTotalIndeterminateRunnable implements Runnable
    {
        private boolean b;

        public SetTotalIndeterminateRunnable( boolean b )
        {
            this.b = b;
        }

        @Override
        public void run()
        {
            totalProgressBar.setIndeterminate( b );
        }
    }
}
