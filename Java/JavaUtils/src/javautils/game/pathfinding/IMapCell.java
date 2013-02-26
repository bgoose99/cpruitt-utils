package javautils.game.pathfinding;

import java.awt.Color;
import java.awt.Graphics2D;

import javautils.game.IRenderable;

/*******************************************************************************
 * Simple map cell interface.
 ******************************************************************************/
public interface IMapCell extends IRenderable
{
    /***************************************************************************
     * Returns true if this cell is traversable, false otherwise.
     * 
     * @return
     **************************************************************************/
    boolean isTraversable();

    /***************************************************************************
     * Sets the traversable flag for this cell.
     * 
     * @param traversable
     **************************************************************************/
    void setTraversable( boolean traversable );

    /***************************************************************************
     * Returns the movement weight for this cell.
     * 
     * @return
     **************************************************************************/
    double getMovementWeight();

    /***************************************************************************
     * Sets the movement weight for this cell.
     * 
     * @param weight
     **************************************************************************/
    void setMovementWeight( double weight );

    /***************************************************************************
     * Renders this cell in an alternate color.
     * 
     * @param g2d
     * @param color
     **************************************************************************/
    void renderAlternateColor( Graphics2D g2d, Color color );

    /***************************************************************************
     * Returns the row index of this cell.
     * 
     * @return
     **************************************************************************/
    int getRow();

    /***************************************************************************
     * Returns the column index of this cell.
     * 
     * @return
     **************************************************************************/
    int getCol();
}
