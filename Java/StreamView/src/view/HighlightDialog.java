package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javautils.IconManager;
import javautils.IconManager.IconSize;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import presenter.IViewUpdater;

/*******************************************************************************
 * Simple dialog for specifying a bit offset and length for highlighting.
 ******************************************************************************/
public class HighlightDialog extends JDialog
{
    private int startBit;
    private int bitLength;

    private JButton acceptButton;
    private JButton cancelButton;

    private JLabel startBitLabel;
    private JTextField startBitTextField;
    private JLabel bitLengthLabel;
    private JTextField bitLengthTextField;

    private IViewUpdater updater;

    /***************************************************************************
     * Constructor
     * 
     * @param updater
     **************************************************************************/
    public HighlightDialog( IViewUpdater updater )
    {
        this.updater = updater;
        setupDialog();
    }

    /***************************************************************************
     * Sets up this dialog.
     **************************************************************************/
    private void setupDialog()
    {
        setTitle( "Highlight bits" );
        setSize( 300, 200 );
        setModal( true );
        setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        setLocationRelativeTo( null );

        startBitLabel = new JLabel( "Start bit:" );
        startBitTextField = new JTextField();
        bitLengthLabel = new JLabel( "Length:" );
        bitLengthTextField = new JTextField();

        acceptButton = new JButton( IconManager.getIcon( IconManager.ACCEPT,
                IconSize.X32 ) );
        acceptButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                accept();
            }
        } );

        cancelButton = new JButton( IconManager.getIcon( IconManager.CANCEL,
                IconSize.X32 ) );
        cancelButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                cancel();
            }
        } );

        setLayout( new GridBagLayout() );
        add( startBitLabel, new GridBagConstraints( 0, 0, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( startBitTextField, new GridBagConstraints( 0, 1, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( bitLengthLabel, new GridBagConstraints( 0, 2, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( bitLengthTextField, new GridBagConstraints( 0, 3, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( acceptButton, new GridBagConstraints( 0, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        add( cancelButton, new GridBagConstraints( 1, 4, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );

        getRootPane().setDefaultButton( acceptButton );
    }

    /***************************************************************************
     * Parses fields and hides dialog.
     **************************************************************************/
    private void accept()
    {
        try
        {
            startBit = Integer.parseInt( startBitTextField.getText() );
            bitLength = Integer.parseInt( bitLengthTextField.getText() );
        } catch( Exception e )
        {
            startBit = 0;
            bitLength = 0;
        }

        startBitTextField.setText( "" );
        bitLengthTextField.setText( "" );
        setVisible( false );
        if( updater != null )
            updater.updateView();
    }

    /***************************************************************************
     * Hides this dialog.
     **************************************************************************/
    private void cancel()
    {
        startBit = 0;
        bitLength = 0;
        startBitTextField.setText( "" );
        bitLengthTextField.setText( "" );
        setVisible( false );
    }

    /***************************************************************************
     * Returns the start bit that was specified.
     * 
     * @return
     **************************************************************************/
    public int getStartBit()
    {
        return startBit;
    }

    /***************************************************************************
     * Returns the bit length that was specified.
     * 
     * @return
     **************************************************************************/
    public int getBitLength()
    {
        return bitLength;
    }
}
