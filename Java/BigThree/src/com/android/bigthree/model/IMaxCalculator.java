package com.android.bigthree.model;

/*******************************************************************************
 * Simple theoretical 1RM calculator interface.
 ******************************************************************************/
public interface IMaxCalculator
{
    /***************************************************************************
     * Calculates a theoretical 1RM based on weight and reps.
     * 
     * @param weight
     * @param reps
     * @return
     **************************************************************************/
    public double calculateMax( int weight, int reps );
}
