package com.android.bigthree.model;

/*******************************************************************************
 * This {@link IMaxCalculator} uses the Brzycki equation to estimate a
 * theoretical 1RM.
 ******************************************************************************/
public class BrzyckiMaxCalculator implements IMaxCalculator
{
    /***************************************************************************
     * Default constructor.
     **************************************************************************/
    public BrzyckiMaxCalculator()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.android.bigthree.model.IMaxCalculator#calculateMax(int, int)
     */
    public double calculateMax( int weight, int reps )
            throws IllegalArgumentException
    {
        if( reps > 15 )
            throw new IllegalArgumentException(
                    "Repetitions must be less than 15." );
        else if( reps < 1 )
            throw new IllegalArgumentException(
                    "Repetitions must be greater than 0." );

        return weight * ( 36.0 / ( 37 - reps ) );
    }

}
