package objects;

import java.awt.Color;
import java.awt.Graphics2D;

import javautils.game.Sprite;
import javautils.vector.Vector2D;
import javautils.vector.VectorUtils;

public class Bullet extends Sprite
{
    public static final double SMALL = 7.0;
    public static final Color COLOR = new Color( 4, 75, 239 );

    private static final double SPEED = 400.0;
    private Color bulletColor;

    public Bullet( double collisionRadius, Vector2D origin, Vector2D direction )
    {
        super();

        setPos( origin );
        setVel( VectorUtils.scaleVector( direction, SPEED ) );

        this.collisionRadius = Math.abs( collisionRadius );
        this.bulletColor = COLOR;
    }

    @Override
    public void render( Graphics2D g2d )
    {
        // ball
        g2d.setColor( bulletColor );
        g2d.fillOval( (int)( pos.x - collisionRadius ),
                (int)( pos.y - collisionRadius ), (int)( collisionRadius * 2 ),
                (int)( collisionRadius * 2 ) );
        // outline
        g2d.setColor( Color.black );
        g2d.drawOval( (int)( pos.x - collisionRadius ),
                (int)( pos.y - collisionRadius ), (int)( collisionRadius * 2 ),
                (int)( collisionRadius * 2 ) );
    }
}
