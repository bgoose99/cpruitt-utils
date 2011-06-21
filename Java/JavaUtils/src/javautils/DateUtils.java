package javautils;

import java.util.Calendar;

/*******************************************************************************
 * Contains some useful functions for accessing a single date/time associated
 * with a run of an application.
 ******************************************************************************/
public final class DateUtils
{
    /***************************************************************************
     * This is the calendar object used for all time/date calls. Note that this
     * calendar is not initialized until one of the functions has been called.
     * Once initialized, it will not change. As such, this calendar is not
     * necessarily associated with the start time of the calling application.
     **************************************************************************/
    private static Calendar calendar = null;

    private DateUtils()
    {
    }

    /***************************************************************************
     * Initializes the calendar object, if necessary.
     * 
     * @see javautils.DateUtils#calendar
     **************************************************************************/
    private static void init()
    {
        if( calendar == null )
            calendar = Calendar.getInstance();
    }

    /***************************************************************************
     * Returns the month.
     * 
     * @return
     * @see javautils.DateUtils#calendar
     **************************************************************************/
    public static int getMonth()
    {
        init();
        return ( calendar.get( Calendar.MONDAY ) + 1 );
    }

    /***************************************************************************
     * Returns the day.
     * 
     * @return
     * @see javautils.DateUtils#calendar
     **************************************************************************/
    public static int getDay()
    {
        init();
        return calendar.get( Calendar.DAY_OF_MONTH );
    }

    /***************************************************************************
     * Returns the year.
     * 
     * @return
     * @see javautils.DateUtils#calendar
     **************************************************************************/
    public static int getYear()
    {
        init();
        return calendar.get( Calendar.YEAR );
    }

    /***************************************************************************
     * Returns the hour.
     * 
     * @return
     * @see javautils.DateUtils#calendar
     **************************************************************************/
    public static int getHour()
    {
        init();
        return calendar.get( Calendar.HOUR );
    }

    /***************************************************************************
     * Returns the minute.
     * 
     * @return
     * @see javautils.DateUtils#calendar
     **************************************************************************/
    public static int getMinute()
    {
        init();
        return calendar.get( Calendar.MINUTE );
    }

    /***************************************************************************
     * Returns the second.
     * 
     * @return
     * @see javautils.DateUtils#calendar
     **************************************************************************/
    public static int getSecond()
    {
        init();
        return calendar.get( Calendar.SECOND );
    }

    /***************************************************************************
     * Returns a formatted date string, with the fields separated by
     * underscores.
     * 
     * @return
     * @see javautils.DateUtils#calendar
     **************************************************************************/
    public static String getUnderscoreSeparatedDate()
    {
        return new String( getYear() + "_" + getMonth() + "_" + getDay() );
    }

    /***************************************************************************
     * Returns a formatted time string, with the fields separated by
     * underscores.
     * 
     * @return
     * @see javautils.DateUtils#calendar
     **************************************************************************/
    public static String getUnderscoreSeparatedTime()
    {
        return new String( getHour() + "_" + getMinute() + "_" + getSecond() );
    }

    /***************************************************************************
     * Returns a formatted date/time string, with the fields separated by
     * underscores.
     * 
     * @return
     * @see javautils.DateUtils#calendar
     **************************************************************************/
    public static String getUnderscoreSeparatedDateTime()
    {
        return new String( getUnderscoreSeparatedDate() + "_"
                + getUnderscoreSeparatedTime() );
    }
}
