package data.problems;

import java.math.BigInteger;

import data.AbstractProblem;

public class Problem016 extends AbstractProblem
{
    public Problem016()
    {
        setNumber( 16 );
        setDescription( "<html>2^15 = 32768 and the sum of its digits is <br>"
                + "<b>3 + 2 + 7 + 6 + 8 = 26.</b><br><br>"
                + "What is the sum of the digits of the number 2^1000?</html>" );
    }

    @Override
    public String getSolution()
    {
        int sum = 0;

        BigInteger bi = new BigInteger( "2" );
        bi = bi.pow( 1000 );

        String s = bi.toString();
        for( int i = 0; i < s.length(); i++ )
        {
            sum += Integer.parseInt( s.substring( i, i + 1 ) );
        }

        return "Sum: " + sum;
    }

}
