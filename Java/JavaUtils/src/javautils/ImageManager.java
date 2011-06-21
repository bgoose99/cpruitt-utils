package javautils;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

/*******************************************************************************
 * This class will act as a generic image manager for any application that
 * requires access to several images. Simply extending this class and adding in
 * the appropriate image names will provide the user with a usable manager. The
 * base implementation of this class is very similar to {@link IconManager}
 * without all of the default image names. Derived classes will call
 * {@link #getImage(String)} in order to access images from the map.
 ******************************************************************************/
public abstract class ImageManager
{
    /** The map that keeps track of all names/images */
    private static Map<String, Image> imageMap = new HashMap<String, Image>();

    /***************************************************************************
     * Loads the specified image.
     * 
     * @param name
     * @return
     **************************************************************************/
    private static Image loadImage( String name )
    {
        ImageIcon icon = new ImageIcon( getImageURL( name ) );
        return icon.getImage();
    }

    /***************************************************************************
     * Returns the URL associated with the supplied name.
     * 
     * @param name
     * @return
     **************************************************************************/
    private static URL getImageURL( String name )
    {
        return Utils.loadResourceURL( name );
    }

    /***************************************************************************
     * Returns the image with the given name. If it does not exist, it is added
     * to the map.
     * 
     * @param name
     * @return
     **************************************************************************/
    private static Image getImageFromMap( String name )
    {
        if( !imageMap.containsKey( name ) )
            imageMap.put( name, loadImage( name ) );
        return imageMap.get( name );
    }

    /***************************************************************************
     * Returns the image with the given name. This method should be called from
     * a similar method in the derived class.
     * 
     * @param name
     * @return
     **************************************************************************/
    protected static Image getImage( String name )
    {
        return getImageFromMap( name );
    }
}
