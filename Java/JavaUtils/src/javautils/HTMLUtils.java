package javautils;

/*******************************************************************************
 * Contains some useful HTML functions.
 ******************************************************************************/
public final class HTMLUtils
{
    public final static String BG_COLOR = "#ADD8E6";
    public final static String HEADING = "#888888";
    public final static String EVEN = "#DEDEDE";
    public final static String ODD = "#FFFFFF";
    public final static String TABLE_BG = "#666666";
    public final static String GREEN = "#008800";
    public final static String RED = "#880000";

    public final static int CELLSPACING = 2;
    public final static int CELLPADDING = 1;
    public final static int BORDER = 1;

    /***************************************************************************
     * Returns a color for an HTML table row. These colors are defined as
     * <code>HTMLUtils.ODD</code> and <code>HTMLUtils.EVEN</code>. Using this
     * method allows one to easily have alternating colors in HTML tables.
     * 
     * @param row
     * @return
     **************************************************************************/
    public static String getRowColor( int row )
    {
        return ( ( row % 2 ) == 1 ? ODD : EVEN );
    }
}
