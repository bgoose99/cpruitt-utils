package javautils.game;

/*******************************************************************************
 * This interface is useful when two objects on a screen can collide with each
 * other.
 * 
 * @param <T>
 ******************************************************************************/
public interface ICollidable<T>
{
    /***************************************************************************
     * Determines whether this object collides with another.
     * 
     * @param that
     * @return
     **************************************************************************/
    public boolean collidesWith( T that );
}
