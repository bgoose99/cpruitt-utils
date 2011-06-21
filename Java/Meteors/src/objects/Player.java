package objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javautils.game.ICollidable;
import javautils.game.IRenderable;
import javautils.game.Sprite;
import javautils.vector.Vector2D;
import javautils.vector.VectorUtils;
import utils.MeteorImageManager;

public class Player implements IRenderable, ICollidable<Sprite>
{
    private final static String[] STATUS = {
            "Words can't describe how bad you are.", "You really suck.",
            "Very, very sad.", "That was pretty bad.",
            "I guess that wasn't terrible.", "Revelling in mediocrity.",
            "Good job.", "Really well done.", "Outstanding!",
            "You are full of awesome!" };
    private final static int MAX_BULLETS = 10;
    private final static int MAX_HEALTH = 200;
    private final static int TWO_THIRDS_HEALTH = (int)( MAX_HEALTH * 0.66 );
    private final static int ONE_THIRD_HEALTH = (int)( MAX_HEALTH * 0.33 );
    private final Dimension screenSize;
    private final Point pos;
    private List<Bullet> bullets;
    private int powerup;
    private int score;
    private int health;

    public Player( Dimension screenSize, Point position )
    {
        this.screenSize = screenSize;
        this.pos = position;
        bullets = new ArrayList<Bullet>();
        powerup = PowerUp.NONE;
        score = 0;
        health = MAX_HEALTH;
    }

    public void advanceScene( double deltaSeconds )
    {
        synchronized( this )
        {
            for( int i = 0; i < bullets.size(); i++ )
            {
                Bullet b = bullets.get( i );
                b.move( deltaSeconds );
                if( !b.isOnScreen( screenSize ) )
                    b.kill();

                if( !b.isAlive() )
                {
                    bullets.remove( i );
                    i--;
                    continue;
                }
            }
        }
    }

    public void fireBullet( Point heading )
    {
        synchronized( this )
        {
            if( bullets.size() < MAX_BULLETS )
            {
                switch( powerup )
                {
                case PowerUp.SPRAY:
                    fireSprayBullets( heading );
                    break;
                case PowerUp.DOUBLE_SIZE:
                    fireBigBullet( heading );
                    break;
                case PowerUp.TRIPLE_SHOT:
                    fireTripleBullets( heading );
                    break;
                case PowerUp.PASS_THROUGH:
                case PowerUp.NONE:
                default:
                    fireRegularBullet( heading );
                    break;
                }
            }
        }
    }

    private void fireRegularBullet( Point heading )
    {
        bullets.add( new Bullet( Color.lightGray, Bullet.REGULAR_SIZE, pos,
                heading ) );
    }

    private void fireBigBullet( Point heading )
    {
        bullets.add( new Bullet( Color.lightGray, Bullet.REGULAR_SIZE * 2, pos,
                heading ) );
    }

    private void fireSprayBullets( Point heading )
    {
        final double SCALE = Math.PI / 15.0;
        // middle
        Vector2D unitNormal = VectorUtils.calculateUnitNormalVector(
                new Vector2D( pos ), new Vector2D( heading ) );
        // left
        Vector2D leftBullet = VectorUtils.rotateVector( unitNormal, -SCALE );
        // right
        Vector2D rightBullet = VectorUtils.rotateVector( unitNormal, SCALE );

        bullets.add( new Bullet( Color.lightGray, Bullet.REGULAR_SIZE, pos,
                leftBullet ) );
        bullets.add( new Bullet( Color.lightGray, Bullet.REGULAR_SIZE, pos,
                unitNormal ) );
        bullets.add( new Bullet( Color.lightGray, Bullet.REGULAR_SIZE, pos,
                rightBullet ) );
    }

    private void fireTripleBullets( Point heading )
    {
        final double SCALE = 20.0;
        // unit vector for all bullets
        Vector2D unitNormal = VectorUtils.calculateUnitNormalVector(
                new Vector2D( pos ), new Vector2D( heading ) );

        // ortho vector for positioning bullets
        Vector2D ortho = VectorUtils.scaleVector(
                VectorUtils.rotateVector( unitNormal, Math.PI / 2 ), SCALE );

        // pos for left bullet
        Vector2D leftPos = new Vector2D( pos.x - ortho.x, pos.y - ortho.y );
        // pos for right bullet
        Vector2D rightPos = new Vector2D( pos.x + ortho.x, pos.y + ortho.y );

        bullets.add( new Bullet( Color.lightGray, Bullet.REGULAR_SIZE, leftPos,
                unitNormal ) );
        bullets.add( new Bullet( Color.lightGray, Bullet.REGULAR_SIZE, pos,
                unitNormal ) );
        bullets.add( new Bullet( Color.lightGray, Bullet.REGULAR_SIZE,
                rightPos, unitNormal ) );
    }

    public int getScore()
    {
        return score;
    }

    public int getHealth()
    {
        return ( health < 0 ? 0 : health );
    }

    public boolean isAlive()
    {
        return ( health <= 0 ? false : true );
    }

    public Image getStatusImage()
    {
        if( health >= TWO_THIRDS_HEALTH )
            return MeteorImageManager.getImage( MeteorImageManager.SMILY_FACE );
        else if( health >= ONE_THIRD_HEALTH )
            return MeteorImageManager.getImage( MeteorImageManager.SAD_FACE );
        else
            return MeteorImageManager.getImage( MeteorImageManager.CRYING_FACE );
    }

    public String getStatusText()
    {
        if( score >= 10000 )
            return STATUS[9];
        else if( score >= 9000 )
            return STATUS[8];
        else if( score >= 8000 )
            return STATUS[7];
        else if( score >= 7000 )
            return STATUS[6];
        else if( score >= 6000 )
            return STATUS[5];
        else if( score >= 5000 )
            return STATUS[4];
        else if( score >= 4000 )
            return STATUS[3];
        else if( score >= 3000 )
            return STATUS[2];
        else if( score >= 2000 )
            return STATUS[1];
        else
            return STATUS[1];
    }

    public void reset()
    {
        bullets.clear();
        health = MAX_HEALTH;
        powerup = PowerUp.NONE;
        score = 0;
    }

    private void collectPowerUp( int type )
    {
        switch( type )
        {
        case PowerUp.HEALTH:
            health += 30;
            if( health > MAX_HEALTH )
                health = MAX_HEALTH;
            break;
        case PowerUp.DOUBLE_SIZE:
        case PowerUp.PASS_THROUGH:
        case PowerUp.TRIPLE_SHOT:
        case PowerUp.SPRAY:
            powerup = type;
            break;
        }
    }

    @Override
    public void render( Graphics2D g2d )
    {
        synchronized( this )
        {
            for( Bullet b : bullets )
                b.render( g2d );
        }
    }

    @Override
    public boolean collidesWith( Sprite that )
    {
        synchronized( this )
        {
            for( Bullet b : bullets )
            {
                if( b.collidesWith( that ) )
                {
                    if( powerup != PowerUp.PASS_THROUGH )
                        b.kill();

                    if( that instanceof Meteor )
                    {
                        score += 125 - that.getCollisionRadius();
                    } else if( that instanceof PowerUp )
                    {
                        collectPowerUp( ( (PowerUp)that ).getType() );
                    }

                    return true;
                }
            }
        }

        // if this is a meteor, see if it hit our defended area
        if( that instanceof Meteor )
        {
            if( ( that.getPos().y + that.getCollisionRadius() ) > ( screenSize.height - 10 ) )
            {
                health -= 0.5 * that.getCollisionRadius();
                return true;
            }
        }

        return false;
    }
}
