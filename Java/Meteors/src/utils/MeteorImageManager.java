package utils;

import java.awt.Image;

import javautils.ImageManager;

public class MeteorImageManager extends ImageManager
{
    public static final String CROSSHAIR_CURSOR = "crosshair_cursor.png";
    public static final String BACKGROUND = "background.png";
    public static final String SMILY_FACE = "face-grin.png";
    public static final String SAD_FACE = "face-sad.png";
    public static final String CRYING_FACE = "face-crying.png";
    public static final String DEVIL_FACE = "face-devilish-2.png";
    public static final String HEALTH_BAR = "health_bar.png";
    public static final String METEOR_1 = "meteor-1.png";
    public static final String METEOR_2 = "meteor-2.png";
    public static final String METEOR_3 = "meteor-3.png";
    public static final String POWERUP_HEALTH = "powerup-health.png";
    public static final String POWERUP_BIG = "powerup-big.png";
    public static final String POWERUP_PASS_THROUGH = "powerup-pass-through.png";
    public static final String POWERUP_SPRAY = "powerup-spray.png";
    public static final String POWERUP_TRIPLE = "powerup-triple.png";

    private static final String basePath = "/resources/images/";

    public static Image getImage( String name )
    {
        return ImageManager.getImage( basePath + name );
    }
}
