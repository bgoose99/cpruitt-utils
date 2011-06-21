package data.problems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.AbstractProblem;

public class Problem004 extends AbstractProblem
{
    public Problem004()
    {
        setNumber( 4 );
        setDescription( "<html>A palindromic number reads the same both ways.<br>"
                + "The largest palindrome made from the product of two 2-digit<br>"
                + "numbers is <b>9009 = 91 × 99</b>.<br><br>"
                + "Find the largest palindrome made from the product of two 3-digit numbers.</html>" );
    }

    @Override
    public String getSolution()
    {
        List<Integer> candidates = new ArrayList<Integer>();

        // start with 999 x 999 and work backwards
        // first palindrome wins
        for( int i = 999; i > 99; i-- )
        {
            for( int j = 999; j > 99; j-- )
            {
                if( isPalindrome( i * j ) )
                {
                    candidates.add( ( i * j ) );
                }
            }
        }
        Collections.sort( candidates );

        return candidates.size() == 0 ? "Solution not found"
                : "Largest palindrome: "
                        + candidates.get( candidates.size() - 1 );
    }

    private boolean isPalindrome( int i )
    {
        String forward = Integer.toString( i );

        StringBuffer sBuf = new StringBuffer( forward );
        sBuf = sBuf.reverse();

        String reverse = sBuf.toString();

        return forward.equals( reverse );
    }
}
