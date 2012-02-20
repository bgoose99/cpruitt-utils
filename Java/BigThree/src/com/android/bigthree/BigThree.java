package com.android.bigthree;

import android.app.Application;

import com.android.bigthree.model.ExerciseDBAdapter;
import com.android.bigthree.model.ExerciseRecordDBAdapter;
import com.android.bigthree.model.IExerciseDBAdapter;
import com.android.bigthree.model.IExerciseRecordDBAdapter;

/*******************************************************************************
 * This class just provides application-level storage for our database objects.
 ******************************************************************************/
public class BigThree extends Application
{
    // application-level storage for our database objects
    private IExerciseDBAdapter exerciseDBAdapter;
    private IExerciseRecordDBAdapter exerciseRecordDBAdapter;

    /***************************************************************************
     * Returns the {@link IExerciseDBAdapter} associated with this application.
     * 
     * @return
     **************************************************************************/
    public IExerciseDBAdapter getExerciseDBAdapter()
    {
        return exerciseDBAdapter;
    }

    /***************************************************************************
     * Returns the {@link IExerciseRecordDBAdapter} associated with this
     * application.
     * 
     * @return
     **************************************************************************/
    public IExerciseRecordDBAdapter getExerciseRecordDBAdapter()
    {
        return exerciseRecordDBAdapter;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate()
    {
        // open our databases
        exerciseDBAdapter = new ExerciseDBAdapter( this );
        exerciseDBAdapter.open();
        exerciseRecordDBAdapter = new ExerciseRecordDBAdapter( this );
        exerciseRecordDBAdapter.open();
    }
}
