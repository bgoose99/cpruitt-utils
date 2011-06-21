package data.problems;

import data.AbstractProblem;

public class Problem026 extends AbstractProblem
{
    public Problem026()
    {
        setNumber( 26 );
        setDescription( "<html>A unit fraction contains 1 in the numerator. The<br>"
                + "decimal representation of the unit fractions with denominators<br>"
                + "2 to 10 are given:<br><br>"
                + "1/2  =   0.5<br>"
                + "1/3  =   0.(3)<br>"
                + "1/4  =   0.25<br>"
                + "1/5  =   0.2<br>"
                + "1/6  =   0.1(6)<br>"
                + "1/7  =   0.(142857)<br>"
                + "1/8  =   0.125<br>"
                + "1/9  =   0.(1)<br>"
                + "1/10 =   0.1<br><br>"
                + "Where 0.1(6) means 0.166666..., and has a 1-digit recurring<br>"
                + "cycle. It can be seen that 1/7 has a 6-digit recurring cycle.<br><br>"
                + "Find the value of d < 1000 for which 1/d contains the longest<br>"
                + "recurring cycle in its decimal fraction part.</html>" );
    }

    @Override
    public String getSolution()
    {
        return null;
    }
}
