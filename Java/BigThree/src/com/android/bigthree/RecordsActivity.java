package com.android.bigthree;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView.OnItemClickListener;
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
import com.android.bigthree.model.RecordChartBuilder;
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

    private AlertDialog.Builder recordDialogBuilder;

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
        case R.id.chartRecordsMenuItem:
            generateChart();
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

        recordListView.setOnItemClickListener( new OnItemClickListener()
        {
            public void onItemClick( AdapterView<?> parent, View view,
                    int position, long id )
            {
                Record r = recordAdapter.getItem( position );
                if( r != null )
                {
                    String msg = "Date: " + r.getDate() + "\n" + "Weight: "
                            + r.getWeight() + "\n" + "Reps: " + r.getReps()
                            + "\n" + "Max: "
                            + String.format( "%.2f", r.getMax() );
                    recordDialogBuilder.setMessage( msg );
                    recordDialogBuilder.show();
                }
                return;
            }
        } );

        // set up our record dialog
        recordDialogBuilder = new AlertDialog.Builder( this );
        recordDialogBuilder.setNeutralButton( "OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick( DialogInterface dialog, int which )
                    {
                        dialog.dismiss();
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
        ProgressDialog dialog = new ProgressDialog( this );
        dialog.setMessage( "Fetching records..." );
        dialog.setIndeterminate( true );

        Thread thread = new Thread( new GetRecordsRunnable( dialog ) );
        thread.start();

        // only show the progress dialog if this window has focus
        if( this.getCurrentFocus() != null )
            dialog.show();
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
     * Generates a chart of the current exercise.
     **************************************************************************/
    private void generateChart()
    {
        try
        {
            Cursor cursor = bigThree.getExerciseRecordDBAdapter()
                    .getRecordsByType( currentExerciseSelection );
            RecordChartBuilder.buildXYChart( this, currentExerciseSelection,
                    bigThree.getExerciseRecordDBAdapter().getDateFormat(),
                    cursor );
        } catch( Exception e )
        {
            MessagePresenter.showToastMessage( this, "Error creating chart:\n"
                    + e.getMessage(), Toast.LENGTH_LONG );
        }
    }

    /***************************************************************************
     * Runnable for retrieving records from the database. This should NOT be run
     * on the UI thread.
     **************************************************************************/
    private class GetRecordsRunnable implements Runnable
    {
        private ProgressDialog dialog;

        /***********************************************************************
         * Constructor
         * 
         * @param dialog
         **********************************************************************/
        public GetRecordsRunnable( ProgressDialog dialog )
        {
            this.dialog = dialog;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        public void run()
        {
            Cursor cursor = bigThree.getExerciseRecordDBAdapter()
                    .getRecordsByType( currentExerciseSelection );

            UpdateRecordsRunnable runnable = new UpdateRecordsRunnable( cursor,
                    dialog );

            runOnUiThread( runnable );
        }
    }

    /***************************************************************************
     * Runnable for updating records table. This should be run on the UI thread.
     **************************************************************************/
    private class UpdateRecordsRunnable implements Runnable
    {
        private Cursor cursor = null;
        private ProgressDialog dialog;

        /***********************************************************************
         * Constructor
         * 
         * @param cursor
         * @param dialog
         **********************************************************************/
        public UpdateRecordsRunnable( Cursor cursor, ProgressDialog dialog )
        {
            this.cursor = cursor;
            this.dialog = dialog;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        public void run()
        {
            recordAdapter.clear();

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
            dialog.dismiss();
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

    /***************************************************************************
     * {@link ArrayAdapter} for displaying custom items in the {@link ListView}.
     **************************************************************************/
    private class RecordsAdapter extends ArrayAdapter<Record>
    {
        private static final float TEXT_SIZE = 20.0f;
        private List<Record> items;

        /***********************************************************************
         * Constructor
         * 
         * @param context
         * @param textViewResourceId
         * @param items
         **********************************************************************/
        public RecordsAdapter( Context context, int textViewResourceId,
                List<Record> items )
        {
            super( context, textViewResourceId, items );
            this.items = items;
        }

        /*
         * (non-Javadoc)
         * 
         * @see android.widget.ArrayAdapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
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
                {
                    v1.setText( r.getDate() );
                    v1.setTextSize( TEXT_SIZE );
                }
                if( v2 != null )
                {
                    v2.setText( String.format( "%.2f", r.getMax() ) );
                    v2.setTextSize( TEXT_SIZE );
                }
            }

            return v;
        }
    }
}
