package objects;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

import javautils.game.ISpriteHandler;
import javautils.game.Sprite;
import javautils.vector.VectorUtils;

public class SpriteHandler implements ISpriteHandler
{
    private List<Sprite> sprites;
    private Dimension screenSize;

    public SpriteHandler( Dimension screenSize )
    {
        this.screenSize = screenSize;
        sprites = new Vector<Sprite>();
    }

    @Override
    public synchronized void addSprite( Sprite sprite )
    {
        sprites.add( sprite );
    }

    @Override
    public synchronized void removeSprite( Sprite sprite )
    {
        sprites.remove( sprite );
    }

    @Override
    public synchronized void renderSprites( Graphics2D g2d )
    {
        for( Sprite s : sprites )
        {
            s.render( g2d );
        }
    }

    @Override
    public synchronized void moveSprites( double deltaSeconds )
    {
        for( int i = 0; i < sprites.size(); i++ )
        {
            Sprite s = sprites.get( i );
            checkForCollisions( s );
            s.move( deltaSeconds );
            if( !s.isOnScreen( screenSize ) )
            {
                sprites.remove( i );
                i--;
                continue;
            }
        }
    }

    @Override
    public int getNumSprites()
    {
        return sprites.size();
    }

    private void checkForCollisions( Sprite s )
    {
        for( Sprite sprite : sprites )
        {
            // if( s.equals( sprite ) )
            if( s == sprite )
                continue;
            if( s.collidesWith( sprite ) )
                VectorUtils.simulateElasticCollision( s, sprite );
        }
    }
}
