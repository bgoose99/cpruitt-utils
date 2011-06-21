package objects;

import java.awt.Color;
import java.awt.Graphics2D;

import javautils.game.Sprite;
import javautils.vector.Vector2D;
import javautils.vector.VectorUtils;

public class Bubble extends Sprite
{
    private static final Color COLOR = new Color( 3, 49, 155 );
    private static final double SPEED = 75.0;
    private static final double ACC = 100.0;
    private Color bulletColor;

    public Bubble( double collisionRadius, Vector2D origin, Vector2D direction )
    {
        super();

        setPos( origin );
        setVel( VectorUtils.scaleVector( direction, SPEED ) );
        setAcc( new Vector2D( 0, ACC ) );

        setCollisionRadius( Math.abs( collisionRadius ) );
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
