package objects;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

import javautils.game.Sprite;
import javautils.vector.Vector2D;
import utils.MeteorImageManager;

public class PowerUp extends Sprite
{
    private static final Random RANDOM = new Random();

    public static final int NONE = 0;
    public static final int TRIPLE_SHOT = 1;
    public static final int DOUBLE_SIZE = 2;
    public static final int PASS_THROUGH = 3;
    public static final int SPRAY = 4;
    public static final int HEALTH = 5;

    private final int type;
    private final Image image;

    public PowerUp( Vector2D position, Vector2D velocity )
    {
        this( RANDOM.nextInt( HEALTH ) + 1, position, velocity );
    }

    public PowerUp( int type, Vector2D position, Vector2D velocity )
    {
        this.type = type;
        pos = position;
        vel = velocity;

        switch( type )
        {
        case TRIPLE_SHOT:
            image = MeteorImageManager
                    .getImage( MeteorImageManager.POWERUP_TRIPLE );
            break;
        case DOUBLE_SIZE:
            image = MeteorImageManager
                    .getImage( MeteorImageManager.POWERUP_BIG );
            break;
        case PASS_THROUGH:
            image = MeteorImageManager
                    .getImage( MeteorImageManager.POWERUP_PASS_THROUGH );
            break;
        case SPRAY:
            image = MeteorImageManager
                    .getImage( MeteorImageManager.POWERUP_SPRAY );
            break;
        case HEALTH:
        default:
            type = PowerUp.HEALTH;
            image = MeteorImageManager
                    .getImage( MeteorImageManager.POWERUP_HEALTH );
            break;
        }

        collisionRadius = image.getWidth( null ) / 2.0;
    }

    public int getType()
    {
        return type;
    }

    @Override
    public void render( Graphics2D g2d )
    {
        g2d.drawImage( image, (int)( pos.x - image.getWidth( null ) / 2 ),
                (int)( pos.y - image.getHeight( null ) / 2 ),
                (int)( pos.x + image.getWidth( null ) / 2 ),
                (int)( pos.y + image.getHeight( null ) / 2 ), 0, 0,
                image.getWidth( null ), image.getHeight( null ), null );
    }
}
