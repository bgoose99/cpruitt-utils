package com.android.bigthree;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/*******************************************************************************
 * This {@link Activity} is the main view of our application.
 ******************************************************************************/
public class MainActivity extends TabActivity
{
    /***************************************************************************
     * Default constructor.
     **************************************************************************/
    public MainActivity()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );

        // set up our main view
        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass( this, LogActivity.class );
        spec = tabHost.newTabSpec( "log" )
                .setIndicator( "Log", res.getDrawable( R.drawable.ic_tab_log ) )
                .setContent( intent );
        tabHost.addTab( spec );

        intent = new Intent().setClass( this, RecordsActivity.class );
        spec = tabHost
                .newTabSpec( "records" )
                .setIndicator( "Records",
                        res.getDrawable( R.drawable.ic_tab_records ) )
                .setContent( intent );
        tabHost.addTab( spec );

        intent = new Intent().setClass( this, PercentagesActivity.class );
        spec = tabHost
                .newTabSpec( "percentages" )
                .setIndicator( "%",
                        res.getDrawable( R.drawable.ic_tab_percentages ) )
                .setContent( intent );
        tabHost.addTab( spec );

        intent = new Intent().setClass( this, MaxCalculatorActivity.class );
        spec = tabHost
                .newTabSpec( "calculator" )
                .setIndicator( "1RM",
                        res.getDrawable( R.drawable.ic_tab_max_calculator ) )
                .setContent( intent );
        tabHost.addTab( spec );

        tabHost.setCurrentTab( 0 );
    }
}