package javautils.game.pathfinding;

import java.util.List;

import javautils.game.IRenderable;

/*******************************************************************************
 * Simple map interface.
 ******************************************************************************/
public interface IMap extends IRenderable
{
    /***************************************************************************
     * Returns the cell at the specified row and column, or null if either are
     * invalid.
     * 
     * @param row
     * @param col
     * @return
     **************************************************************************/
    IMapCell getCell( int row, int col );

    /***************************************************************************
     * Returns the cells adjacent to the supplied cell.
     * 
     * @param cell
     * @return
     **************************************************************************/
    List<IMapCell> getAdjacentCells( IMapCell cell );

    /***************************************************************************
     * Returns all cells in this map, in no particular order.
     * 
     * @return
     **************************************************************************/
    List<IMapCell> getCells();

    /***************************************************************************
     * Returns the cell width, in pixels.
     * 
     * @return
     **************************************************************************/
    int getCellWidth();

    /***************************************************************************
     * Returns the cell height, in pixels.
     * 
     * @return
     **************************************************************************/
    int getCellHeight();

    /***************************************************************************
     * Returns the width of this map.
     * 
     * @return
     **************************************************************************/
    int getWidth();

    /***************************************************************************
     * Returns the height of this map.
     * 
     * @return
     **************************************************************************/
    int getHeight();
}
