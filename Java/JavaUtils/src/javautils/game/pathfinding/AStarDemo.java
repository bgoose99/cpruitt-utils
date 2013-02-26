package javautils.game.pathfinding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javautils.game.GameFrame;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/*******************************************************************************
 * Demonstration of the A* path finding algorithm.
 ******************************************************************************/
public class AStarDemo extends GameFrame
{
    private static final int WIDTH = 640;
    private static final int HEIGHT = 640;
    private static final Dimension SCREEN_SIZE = new Dimension( WIDTH, HEIGHT );

    // simple state enum
    private enum PaintState
    {
        START, TARGET, OBSTACLE
    };

    private IMap map;
    private IMapCell startCell;
    private IMapCell targetCell;
    private AStarPathFinder pathFinder;
    private PaintState paintState;
    private IMapPath path;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public AStarDemo()
    {
        super( SCREEN_SIZE );

        setTitle( "Path Finder Demo" );

        map = new DefaultMap( 10, 10 );
        startCell = map.getCell( 0, 0 );
        targetCell = map.getCell( 0, 4 );
        pathFinder = new AStarPathFinder( new DefaultMapPathFactory() );
        paintState = PaintState.START;
        path = null;

        JButton runButton = new JButton( "Run" );
        runButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                runPathFinder();
            }
        } );

        final JToggleButton startToggle = new JToggleButton( "Start", true );
        startToggle.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                if( startToggle.isSelected() )
                    paintState = PaintState.START;
            }
        } );

        final JToggleButton targetToggle = new JToggleButton( "Target", false );
        targetToggle.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                if( targetToggle.isSelected() )
                    paintState = PaintState.TARGET;
            }
        } );

        final JToggleButton obstacleToggle = new JToggleButton( "Obstacle",
                false );
        obstacleToggle.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                if( obstacleToggle.isSelected() )
                    paintState = PaintState.OBSTACLE;
            }
        } );

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add( startToggle );
        buttonGroup.add( targetToggle );
        buttonGroup.add( obstacleToggle );

        JButton resetButton = new JButton( "Reset" );
        resetButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                path = null;
                for( IMapCell c : map.getCells() )
                    c.setTraversable( true );
            }
        } );

        JPanel bottomPanel = new JPanel();
        bottomPanel.add( runButton );
        bottomPanel.add( startToggle );
        bottomPanel.add( targetToggle );
        bottomPanel.add( obstacleToggle );
        bottomPanel.add( resetButton );

        canvas.addMouseListener( new MyMouseAdapter() );

        this.add( bottomPanel, BorderLayout.PAGE_END );
        pack();
    }

    /***************************************************************************
     * Runs the path finding algorithm.
     **************************************************************************/
    private void runPathFinder()
    {
        if( startCell != null && targetCell != null )
        {
            path = pathFinder.findPath( map, startCell, targetCell );
        }
    }

    /***************************************************************************
     * Returns the clicked cell, or null if one was not found.
     * 
     * @param p
     * @return
     **************************************************************************/
    private IMapCell getClickedCell( Point p )
    {
        return map.getCell( p.y / 64, p.x / 64 );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.GameFrame#renderScene(java.awt.Graphics2D)
     */
    @Override
    protected void renderScene( Graphics2D g2d )
    {
        map.render( g2d );

        // draw path
        if( path != null )
            for( IMapCell c : path.getNodes() )
                c.renderAlternateColor( g2d, Color.orange );

        // draw over start and target cells
        if( startCell != null )
            startCell.renderAlternateColor( g2d, Color.green );
        if( targetCell != null )
            targetCell.renderAlternateColor( g2d, Color.red );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.game.GameFrame#advanceScene(double)
     */
    @Override
    protected void advanceScene( double deltaSeconds )
    {
        // do nothing
    }

    /***************************************************************************
     * Simple custom {@link MouseAdapter}.
     **************************************************************************/
    private class MyMouseAdapter extends MouseAdapter
    {
        @Override
        public void mouseClicked( MouseEvent e )
        {
            Point p = e.getPoint();
            IMapCell c = getClickedCell( p );
            if( c != null )
            {
                switch( paintState )
                {
                case START:
                    startCell = c;
                    break;
                case TARGET:
                    targetCell = c;
                    break;
                case OBSTACLE:
                    c.setTraversable( false );
                    break;
                }
            }
        }
    }

    /***************************************************************************
     * Program entry point.
     * 
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        GameFrame frame = new AStarDemo();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
        frame.run();
    }
}
