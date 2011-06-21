package data.problems;

import data.AbstractProblem;

public class Problem018 extends AbstractProblem
{
    public Problem018()
    {
        setNumber( 18 );
        setDescription( "<html>By starting at the top of the triangle below and moving to<br>"
                + "adjacent numbers on the row below, the maximum total from<br>"
                + "top to bottom is 23.<br><br>"
                + "<center><font color=\"red\"><b>3</b></font><br>"
                + "<font color=\"red\"><b>7</b></font> 4<br>"
                + "2 <font color=\"red\"><b>4</b></font> 6<br>"
                + "8 5 <font color=\"red\"><b>9</b></font> 3</center><br><br>"
                + "That is, 3 + 7 + 4 + 9 = 23.<br><br>"
                + "Find the maximum total from top to bottom of the triangle below:<br><br>"
                + "<center>75<br>"
                + "95 64<br>"
                + "17 47 82<br>"
                + "18 35 87 10<br>"
                + "20 04 82 47 65<br>"
                + "19 01 23 75 03 34<br>"
                + "88 02 77 73 07 63 67<br>"
                + "99 65 04 28 06 16 70 92<br>"
                + "41 41 26 56 83 40 80 70 33<br>"
                + "41 48 72 33 47 32 37 16 94 29<br>"
                + "53 71 44 65 25 43 91 52 97 51 14<br>"
                + "70 11 33 28 77 73 17 78 39 68 17 57<br>"
                + "91 71 52 38 17 14 91 43 58 50 27 29 48<br>"
                + "63 66 04 68 89 53 67 30 73 16 69 87 40 31<br>"
                + "04 62 98 27 23 09 70 98 73 93 38 53 60 04 23</center><br><br>"
                + "NOTE: As there are only 16384 routes, it is possible<br>"
                + "to solve this problem by trying every route. However,<br>"
                + "Problem 67, is the same challenge with a triangle containing<br>"
                + "one-hundred rows; it cannot be solved by brute force, and<br>"
                + "requires a clever method! ;o)</html>" );
    }

    @Override
    public String getSolution()
    {
        // for this one, each position in the triangle (above the last row) has
        // two paths from it
        // if we build a new triangle with the sums of the paths, we just have
        // to find the max in the last row when we're done
        int[][] m = { { 75 }, { 95, 64 }, { 17, 47, 82 }, { 18, 35, 87, 10 },
                { 20, 4, 82, 47, 65 }, { 19, 1, 23, 75, 3, 34 },
                { 88, 2, 77, 73, 7, 63, 67 }, { 99, 65, 4, 28, 6, 16, 70, 92 },
                { 41, 41, 26, 56, 83, 40, 80, 70, 33 },
                { 41, 48, 72, 33, 47, 32, 37, 16, 94, 29 },
                { 53, 71, 44, 65, 25, 43, 91, 52, 97, 51, 14 },
                { 70, 11, 33, 28, 77, 73, 17, 78, 39, 68, 17, 57 },
                { 91, 71, 52, 38, 17, 14, 91, 43, 58, 50, 27, 29, 48 },
                { 63, 66, 4, 68, 89, 53, 67, 30, 73, 16, 69, 87, 40, 31 },
                { 4, 62, 98, 27, 23, 9, 70, 98, 73, 93, 38, 53, 60, 4, 23 } };

        int[][] sum = m;

        for( int i = 1; i < m.length; i++ )
        {
            // left edge
            sum[i][0] = m[i][0] + m[i - 1][0];
            // right edge
            sum[i][m[i].length - 1] = m[i][m[i].length - 1]
                    + m[i - 1][m[i - 1].length - 1];
            // middle
            for( int j = 1; j < m[i].length - 1; j++ )
            {
                sum[i][j] = Math.max( m[i][j] + m[i - 1][j - 1], m[i][j]
                        + m[i - 1][j] );
            }
        }

        // find max along bottom of triangle
        int max = 0;
        for( int i = 0; i < m[m.length - 1].length; i++ )
        {
            max = Math.max( max, m[m.length - 1][i] );
        }

        return "Max route: " + max;
    }
}
