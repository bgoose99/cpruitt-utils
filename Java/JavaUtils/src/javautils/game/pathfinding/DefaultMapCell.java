package javautils.game.pathfinding;

import java.awt.Color;
import java.awt.Graphics2D;

/*******************************************************************************
 * Simple {@link IMapCell} implementation.
 ******************************************************************************/
public class DefaultMapCell implements IMapCell
{
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    private boolean traversable;
    private double weight;

    private final int row;
    private final int col;

    /***************************************************************************
     * Constructor
     * 
     * @param traversable
     * @param weight
     * @param row
     * @param col
     **************************************************************************/
    public DefaultMapCell( boolean traversable, double weight, int row, int col )
    {
        this.traversable = traversable;
        this.weight = weight;
        this.row = row;
        this.col = col;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.IRenderable#render(java.awt.Graphics2D)
     */
    @Override
    public void render( Graphics2D g2d )
    {
        renderAlternateColor( g2d, traversable ? Color.lightGray
                : Color.darkGray );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMapCell#isTraversable()
     */
    @Override
    public boolean isTraversable()
    {
        return traversable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMapCell#setTraversable(boolean)
     */
    @Override
    public void setTraversable( boolean traversable )
    {
        this.traversable = traversable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMapCell#getMovementWeight()
     */
    @Override
    public double getMovementWeight()
    {
        return weight;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMapCell#setMovementWeight(double)
     */
    @Override
    public void setMovementWeight( double weight )
    {
        this.weight = weight;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.game.pathfinding.IMapCell#renderAlternateColor(java.awt.Graphics2D
     * , java.awt.Color)
     */
    @Override
    public void renderAlternateColor( Graphics2D g2d, Color color )
    {
        g2d.setColor( color );
        g2d.fill3DRect( col * WIDTH, row * HEIGHT, WIDTH - 1, HEIGHT - 1, true );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMapCell#getRow()
     */
    @Override
    public int getRow()
    {
        return row;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMapCell#getCol()
     */
    @Override
    public int getCol()
    {
        return col;
    }

}
