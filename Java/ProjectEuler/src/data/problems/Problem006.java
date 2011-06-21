package data.problems;

import data.AbstractProblem;

public class Problem006 extends AbstractProblem
{
    public Problem006()
    {
        setNumber( 6 );
        setDescription( "<html>The sum of the squares of the first ten natural numbers is,<br>"
                + "1^2 + 2^2 + ... + 10^2 = 385<br>"
                + "The square of the sum of the first ten natural numbers is,<br>"
                + "(1 + 2 + ... + 10)^2 = 55^2 = 3025<br>"
                + "Hence the difference between the sum of the squares of the first<br>"
                + "ten natural numbers and the square of the sum is 3025 − 385 = 2640.<br><br>"
                + "Find the difference between the sum of the squares of the first<br>"
                + "one hundred natural numbers and the square of the sum.</html>" );
    }

    @Override
    public String getSolution()
    {
        long sumOfSquares = 0;
        long squareOfSums = 0;
        for( int i = 1; i < 101; i++ )
        {
            sumOfSquares += i * i;
            squareOfSums += i;
        }
        squareOfSums *= squareOfSums;

        return "Difference: " + Math.abs( squareOfSums - sumOfSquares );
    }

}
