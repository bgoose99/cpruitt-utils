package javautils.game.pathfinding;

import java.util.List;

/*******************************************************************************
 * Simple map path interface.
 **************************************************************************/
public interface IMapPath
{
    /***************************************************************************
     * Returns the next node from the path and pops it from the list.
     * 
     * @return
     **************************************************************************/
    IMapCell getNextNode();

    /***************************************************************************
     * Returns a list of all nodes in this path.
     * 
     * @return
     **************************************************************************/
    List<IMapCell> getNodes();

    /***************************************************************************
     * Appends the node to this path.
     * 
     * @param node
     **************************************************************************/
    void appendNode( IMapCell node );

    /***************************************************************************
     * Prepends the node to this path.
     * 
     * @param node
     **************************************************************************/
    void prependNode( IMapCell node );

    /***************************************************************************
     * Returns the number of nodes in this path.
     * 
     * @return
     **************************************************************************/
    int getNumberOfSteps();
}