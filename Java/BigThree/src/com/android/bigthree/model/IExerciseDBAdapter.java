package com.android.bigthree.model;

import android.database.Cursor;

public interface IExerciseDBAdapter
{
    // Row IDs
    public static final int KEY_ID_ROWID = 0;
    public static final int KEY_DESC_ROWID = 1;

    // Column names
    public static final String KEY_ID = "_id";
    public static final String KEY_DESC = "desc";

    /**
     * Opens the database connection.
     */
    public void open();

    /**
     * Closes the database connection.
     */
    public void close();

    /**
     * Inserts an exercise with the supplied description into the database.
     * 
     * @param description
     * @return
     */
    public long insertExercise( String description );

    /**
     * Deletes the exercise with the supplied description from the database.
     * 
     * @param description
     * @return
     */
    public boolean deleteExercise( String description );

    /**
     * Returns all exercises in the database.
     * 
     * @return
     */
    public Cursor getAllExercises();

    /**
     * Returns the exercise with the supplied description.
     * 
     * @param description
     * @return
     */
    public Cursor getExercise( String description );

    /**
     * Updates the supplied record ID with the exercise description provided.
     * 
     * @param rowId
     * @param description
     * @return
     */
    public boolean updateExercise( long rowId, String description );

    /**
     * Adds a listener to this database. The listener will be notified any time
     * the database is modified.
     * 
     * @param listener
     */
    public void addDBListener( IDBListener listener );

    /**
     * Removes a listener from this database.
     * 
     * @param listener
     * @return
     */
    public boolean removeDBListener( IDBListener listener );
}
