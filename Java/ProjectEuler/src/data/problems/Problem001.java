package data.problems;

import data.AbstractProblem;

public class Problem001 extends AbstractProblem
{
    public Problem001()
    {
        setNumber( 1 );
        setDescription( "<html>If we list all the natural numbers below 10 that are<br>"
                + "multiples of 3 or 5, we get 3, 5, 6 and 9.<br>"
                + "The sum of these multiples is 23.<br><br>"
                + "Find the sum of all the multiples of 3 or 5 below 1000.</html>" );
    }

    @Override
    public String getSolution()
    {
        int answer = 0;

        for( int i = 3; i < 1000; i++ )
            if( i % 3 == 0 || i % 5 == 0 )
                answer += i;

        return "Sum: " + answer;
    }

}
