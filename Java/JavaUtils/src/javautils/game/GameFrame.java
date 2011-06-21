package javautils.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public abstract class GameFrame extends JFrame
{
    protected final Font defaultFont;

    protected Dimension canvasSize;
    protected Canvas canvas;
    private BufferStrategy bufferStrategy;
    private GraphicsEnvironment graphicsEnv;
    private GraphicsDevice graphicsDevice;
    private GraphicsConfiguration graphicsConfig;
    private BufferedImage bufferedImage;
    private Graphics graphics;
    private Graphics2D graphics2D;
    protected Color backgroundColor;

    private boolean displayFps;
    private Color fpsColor;

    private boolean isRunning;

    /** Renders the scene. */
    protected abstract void renderScene( Graphics2D g2d );

    /** Advances the scene. */
    protected abstract void advanceScene( double deltaSeconds );

    /***************************************************************************
     * Constructor
     * 
     * @param canvasSize
     **************************************************************************/
    public GameFrame( Dimension canvasSize )
    {
        super();

        this.canvasSize = canvasSize;

        defaultFont = new Font( "Courier New", Font.BOLD, 12 );
        displayFps = true;
        fpsColor = Color.black;
        isRunning = true;

        canvas = new Canvas();
        canvas.setIgnoreRepaint( true );
        canvas.setSize( this.canvasSize );

        setupFrame();
        setupCanvas();

        addWindowListener( new GameFrameWindowAdapter() );
    }

    /***************************************************************************
     * Sets up this frame.
     **************************************************************************/
    protected void setupFrame()
    {
        setLayout( new BorderLayout() );
        add( canvas, BorderLayout.CENTER );
        pack();
    }

    /***************************************************************************
     * Sets up the canvas used for drawing.
     **************************************************************************/
    protected void setupCanvas()
    {
        // create back buffer
        canvas.createBufferStrategy( 2 );
        bufferStrategy = canvas.getBufferStrategy();

        // get graphics config
        graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsDevice = graphicsEnv.getDefaultScreenDevice();
        graphicsConfig = graphicsDevice.getDefaultConfiguration();

        // create off-screen surface for drawing
        bufferedImage = graphicsConfig.createCompatibleImage( canvasSize.width,
                canvasSize.height );
    }

    /***************************************************************************
     * Main game loop.
     **************************************************************************/
    public final void run()
    {
        int frames = 0;
        double fps = 0.0;
        long totalTime = 0;
        long currentTime = System.currentTimeMillis();
        long lastTime = currentTime;
        double deltaSeconds = 0.0;

        while( isRunning )
        {
            try
            {
                currentTime = System.currentTimeMillis();

                // calculate elapsed time
                deltaSeconds = ( currentTime - lastTime ) / 1000.0;

                // calculate fps
                totalTime += currentTime - lastTime;
                lastTime = currentTime;

                if( totalTime > 1000 )
                {
                    totalTime -= 1000;
                    fps = frames;
                    frames = 0;
                }

                frames++;

                // clear the back buffer
                graphics2D = bufferedImage.createGraphics();
                graphics2D.setColor( backgroundColor );
                graphics2D.fillRect( 0, 0, canvasSize.width, canvasSize.height );

                // render
                renderScene( graphics2D );

                // draw FPS, if necessary
                if( displayFps )
                {
                    graphics2D.setFont( defaultFont );
                    graphics2D.setColor( fpsColor );
                    graphics2D.drawString( "FPS: " + fps, 10, 10 );
                }

                // blit image and flip
                graphics = bufferStrategy.getDrawGraphics();
                graphics.drawImage( bufferedImage, 0, 0, null );

                if( !bufferStrategy.contentsLost() )
                    bufferStrategy.show();

                // advance scene
                advanceScene( deltaSeconds );

                // let the OS have some time
                Thread.yield();
            } catch( Exception e )
            {
                e.printStackTrace();
                return;
            }
        }
    }

    /***************************************************************************
     * Tells this frame whether it should display frames-per-second or not.
     * 
     * @param display
     **************************************************************************/
    protected void setDisplayFps( boolean display )
    {
        displayFps = display;
    }

    /***************************************************************************
     * Sets the color used to display the FPS.
     * 
     * @param color
     **************************************************************************/
    protected void setFpsColor( Color color )
    {
        fpsColor = color;
    }

    /***************************************************************************
     * Exits this application.
     **************************************************************************/
    protected void exit()
    {
        isRunning = false;

        if( graphics != null )
            graphics.dispose();
        if( graphics2D != null )
            graphics2D.dispose();

        this.dispose();
        System.out.println( "Application exiting" );
    }

    /***************************************************************************
     * Custom {@link WindowAdapter}.
     **************************************************************************/
    private class GameFrameWindowAdapter extends WindowAdapter
    {
        @Override
        public void windowClosing( WindowEvent e )
        {
            exit();
        }
    }
}
