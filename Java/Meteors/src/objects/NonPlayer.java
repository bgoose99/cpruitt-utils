package objects;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javautils.game.ICollidable;
import javautils.game.IRenderable;
import javautils.game.Sprite;
import javautils.vector.Vector2D;
import javautils.vector.VectorUtils;

public class NonPlayer implements IRenderable
{
    private final static int MAX_DIFFICULTY = 5;
    private final static Random RANDOM = new Random();
    private final Dimension screenSize;
    private List<PowerUp> powerUps;
    private List<Meteor> meteors;

    private double nextMeteorSpawnTime = 1.0;
    private double nextPowerUpSpawnTime = 10.0;
    private double timeSinceMeteorSpawn = 0.0;
    private double timeSincePowerUpSpawn = 0.0;
    private int difficulty = 1;

    public NonPlayer( Dimension screenSize )
    {
        this.screenSize = screenSize;
        powerUps = new ArrayList<PowerUp>();
        meteors = new ArrayList<Meteor>();
    }

    public void advanceScene( double deltaSeconds )
    {
        timeSinceMeteorSpawn += deltaSeconds;
        timeSincePowerUpSpawn += deltaSeconds;

        if( timeSinceMeteorSpawn >= nextMeteorSpawnTime )
        {
            timeSinceMeteorSpawn = 0.0;

            Meteor m = null;
            double size = 50.0;
            double speed = 100.0;

            switch( difficulty )
            {
            case 5:
                nextMeteorSpawnTime = ( 500 + RANDOM.nextInt( 750 ) ) / 1000.0;
                size = 10 + RANDOM.nextInt( 50 );
                speed = 200 + RANDOM.nextInt( 50 );
                break;
            case 4:
                nextMeteorSpawnTime = ( 1000 + RANDOM.nextInt( 750 ) ) / 1000.0;
                size = 20 + RANDOM.nextInt( 50 );
                speed = 175 + RANDOM.nextInt( 50 );
                break;
            case 3:
                nextMeteorSpawnTime = ( 1500 + RANDOM.nextInt( 750 ) ) / 1000.0;
                size = 30 + RANDOM.nextInt( 50 );
                speed = 150 + RANDOM.nextInt( 50 );
                break;
            case 2:
                nextMeteorSpawnTime = ( 2000 + RANDOM.nextInt( 750 ) ) / 1000.0;
                size = 40 + RANDOM.nextInt( 50 );
                speed = 125 + RANDOM.nextInt( 50 );
                break;
            case 1:
            default:
                nextMeteorSpawnTime = ( 2500 + RANDOM.nextInt( 750 ) ) / 1000.0;
                size = 50 + RANDOM.nextInt( 50 );
                speed = 100 + RANDOM.nextInt( 50 );
                break;
            }

            Vector2D meteorPos = new Vector2D(
                    RANDOM.nextInt( screenSize.width ), -size );
            Vector2D meteorVel = VectorUtils
                    .scaleVector(
                            VectorUtils.getRandomDownwardUnitVector2D( 30, 100 ),
                            speed );
            m = new Meteor( size, meteorPos, meteorVel );
            synchronized( this )
            {
                meteors.add( m );
            }
        }

        if( timeSincePowerUpSpawn >= nextPowerUpSpawnTime )
        {
            timeSincePowerUpSpawn = 0.0;
            nextPowerUpSpawnTime = ( 5000 + RANDOM.nextInt( 5000 ) ) / 1000.0;
            double speed = 100 + RANDOM.nextInt( 50 );
            Vector2D powerupPos = new Vector2D(
                    50 + RANDOM.nextInt( screenSize.width - 50 ), 0 );
            Vector2D powerupVel = new Vector2D( 0, speed );
            PowerUp p = new PowerUp( powerupPos, powerupVel );
            synchronized( this )
            {
                powerUps.add( p );
            }
        }

        synchronized( this )
        {
            for( int i = 0; i < meteors.size(); i++ )
            {
                Meteor m = meteors.get( i );
                m.move( deltaSeconds );
                if( !m.isOnScreen( screenSize ) )
                    m.kill();

                if( !m.isAlive() )
                {
                    meteors.remove( i );
                    i--;
                    continue;
                }
            }

            for( int i = 0; i < powerUps.size(); i++ )
            {
                PowerUp p = powerUps.get( i );
                p.move( deltaSeconds );
                if( !p.isOnScreen( screenSize ) )
                    p.kill();

                if( !p.isAlive() )
                {
                    powerUps.remove( i );
                    i--;
                    continue;
                }
            }
        }
    }

    public void detectCollisions( ICollidable<Sprite> collidable )
    {
        synchronized( this )
        {
            for( Meteor m : meteors )
            {
                if( collidable.collidesWith( m ) )
                    m.kill();
            }

            for( PowerUp p : powerUps )
            {
                if( collidable.collidesWith( p ) )
                    p.kill();
            }
        }
    }

    public void setDifficulty( int i )
    {
        if( i < 1 )
            difficulty = 1;
        else if( i > MAX_DIFFICULTY )
            difficulty = MAX_DIFFICULTY;
        else
            difficulty = i;
    }

    public void reset()
    {
        nextMeteorSpawnTime = 1.0;
        nextPowerUpSpawnTime = 5.0;
        meteors.clear();
        powerUps.clear();
    }

    @Override
    public void render( Graphics2D g2d )
    {
        synchronized( this )
        {
            for( PowerUp p : powerUps )
                p.render( g2d );

            for( Meteor m : meteors )
                m.render( g2d );
        }
    }
}
