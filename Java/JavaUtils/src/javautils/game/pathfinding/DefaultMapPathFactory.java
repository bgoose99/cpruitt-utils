package javautils.game.pathfinding;

/*******************************************************************************
 * Simple {@link IMapPathFactory} implementation.
 ******************************************************************************/
public class DefaultMapPathFactory implements IMapPathFactory
{
    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.pathfinding.IMapPathFactory#createMapPath()
     */
    @Override
    public IMapPath createMapPath()
    {
        return new DefaultMapPath();
    }

}
