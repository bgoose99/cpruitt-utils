package javautils;

import java.awt.Color;

/*******************************************************************************
 * This class contains useful color manipulation functions.
 ******************************************************************************/
public final class ColorUtils
{
    /***************************************************************************
     * Scales a color's RGB values by the given scale, bounded to (0,255).
     * 
     * @param color
     * @param scale
     * @return
     **************************************************************************/
    public final static Color getScaledColor( Color color, int scale )
    {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        red += scale;
        if( red > 255 )
            red = 255;
        else if( red < 0 )
            red = 0;

        green += scale;
        if( green > 255 )
            green = 255;
        else if( green < 0 )
            green = 0;

        blue += scale;
        if( blue > 255 )
            blue = 255;
        else if( blue < 0 )
            blue = 0;

        return new Color( red, green, blue );
    }
}
