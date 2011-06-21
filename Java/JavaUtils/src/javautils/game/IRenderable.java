package javautils.game;

import java.awt.Graphics2D;

/*******************************************************************************
 * This interface is useful for any object that is rendered using a 2D graphics
 * object.
 ******************************************************************************/
public interface IRenderable
{
    /***************************************************************************
     * Renders this object.
     * 
     * @param g2d
     **************************************************************************/
    public void render( Graphics2D g2d );
}
