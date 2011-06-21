package data.problems;

import data.AbstractProblem;

public class Problem009 extends AbstractProblem
{
    public Problem009()
    {
        setNumber( 9 );
        setDescription( "<html>A Pythagorean triplet is a set of three natural numbers,<br>"
                + "<b>a &lt; b &lt; c</b>, for which, <b>a^2 + b^2 = c^2</b><br>"
                + "For example, 3^2 + 4^2 = 9 + 16 = 25 = 5^2.<br><br>"
                + "There exists exactly one Pythagorean triplet for which<br>"
                + "a + b + c = 1000.<br>" + "Find the product abc.</html>" );
    }

    @Override
    public String getSolution()
    {
        int a = 0;
        int b = 0;
        int c = 0;
        OUT:
        {
            for( a = 1; a < 1000; a++ )
            {
                for( b = a + 1; b < 1000; b++ )
                {
                    for( c = b + 1; c < 1000; c++ )
                    {
                        if( ( a + b + c ) == 1000
                                && isPythagoreanTriplet( a, b, c ) )
                            break OUT;
                    }
                }
            }
        }

        return "Triplet: " + a + ", " + b + ", " + c + "\n" + "Product: "
                + ( a * b * c );
    }

    private boolean isPythagoreanTriplet( int a, int b, int c )
    {
        return ( ( ( a * a ) + ( b * b ) ) == ( c * c ) );
    }
}
