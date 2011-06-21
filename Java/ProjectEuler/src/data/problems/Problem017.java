package data.problems;

import data.AbstractProblem;

public class Problem017 extends AbstractProblem
{
    public Problem017()
    {
        setNumber( 17 );
        setDescription( "<html>If the numbers 1 to 5 are written out in words:<br>"
                + "one, two, three, four, five, then there are 3 + 3 + 5 + 4 + 4 = 19<br>"
                + "letters used in total.<br><br>"
                + "If all the numbers from 1 to 1000 (one thousand) inclusive<br>"
                + "were written out in words, how many letters would be used?<br><br>"
                + "NOTE: Do not count spaces or hyphens. For example, 342<br>"
                + "(three hundred and forty-two) contains 23 letters and 115<br>"
                + "(one hundred and fifteen) contains 20 letters. The use of<br>"
                + "\"and\" when writing out numbers is in compliance with British<br>"
                + "usage.</html>" );
    }

    @Override
    public String getSolution()
    {
        int sum = 0;

        for( int i = 1; i <= 1000; i++ )
        {
            sum += getNumLetters( i );
        }

        return "Total: " + sum;
    }

    private int getNumLetters( int i )
    {
        int ONES[] = { 0, 3, 3, 5, 4, 4, 3, 5, 5, 4 }; // 36
        int TEENS[] = { 3, 6, 6, 8, 8, 7, 7, 9, 8, 8 }; // 70
        int TENS[] = { 0, 0, 6, 6, 5, 5, 5, 7, 6, 6 }; // 46
        int HUNDREDS = 7;
        int THOUSANDS = 8;
        int AND = 3;

        int numLetters = 0;
        int t;

        // thousands
        t = ( ONES[i / 1000] ) + ( THOUSANDS * ( ( i / 1000 ) == 0 ? 0 : 1 ) );
        numLetters += t;

        // hundreds
        i = i % 1000;
        t = ( ONES[i / 100] ) + ( HUNDREDS * ( ( i / 100 ) == 0 ? 0 : 1 ) );
        numLetters += t;

        // tens
        if( i >= 100 )
        {
            i = i % 100;
            if( i > 0 )
            {
                numLetters += AND;
            }
        }

        if( i >= 10 && i < 20 )
        {
            t = TEENS[i % 10];
            numLetters += t;
        } else
        {
            t = TENS[i / 10] + ONES[i % 10];
            numLetters += t;
        }

        return numLetters;
    }
}
