package com.android.bigthree.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import android.database.Cursor;

public class RecordsSerializer
{
    private static final String DELIM = ",";
    private IExerciseRecordDBAdapter dbAdapter;

    public RecordsSerializer( IExerciseRecordDBAdapter dbAdapter )
    {
        this.dbAdapter = dbAdapter;
    }

    public void writeRecordsToFile( File outputFile ) throws Exception
    {
        BufferedWriter out = null;

        try
        {
            out = new BufferedWriter( new FileWriter( outputFile ) );

            Cursor c = dbAdapter.getAllRecords();
            if( c != null && c.moveToFirst() )
            {
                do
                {
                    out.write( c
                            .getString( IExerciseRecordDBAdapter.KEY_DESC_ROWID )
                            + DELIM
                            + c.getString( IExerciseRecordDBAdapter.KEY_DATE_ROWID )
                            + DELIM
                            + c.getInt( IExerciseRecordDBAdapter.KEY_WEIGHT_ROWID )
                            + DELIM
                            + c.getInt( IExerciseRecordDBAdapter.KEY_REPS_ROWID )
                            + DELIM
                            + String.format(
                                    "%.2f",
                                    c.getDouble( IExerciseRecordDBAdapter.KEY_MAX_ROWID ) )
                            + "\n" );
                } while( c.moveToNext() );
            }
        } catch( Exception e )
        {
            throw e;
        } finally
        {
            try
            {
                out.close();
            } catch( Exception e )
            {
            }
        }
    }
}
