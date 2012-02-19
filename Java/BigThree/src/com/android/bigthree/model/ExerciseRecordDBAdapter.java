package com.android.bigthree.model;

import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.bigthree.MessagePresenter;

public class ExerciseRecordDBAdapter implements IExerciseRecordDBAdapter
{
    private static final String TAG = "ExerciseRecordDBAdapter";
    private static final String DB_NAME = "myRecords";
    private static final String DB_TABLE = "records";
    private static final int DB_VERSION = 2;

    private static final String DB_CREATE = "create table " + DB_TABLE + "("
            + KEY_ID + " integer primary key autoincrement, " + KEY_DATE
            + " text not null, " + KEY_DESC + " text not null, " + KEY_WEIGHT
            + " integer not null, " + KEY_REPS + " integer not null, "
            + KEY_MAX + " double not null);";

    private static final String[] QUERY_ALL = { KEY_ID, KEY_DATE, KEY_DESC,
            KEY_WEIGHT, KEY_REPS, KEY_MAX };

    private final Context context;

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;

    private List<IDBListener> listeners;

    /**
     * Constructor
     * 
     * @param context
     */
    public ExerciseRecordDBAdapter( Context context )
    {
        this.context = context;
        dbHelper = new DBHelper( this.context );
        listeners = new Vector<IDBListener>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.android.bigthree.model.IExerciseRecordDBAdapter#open()
     */
    public synchronized void open()
    {
        try
        {
            db = dbHelper.getWritableDatabase();
        } catch( Exception e )
        {
            MessagePresenter.showToastMessage( context,
                    "Error opening records database" );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.android.bigthree.model.IExerciseRecordDBAdapter#close()
     */
    public synchronized void close()
    {
        dbHelper.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.android.bigthree.model.IExerciseRecordDBAdapter#insertRecord(java
     * .lang.String, java.lang.String, int, int, double)
     */
    public synchronized long insertRecord( String date, String description,
            int weight, int reps, double max )
    {
        long rval = -1;
        ContentValues values = new ContentValues();
        values.put( KEY_DATE, date );
        values.put( KEY_DESC, description );
        values.put( KEY_WEIGHT, weight );
        values.put( KEY_REPS, reps );
        values.put( KEY_MAX, max );

        try
        {
            rval = db.insertOrThrow( DB_TABLE, null, values );
        } catch( Exception e )
        {
            MessagePresenter.showToastMessage( context,
                    "Error inserting record." + " Please try again." );
        }

        if( rval > 0 )
            notifyListeners();

        return rval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.android.bigthree.model.IExerciseRecordDBAdapter#deleteRecord(long)
     */
    public synchronized boolean deleteRecord( long id )
    {
        boolean rval = db.delete( DB_TABLE, KEY_ID + "=" + id, null ) > 0;

        if( rval )
            notifyListeners();

        return rval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.android.bigthree.model.IExerciseRecordDBAdapter#getAllRecords()
     */
    public synchronized Cursor getAllRecords()
    {
        return db.query( DB_TABLE, QUERY_ALL, null, null, null, null, null );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.android.bigthree.model.IExerciseRecordDBAdapter#getRecordsByType(
     * java.lang.String)
     */
    public synchronized Cursor getRecordsByType( String description )
    {
        Cursor cursor = db
                .query( true, DB_TABLE, QUERY_ALL, KEY_DESC + "='"
                        + description + "'", null, null, null, KEY_DATE
                        + " DESC", null );

        if( cursor != null )
            cursor.moveToFirst();

        return cursor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.android.bigthree.model.IExerciseRecordDBAdapter#updateExercise(long,
     * java.lang.String, java.lang.String, int, int, double)
     */
    public synchronized boolean updateRecord( long rowId, String date,
            String description, int weight, int reps, double max )
    {
        ContentValues values = new ContentValues();
        values.put( KEY_DATE, date );
        values.put( KEY_DESC, description );
        values.put( KEY_WEIGHT, weight );
        values.put( KEY_REPS, reps );
        values.put( KEY_MAX, max );

        boolean rval = db.update( DB_TABLE, values, KEY_ID + "=" + rowId, null ) > 0;

        if( rval )
            notifyListeners();

        return rval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.android.bigthree.model.IExerciseRecordDBAdapter#addDBListener(com
     * .android.bigthree.model.IDBListener)
     */
    public void addDBListener( IDBListener listener )
    {
        listeners.add( listener );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.android.bigthree.model.IExerciseRecordDBAdapter#removeDBListener(
     * com.android.bigthree.model.IDBListener)
     */
    public boolean removeDBListener( IDBListener listener )
    {
        return listeners.remove( listener );
    }

    /**
     * 
     */
    private void notifyListeners()
    {
        for( IDBListener l : listeners )
            l.notifyContentsChanged();
    }

    /**
     * 
     */
    private static class DBHelper extends SQLiteOpenHelper
    {
        public DBHelper( Context context )
        {
            super( context, DB_NAME, null, DB_VERSION );
        }

        @Override
        public void onCreate( SQLiteDatabase db )
        {
            db.execSQL( DB_CREATE );
        }

        @Override
        public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
        {
            Log.w( TAG, "Upgrading database from version " + oldVersion
                    + " to " + newVersion
                    + ", which will destroy all old data." );
            db.execSQL( "DROP TABLE IF EXISTS " + DB_TABLE );
            onCreate( db );
        }
    }
}
