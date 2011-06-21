package data.problems;

import java.math.BigInteger;

import data.AbstractProblem;

public class Problem020 extends AbstractProblem
{
    public Problem020()
    {
        setNumber( 20 );
        setDescription( "<html>n! means n × (n − 1) × ... × 3 × 2 × 1<br><br>"
                + "For example, 10! = 10 × 9 × ... × 3 × 2 × 1 = 3628800,<br>"
                + "and the sum of the digits in the number 10! is<br>"
                + "3 + 6 + 2 + 8 + 8 + 0 + 0 = 27.<br><br>"
                + "Find the sum of the digits in the number 100!</html>" );
    }

    @Override
    public String getSolution()
    {
        BigInteger factorial = new BigInteger( "1" );
        for( int i = 2; i <= 100; i++ )
        {
            factorial = factorial.multiply( new BigInteger( "" + i ) );
        }

        String s = factorial.toString();
        int sum = 0;
        for( int i = 0; i < s.length(); i++ )
        {
            sum += Integer.parseInt( s.substring( i, i + 1 ) );
        }

        return "Sum: " + sum;
    }

}
