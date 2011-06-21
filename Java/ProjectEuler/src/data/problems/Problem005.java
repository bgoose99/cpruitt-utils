package data.problems;

import data.AbstractProblem;

public class Problem005 extends AbstractProblem
{
    public Problem005()
    {
        setNumber( 5 );
        setDescription( "<html>2520 is the smallest number that can be divided<br>"
                + "by each of the numbers from 1 to 10 without any remainder.<br><br>"
                + "What is the smallest positive number that is evenly divisible<br>"
                + "by all of the numbers from 1 to 20?</html>" );
    }

    @Override
    public String getSolution()
    {
        // need only check 20 down to 11 as all other number are factors of
        // these
        for( int i = 20; i < 1000000000; i += 20 )
        {
            for( int j = 20; j > 10; j-- )
            {
                if( i % j != 0 )
                    break;
                if( j == 11 )
                    return "LCM: " + i;
            }
        }

        return "Solution not found";
    }

}
