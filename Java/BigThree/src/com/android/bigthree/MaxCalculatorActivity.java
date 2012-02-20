package com.android.bigthree;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.bigthree.model.BrzyckiMaxCalculator;
import com.android.bigthree.model.IMaxCalculator;

/***************************************************************************
 * This {@link Activity} provides the user with a theoretical 1RM calculator.
 **************************************************************************/
public class MaxCalculatorActivity extends Activity
{
    private IMaxCalculator maxCalculator;

    private Button calculateButton;
    private TextView weightView;
    private TextView repsView;
    private TextView maxView;

    /***************************************************************************
     * Default constructor.
     **************************************************************************/
    public MaxCalculatorActivity()
    {
        super();

        maxCalculator = new BrzyckiMaxCalculator();
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
        setContentView( R.layout.max_calculator_layout );

        calculateButton = (Button)findViewById( R.id.calculateButton );
        weightView = (TextView)findViewById( R.id.weight );
        repsView = (TextView)findViewById( R.id.reps );
        maxView = (TextView)findViewById( R.id.theoreticalMax );

        calculateButton.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                int weight = 0;
                int reps = 0;
                try
                {
                    weight = Integer.parseInt( weightView.getText().toString() );
                } catch( Exception e )
                {

                    MessagePresenter.showErrorDialog(
                            MaxCalculatorActivity.this, "Error",
                            "Invalid weight input: '" + weightView.getText()
                                    + "'" );
                    return;
                }

                try
                {
                    reps = Integer.parseInt( repsView.getText().toString() );
                } catch( Exception e )
                {
                    MessagePresenter.showErrorDialog(
                            MaxCalculatorActivity.this, "Error",
                            "Invalid reps input: '" + repsView.getText() + "'" );
                    return;
                }

                try
                {
                    maxView.setText( String.format( "%.2f",
                            maxCalculator.calculateMax( weight, reps ) ) );
                } catch( Exception e )
                {
                    MessagePresenter.showErrorDialog(
                            MaxCalculatorActivity.this, "Error", e.getMessage() );
                    return;
                }
            }
        } );
    }
}
