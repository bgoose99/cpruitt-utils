package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javautils.IconManager;
import javautils.IconManager.IconSize;
import javautils.licensing.FatCowLicenseDialog;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/*******************************************************************************
 * This dialog shows information about the HexEditor application.
 ******************************************************************************/
public class AboutDialog extends JDialog
{
    private final static String ABOUT = "<html><b>Hex Editor</b><br>"
            + "HexEditor was inspired by a fellow programmer's Java implementation<br>"
            + "of a hex editor. After convincing myself of the usefulness of such<br>"
            + "an application, I decided to write my own.<br>"
            + "Written by Charles Pruitt, 2010<br>(Thanks Joseph!)</html>";

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public AboutDialog()
    {
        setupFrame();
    }

    /***************************************************************************
     * Sets up this frame (dialog...whatever).
     **************************************************************************/
    private void setupFrame()
    {
        JLabel label = new JLabel( ABOUT );
        label.setIcon( IconManager
                .getIcon( IconManager.LIGHTBULB, IconSize.X32 ) );
        label.setIconTextGap( 10 );

        JButton closeButton = new JButton();
        closeButton.setFocusable( false );
        closeButton.setAction( new AbstractAction( "OK", IconManager.getIcon(
                IconManager.ACCEPT, IconManager.IconSize.X16 ) )
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                AboutDialog.this.dispose();
            }
        } );

        JButton attribButton = new JButton();
        attribButton.setFocusable( false );
        attribButton.setAction( new AbstractAction( "Attributions" )
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                FatCowLicenseDialog dialog = new FatCowLicenseDialog( null );
                dialog.setVisible( true );
            }
        } );

        setLayout( new GridBagLayout() );
        add( label, new GridBagConstraints( 0, 0, 2, 1, 0.99, 0.01,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        add( attribButton, new GridBagConstraints( 0, 1, 1, 1, 0.01, 0.01,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        add( closeButton, new GridBagConstraints( 1, 1, 1, 1, 0.01, 0.01,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        pack();

        getRootPane().setDefaultButton( closeButton );
        setTitle( "About HexEditor" );
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        setAlwaysOnTop( true );
        setModal( true );
        setLocationRelativeTo( null );
        setIconImage( IconManager.getImage( IconManager.YINYANG, IconSize.X16 ) );
    }
}
