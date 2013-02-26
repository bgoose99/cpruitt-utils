package javautils.game.pathfinding;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

/*******************************************************************************
 * Simple {@link IMap} implementation.
 ******************************************************************************/
public class DefaultMap implements IMap
{
    private IMapCell[][] map;

    /***************************************************************************
     * Constructor
     * 
     * @param rows
     * @param columns
     **************************************************************************/
    public DefaultMap( int rows, int columns )
    {
        map = new IMapCell[rows][columns];
        for( int i = 0; i < rows; i++ )
            for( int j = 0; j < columns; j++ )
                map[i][j] = new DefaultMapCell( true, 1.0, i, j );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMap#getCell(int, int)
     */
    @Override
    public IMapCell getCell( int row, int col )
    {
        return map[row][col];
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.game.pathfinding.IMap#getAdjacentCells(javautils.game.pathfinding
     * .IMapCell)
     */
    @Override
    public List<IMapCell> getAdjacentCells( IMapCell cell )
    {
        List<IMapCell> neighbors = new Vector<IMapCell>();
        int r = -1;
        int c = -1;

        for( int row = 0; row < map.length; row++ )
        {
            for( int col = 0; col < map[row].length; col++ )
            {
                if( cell == map[row][col] )
                {
                    r = row;
                    c = col;
                    break;
                }
            }
        }

        if( r == -1 || c == -1 )
            return null;

        // TODO: allow diagonal movement here
        // add left
        if( ( c - 1 ) >= 0 )
            if( map[r][c - 1].isTraversable() )
                neighbors.add( map[r][c - 1] );
        // add top
        if( ( r - 1 ) >= 0 )
            if( map[r - 1][c].isTraversable() )
                neighbors.add( map[r - 1][c] );
        // add right
        if( ( c + 1 ) < map[r].length )
            if( map[r][c + 1].isTraversable() )
                neighbors.add( map[r][c + 1] );
        // add bottom
        if( ( r + 1 ) < map.length )
            if( map[r + 1][c].isTraversable() )
                neighbors.add( map[r + 1][c] );

        return neighbors;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMap#getCells()
     */
    @Override
    public List<IMapCell> getCells()
    {
        List<IMapCell> cells = new Vector<IMapCell>();
        for( int row = 0; row < map.length; row++ )
            for( int col = 0; col < map[row].length; col++ )
                cells.add( map[row][col] );
        return cells;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMap#getCellWidth()
     */
    @Override
    public int getCellWidth()
    {
        return DefaultMapCell.WIDTH;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMap#getCellHeight()
     */
    @Override
    public int getCellHeight()
    {
        return DefaultMapCell.HEIGHT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMap#getWidth()
     */
    @Override
    public int getWidth()
    {
        return map[0].length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMap#getHeight()
     */
    @Override
    public int getHeight()
    {
        return map.length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.IRenderable#render(java.awt.Graphics2D)
     */
    @Override
    public void render( Graphics2D g2d )
    {
        for( int row = 0; row < map.length; row++ )
            for( int col = 0; col < map[row].length; col++ )
                map[row][col].render( g2d );
    }

}
