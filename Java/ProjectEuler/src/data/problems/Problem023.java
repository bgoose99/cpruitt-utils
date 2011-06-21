package data.problems;

import java.util.ArrayList;
import java.util.List;

import data.AbstractProblem;

public class Problem023 extends AbstractProblem
{
    public Problem023()
    {
        setNumber( 23 );
        setDescription( "<html>A perfect number is a number for which the sum of its<br>"
                + "proper divisors is exactly equal to the number. For<br>"
                + "example, the sum of the proper divisors of 28 would be<br>"
                + "1 + 2 + 4 + 7 + 14 = 28, which means that 28 is a perfect<br>"
                + "number.<br><br>"
                + "A number n is called deficient if the sum of its proper<br>"
                + "divisors is less than n and it is called abundant if this<br>"
                + "sum exceeds n.<br><br>"
                + "As 12 is the smallest abundant number, 1 + 2 + 3 + 4 + 6 = 16,<br>"
                + "the smallest number that can be written as the sum of two<br>"
                + "abundant numbers is 24. By mathematical analysis, it can<br>"
                + "be shown that all integers greater than 28123 can be written<br>"
                + "as the sum of two abundant numbers. However, this upper limit<br>"
                + "cannot be reduced any further by analysis even though it is<br>"
                + "known that the greatest number that cannot be expressed as<br>"
                + "the sum of two abundant numbers is less than this limit.<br><br>"
                + "Find the sum of all the positive integers which cannot be<br>"
                + "written as the sum of two abundant numbers.</html>" );
    }

    @Override
    public String getSolution()
    {
        List<Integer> list = new ArrayList<Integer>();
        long sum = 0;

        // find all abundant number < 28123
        for( int i = 1; i < 28123; i++ )
        {
            if( isAbundant( i ) )
                list.add( i );
        }

        for( int i = 1; i < 28123; i++ )
        {
            if( !isAbundantSummable( i, list ) )
                sum += i;
        }

        return "Sum: " + sum;
    }

    private boolean isAbundant( int n )
    {
        int sum = 1;

        for( int i = 2; i < n; i++ )
        {
            if( n % i == 0 )
                sum += i;
        }

        return ( sum > n );
    }

    private boolean isAbundantSummable( int n, List<Integer> abundants )
    {
        int a, b;
        boolean done = false;
        for( int i = 0; i < abundants.size(); i++ )
        {
            a = abundants.get( i ).intValue();
            if( a > n )
                break;

            for( int j = i; j < abundants.size(); j++ )
            {
                b = abundants.get( j ).intValue();
                if( b > n )
                    break;

                if( a + b == n )
                {
                    done = true;
                    break;
                }
            }
            if( done )
                break;
        }

        return done;
    }
}
