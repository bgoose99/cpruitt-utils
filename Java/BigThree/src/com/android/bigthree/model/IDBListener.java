package com.android.bigthree.model;

/*******************************************************************************
 * This interface allows a user of a database to listen for changes to the
 * database and perform necessary updates when a change occurs.
 ******************************************************************************/
public interface IDBListener
{
    /***************************************************************************
     * Used to notify the listener that some database contents have changed.
     **************************************************************************/
    public void notifyContentsChanged();
}
