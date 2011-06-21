package ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import objects.NonPlayer;
import objects.Player;
import utils.MeteorImageManager;

public class GameFrame extends JFrame
{
    public final static int WIDTH = 800;
    public final static int HEIGHT = 800;

    private enum GameState
    {
        SPLASH_SCREEN, PLAY, PAUSE, WIN, END_GAME
    }

    private GameState state;

    private Canvas canvas = null;
    private BufferStrategy bufferStrategy = null;
    private GraphicsEnvironment graphicsEnvironment = null;
    private GraphicsDevice graphicsDevice = null;
    private GraphicsConfiguration graphicsConfig = null;
    private BufferedImage image = null;
    private Graphics graphics = null;
    private Graphics2D graphics2D = null;
    private Color bgcolor = null;
    private Font defaultFont = null;
    private Font pauseFont = null;
    private Font scoreFont = null;

    private boolean displayFPS = true;

    private Player player;
    private final Point playerStatusPoint;
    private final Rectangle playerHealthContainer;
    private final Rectangle playerHealthBox;

    private NonPlayer nonPlayer;

    private boolean running = true;

    public GameFrame()
    {
        // set up finals
        playerStatusPoint = new Point( 10, HEIGHT - 50 );
        playerHealthContainer = new Rectangle( 40, HEIGHT - 50, 204, 24 );
        playerHealthBox = new Rectangle( 42, HEIGHT - 48, 200, 20 );

        // init state
        state = GameState.SPLASH_SCREEN;

        // set up bg color
        bgcolor = Color.black;

        // set up fonts
        defaultFont = new Font( "Courier New", Font.BOLD, 12 );
        pauseFont = new Font( Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 40 );
        scoreFont = new Font( Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 14 );

        // set up the canvas
        canvas = new Canvas();
        canvas.setIgnoreRepaint( true );
        canvas.setSize( WIDTH, HEIGHT );
        canvas.addMouseListener( new CustomMouseAdapter() );
        canvas.addKeyListener( new CustomKeyAdapter() );

        // set up objects
        player = new Player( new Dimension( WIDTH, HEIGHT ), new Point(
                WIDTH / 2, HEIGHT - 10 ) );
        nonPlayer = new NonPlayer( new Dimension( WIDTH, HEIGHT ) );

        setupFrame();
        setupCanvas();
    }

    private void setupFrame()
    {
        this.addWindowListener( new CustomWindowAdapter() );
        this.setResizable( false );
        this.setTitle( "Meteors v1.0" );
        this.setLayout( new BorderLayout() );
        this.setBackground( bgcolor );
        this.add( canvas, BorderLayout.CENTER );

        // get the cursor image
        Image cursorImage = MeteorImageManager
                .getImage( MeteorImageManager.CROSSHAIR_CURSOR );

        // put the hotspot in the middle of the cursor
        Point hotspot = new Point( cursorImage.getWidth( null ) / 2 + 1,
                cursorImage.getHeight( null ) / 2 + 1 );

        // set the custom cursor
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImage, hotspot, "Crosshair Cursor" );
        this.setCursor( cursor );

        pack();
    }

    private void setupCanvas()
    {
        // create the back buffer
        canvas.createBufferStrategy( 2 );
        bufferStrategy = canvas.getBufferStrategy();

        // get the graphics config
        graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        graphicsConfig = graphicsDevice.getDefaultConfiguration();

        // create off-screen surface for drawing
        image = graphicsConfig.createCompatibleImage( WIDTH, HEIGHT );
    }

    public void run()
    {
        int frames = 0;
        double fps = 0.0;
        long totalTime = 0;
        long currentTime = System.currentTimeMillis();
        long lastTime = currentTime;
        double deltaSeconds = 0.0;

        Image bgImage = MeteorImageManager
                .getImage( MeteorImageManager.BACKGROUND );

        while( running )
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
                graphics2D = image.createGraphics();
                graphics2D.setColor( bgcolor );
                graphics2D.fillRect( 0, 0, WIDTH, HEIGHT );

                switch( state )
                {
                case SPLASH_SCREEN:
                    graphics2D.setFont( pauseFont );
                    graphics2D.setColor( Color.white );
                    graphics2D.drawString( "Click to play", 300, 300 );
                    break;
                case PLAY:
                    // draw bg image
                    graphics2D.drawImage( bgImage, 0, 0, WIDTH, HEIGHT, null );

                    // render player objects
                    player.render( graphics2D );

                    // render non-player objects
                    nonPlayer.render( graphics2D );

                    // draw the score
                    graphics2D.setFont( scoreFont );
                    graphics2D.setColor( Color.white );
                    graphics2D.drawString( "Score: " + player.getScore(), 10,
                            10 );

                    // draw the player's status image
                    graphics2D.drawImage( player.getStatusImage(),
                            playerStatusPoint.x, playerStatusPoint.y, null );

                    // draw the player's health
                    // color box
                    graphics2D.setColor( Color.red );
                    graphics2D.fillRect( playerHealthContainer.x,
                            playerHealthContainer.y,
                            playerHealthContainer.width,
                            playerHealthContainer.height );
                    // black box
                    graphics2D.setColor( Color.black );
                    graphics2D.fillRect( playerHealthBox.x, playerHealthBox.y,
                            playerHealthBox.width, playerHealthBox.height );
                    // health bar
                    graphics2D.drawImage( MeteorImageManager
                            .getImage( MeteorImageManager.HEALTH_BAR ),
                            playerHealthBox.x, playerHealthBox.y,
                            playerHealthBox.x + player.getHealth(),
                            playerHealthBox.y + playerHealthBox.height, 0, 0,
                            player.getHealth(), 20, null );

                    // advance the scene
                    player.advanceScene( deltaSeconds );
                    nonPlayer.advanceScene( deltaSeconds );

                    // check for collisions
                    nonPlayer.detectCollisions( player );

                    // check to see if the player is dead
                    if( !player.isAlive() )
                        state = GameState.END_GAME;

                    break;
                case PAUSE:
                    // draw bg image
                    graphics2D.drawImage( bgImage, 0, 0, WIDTH, HEIGHT, null );

                    // render player objects
                    player.render( graphics2D );

                    // render non-player objects
                    nonPlayer.render( graphics2D );

                    // render paused text
                    graphics2D.setFont( pauseFont );
                    graphics2D.setColor( Color.white );
                    graphics2D.drawString( "PAUSED", WIDTH / 2 - 100,
                            HEIGHT / 2 );

                    break;
                case WIN:
                    break;
                case END_GAME:
                    // render some text
                    graphics2D.setFont( pauseFont );
                    graphics2D.setColor( Color.white );
                    graphics2D.drawString( player.getStatusText(), 50, 300 );
                    graphics2D.setFont( defaultFont );
                    graphics2D.drawString( "Press spacebar to play again.",
                            100, 400 );

                    break;
                }

                if( displayFPS )
                {
                    // draw fps
                    graphics2D.setFont( defaultFont );
                    graphics2D.setColor( Color.yellow );
                    graphics2D.drawString( "FPS: " + fps, WIDTH - 75, 10 );
                }

                // blit the image and flip
                graphics = bufferStrategy.getDrawGraphics();
                graphics.drawImage( image, 0, 0, null );
                if( !bufferStrategy.contentsLost() )
                    bufferStrategy.show();

                // give the OS a little time
                Thread.yield();
            } catch( Exception e )
            {
                e.printStackTrace();
                continue;
            }
        }
    }

    public void exit()
    {
        System.out.println( "In exit handler" );
        running = false;

        if( graphics != null )
            graphics.dispose();
        if( graphics2D != null )
            graphics2D.dispose();

        System.exit( 0 );
    }

    private class CustomWindowAdapter extends WindowAdapter
    {
        @Override
        public void windowClosed( WindowEvent e )
        {
            exit();
        }

        @Override
        public void windowClosing( WindowEvent e )
        {
            exit();
        }
    }

    private class CustomMouseAdapter extends MouseAdapter
    {
        @Override
        public void mouseReleased( MouseEvent e )
        {
            switch( state )
            {
            case SPLASH_SCREEN:
                state = GameState.PLAY;
                break;
            case PLAY:
                player.fireBullet( e.getPoint() );
                break;
            case PAUSE:
            case WIN:
            case END_GAME:
            default:
                break;
            }
        }
    }

    private class CustomKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyTyped( KeyEvent e )
        {
            if( e.getKeyChar() == 'p' || e.getKeyChar() == 'P' )
            {
                switch( state )
                {
                case PLAY:
                    state = GameState.PAUSE;
                    break;
                case PAUSE:
                    state = GameState.PLAY;
                    break;
                default:
                    break;
                }
            } else if( e.getKeyChar() == 'f' || e.getKeyChar() == 'F' )
            {
                displayFPS = !displayFPS;
            } else if( e.getKeyChar() == ' ' )
            {
                switch( state )
                {
                case WIN:
                case END_GAME:
                    state = GameState.SPLASH_SCREEN;
                    player.reset();
                    nonPlayer.reset();
                    break;
                default:
                    break;
                }
            }
        }
    }
}
