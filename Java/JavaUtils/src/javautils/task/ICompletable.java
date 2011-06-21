package javautils.task;

/*******************************************************************************
 * This interface is for objects that need to be notified when they are complete
 * and/or cancelled.
 ******************************************************************************/
public interface ICompletable
{
    /***************************************************************************
     * Notifies this object that it is now complete.
     **************************************************************************/
    public void notifyComplete();

    /***************************************************************************
     * Notifies this object that it has been cancelled.
     **************************************************************************/
    public void notifyCancelled();
}
