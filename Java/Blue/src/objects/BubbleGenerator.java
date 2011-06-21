package objects;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javautils.game.ISpriteHandler;
import javautils.vector.Vector2D;
import javautils.vector.VectorUtils;

import javax.swing.Timer;

public class BubbleGenerator implements ActionListener
{
    private ISpriteHandler spriteHandler;
    private final Dimension screenSize;
    private Timer timer;
    private Random rand;

    public BubbleGenerator( ISpriteHandler spriteHandler, Dimension screenSize )
    {
        this.spriteHandler = spriteHandler;
        this.screenSize = screenSize;
        timer = new Timer( 1500, this );
        rand = new Random();
    }

    public void startGenerating()
    {
        timer.start();
    }

    public void stopGenerating()
    {
        timer.stop();
    }

    @Override
    public void actionPerformed( ActionEvent arg0 )
    {
        // add bubble
        int size = rand.nextInt( 200 ) + 30;
        // Point pOrigin = new Point( screenSize.width / 2, screenSize.height /
        // 2 );
        // Vector2D vOrigin = new Vector2D( pOrigin );
        // Vector2D dir = new Vector2D( 0.0, 0.0 );
        // spriteHandler.addSprite( new Bubble( size, vOrigin, dir ) );
        Point pOrigin = new Point( rand.nextInt( screenSize.width ),
                -( size / 2 ) );
        Point pDest = new Point( rand.nextInt( screenSize.width ),
                screenSize.height );
        Vector2D vOrigin = new Vector2D( pOrigin );
        Vector2D dir = VectorUtils.calculateUnitNormalVector( vOrigin,
                new Vector2D( pDest ) );

        Bubble b = new Bubble( size, vOrigin, dir );
        // TODO: make sure this bubble doesn't intersect any others
        spriteHandler.addSprite( b );

        // set timer interval
        timer.setDelay( rand.nextInt( 3000 ) + 2000 );
    }
}
