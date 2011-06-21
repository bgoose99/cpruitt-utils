package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javautils.game.GameFrame;
import javautils.game.ISpriteHandler;
import javautils.vector.Vector2D;
import javautils.vector.VectorUtils;
import objects.BubbleGenerator;
import objects.Bullet;
import objects.SpriteHandler;

/*******************************************************************************
 * The main frame of this application. This class contains the main game loop.
 ******************************************************************************/
public class BlueFrame extends GameFrame
{
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 800;

    private static final Dimension SCREEN_SIZE = new Dimension( SCREEN_WIDTH,
            SCREEN_HEIGHT );

    /** This is the "base" of the player. */
    private final Point BASE = new Point( SCREEN_WIDTH / 2, SCREEN_HEIGHT );
    private final Rectangle BASE_RECT = new Rectangle( BASE.x - 25,
            BASE.y - 25, 50, 50 );
    private final int TURRET_RADIUS = 10;
    private Rectangle TURRET_RECT = new Rectangle( 0, 0, 0, 0 );

    private final BlueMouseAdapter mouseAdapter;

    private ISpriteHandler spriteHandler;
    private BubbleGenerator bubbleGenerator;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public BlueFrame()
    {
        super( SCREEN_SIZE );

        TURRET_RECT.height = TURRET_RECT.width = 2 * TURRET_RADIUS;

        spriteHandler = new SpriteHandler( SCREEN_SIZE );
        bubbleGenerator = new BubbleGenerator( spriteHandler, SCREEN_SIZE );

        backgroundColor = new Color( 176, 200, 254 );

        mouseAdapter = new BlueMouseAdapter( spriteHandler, BASE );
        canvas.addMouseListener( mouseAdapter );
        canvas.addMouseMotionListener( mouseAdapter );
        canvas.addKeyListener( new CustomKeyAdapter() );

        setTitle( "Blue" );

        bubbleGenerator.startGenerating();
    }

    @Override
    protected void renderScene( Graphics2D g2d )
    {
        Point p = mouseAdapter.getMouseCoords();
        Vector2D normal = VectorUtils.scaleVector( VectorUtils
                .calculateUnitNormalVector( new Vector2D( BASE ), new Vector2D(
                        p ) ), 25.0 );
        TURRET_RECT.x = (int)( BASE.x + normal.x - TURRET_RADIUS );
        TURRET_RECT.y = (int)( BASE.y + normal.y - TURRET_RADIUS );
        // double theta = Math.atan2( p.x - BASE.x, SCREEN_SIZE.height - p.y )
        // * ( 180.0 / Math.PI );

        g2d.setColor( Color.black );
        g2d.drawString( "Sprites: " + spriteHandler.getNumSprites(),
                SCREEN_WIDTH - 100, 10 );

        // draw sprites
        spriteHandler.renderSprites( g2d );

        // draw player
        g2d.setColor( Color.black );
        g2d.fillOval( TURRET_RECT.x, TURRET_RECT.y, TURRET_RECT.width,
                TURRET_RECT.height );

        g2d.setColor( Bullet.COLOR );
        g2d.fillOval( BASE_RECT.x, BASE_RECT.y, BASE_RECT.width,
                BASE_RECT.height );
        g2d.setColor( Color.black );
        g2d.drawOval( BASE_RECT.x, BASE_RECT.y, BASE_RECT.width,
                BASE_RECT.height );
    }

    @Override
    protected void advanceScene( double deltaSeconds )
    {
        spriteHandler.moveSprites( deltaSeconds );
    }

    private class CustomKeyAdapter extends KeyAdapter
    {
        // private boolean b = true;

        @Override
        public void keyTyped( KeyEvent e )
        {
            // if( e.getKeyChar() == ' ' )
            // {
            // if( b )
            // {
            // bubbleGenerator.startGenerating();
            // b = false;
            // } else
            // {
            // bubbleGenerator.stopGenerating();
            // b = true;
            // }
            // }
        }
    }
}
