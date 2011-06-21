package data.problems;

import java.util.Calendar;

import data.AbstractProblem;

public class Problem019 extends AbstractProblem
{
    public Problem019()
    {
        setNumber( 19 );
        setDescription( "<html>You are given the following information, but you may prefer<br>"
                + "to do some research for yourself.<br>"
                + "<ul><li>1 Jan 1900 was a Monday.</li>"
                + "<li>Thirty days has September,<br>"
                + "April, June and November.<br>"
                + "All the rest have thirty-one,<br>"
                + "Saving February alone,<br>"
                + "Which has twenty-eight, rain or shine.<br>"
                + "And on leap years, twenty-nine.</li>"
                + "<li>A leap year occurs on any year evenly divisible by 4,<br>"
                + "but not on a century unless it is divisible by 400.</li></ul><br>"
                + "How many Sundays fell on the first of the month during<br>"
                + "the twentieth century (1 Jan 1901 to 31 Dec 2000)?</html>" );
    }

    @Override
    public String getSolution()
    {
        int monthOfSundays = 0;
        Calendar cal = Calendar.getInstance();

        for( int year = 1901; year <= 2000; year++ )
        {
            for( int month = 0; month < 12; month++ )
            {
                cal.set( year, month, 1 );
                if( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY )
                {
                    monthOfSundays++;
                }
            }
        }

        return "Total Sundays: " + monthOfSundays;
    }

}
