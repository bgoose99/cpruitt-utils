package com.android.bigthree.model;

import android.database.Cursor;

public interface IExerciseRecordDBAdapter
{
    // Row IDs
    public static final int KEY_ID_ROWID = 0;
    public static final int KEY_DATE_ROWID = 1;
    public static final int KEY_DESC_ROWID = 2;
    public static final int KEY_WEIGHT_ROWID = 3;
    public static final int KEY_REPS_ROWID = 4;
    public static final int KEY_MAX_ROWID = 5;

    // Column names
    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_DESC = "desc";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_REPS = "reps";
    public static final String KEY_MAX = "max";

    /**
     * Opens the database connection.
     */
    public void open();

    /**
     * Closes the database connection.
     */
    public void close();

    /**
     * Inserts a record into the database.
     * 
     * @param date
     * @param description
     * @param weight
     * @param reps
     * @param max
     * @return
     */
    public long insertRecord( String date, String description, int weight,
            int reps, double max );

    /**
     * Deletes a record from the database.
     * 
     * @param id
     * @return
     */
    public boolean deleteRecord( long id );

    /**
     * Returns all records in the database.
     * 
     * @return
     */
    public Cursor getAllRecords();

    /**
     * Returns all records with the supplied description.
     * 
     * @param description
     * @return
     */
    public Cursor getRecordsByType( String description );

    /**
     * Deletes all records with the given description.
     * 
     * @param description
     * @return
     */
    public boolean deleteRecordsByType( String description );

    /**
     * Updates a single exercise record.
     * 
     * @param rowId
     * @param date
     * @param description
     * @param weight
     * @param reps
     * @param max
     * @return
     */
    public boolean updateRecord( long rowId, String date, String description,
            int weight, int reps, double max );

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
