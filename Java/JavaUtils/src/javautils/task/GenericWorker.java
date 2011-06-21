package javautils.task;

import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/*******************************************************************************
 * This class is a simple {@link SwingWorker} that is meant to run in the
 * background and do some arbitrary amount of processing. Listeners, in the form
 * of {@link ICompletable}s, can be added and are notified when processing is
 * complete or canceled.
 ******************************************************************************/
public abstract class GenericWorker extends SwingWorker<Integer, String>
        implements ICompletable
{
    private List<ICompletable> listeners;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public GenericWorker()
    {
        listeners = new Vector<ICompletable>();
    }

    /***************************************************************************
     * Adds a listener that will be notified when this thread completes or is
     * canceled.
     * 
     * @param c
     **************************************************************************/
    public void addCompleteListener( ICompletable c )
    {
        listeners.add( 0, c );
    }

    /***************************************************************************
     * Removes a listener from this thread.
     * 
     * @param c
     **************************************************************************/
    public void removeCompleteListener( ICompletable c )
    {
        listeners.remove( c );
    }

    /***************************************************************************
     * This is the main workhorse of the thread. All derived classes must
     * implement this method.
     * 
     * @throws Exception
     **************************************************************************/
    protected abstract void doWorkerTask() throws Exception;

    /***************************************************************************
     * Notifies all registered listeners that processing has stopped.
     * 
     * @param complete
     **************************************************************************/
    private void notifyListeners( boolean complete )
    {
        for( ICompletable c : listeners )
        {
            if( complete )
            {
                c.notifyComplete();
            } else
            {
                c.notifyCancelled();
            }
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
        // being notified that we are complete is probably exactly the same as
        // being canceled
        notifyCancelled();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.task.ICompletable#notifyCancelled()
     */
    @Override
    public void notifyCancelled()
    {
        // stop thread
        if( !isDone() )
            cancel( true );
        notifyListeners( false );
        JOptionPane.showMessageDialog( null, "Processing cancelled",
                "Cancelled", JOptionPane.INFORMATION_MESSAGE );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.SwingWorker#doInBackground()
     */
    @Override
    protected Integer doInBackground() throws Exception
    {
        try
        {
            doWorkerTask();
        } catch( Exception e )
        {
            JOptionPane.showMessageDialog(
                    null,
                    "Error encountered while processing.\nError: "
                            + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE );
            notifyListeners( false );
            return -1;
        }

        notifyListeners( true );
        return 0;
    }
}
