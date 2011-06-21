package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import javautils.game.Sprite;
import javautils.vector.Vector2D;
import javautils.vector.VectorUtils;

public class Bullet extends Sprite
{
    public final static int REGULAR_SIZE = 10;

    private final static double SPEED = 300.0;
    private final int size;
    private Color color;

    public Bullet( Color c, double radius, Point position, Point heading )
    {
        this( c, radius, position, VectorUtils.calculateUnitNormalVector(
                new Vector2D( position ), new Vector2D( heading ) ) );
    }

    public Bullet( Color c, double radius, Point position, Vector2D unitNormal )
    {
        this( c, radius, new Vector2D( position ), unitNormal );
    }

    public Bullet( Color c, double radius, Vector2D position,
            Vector2D unitNormal )
    {
        pos = position;
        vel = VectorUtils.scaleVector( unitNormal, SPEED );
        color = c;
        collisionRadius = radius;
        size = (int)radius;
    }

    @Override
    public void render( Graphics2D g2d )
    {
        g2d.setColor( color );
        g2d.fillOval( (int)( pos.x - size / 2 ), (int)( pos.y - size / 2 ),
                size, size );
    }
}
