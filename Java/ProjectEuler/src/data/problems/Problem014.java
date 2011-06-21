package data.problems;

import data.AbstractProblem;

public class Problem014 extends AbstractProblem
{
    public Problem014()
    {
        setNumber( 14 );
        setDescription( "<html>The following iterative sequence is defined for the set of<br>"
                + "positive integers:<br><br>"
                + "<b>n → n/2 (n is even)<br>"
                + "n → 3n + 1 (n is odd)</b><br><br>"
                + "Using the rule above and starting with 13, we generate the following sequence:<br><br>"
                + "<b>13 → 40 → 20 → 10 → 5 → 16 → 8 → 4 → 2 → 1</b><br><br>"
                + "It can be seen that this sequence (starting at 13 and finishing at 1)<br>"
                + "contains 10 terms. Although it has not been proved yet<br>"
                + "(Collatz Problem), it is thought that all starting numbers finish at 1.<br><br>"
                + "Which starting number, under one million, produces the longest chain?<br><br>"
                + "NOTE: Once the chain starts the terms are allowed to go above one million.</html>" );
    }

    @Override
    public String getSolution()
    {
        int max = 0;
        int index = 0;

        for( int i = 1; i < 1000000; i++ )
        {
            int len = getChainLength( i );
            if( len > max )
            {
                max = len;
                index = i;
            }
        }

        return "Starting number " + index + " had length " + max;
    }

    private int getChainLength( long startingNumber )
    {
        int counter = 1;

        while( startingNumber > 1 )
        {
            if( startingNumber % 2 == 0 )
            {
                startingNumber = startingNumber / 2;
            } else
            {
                startingNumber = ( 3 * startingNumber ) + 1;
            }
            counter++;
        }

        return counter;
    }
}
