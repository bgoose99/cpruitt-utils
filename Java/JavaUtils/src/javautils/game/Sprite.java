package javautils.game;

import java.awt.Dimension;

import javautils.vector.Vector2D;
import javautils.vector.VectorUtils;

/*******************************************************************************
 * This class represents any object drawn on screen that can collide with other
 * objects.
 ******************************************************************************/
public abstract class Sprite implements IRenderable, ICollidable<Sprite>
{
    /** Centroid of this sprite in screen coordinates */
    protected Vector2D pos;

    /** Velocity vector of this sprite */
    protected Vector2D vel;

    /** Acceleration vector of this sprite */
    protected Vector2D acc;

    /** Radius for simple 2D circle collision detection */
    protected double collisionRadius = 10.0;

    protected boolean alive;

    /***************************************************************************
     * Default constructor
     **************************************************************************/
    public Sprite()
    {
        setPos( new Vector2D( 0, 0 ) );
        setVel( new Vector2D( 0, 0 ) );
        setAcc( new Vector2D( 0, 0 ) );
        alive = true;
    }

    /***************************************************************************
     * Determines whether this sprite is on a screen with origin (0,0) and the
     * supplied size.
     * 
     * @param screenSize
     * @return
     **************************************************************************/
    public boolean isOnScreen( Dimension screenSize )
    {
        // check left/right
        if( pos.x + collisionRadius <= 0
                || pos.x - collisionRadius >= screenSize.width )
            return false;

        // check top/bottom
        if( pos.y + collisionRadius <= 0
                || pos.y - collisionRadius >= screenSize.height )
            return false;

        return true;
    }

    /***************************************************************************
     * Moves this sprite by deltaSeconds * velocity.
     * 
     * @param deltaSeconds
     **************************************************************************/
    public void move( double deltaSeconds )
    {
        // TODO: acceleration equations are too simplistic
        // modify velocity
        vel.x += ( acc.x * deltaSeconds );
        vel.y += ( acc.y * deltaSeconds );
        // modify position
        pos.x += ( vel.x * deltaSeconds );
        pos.y += ( vel.y * deltaSeconds );
    }

    /***************************************************************************
     * Returns this sprite's position.
     * 
     * @return
     **************************************************************************/
    public Vector2D getPos()
    {
        return pos;
    }

    /***************************************************************************
     * Sets this sprite's position.
     * 
     * @param pos
     **************************************************************************/
    public void setPos( Vector2D pos )
    {
        this.pos = pos;
    }

    /***************************************************************************
     * Returns this sprite's velocity.
     * 
     * @return
     **************************************************************************/
    public Vector2D getVel()
    {
        return vel;
    }

    /***************************************************************************
     * Sets this sprite's velocity.
     * 
     * @param vel
     **************************************************************************/
    public void setVel( Vector2D vel )
    {
        this.vel = vel;
    }

    /***************************************************************************
     * Returns this sprite's acceleration.
     * 
     * @return
     **************************************************************************/
    public Vector2D getAcc()
    {
        return acc;
    }

    /***************************************************************************
     * Sets this sprite's acceleration.
     * 
     * @param acc
     **************************************************************************/
    public void setAcc( Vector2D acc )
    {
        this.acc = acc;
    }

    /***************************************************************************
     * Returns this sprite's collision radius.
     * 
     * @return
     **************************************************************************/
    public double getCollisionRadius()
    {
        return collisionRadius;
    }

    /***************************************************************************
     * Sets this sprite's collision radius.
     * 
     * @param collisionRadius
     **************************************************************************/
    public void setCollisionRadius( double collisionRadius )
    {
        this.collisionRadius = collisionRadius;
    }

    /***************************************************************************
     * Returns this sprite's mass.
     * 
     * @return
     **************************************************************************/
    public double getMass()
    {
        return collisionRadius;
    }

    /***************************************************************************
     * Returns this sprite's state.
     * 
     * @return
     **************************************************************************/
    public boolean isAlive()
    {
        return alive;
    }

    /***************************************************************************
     * Kills this sprite. It is up to the implementer to handle this state.
     **************************************************************************/
    public void kill()
    {
        alive = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.ICollidable#collidesWith(java.lang.Object)
     */
    @Override
    public boolean collidesWith( Sprite that )
    {
        if( VectorUtils.calculateDistance( this.pos, that.pos ) <= ( this.collisionRadius + that.collisionRadius ) )
            return true;
        return false;
    }
}
