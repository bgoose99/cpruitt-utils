package com.android.bigthree;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*******************************************************************************
 * This {@link Activity} provides the user with a simple percentage calculator.
 ******************************************************************************/
public class PercentagesActivity extends Activity
{
    private TextView weightTextView;
    private TextView p95TextView;
    private TextView p90TextView;
    private TextView p85TextView;
    private TextView p80TextView;
    private TextView p75TextView;
    private TextView p70TextView;
    private TextView p65TextView;
    private TextView p60TextView;
    private TextView p55TextView;
    private TextView p50TextView;
    private Button calculateButton;

    /***************************************************************************
     * Default constructor.
     **************************************************************************/
    public PercentagesActivity()
    {
        super();
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
        setContentView( R.layout.percentages_layout );

        weightTextView = (TextView)findViewById( R.id.percentages_weight );
        p95TextView = (TextView)findViewById( R.id.percentages_p95Value );
        p90TextView = (TextView)findViewById( R.id.percentages_p90Value );
        p85TextView = (TextView)findViewById( R.id.percentages_p85Value );
        p80TextView = (TextView)findViewById( R.id.percentages_p80Value );
        p75TextView = (TextView)findViewById( R.id.percentages_p75Value );
        p70TextView = (TextView)findViewById( R.id.percentages_p70Value );
        p65TextView = (TextView)findViewById( R.id.percentages_p65Value );
        p60TextView = (TextView)findViewById( R.id.percentages_p60Value );
        p55TextView = (TextView)findViewById( R.id.percentages_p55Value );
        p50TextView = (TextView)findViewById( R.id.percentages_p50Value );

        calculateButton = (Button)findViewById( R.id.percentages_calculateButton );
        calculateButton.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                try
                {
                    int weight = Integer.parseInt( weightTextView.getText()
                            .toString() );
                    p95TextView.setText( String.format( "%.2f", weight * 0.95 ) );
                    p90TextView.setText( String.format( "%.2f", weight * 0.90 ) );
                    p85TextView.setText( String.format( "%.2f", weight * 0.85 ) );
                    p80TextView.setText( String.format( "%.2f", weight * 0.80 ) );
                    p75TextView.setText( String.format( "%.2f", weight * 0.75 ) );
                    p70TextView.setText( String.format( "%.2f", weight * 0.70 ) );
                    p65TextView.setText( String.format( "%.2f", weight * 0.65 ) );
                    p60TextView.setText( String.format( "%.2f", weight * 0.60 ) );
                    p55TextView.setText( String.format( "%.2f", weight * 0.55 ) );
                    p50TextView.setText( String.format( "%.2f", weight * 0.50 ) );
                } catch( Exception e )
                {
                    MessagePresenter.showErrorDialog( PercentagesActivity.this,
                            "Error",
                            "Invalid input: '" + weightTextView.getText() + "'" );
                }
            }
        } );
    }
}
