package com.android.bigthree;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.bigthree.model.IDBListener;
import com.android.bigthree.model.IExerciseDBAdapter;
import com.android.bigthree.model.IExerciseRecordDBAdapter;

public class RecordsActivity extends Activity
{
    private Spinner exerciseSpinner;
    private SimpleCursorAdapter spinnerAdapter;
    private IDBListener exerciseDbListener;
    private IDBListener recordsDbListener;
    private BigThree bigThree;
    private String currentExerciseSelection;
    private TableLayout tableLayout;
    private TableRow headerRow;

    private final LayoutParams rowLayoutParams = new LayoutParams(
            LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT );

    /**
     * Default constructor.
     */
    public RecordsActivity()
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
        inflater.inflate( R.menu.records_menu, menu );
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
        case R.id.saveRecordsMenuItem:
            MessagePresenter.showToastMessage( this, "Save me, bizzle" );
            return true;
        case R.id.deleteRecordsMenuItem:
            MessagePresenter.showToastMessage( this, "Delete me, bizzle" );
            return true;
        default:
            return super.onOptionsItemSelected( item );
        }
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
        setContentView( R.layout.records_layout );

        // get handles to our components
        exerciseSpinner = (Spinner)findViewById( R.id.records_exerciseSpinner );
        tableLayout = (TableLayout)findViewById( R.id.records_tableLayout );

        TextView dateHeaderView = new TextView( this );
        dateHeaderView.setText( R.string.date );
        dateHeaderView.setGravity( Gravity.LEFT );
        TextView maxHeaderView = new TextView( this );
        maxHeaderView.setText( R.string.max );
        maxHeaderView.setGravity( Gravity.RIGHT );
        headerRow = new TableRow( this );
        headerRow.setLayoutParams( rowLayoutParams );
        headerRow.addView( dateHeaderView );
        headerRow.addView( maxHeaderView );

        exerciseDbListener = new ExerciseDBListener();
        recordsDbListener = new RecordsDBListener();

        bigThree = (BigThree)this.getApplication();
        bigThree.getExerciseDBAdapter().addDBListener( exerciseDbListener );
        bigThree.getExerciseRecordDBAdapter().addDBListener( recordsDbListener );

        spinnerAdapter = null;

        currentExerciseSelection = "Unknown";

        // hook up listeners
        exerciseSpinner.setOnItemSelectedListener( new OnItemSelectedListener()
        {
            public void onItemSelected( AdapterView<?> parentView,
                    View selectedItemView, int position, long id )
            {
                Cursor c = (Cursor)parentView.getItemAtPosition( position );
                selectItem( c.getString( IExerciseDBAdapter.KEY_DESC_ROWID ) );
            }

            public void onNothingSelected( AdapterView<?> arg0 )
            {
            }
        } );

        // fill our spinner(s)
        fillExerciseSpinner();
    }

    /**
     * 
     */
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

    /**
     * 
     * @param newSelection
     */
    private void selectItem( String newSelection )
    {
        if( !currentExerciseSelection.equals( newSelection ) )
        {
            currentExerciseSelection = newSelection;
            updateTable();
        }
    }

    /**
     * 
     */
    private void updateTable()
    {
        // clear table
        tableLayout.removeAllViews();

        // get records based on current exercise
        Cursor cursor = bigThree.getExerciseRecordDBAdapter().getRecordsByType(
                currentExerciseSelection );

        // add header
        tableLayout.addView( headerRow );

        // add records
        if( cursor.moveToFirst() )
        {
            do
            {
                TextView v1 = new TextView( this );
                v1.setText( cursor
                        .getString( IExerciseRecordDBAdapter.KEY_DATE_ROWID ) );
                v1.setGravity( Gravity.LEFT );
                TextView v2 = new TextView( this );
                v2.setText( String.format( "%.2f", cursor
                        .getDouble( IExerciseRecordDBAdapter.KEY_MAX_ROWID ) ) );
                v2.setGravity( Gravity.RIGHT );

                TableRow row = new TableRow( this );
                row.setLayoutParams( rowLayoutParams );
                row.addView( v1 );
                row.addView( v2 );
                tableLayout.addView( row );
            } while( cursor.moveToNext() );
        } else
        {
            TextView v = new TextView( this );
            v.setText( R.string.noRecords );
            v.setGravity( Gravity.CENTER );

            TableRow row = new TableRow( this );
            row.setLayoutParams( rowLayoutParams );
            row.addView( v );
            tableLayout.addView( row );
        }

        cursor.close();
    }

    /**
     * 
     */
    private class ExerciseDBListener implements IDBListener
    {
        public void notifyContentsChanged()
        {
            fillExerciseSpinner();
        }
    }

    /**
     * 
     */
    private class RecordsDBListener implements IDBListener
    {
        public void notifyContentsChanged()
        {
            updateTable();
        }
    }
}
