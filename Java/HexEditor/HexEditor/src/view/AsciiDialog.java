package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javautils.IconManager;
import javautils.IconManager.IconSize;

import javax.swing.JDialog;
import javax.swing.JScrollPane;

/*******************************************************************************
 * This dialog shows an {@link AsciiTable}.
 ******************************************************************************/
public class AsciiDialog extends JDialog
{
    /***************************************************************************
     * Constructor
     **************************************************************************/
    public AsciiDialog()
    {
        setupFrame();
    }

    /***************************************************************************
     * Sets up this frame (dialog...whatever).
     **************************************************************************/
    private void setupFrame()
    {
        AsciiTable table = new AsciiTable();
        JScrollPane pane = new JScrollPane( table );

        setLayout( new GridBagLayout() );
        add( pane, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        pack();

        setTitle( "ASCII Character Code Table" );
        setLocationRelativeTo( null );
        setIconImage( IconManager.getImage( IconManager.KEY_A, IconSize.X16 ) );
    }
}
