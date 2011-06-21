package javautils.task;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/*******************************************************************************
 * This class is a generic worker class. It is meant to operate hand-in-hand
 * with a {@link WorkerDialog} dialog. Classes that extend this class need only
 * override the <code>doWorkerTask</code> function and put all processing to be
 * done on this thread in that function. <br>
 * Calls to <code>publish()</code> will update the current progress bar's
 * description, and calls to <code>setProgress()</code> will update both the
 * current and total progress bar values.
 * 
 * @see WorkerDialog
 * @see javax.swing.SwingWorker
 * 
 ******************************************************************************/
public abstract class WorkerThread extends SwingWorker<Integer, String>
{
    /** The dialog associated with this worker */
    private WorkerDialog dialog = null;

    /** The informer associated with this worker */
    private Informer informer = null;

    /***************************************************************************
     * Sets the <code>WorkerDialog</code> associated with this thread.
     * 
     * @param dialog
     **************************************************************************/
    public void setDialog( WorkerDialog dialog )
    {
        this.dialog = dialog;
    }

    /***************************************************************************
     * Sets the <code>Informer</code> associated with this thread.
     * 
     * @param informer
     **************************************************************************/
    public void setInformer( Informer informer )
    {
        this.informer = informer;
    }

    /***************************************************************************
     * Sets the status of the current progress bar.
     * 
     * @param b
     **************************************************************************/
    protected void setCurrentIndeterminate( boolean b )
    {
        if( dialog != null )
            dialog.setCurrentIndeterminate( b );
    }

    /***************************************************************************
     * Sets the status of the total progress bar.
     * 
     * @param b
     **************************************************************************/
    protected void setTotalIndeterminate( boolean b )
    {
        if( dialog != null )
            dialog.setTotalIndeterminate( b );
    }

    /***************************************************************************
     * This method gets data any time the <code>publish()</code> method is
     * called from within the thread. This data is then sent to the
     * <code>Informer</code> interface in order to update the UI.
     **************************************************************************/
    @Override
    protected void process( List<String> chunks )
    {
        for( String message : chunks )
        {
            if( informer != null )
                informer.messageChanged( message );
        }
    }

    /***************************************************************************
     * This method is called when the thread is finished processing, or the user
     * cancels this thread.
     **************************************************************************/
    @Override
    protected void done()
    {
        if( dialog != null )
            dialog.dispose();
    }

    /***************************************************************************
     * This is the standard <code>SwingWorker</code> execution function.
     * Normally, all sub-classes of this one would implement this method.
     * However, any exceptions thrown are not actually caught by the
     * application. So, we'll force sub-classes to implement the
     * <code>doWorkerTask()</code> method in order to catch potentially
     * meaningful exceptions.
     **************************************************************************/
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
            return -1;
        }

        return 0;
    }

    /***************************************************************************
     * Sets the number of tasks this thread will perform.
     * 
     * @param numTasks
     **************************************************************************/
    protected void setNumTasks( int numTasks )
    {
        if( dialog != null )
            dialog.setNumTasks( numTasks );
    }

    /***************************************************************************
     * Notifies the dialog associated with this thread that an arbitrary chunk
     * of processing is complete.
     **************************************************************************/
    protected void taskDone()
    {
        setProgress( 0 );
        if( dialog != null )
            dialog.taskDone();
    }

    /***************************************************************************
     * This method must be implemented by any sub-classes.
     * 
     * @throws Exception
     **************************************************************************/
    protected abstract void doWorkerTask() throws Exception;
}
