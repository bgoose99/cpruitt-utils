package data.problems;

import data.AbstractProblem;

public class Problem015 extends AbstractProblem
{
    public Problem015()
    {
        setNumber( 15 );
        setDescription( "<html>Starting in the top left corner of a 2×2 grid, there are 6<br>"
                + "routes (without backtracking) to the bottom right corner.<br><br>"
                + "How many routes are there through a 20×20 grid?</html>" );
    }

    @Override
    public String getSolution()
    {
        /**
         * This is a vast simplification of this problem. We essentially have a
         * 21 x 21 matrix of vertices, or points on our grid. There is exactly
         * one route to get to any point in the first row or column of this
         * matrix. After that, you simply sum up the number of routes to the
         * left and above the current vertex to see how many routes there are to
         * the current vertex. After that's complete, you can see how many
         * routes there are to any node in the grid.
         */

        long[][] m = new long[21][21];
        // initialize the first row/column
        for( int i = 0; i < m.length; i++ )
        {
            m[0][i] = 1;
            m[i][0] = 1;
        }

        // calculate the routes for the remaining rows/columns
        for( int i = 1; i < m.length; i++ )
        {
            for( int j = 1; j < m[i].length; j++ )
            {
                m[i][j] = m[i][j - 1] + m[i - 1][j];
            }
        }

        // look at the last one
        return "Number of routes: " + m[20][20];
    }

}
