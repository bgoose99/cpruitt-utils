package view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javautils.Utils;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class HexNavPanel extends JPanel implements NavPanel
{
    private JLabel filenameLabel;
    private JLabel blockLabel;
    private SpinnerNumberModel spinnerNumberModel;
    private JSpinner blockSpinner;
    private JButton gotoButton;
    private JButton gotoPrevButton;
    private JButton gotoNextButton;
    private JLabel bytesLabel;
    private JLabel offsetLabel;
    private JLabel binLabel;
    private JLabel octLabel;
    private JLabel decLabel;
    private JProgressBar progressBar;

    public HexNavPanel()
    {
        super();

        setupPanel();
    }

    private void setupPanel()
    {
        filenameLabel = new JLabel( "untitled.bin" );
        blockLabel = new JLabel( "Block 0 of 0" );
        spinnerNumberModel = new SpinnerNumberModel( 0, 0, 0, 1 );
        blockSpinner = new JSpinner( spinnerNumberModel );
        blockSpinner.addMouseWheelListener( new MouseWheelListener()
        {
            @Override
            public void mouseWheelMoved( MouseWheelEvent e )
            {
                if( e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL )
                {
                    if( e.getWheelRotation() < 0 )
                        blockSpinner.setValue( blockSpinner.getNextValue() );
                    else
                        blockSpinner.setValue( blockSpinner.getPreviousValue() );
                }
            }
        } );
        gotoButton = new JButton();
        gotoButton.setFocusable( false );
        gotoButton.setEnabled( false );
        gotoPrevButton = new JButton();
        gotoPrevButton.setFocusable( false );
        gotoPrevButton.setEnabled( false );
        gotoNextButton = new JButton();
        gotoNextButton.setFocusable( false );
        gotoNextButton.setEnabled( false );
        bytesLabel = new JLabel( "0 bytes" );
        offsetLabel = new JLabel( "Offset: 0" );
        binLabel = new JLabel( "Bin: " );
        octLabel = new JLabel( "Oct: " );
        decLabel = new JLabel( "Dec: " );
        progressBar = new JProgressBar();
        progressBar.setMinimum( 0 );

        setLayout( new BoxLayout( this, BoxLayout.PAGE_AXIS ) );

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout( new GridBagLayout() );

        upperPanel
                .add( filenameLabel, new GridBagConstraints( 0, 0, 1, 1, 0.55,
                        1.0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        upperPanel.add( blockLabel, new GridBagConstraints( 1, 0, 1, 1, 0.37,
                1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        upperPanel.add( blockSpinner, new GridBagConstraints( 2, 0, 1, 1, 0.05,
                1.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        upperPanel.add( gotoButton, new GridBagConstraints( 3, 0, 1, 1, 0.01,
                1.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        upperPanel.add( gotoPrevButton, new GridBagConstraints( 4, 0, 1, 1,
                0.01, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        upperPanel.add( gotoNextButton, new GridBagConstraints( 5, 0, 1, 1,
                0.01, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );

        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout( new GridBagLayout() );

        lowerPanel.add( bytesLabel, new GridBagConstraints( 0, 0, 1, 1, 0.3,
                1.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        lowerPanel.add( offsetLabel, new GridBagConstraints( 1, 0, 1, 1, 0.25,
                1.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        lowerPanel.add( binLabel, new GridBagConstraints( 2, 0, 1, 1, 0.15,
                1.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        lowerPanel.add( octLabel, new GridBagConstraints( 3, 0, 1, 1, 0.1, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        lowerPanel.add( decLabel, new GridBagConstraints( 4, 0, 1, 1, 0.1, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        lowerPanel.add( progressBar, new GridBagConstraints( 5, 0, 1, 1, 0.1,
                1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );

        add( upperPanel );
        add( lowerPanel );

    }

    @Override
    public void setFileDetails( String filename, long bytes )
    {
        filenameLabel.setText( filename );
        bytesLabel.setText( Utils.formatNumber( bytes ) + " bytes" );
    }

    @Override
    public void setOffset( long offset )
    {
        offsetLabel.setText( "Offset: " + Utils.formatNumber( offset ) );
    }

    @Override
    public void setBlock( int blockNumber, int blockCount )
    {
        blockLabel.setText( "Block " + Utils.formatNumber( blockNumber )
                + " of " + Utils.formatNumber( blockCount ) );
        if( ( new Integer( blockCount ) ) != spinnerNumberModel.getMaximum() )
        {
            // set up new spinner model
            spinnerNumberModel = new SpinnerNumberModel( blockNumber, 0,
                    blockCount, 1 );
            blockSpinner.setModel( spinnerNumberModel );
        } else
        {
            spinnerNumberModel.setValue( blockNumber );
        }
        progressBar.setMaximum( blockCount );
        progressBar.setValue( blockNumber );
    }

    @Override
    public int getUserSpecifiedBlock()
    {
        return (int)spinnerNumberModel.getValue();
    }

    @Override
    public void setGotoAction( Action gotoAction )
    {
        gotoButton.setAction( gotoAction );
    }

    @Override
    public void setGotoPrevAction( Action gotoPrevAction )
    {
        gotoPrevButton.setAction( gotoPrevAction );
    }

    @Override
    public void setGotoNextAction( Action gotoNextAction )
    {
        gotoNextButton.setAction( gotoNextAction );
    }

    @Override
    public void setCurrentByte( byte b )
    {
        binLabel.setText( "Bin: "
                + String.format( "%8s", Integer.toBinaryString( 0xFF & b ) )
                        .replace( ' ', '0' ) );
        octLabel.setText( "Oct: "
                + String.format( "%3s", Integer.toOctalString( 0xFF & b ) )
                        .replace( ' ', '0' ) );
        decLabel.setText( "Dec: "
                + String.format( "%3s", Integer.toString( 0xFF & b ) ).replace(
                        ' ', '0' ) );
    }

    @Override
    public Component getComponent()
    {
        return this;
    }
}
