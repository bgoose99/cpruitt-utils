package ui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javautils.game.ISpriteHandler;
import javautils.vector.Vector2D;
import javautils.vector.VectorUtils;

import javax.swing.Timer;

import objects.Bullet;

/*******************************************************************************
 * Custom {@link MouseAdapter}.
 ******************************************************************************/
public class BlueMouseAdapter extends MouseAdapter
{
    private final Point playerLocation;
    private Point mouseCoords;
    private boolean mousePressed;
    private Timer timer;

    /***************************************************************************
     * Constructor
     * 
     * @param spriteHandler
     * @param playerLocation
     **************************************************************************/
    public BlueMouseAdapter( ISpriteHandler spriteHandler, Point playerLocation )
    {
        this.playerLocation = playerLocation;
        mouseCoords = new Point();
        mousePressed = false;
        timer = new Timer( 100, new FireActionListener( spriteHandler ) );
        timer.setInitialDelay( 0 );
    }

    /***************************************************************************
     * Returns the mouse coordinates.
     * 
     * @return
     **************************************************************************/
    public Point getMouseCoords()
    {
        return mouseCoords;
    }

    @Override
    public void mousePressed( MouseEvent e )
    {
        mousePressed = true;
        timer.start();
    }

    @Override
    public void mouseReleased( MouseEvent e )
    {
        mousePressed = false;
        timer.stop();
    }

    @Override
    public void mouseDragged( MouseEvent e )
    {
        mouseCoords = e.getPoint();
    }

    @Override
    public void mouseMoved( MouseEvent e )
    {
        mouseCoords = e.getPoint();
    }

    /***************************************************************************
     * Custom {@link ActionListener} used to continuously fire bullets as long
     * as the mouse remains clicked.
     **************************************************************************/
    private class FireActionListener implements ActionListener
    {
        private ISpriteHandler spriteHandler;

        /***********************************************************************
         * Constructor
         * 
         * @param spriteHandler
         **********************************************************************/
        public FireActionListener( ISpriteHandler spriteHandler )
        {
            this.spriteHandler = spriteHandler;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( mousePressed )
            {
                Vector2D origin = new Vector2D( playerLocation );
                Vector2D mouse = new Vector2D( mouseCoords );
                Vector2D dir = VectorUtils.calculateUnitNormalVector( origin,
                        mouse );

                spriteHandler
                        .addSprite( new Bullet( Bullet.SMALL, origin, dir ) );
            }
        }
    }
}
