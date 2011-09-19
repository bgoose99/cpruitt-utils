package javautils;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

/*******************************************************************************
 * This class manages icons used in an application so that a single icon's image
 * is loaded in memory only once.
 ******************************************************************************/
public final class IconManager
{
    /***************************************************************************
     * This enum describes an icon's size. NOTE: Not all images are available in
     * all sizes.
     **************************************************************************/
    public static enum IconSize
    {
        X16, X32, X64;
    }

    // TODO: Add more images as needed
    public static final String ACCEPT = "accept.png";
    public static final String ADD = "add.png";
    public static final String APPLICATION_GO = "application_go.png";
    public static final String ARROW_LEFT = "arrow_left.png";
    public static final String ARROW_REFRESH = "arrow_refresh.png";
    public static final String ARROW_RIGHT = "arrow_right.png";
    public static final String BRICK_ADD = "brick_add.png";
    public static final String BRICK_DELETE = "brick_delete.png";
    public static final String BRICK_GO = "brick_go.png";
    public static final String BULB = "bulb.png";
    public static final String BULLET_ADD = "bullet_add.png";
    public static final String BULLET_DELETE = "bullet_delete.png";
    public static final String BULLET_GO = "bullet_go.png";
    public static final String CANCEL = "cancel.png";
    public static final String CLOCK = "clock.png";
    public static final String COG = "cog.png";
    public static final String CUT = "cut.png";
    public static final String DICE = "dice.png";
    public static final String DISK = "disk.png";
    public static final String DISK_MULTIPLE = "disk_multiple.png";
    public static final String DOCUMENT_PAGE = "document_page.png";
    public static final String DOCUMENT_PAGE_LAST = "document_page_last.png";
    public static final String DOCUMENT_PAGE_NEXT = "document_page_next.png";
    public static final String DOCUMENT_PAGE_PREVIOUS = "document_page_previous.png";
    public static final String DOOR_IN = "door_in.png";
    public static final String DRAW_ERASER = "draw_eraser.png";
    public static final String ERROR = "error.png";
    public static final String EXCLAMATION = "exclamation.png";
    public static final String FIND = "find.png";
    public static final String FOLDER = "folder.png";
    public static final String FOLDER_ADD = "folder_add.png";
    public static final String FOLDER_DELETE = "folder_delete.png";
    public static final String HELP = "help.png";
    public static final String HOUSE = "house.png";
    public static final String KEY_A = "key_a.png";
    public static final String KEYBOARD_MAGNIFY = "keyboard_magnify.png";
    public static final String LIGHTBULB = "lightbulb.png";
    public static final String LOCK = "lock.png";
    public static final String PAGE_ADD = "page_add.png";
    public static final String RESULTSET_FIRST = "resultset_first.png";
    public static final String RESULTSET_LAST = "resultset_last.png";
    public static final String RESULTSET_NEXT = "resultset_next.png";
    public static final String RESULTSET_PREVIOUS = "resultset_previous.png";
    public static final String STOP = "stop.png";
    public static final String UNIVERSAL_BINARY = "universal_binary.png";
    public static final String USER = "user.png";
    public static final String USER_SILHOUETTE = "user_silhouette.png";
    public static final String YINYANG = "yinyang.png";

    // Base path to all images
    private static final String IMAGE_PATH = "/javautils/icons/";

    // Sub-directories
    private static final String X16_SUBDIR = "x16/";
    private static final String X32_SUBDIR = "x32/";
    private static final String X64_SUBDIR = "x64/";

    private static HashMap<String, ImageIcon> imageMap = new HashMap<String, ImageIcon>();

    /***************************************************************************
     * Private constructor prevents instantiation.
     **************************************************************************/
    private IconManager()
    {
        ;
    }

    /***************************************************************************
     * Loads an image into memory.
     * 
     * @param name
     * @return
     **************************************************************************/
    private static ImageIcon loadImage( String name )
    {
        return new ImageIcon( getIconURL( name ) );
    }

    /***************************************************************************
     * Returns the supplied icon name's URL.
     * 
     * @param name
     * @return
     **************************************************************************/
    private static URL getIconURL( String name )
    {
        return Utils.loadResourceURL( IMAGE_PATH + name );
    }

    /***************************************************************************
     * Returns the requested icon from the map.
     * 
     * @param name
     * @return
     **************************************************************************/
    private static ImageIcon getImageFromMap( String name )
    {
        if( !imageMap.containsKey( name ) )
            imageMap.put( name, loadImage( name ) );
        return imageMap.get( name );
    }

    /***************************************************************************
     * Returns the sub-directory associated with <code>size</code>.
     * 
     * @param size
     * @return
     **************************************************************************/
    private static String getIconSubdirectory( IconSize size )
    {
        switch( size )
        {
        case X16:
            return X16_SUBDIR;
        case X32:
            return X32_SUBDIR;
        case X64:
            return X64_SUBDIR;
        default:
            return "";
        }
    }

    /***************************************************************************
     * Returns the icon with the specified name.
     * 
     * @param name
     * @return
     **************************************************************************/
    public static ImageIcon getIcon( String name, IconSize size )
    {
        return getImageFromMap( getIconSubdirectory( size ) + name );
    }

    /***************************************************************************
     * Returns the image with the specified name.
     * 
     * @param name
     * @return
     **************************************************************************/
    public static Image getImage( String name, IconSize size )
    {
        ImageIcon icon = getIcon( name, size );
        if( icon != null )
            return icon.getImage();
        else
            return null;
    }
}
