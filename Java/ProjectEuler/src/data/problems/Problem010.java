package data.problems;

import java.math.BigInteger;

import data.AbstractProblem;

public class Problem010 extends AbstractProblem
{
    public Problem010()
    {
        setNumber( 10 );
        setDescription( "<html>The sum of the primes below 10 is 2 + 3 + 5 + 7 = 17.<br><br>"
                + "Find the sum of all the primes below two million.</html>" );
    }

    @Override
    public String getSolution()
    {
        BigInteger prime = new BigInteger( "2" );
        BigInteger limit = new BigInteger( "2000000" );
        BigInteger sum = prime;

        while( true )
        {
            prime = prime.nextProbablePrime();
            if( prime.compareTo( limit ) >= 0 )
                break;
            sum = sum.add( prime );
        }

        return "Sum: " + sum.toString();
    }

}
