package objects;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Random;

import javautils.game.Sprite;
import javautils.vector.Vector2D;
import utils.MeteorImageManager;

public class Meteor extends Sprite
{
    private final static Random RANDOM = new Random();
    private final int size;
    private final Image image;
    private final double rotationSpeed; // degrees/second
    private double currentAngle = 0.0;

    public Meteor( double radius, Vector2D position, Vector2D velocity )
    {
        pos = position;
        vel = velocity;
        collisionRadius = radius;
        size = (int)radius;

        // pick an image
        int index = RANDOM.nextInt( 3 );
        switch( index )
        {
        case 2:
            image = MeteorImageManager.getImage( MeteorImageManager.METEOR_3 );
            break;
        case 1:
            image = MeteorImageManager.getImage( MeteorImageManager.METEOR_2 );
            break;
        case 0:
        default:
            image = MeteorImageManager.getImage( MeteorImageManager.METEOR_1 );
            break;
        }

        rotationSpeed = 50.0 * RANDOM.nextGaussian();
    }

    @Override
    public void move( double deltaSeconds )
    {
        pos.x = ( pos.x + ( vel.x * deltaSeconds ) );
        pos.y = ( pos.y + ( vel.y * deltaSeconds ) );
        currentAngle += rotationSpeed * deltaSeconds;
    }

    @Override
    public void render( Graphics2D g2d )
    {
        AffineTransform origTransform = g2d.getTransform();
        AffineTransform newTransform = (AffineTransform)origTransform.clone();

        newTransform.rotate( Math.toRadians( currentAngle ), (int)pos.x,
                (int)pos.y );

        g2d.setTransform( newTransform );
        g2d.drawImage( image, (int)( pos.x - size ), (int)( pos.y - size ),
                (int)( pos.x + size ), (int)( pos.y + size ), 0, 0,
                image.getWidth( null ), image.getHeight( null ), null );
        g2d.setTransform( origTransform );
    }
}
