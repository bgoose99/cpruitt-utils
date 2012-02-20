package com.android.bigthree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bigthree.model.IDBListener;
import com.android.bigthree.model.IExerciseDBAdapter;
import com.android.bigthree.model.IExerciseRecordDBAdapter;
import com.android.bigthree.model.Record;
import com.android.bigthree.model.RecordsSerializer;

/*******************************************************************************
 * This {@link Activity} provides the user with a UI to display and edit their
 * exercise records.
 **************************************************************************/
public class RecordsActivity extends Activity
{
    private Spinner exerciseSpinner;
    private SimpleCursorAdapter spinnerAdapter;
    private IDBListener exerciseDbListener;
    private IDBListener recordsDbListener;
    private BigThree bigThree;
    private String currentExerciseSelection;
    private ListView recordListView;
    private List<Record> recordList;
    private ArrayAdapter<Record> recordAdapter;
    private TableRow headerRow;

    private final LayoutParams rowLayoutParams = new LayoutParams(
            LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT );

    /***************************************************************************
     * Default constructor.
     **************************************************************************/
    public RecordsActivity()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
     * android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public void onCreateContextMenu( ContextMenu menu, View v,
            ContextMenuInfo menuInfo )
    {
        super.onCreateContextMenu( menu, v, menuInfo );
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.record_context_menu, menu );
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onContextItemSelected( MenuItem item )
    {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)item
                .getMenuInfo();
        switch( item.getItemId() )
        {
        case R.id.deleteRecordMenuItem:
            deleteRecord( info.id );
            return true;
        default:
            return super.onContextItemSelected( item );
        }
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
            saveRecordsToFile();
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
        recordListView = (ListView)findViewById( R.id.records_listView );
        recordList = new ArrayList<Record>();
        recordAdapter = new RecordsAdapter( this,
                R.layout.single_record_layout, recordList );
        recordListView.setAdapter( recordAdapter );
        registerForContextMenu( recordListView );

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
     * Called when the user makes a selection in the exercise spinner.
     * 
     * @param newSelection
     **************************************************************************/
    private void selectItem( String newSelection )
    {
        if( !currentExerciseSelection.equals( newSelection ) )
        {
            currentExerciseSelection = newSelection;
            updateTable();
        }
    }

    /***************************************************************************
     * Updates the records table when the selected exercise changes.
     **************************************************************************/
    private void updateTable()
    {
        recordAdapter.clear();

        Cursor cursor = bigThree.getExerciseRecordDBAdapter().getRecordsByType(
                currentExerciseSelection );

        if( cursor != null && cursor.moveToFirst() )
        {
            do
            {
                Record r = new Record(
                        cursor.getLong( IExerciseRecordDBAdapter.KEY_ID_ROWID ),
                        cursor.getString( IExerciseRecordDBAdapter.KEY_DATE_ROWID ),
                        cursor.getString( IExerciseRecordDBAdapter.KEY_DESC_ROWID ),
                        cursor.getInt( IExerciseRecordDBAdapter.KEY_WEIGHT_ROWID ),
                        cursor.getInt( IExerciseRecordDBAdapter.KEY_REPS_ROWID ),
                        cursor.getDouble( IExerciseRecordDBAdapter.KEY_MAX_ROWID ) );
                recordAdapter.add( r );
            } while( cursor.moveToNext() );
            cursor.close();
        } else
        {
            Record r = new Record();
            r.setDate( "No Records. SFW!" );
            recordAdapter.add( r );
        }

        recordAdapter.notifyDataSetChanged();
    }

    /***************************************************************************
     * Removes a single record from the database. Note that the supplied id is
     * an index into the list adapter.
     * 
     * @param id
     **************************************************************************/
    private void deleteRecord( long id )
    {
        Record r = recordAdapter.getItem( (int)id );
        if( bigThree.getExerciseRecordDBAdapter().deleteRecord( r.getId() ) )
            MessagePresenter.showToastMessage( this, "Record deleted" );
        else
            MessagePresenter.showToastMessage( this, "Error deleting record" );
    }

    /***************************************************************************
     * Saves all the user's records to a CSV file.
     **************************************************************************/
    private void saveRecordsToFile()
    {
        // make sure external storage is available
        String state = Environment.getExternalStorageState();
        if( Environment.MEDIA_MOUNTED.equals( state ) )
        {
            // Get path to our application's directory
            File file = new File( getExternalFilesDir( null ),
                    getString( R.string.csvFile ) );
            RecordsSerializer serializer = new RecordsSerializer(
                    bigThree.getExerciseRecordDBAdapter() );
            try
            {
                serializer.writeRecordsToFile( file );
            } catch( Exception e )
            {
                MessagePresenter.showToastMessage( this,
                        "Error writing to file: " + file.getAbsolutePath()
                                + "\n" + e.getMessage() );
                return;
            }

            MessagePresenter.showToastMessage( this, "Records written to: "
                    + file.getAbsolutePath(), Toast.LENGTH_LONG );
        } else
        {
            MessagePresenter.showToastMessage( this,
                    "Storage media is not available for writing." );
        }
    }

    /***************************************************************************
     * Simple {@link IDBListener} for exercise changes.
     **************************************************************************/
    private class ExerciseDBListener implements IDBListener
    {
        public void notifyContentsChanged()
        {
            fillExerciseSpinner();
        }
    }

    /***************************************************************************
     * Simple {@link IDBListener} for record changes.
     **************************************************************************/
    private class RecordsDBListener implements IDBListener
    {
        public void notifyContentsChanged()
        {
            updateTable();
        }
    }

    /**
     * 
     */
    private class RecordsAdapter extends ArrayAdapter<Record>
    {
        private List<Record> items;

        public RecordsAdapter( Context context, int textViewResourceId,
                List<Record> items )
        {
            super( context, textViewResourceId, items );
            this.items = items;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            View v = convertView;
            if( v == null )
            {
                LayoutInflater li = (LayoutInflater)getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                v = li.inflate( R.layout.single_record_layout, null );
            }

            Record r = items.get( position );
            if( r != null )
            {
                TextView v1 = (TextView)v.findViewById( R.id.record_dateView );
                TextView v2 = (TextView)v.findViewById( R.id.record_maxView );
                if( v1 != null )
                    v1.setText( r.getDate() );
                if( v2 != null )
                    v2.setText( String.format( "%.2f", r.getMax() ) );
            }

            return v;
        }
    }
}
