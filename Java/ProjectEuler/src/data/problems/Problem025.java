package data.problems;

import java.math.BigInteger;

import data.AbstractProblem;

public class Problem025 extends AbstractProblem
{
    public Problem025()
    {
        setNumber( 25 );
        setDescription( "<html>The Fibonacci sequence is defined by the recurrence<br>"
                + "relation:<br><br>"
                + "<b>F(n) = F(n−1) + F(n−2), where F(1) = 1 and F(2) = 1.</b><br><br>"
                + "Hence the first 12 terms will be:<br><br>"
                + "<b>F(1)  = 1<br>"
                + "F(2)  = 1<br>"
                + "F(3)  = 2<br>"
                + "F(4)  = 3<br>"
                + "F(5)  = 5<br>"
                + "F(6)  = 8<br>"
                + "F(7)  = 13<br>"
                + "F(8)  = 21<br>"
                + "F(9)  = 34<br>"
                + "F(10) = 55<br>"
                + "F(11) = 89<br>"
                + "F(12) = 144</b><br><br>"
                + "The 12th term, F(12), is the first term to contain three digits.<br>"
                + "What is the first term in the Fibonacci sequence to contain 1000 digits?</html>" );
    }

    @Override
    public String getSolution()
    {
        BigInteger a = new BigInteger( "1" );
        BigInteger b = new BigInteger( "1" );
        BigInteger c = a.add( b );

        int index = 3;
        while( c.toString().length() < 1000 )
        {
            a = b;
            b = c;
            c = a.add( b );
            index++;
        }

        return "Index " + index + " has " + c.toString().length() + " digits";
    }

}
