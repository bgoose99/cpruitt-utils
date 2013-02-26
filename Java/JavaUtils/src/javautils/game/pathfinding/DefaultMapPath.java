package javautils.game.pathfinding;

import java.util.LinkedList;
import java.util.List;

/*******************************************************************************
 * Simple {@IMapPath} implementation.
 ******************************************************************************/
public class DefaultMapPath implements IMapPath
{
    private LinkedList<IMapCell> nodes;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public DefaultMapPath()
    {
        nodes = new LinkedList<IMapCell>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMapPath#getNextNode()
     */
    @Override
    public IMapCell getNextNode()
    {
        return ( nodes.isEmpty() ? null : nodes.pop() );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMapPath#getNodes()
     */
    @Override
    public List<IMapCell> getNodes()
    {
        return new LinkedList<IMapCell>( nodes );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.game.pathfinding.IMapPath#appendNode(javautils.game.pathfinding
     * .IMapCell)
     */
    @Override
    public void appendNode( IMapCell node )
    {
        nodes.addLast( node );
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.game.pathfinding.IMapPath#prependNode(javautils.game.pathfinding
     * .IMapCell)
     */
    @Override
    public void prependNode( IMapCell node )
    {
        nodes.addFirst( node );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMapPath#getNumberOfSteps()
     */
    @Override
    public int getNumberOfSteps()
    {
        return nodes.size();
    }

}
