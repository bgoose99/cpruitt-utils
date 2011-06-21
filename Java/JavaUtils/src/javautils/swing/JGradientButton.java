package javautils.swing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;

import javautils.ColorUtils;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*******************************************************************************
 * The <code>JGradientButton</code> acts like a normal JButton with one
 * exception: the content are is filled with a color gradient. As such, the
 * <code>setContentAreaFilled</code> method has been overridden to remove its
 * functionality.
 * 
 * @author pruittcd
 * @see javax.swing.JButton
 ******************************************************************************/
public class JGradientButton extends JButton
{
    /** Margin around this button. Should be even. */
    private static final int MARGIN = 2;

    /** Arc width for drawing rounded corners. */
    private static final int ARC_WIDTH = 5;

    /** Legal gradient paint directions. */
    public static enum GradientDirection
    {
        TOP_TO_BOTTOM, BOTTOM_TO_TOP, LEFT_TO_RIGHT, RIGHT_TO_LEFT;
    }

    private GradientDirection gradientDirection;
    private Color firstColor;
    private Color secondColor;
    private boolean borderPainted;

    /***************************************************************************
     * Constructor
     * 
     * @param firstColor
     * @param secondColor
     **************************************************************************/
    public JGradientButton( Color firstColor, Color secondColor )
    {
        super();
        this.setFirstColor( firstColor );
        this.setSecondColor( secondColor );
        super.setContentAreaFilled( false );
        super.setBorderPainted( false );
        this.setRolloverEnabled( true );
        this.gradientDirection = GradientDirection.TOP_TO_BOTTOM;
        this.borderPainted = true;
    }

    /***************************************************************************
     * Constructor
     * 
     * @param firstColor
     * @param secondColor
     * @param text
     **************************************************************************/
    public JGradientButton( Color firstColor, Color secondColor, String text )
    {
        this( firstColor, secondColor );
        this.setText( text );
    }

    /***************************************************************************
     * Paints a gradient where this button's content area would usually be
     * painted, then calls super.paintComponent().
     **************************************************************************/
    @Override
    protected void paintComponent( Graphics g )
    {
        Graphics2D g2d = (Graphics2D)g;
        Point point1 = new Point();
        Point point2 = new Point();
        Color color1;
        Color color2;

        // determine direction to paint the gradient
        switch( gradientDirection )
        {
        case BOTTOM_TO_TOP:
            // bottom
            point1.x = this.getWidth() / 2;
            point1.y = this.getHeight() - MARGIN;
            // top
            point2.x = this.getWidth() / 2;
            point2.y = MARGIN;
            break;
        case LEFT_TO_RIGHT:
            // left
            point1.x = MARGIN;
            point1.y = this.getHeight() / 2;
            // right
            point2.x = this.getWidth() - MARGIN;
            point2.y = this.getHeight() / 2;
            break;
        case RIGHT_TO_LEFT:
            // right
            point1.x = this.getWidth() - MARGIN;
            point1.y = this.getHeight() / 2;
            // left
            point2.x = MARGIN;
            point2.y = this.getHeight() / 2;
            break;
        case TOP_TO_BOTTOM:
        default:
            // top
            point1.x = this.getWidth() / 2;
            point1.y = MARGIN;
            // bottom
            point2.x = this.getWidth() / 2;
            point2.y = this.getHeight() - MARGIN;
            break;
        }

        // determine what colors to paint
        if( this.model.isRollover() && !this.model.isPressed() )
        {
            color1 = ColorUtils.getScaledColor( firstColor, 20 );
            color2 = ColorUtils.getScaledColor( secondColor, 20 );
        } else if( this.model.isPressed() )
        {
            color1 = ColorUtils.getScaledColor( firstColor, -20 );
            color2 = ColorUtils.getScaledColor( secondColor, -20 );
        } else
        {
            color1 = firstColor;
            color2 = secondColor;
        }

        // paint the gradient
        GradientPaint grad = new GradientPaint( point1.x, point1.y, color1,
                point2.x, point2.y, color2, false );
        g2d.setPaint( grad );
        g2d.fillRoundRect( MARGIN, MARGIN, this.getWidth() - ( 2 * MARGIN ),
                this.getHeight() - ( 2 * MARGIN ), ARC_WIDTH, ARC_WIDTH );

        // paint the border, if necessary
        if( this.borderPainted )
        {
            g2d.setPaint( Color.black );
            g2d.drawRoundRect( MARGIN, MARGIN,
                    this.getWidth() - ( 2 * MARGIN ), this.getHeight()
                            - ( 2 * MARGIN ), ARC_WIDTH, ARC_WIDTH );
        }

        // finally, call the standard JButton paintComponent method
        super.paintComponent( g );
    }

    /***************************************************************************
     * This method does nothing for a JGradientButton.
     **************************************************************************/
    @Override
    public void setContentAreaFilled( boolean b )
    {
        return;
    }

    /***************************************************************************
     * Sets the first color used when drawing the gradient for this button.
     * 
     * @param color
     **************************************************************************/
    public void setFirstColor( Color color )
    {
        if( color == null )
            this.firstColor = Color.lightGray;
        else
            this.firstColor = color;
    }

    /***************************************************************************
     * Sets the second color used when drawing the gradient for this button.
     * 
     * @param color
     **************************************************************************/
    public void setSecondColor( Color color )
    {
        if( color == null )
            this.secondColor = Color.gray;
        else
            this.secondColor = color;
    }

    /***************************************************************************
     * Sets the direction the gradient is painted. It will always be painted
     * from the first color to the second color.
     * 
     * @param gradientDirection
     **************************************************************************/
    public void setGradientDirection( GradientDirection gradientDirection )
    {
        this.gradientDirection = gradientDirection;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setBorderPainted( boolean painted )
    {
        this.borderPainted = painted;
    }

    /***************************************************************************
     * JGradientButton demo frame.
     * 
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        class DemoRunner extends FrameRunner
        {
            @Override
            protected JFrame createFrame()
            {
                JFrame frame = new JFrame();
                JPanel panel = new JPanel();

                JGradientButton button1 = new JGradientButton( Color.blue,
                        Color.cyan, "Bottom to Top" );
                button1.setGradientDirection( GradientDirection.BOTTOM_TO_TOP );
                button1.setFocusable( false );

                JGradientButton button2 = new JGradientButton( Color.blue,
                        Color.cyan, "Top to Bottom" );
                button2.setGradientDirection( GradientDirection.TOP_TO_BOTTOM );
                button2.setFocusable( false );

                JGradientButton button3 = new JGradientButton( Color.blue,
                        Color.cyan, "Left to Right" );
                button3.setGradientDirection( GradientDirection.LEFT_TO_RIGHT );
                button3.setBorderPainted( false );
                button3.setFocusable( false );

                JGradientButton button4 = new JGradientButton( Color.blue,
                        Color.cyan, "Right to Left" );
                button4.setGradientDirection( GradientDirection.RIGHT_TO_LEFT );
                button4.setBorderPainted( false );
                button4.setFocusable( false );

                panel.setLayout( new GridBagLayout() );

                panel.add( button1,
                        new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( button2,
                        new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( button3,
                        new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( button4,
                        new GridBagConstraints( 1, 1, 1, 1, 1.0, 1.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.BOTH,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );

                frame.setTitle( "JGradientButton demo" );
                frame.setContentPane( panel );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( 300, 300 );
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );

                return frame;
            }

            @Override
            protected boolean validate()
            {
                return true;
            }
        }

        SwingUtilities.invokeLater( new DemoRunner() );
    }
}
