package data.problems;

import java.math.BigInteger;

import data.AbstractProblem;

public class Problem007 extends AbstractProblem
{
    public Problem007()
    {
        setNumber( 7 );
        setDescription( "<html>By listing the first six prime numbers: 2, 3, 5, 7,<br>"
                + "11, and 13, we can see that the 6th prime is 13.<br><br>"
                + "What is the 10,001st prime number?</html>" );
    }

    @Override
    public String getSolution()
    {
        BigInteger bi = new BigInteger( "2" );
        for( int i = 0; i < 10000; i++ )
        {
            bi = bi.nextProbablePrime();
        }

        return "10,001st prime: " + bi.toString();
    }

}
