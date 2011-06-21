package data.problems;

import data.AbstractProblem;

public class Problem021 extends AbstractProblem
{
    public Problem021()
    {
        setNumber( 21 );
        setDescription( "<html>Let d(n) be defined as the sum of proper divisors of n<br>"
                + "(numbers less than n which divide evenly into n).<br>"
                + "If d(a) = b and d(b) = a, where a ≠ b, then a and b<br>"
                + "are an amicable pair and each of a and b are called<br>"
                + "amicable numbers.<br><br>"
                + "For example, the proper divisors of 220 are 1, 2, 4, 5,<br>"
                + "10, 11, 20, 22, 44, 55 and 110; therefore d(220) = 284.<br>"
                + "The proper divisors of 284 are 1, 2, 4, 71 and 142;<br>"
                + "so d(284) = 220.<br><br>"
                + "Evaluate the sum of all the amicable numbers under 10000.</html>" );
    }

    @Override
    public String getSolution()
    {
        int sum = 0;
        int da = 0;
        int db = 0;
        int i = 2;
        while( i < 10000 )
        {
            da = sumProperDivisors( i );

            if( da != i )
            {
                db = sumProperDivisors( da );
                if( da < 10000 && db < 10000 && db == i )
                {
                    // this part is inefficient, because I could eliminate
                    // checking for da, but it's fast enough (<0.1 s)
                    sum += i;
                }
            }

            i++;
        }

        return "Sum: " + sum;
    }

    private int sumProperDivisors( int num )
    {
        int sum = 1;

        int limit = num;
        for( int i = 2; i < limit; i++ )
        {
            if( num % i == 0 )
            {
                limit = num / i;
                sum += i + limit;
            }
        }

        return sum;
    }
}
