package data.problems;

import java.util.ArrayList;
import java.util.List;

import data.AbstractProblem;

public class Problem024 extends AbstractProblem
{
    public Problem024()
    {
        setNumber( 24 );
        setDescription( "<html>A permutation is an ordered arrangement of objects.<br>"
                + "For example, 3124 is one possible permutation of the<br>"
                + "digits 1, 2, 3 and 4. If all of the permutations are<br>"
                + "listed numerically or alphabetically, we call it lexicographic<br>"
                + "order. The lexicographic permutations of 0, 1 and 2 are:<br><br>"
                + "<b>012   021   102   120   201   210</b><br><br>"
                + "What is the millionth lexicographic permutation of the digits<br>"
                + "0, 1, 2, 3, 4, 5, 6, 7, 8 and 9?</html>" );
    }

    @Override
    public String getSolution()
    {
        List<Integer> list = new ArrayList<Integer>();
        int perms[] = new int[10];
        perms[0] = 9 * 8 * 7 * 6 * 5 * 4 * 3 * 2;
        perms[1] = 8 * 7 * 6 * 5 * 4 * 3 * 2;
        perms[2] = 7 * 6 * 5 * 4 * 3 * 2;
        perms[3] = 6 * 5 * 4 * 3 * 2;
        perms[4] = 5 * 4 * 3 * 2;
        perms[5] = 4 * 3 * 2;
        perms[6] = 3 * 2;
        perms[7] = 2;
        perms[8] = 1;
        perms[9] = 0;
        for( int i = 0; i < 10; i++ )
        {
            list.add( new Integer( i ) );
        }

        /**
         * we know how many permutations exist *per digit* for each of the
         * slots. E.g. there are 9! permutations for each digit in the very
         * first number position. 8! for each in the second, etc. So, we'll find
         * out which digit goes in each place by finding out what permutation
         * we're on.
         */
        String solution = "";
        int perm = 999999;
        for( int i = 0; i < 10; i++ )
        {
            int bin = 0;
            if( perms[i] != 0 )
                bin = perm / perms[i];

            if( bin < list.size() )
            {
                solution += list.get( bin ).toString();
                list.remove( bin );
            } else
            {
                System.out.println( "Error...bin too big: " + bin );
            }
            if( perms[i] != 0 )
                perm = perm % perms[i];
        }

        return "Solution: " + solution;
    }

}
