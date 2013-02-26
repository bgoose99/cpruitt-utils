package javautils.game.pathfinding;

/*******************************************************************************
 * Simple factory interface for creating an {@link IMapPath}.
 ******************************************************************************/
public interface IMapPathFactory
{
    /***************************************************************************
     * Returns a new, empty {@link IMapPath}.
     * 
     * @return
     **************************************************************************/
    IMapPath createMapPath();
}
