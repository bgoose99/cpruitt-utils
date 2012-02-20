package com.android.bigthree;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.bigthree.model.BrzyckiMaxCalculator;
import com.android.bigthree.model.IDBListener;
import com.android.bigthree.model.IExerciseDBAdapter;
import com.android.bigthree.model.IMaxCalculator;

/*******************************************************************************
 * This {@link Activity} provides the user with a place to log exercises.
 ******************************************************************************/
public class LogActivity extends Activity
{
    private Button dateButton;
    private Spinner exerciseSpinner;
    private EditText weightPicker;
    private EditText repsPicker;
    private Button addRecordButton;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    private OnDateSetListener dateSetListener;
    private SimpleCursorAdapter spinnerAdapter;
    private IDBListener dbListener;
    private BigThree bigThree;

    private IMaxCalculator maxCalculator;

    private static final int DATE_DIALOG_ID = 0;
    private static final int EXERCISE_DIALOG_ID = 1;
    private static final int RM_EXERCISE_DIALOG_ID = 2;
    private static final int ABOUT_DIALOG_ID = 3;

    /***************************************************************************
     * Default constructor
     **************************************************************************/
    public LogActivity()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.log_menu, menu );
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() )
        {
        case R.id.addExerciseMenuItem:
            showDialog( EXERCISE_DIALOG_ID );
            return true;
        case R.id.deleteExerciseMenuItem:
            showDialog( RM_EXERCISE_DIALOG_ID );
            return true;
        case R.id.aboutMenuItem:
            showDialog( ABOUT_DIALOG_ID );
            return true;
        default:
            return super.onOptionsItemSelected( item );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected Dialog onCreateDialog( int id )
    {
        switch( id )
        {
        case DATE_DIALOG_ID:
            return showDatePickerDialog();
        case EXERCISE_DIALOG_ID:
            return showExerciseInputDialog();
        case RM_EXERCISE_DIALOG_ID:
            return showDeleteExerciseInputDialog();
        case ABOUT_DIALOG_ID:
            return showAboutDialog();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.log_layout );

        // get handles to our elements
        dateButton = (Button)findViewById( R.id.log_dateButton );
        exerciseSpinner = (Spinner)findViewById( R.id.log_exerciseSpinner );
        weightPicker = (EditText)findViewById( R.id.log_weight );
        repsPicker = (EditText)findViewById( R.id.log_reps );
        addRecordButton = (Button)findViewById( R.id.log_addRecordButton );

        dbListener = new DBListener();
        bigThree = (BigThree)this.getApplication();
        bigThree.getExerciseDBAdapter().addDBListener( dbListener );
        spinnerAdapter = null;

        maxCalculator = new BrzyckiMaxCalculator();

        // fill spinner(s)
        fillExerciseSpinner();

        // hook up button click listeners
        addRecordButton.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                addRecord();
            }
        } );

        dateButton.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                showDialog( DATE_DIALOG_ID );
            }
        } );

        dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            public void onDateSet( DatePicker view, int year, int monthOfYear,
                    int dayOfMonth )
            {
                selectedYear = year;
                selectedMonth = monthOfYear;
                selectedDay = dayOfMonth;
                udpateDateDisplay();
            }
        };

        // get the current date
        final Calendar c = Calendar.getInstance();
        selectedYear = c.get( Calendar.YEAR );
        selectedMonth = c.get( Calendar.MONTH );
        selectedDay = c.get( Calendar.DAY_OF_MONTH );

        udpateDateDisplay();
    }

    /***************************************************************************
     * Fills the exercise spinner with items from the database.
     **************************************************************************/
    private void fillExerciseSpinner()
    {
        Cursor cursor = bigThree.getExerciseDBAdapter().getAllExercises();
        if( spinnerAdapter == null )
        {
            String[] from = new String[] { IExerciseDBAdapter.KEY_DESC };
            int[] to = new int[] { android.R.id.text1 };
            spinnerAdapter = new SimpleCursorAdapter( this,
                    android.R.layout.simple_spinner_item, cursor, from, to );
            spinnerAdapter
                    .setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            exerciseSpinner.setAdapter( spinnerAdapter );
        } else
        {
            spinnerAdapter.changeCursor( cursor );
        }
    }

    /***************************************************************************
     * Updates the date display to show the currently selected date.
     **************************************************************************/
    private void udpateDateDisplay()
    {
        Calendar c = Calendar.getInstance();
        if( selectedMonth == c.get( Calendar.MONTH )
                && selectedDay == c.get( Calendar.DAY_OF_MONTH )
                && selectedYear == c.get( Calendar.YEAR ) )
            dateButton.setText( R.string.dateDisplay );
        else
            dateButton.setText( ( selectedMonth + 1 ) + "-" + selectedDay + "-"
                    + selectedYear );
    }

    /***************************************************************************
     * Adds an exercise. If successful, this exercise will now be tracked by the
     * application.
     * 
     * @param exerciseName
     **************************************************************************/
    private void addExercise( String exerciseName )
    {
        boolean success = bigThree.getExerciseDBAdapter().insertExercise(
                exerciseName ) > 0;

        if( success )
            MessagePresenter.showToastMessage( this, "Exercise " + exerciseName
                    + " will now be tracked." );
    }

    /***************************************************************************
     * Deletes an exercise. If successful, the exercise will no long be tracked
     * by the application. NOTE: this method will delete all records of the
     * current exercise.
     * 
     * @param exerciseName
     **************************************************************************/
    private void deleteExercise( String exerciseName )
    {
        boolean success = bigThree.getExerciseDBAdapter().deleteExercise(
                exerciseName );

        if( success )
            bigThree.getExerciseRecordDBAdapter().deleteRecordsByType(
                    exerciseName );

        if( success )
            MessagePresenter.showToastMessage( this, "Exercise " + exerciseName
                    + " has been removed from tracking." );
    }

    /***************************************************************************
     * Adds an exercise record.
     **************************************************************************/
    private void addRecord()
    {
        String date = "";
        String desc = "";
        int weight = 0;
        int reps = 0;
        double max = 0.0;

        date = selectedYear + "-" + String.format( "%02d", selectedMonth + 1 )
                + "-" + String.format( "%02d", selectedDay );

        try
        {
            Cursor c = (Cursor)exerciseSpinner.getSelectedItem();
            desc = c.getString( IExerciseDBAdapter.KEY_DESC_ROWID );
        } catch( Exception e )
        {
            MessagePresenter.showErrorDialog( this, "Error",
                    "Unrecognized exercise selection!" );
            return;
        }

        try
        {
            weight = Integer.parseInt( weightPicker.getText().toString() );
        } catch( Exception e )
        {
            MessagePresenter.showErrorDialog( this, "Error",
                    "Invalid weight: '" + weightPicker.getText() + "'" );
            return;
        }

        try
        {
            reps = Integer.parseInt( repsPicker.getText().toString() );
        } catch( Exception e )
        {
            MessagePresenter.showErrorDialog( this, "Error", "Invalid reps: '"
                    + repsPicker.getText() + "'" );
            return;
        }

        try
        {
            max = maxCalculator.calculateMax( weight, reps );
        } catch( Exception e )
        {
            MessagePresenter.showErrorDialog( this, "Error", e.getMessage() );
            return;
        }

        if( !( bigThree.getExerciseRecordDBAdapter().insertRecord( date, desc,
                weight, reps, max ) > 0 ) )
            MessagePresenter.showErrorDialog( this, "Warning",
                    "Error inserting record." );
        else
            MessagePresenter.showToastMessage( this, "Record inserted" );
    }

    /***************************************************************************
     * Returns a dialog allowing the user to select a date.
     * 
     * @return
     **************************************************************************/
    private Dialog showDatePickerDialog()
    {
        return new DatePickerDialog( this, dateSetListener, selectedYear,
                selectedMonth, selectedDay );
    }

    /***************************************************************************
     * Returns a dialog allowing the user to specify a new exercise to track.
     * 
     * @return
     **************************************************************************/
    private Dialog showExerciseInputDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "New exercise" );
        builder.setMessage( "Input new exercise name" );

        final EditText input = new EditText( this );
        builder.setView( input );

        builder.setPositiveButton( "OK", new DialogInterface.OnClickListener()
        {
            public void onClick( DialogInterface dialog, int which )
            {
                addExercise( input.getText().toString() );
                dialog.dismiss();
            }
        } );

        builder.setNegativeButton( "Cancel",
                new DialogInterface.OnClickListener()
                {
                    public void onClick( DialogInterface dialog, int which )
                    {
                        dialog.dismiss();
                    }
                } );

        return builder.show();
    }

    /***************************************************************************
     * Returns a dialog allowing the user to remove an exercise from tracking.
     * 
     * @return
     **************************************************************************/
    private Dialog showDeleteExerciseInputDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "Remove exercise" );
        builder.setMessage( "Select exercise to delete.\n"
                + "NOTE: This will delete the exercise from tracking, "
                + "including ALL records." );

        // Build a spinner filled with exercises
        final Spinner spinner = new Spinner( this );
        Cursor cursor = bigThree.getExerciseDBAdapter().getAllExercises();
        String[] from = new String[] { IExerciseDBAdapter.KEY_DESC };
        int[] to = new int[] { android.R.id.text1 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter( this,
                android.R.layout.simple_spinner_item, cursor, from, to );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( adapter );

        builder.setView( spinner );

        builder.setPositiveButton( "OK", new DialogInterface.OnClickListener()
        {
            public void onClick( DialogInterface dialog, int which )
            {
                Cursor c = (Cursor)spinner.getSelectedItem();
                String desc = c.getString( IExerciseDBAdapter.KEY_DESC_ROWID );
                deleteExercise( desc );
                dialog.dismiss();
            }
        } );

        builder.setNegativeButton( "Cancel",
                new DialogInterface.OnClickListener()
                {
                    public void onClick( DialogInterface dialog, int which )
                    {
                        dialog.dismiss();
                    }
                } );

        return builder.show();
    }

    /***************************************************************************
     * Returns a dialog that displays information about this application.
     * 
     * @return
     **************************************************************************/
    private Dialog showAboutDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "About " + getString( R.string.app_name ) );
        SpannableString msg = new SpannableString(
                getString( R.string.aboutText ) );
        Linkify.addLinks( msg, Linkify.ALL );
        builder.setMessage( msg );

        builder.setNeutralButton( "OK", new DialogInterface.OnClickListener()
        {
            public void onClick( DialogInterface dialog, int which )
            {
                dialog.dismiss();
            }
        } );

        Dialog d = builder.show();
        ( (TextView)d.findViewById( android.R.id.message ) )
                .setMovementMethod( LinkMovementMethod.getInstance() );

        return d;
    }

    /***************************************************************************
     * Simple {@link IDBListener} to update the UI when the database changes.
     **************************************************************************/
    private class DBListener implements IDBListener
    {
        public void notifyContentsChanged()
        {
            fillExerciseSpinner();
        }
    }
}
