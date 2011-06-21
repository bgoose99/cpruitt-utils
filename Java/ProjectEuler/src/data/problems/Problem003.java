package data.problems;

import java.math.BigInteger;

import data.AbstractProblem;

public class Problem003 extends AbstractProblem
{
    public Problem003()
    {
        setNumber( 3 );
        setDescription( "<html>The prime factors of 13195 are 5, 7, 13 and 29.<br><br>"
                + "What is the largest prime factor of the number <b>600851475143</b>?</html>" );
    }

    @Override
    public String getSolution()
    {
        BigInteger compare = new BigInteger( "600851475143" );
        BigInteger temp = new BigInteger( "3" );
        BigInteger factor = new BigInteger( "0" );

        while( temp.compareTo( compare ) <= 0 )
        {
            if( compare.remainder( temp ).compareTo( BigInteger.ZERO ) == 0 )
            {
                compare = compare.divide( temp );
                factor = new BigInteger( temp.toString() );
            }
            temp = temp.nextProbablePrime();
        }

        return "Highest prime factor: " + factor;
    }

}
