package javautils.game;

import java.awt.Graphics2D;

/*******************************************************************************
 * {@link Sprite} handler interface.
 ******************************************************************************/
public interface ISpriteHandler
{
    /***************************************************************************
     * Adds a sprite to this handler.
     * 
     * @param sprite
     **************************************************************************/
    public void addSprite( Sprite sprite );

    /***************************************************************************
     * Removes a sprite from this handler.
     * 
     * @param sprite
     **************************************************************************/
    public void removeSprite( Sprite sprite );

    /***************************************************************************
     * Renders all sprites associated with this handler.
     * 
     * @param g2d
     **************************************************************************/
    public void renderSprites( Graphics2D g2d );

    /***************************************************************************
     * Moves all sprites associated with this handler.
     * 
     * @param deltaSeconds
     **************************************************************************/
    public void moveSprites( double deltaSeconds );

    /***************************************************************************
     * Returns the number of sprites associated with this handler.
     * 
     * @return
     **************************************************************************/
    public int getNumSprites();
}
