package javautils.game.pathfinding;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

/*******************************************************************************
 * A* path finder.
 ******************************************************************************/
public class AStarPathFinder
{
    private Queue<NodeData> open;
    private Queue<NodeData> closed;
    private List<NodeData> searchSpace;
    private IMapPathFactory pathFactory;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public AStarPathFinder( IMapPathFactory pathFactory )
    {
        open = new PriorityQueue<NodeData>();
        closed = new PriorityQueue<NodeData>();
        searchSpace = new Vector<NodeData>();
        this.pathFactory = pathFactory;
    }

    /***************************************************************************
     * Finds a path in the supplied {@link IMap} from the start to the target
     * {@link IMapCell}.
     * 
     * @param map
     * @param start
     * @param target
     * @return
     **************************************************************************/
    public IMapPath findPath( IMap map, IMapCell start, IMapCell target )
    {
        // clear out our lists
        open.clear();
        closed.clear();
        searchSpace.clear();

        // init search space
        for( IMapCell cell : map.getCells() )
        {
            searchSpace.add( new NodeData( cell ) );
        }

        // add starting cell to list of open cells
        NodeData startNode = getNodeByCell( start );
        if( startNode == null )
            return null;
        open.add( startNode );

        NodeData node = open.poll();
        while( node != null && node.cell != target )
        {
            processNode( map, node, target );
            node = open.poll();
        }

        // we've either got a valid path, or we can't get there from here

        // walk backwards through our nodes, adding each to the front of the
        // path
        IMapPath path = pathFactory.createMapPath();
        while( node != null )
        {
            path.prependNode( node.cell );
            node = node.parent;
        }

        // clear out our lists
        open.clear();
        closed.clear();
        searchSpace.clear();

        return path;
    }

    /***************************************************************************
     * Processes a single node.
     * 
     * @param map
     * @param currentNode
     * @param target
     **************************************************************************/
    private void processNode( IMap map, NodeData currentNode, IMapCell target )
    {
        int cost;
        boolean inOpen;
        boolean inClosed;

        // add to closed list
        closed.add( currentNode );

        // get a list of adjacent nodes
        for( NodeData neighbor : getAdjacentNodes( map, currentNode ) )
        {
            cost = currentNode.g
                    + getManhattanHeuristic( currentNode.cell, neighbor.cell );
            inOpen = open.contains( neighbor );
            inClosed = closed.contains( neighbor );

            // if this node is in the closed list, ignore it
            if( inClosed )
            {
                continue;
            }

            if( !inOpen )
            {
                // not in the open list; calculate values and add it
                neighbor.parent = currentNode;
                neighbor.g = cost;
                neighbor.h = getManhattanHeuristic( neighbor.cell, target );
                open.add( neighbor );
            } else
            {
                // it's in the open list, check to see if this path is better
                if( cost < neighbor.g )
                {
                    // this path is better; remove from list, recalculate, and
                    // add back to list
                    open.remove( neighbor );
                    neighbor.parent = currentNode;
                    neighbor.g = cost;
                    neighbor.h = getManhattanHeuristic( neighbor.cell, target );
                    open.add( neighbor );
                }
            }
        }
    }

    /***************************************************************************
     * Calculates a simple Manhattan heuristic value between the two supplied
     * {@link IMapCell}s.
     * 
     * @param from
     * @param to
     * @return
     **************************************************************************/
    private int getManhattanHeuristic( IMapCell from, IMapCell to )
    {
        return Math.abs( to.getCol() - from.getCol() )
                + Math.abs( to.getRow() - from.getRow() );
    }

    /***************************************************************************
     * Gets nodes in the supplied {@link IMap} adjacent to the supplied
     * {@link NodeData} object.
     * 
     * @param map
     * @param node
     * @return
     **************************************************************************/
    private List<NodeData> getAdjacentNodes( IMap map, NodeData node )
    {
        List<NodeData> nodes = new Vector<NodeData>();
        for( IMapCell c : map.getAdjacentCells( node.cell ) )
            nodes.add( getNodeByCell( c ) );
        return nodes;
    }

    /***************************************************************************
     * Returns the {@link NodeData} object associated with the supplied
     * {@link IMapCell}.
     * 
     * @param cell
     * @return
     **************************************************************************/
    private NodeData getNodeByCell( IMapCell cell )
    {
        for( NodeData n : searchSpace )
        {
            if( n.cell == cell )
                return n;
        }
        return null;
    }

    /***************************************************************************
     * Simple class for storing node data.
     **************************************************************************/
    private class NodeData implements Comparable<NodeData>
    {
        private final IMapCell cell;
        private NodeData parent;
        private int g;
        private int h;

        public NodeData( IMapCell cell )
        {
            this.cell = cell;
            parent = null;
            g = 0;
            h = 0;
        }

        @Override
        public int compareTo( NodeData that )
        {
            return ( this.g + this.h ) - ( that.g + that.h );
        }
    }
}
