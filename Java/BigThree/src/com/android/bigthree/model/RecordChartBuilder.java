package com.android.bigthree.model;

import java.text.DateFormat;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

/*******************************************************************************
 * Simple class for generating charts from database records.
 ******************************************************************************/
public class RecordChartBuilder
{
    private static String DATE_FORMAT = "MM-dd";

    /***************************************************************************
     * Builds an XY chart using records from the supplied {@link Cursor}.
     * 
     * @param context
     * @param exerciseName
     * @param dateFormat
     * @param cursor
     * @throws Exception
     **************************************************************************/
    public static void buildXYChart( Context context, String exerciseName,
            DateFormat dateFormat, Cursor cursor ) throws Exception
    {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        TimeSeries series = new TimeSeries( exerciseName );

        if( cursor != null && cursor.moveToFirst() )
        {
            do
            {
                // get the date and convert
                Date d = dateFormat.parse( cursor
                        .getString( IExerciseRecordDBAdapter.KEY_DATE_ROWID ) );
                // get the max
                double max = cursor
                        .getDouble( IExerciseRecordDBAdapter.KEY_MAX_ROWID );
                series.add( d, max );
            } while( cursor.moveToNext() );
        }

        dataset.addSeries( series );

        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize( 25 );
        renderer.setChartTitleTextSize( 30 );
        renderer.setLabelsTextSize( 25 );
        renderer.setLegendTextSize( 25 );
        renderer.setPointSize( 10f );
        renderer.setMargins( new int[] { 20, 20, 20, 0 } );
        renderer.setGridColor( Color.DKGRAY );
        renderer.setShowGrid( true );

        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor( Color.GREEN );
        r.setPointStyle( PointStyle.DIAMOND );
        r.setFillPoints( true );
        renderer.addSeriesRenderer( r );
        renderer.setAxesColor( Color.DKGRAY );
        renderer.setLabelsColor( Color.LTGRAY );

        Intent intent = ChartFactory.getTimeChartIntent( context, dataset,
                renderer, DATE_FORMAT, exerciseName );
        context.startActivity( intent );
    }
}
