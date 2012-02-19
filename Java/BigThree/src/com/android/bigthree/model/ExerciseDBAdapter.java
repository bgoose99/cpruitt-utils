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

public class ExerciseDBAdapter implements IExerciseDBAdapter
{
    private static final String TAG = "ExerciseDBAdapter";
    private static final String DB_NAME = "myExercises";
    private static final String DB_TABLE = "exercises";
    private static final int DB_VERSION = 3;

    private static final String[] BIG_THREE = { "Bench Press", "Deadlift",
            "Squat" };

    private static final String DB_CREATE = "create table " + DB_TABLE + "("
            + KEY_ID + " integer primary key autoincrement, " + KEY_DESC
            + " text not null, unique(" + KEY_DESC + "));";

    private final Context context;

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;

    private List<IDBListener> listeners;

    /**
     * Constructor
     * 
     * @param context
     */
    public ExerciseDBAdapter( Context context )
    {
        this.context = context;
        dbHelper = new DBHelper( this.context );
        listeners = new Vector<IDBListener>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.android.bigthree.model.IExerciseDBAdapter#open()
     */
    public synchronized void open()
    {
        try
        {
            db = dbHelper.getWritableDatabase();
        } catch( Exception e )
        {
            MessagePresenter.showToastMessage( context,
                    "Error opening exercise database" );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.android.bigthree.model.IExerciseDBAdapter#close()
     */
    public synchronized void close()
    {
        dbHelper.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.android.bigthree.model.IExerciseDBAdapter#insertExercise(java.lang
     * .String)
     */
    public synchronized long insertExercise( String description )
    {
        long rval = -1;
        ContentValues initialValues = new ContentValues();
        initialValues.put( KEY_DESC, description );

        try
        {
            rval = db.insertOrThrow( DB_TABLE, null, initialValues );
        } catch( Exception e )
        {
            MessagePresenter.showToastMessage( context,
                    "Error inserting exercise '" + description
                            + "'. Check name and try again." );
        }

        if( rval > 0 )
            notifyListeners();

        return rval;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.android.bigthree.model.IExerciseDBAdapter#deleteExercise(java.lang
     * .String)
     */
    public synchronized boolean deleteExercise( String description )
    {
        boolean success = db.delete( DB_TABLE, KEY_DESC + "='" + description
                + "'", null ) > 0;

        if( success )
            notifyListeners();

        return success;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.android.bigthree.model.IExerciseDBAdapter#getAllExercises()
     */
    public synchronized Cursor getAllExercises()
    {
        Cursor cursor = db.query( DB_TABLE, new String[] { KEY_ID, KEY_DESC },
                null, null, null, null, KEY_DESC );

        return cursor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.android.bigthree.model.IExerciseDBAdapter#getExercise(java.lang.String
     * )
     */
    public synchronized Cursor getExercise( String description )
    {
        Cursor cursor = db.query( true, DB_TABLE, new String[] { KEY_ID,
                KEY_DESC }, KEY_DESC + "='" + description + "'", null, null,
                null, null, null );

        if( cursor != null )
            cursor.moveToFirst();

        return cursor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.android.bigthree.model.IExerciseDBAdapter#updateExercise(long,
     * java.lang.String)
     */
    public synchronized boolean updateExercise( long rowId, String description )
    {
        ContentValues values = new ContentValues();
        values.put( KEY_DESC, description );
        boolean success = db.update( DB_TABLE, values, KEY_ID + "=" + rowId,
                null ) > 0;

        if( success )
            notifyListeners();

        return success;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.android.bigthree.model.IExerciseDBAdapter#addDBListener(com.android
     * .bigthree.model.IDBListener)
     */
    public void addDBListener( IDBListener listener )
    {
        listeners.add( listener );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.android.bigthree.model.IExerciseDBAdapter#removeDBListener(com.android
     * .bigthree.model.IDBListener)
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
            // create the database
            db.execSQL( DB_CREATE );

            // add the big three
            for( String s : BIG_THREE )
            {
                ContentValues initialValues = new ContentValues();
                initialValues.put( KEY_DESC, s );
                db.insert( DB_TABLE, null, initialValues );
            }
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
