package javautils.task;

/*******************************************************************************
 * This interface is used when an object should be notified when a message
 * changes. A good example is the case when a status message changes on a dialog
 * that tells the user about the progress of a lengthy processing task.
 ******************************************************************************/
public interface Informer
{
    /***************************************************************************
     * Used to inform this object that a message has changed.
     * 
     * @param message
     **************************************************************************/
    public void messageChanged( String message );
}
