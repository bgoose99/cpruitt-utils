package javautils.licensing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javautils.Utils;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/*******************************************************************************
 * This dialog is used to display the license information for the Fat Cow icons
 * contained in this project.
 ******************************************************************************/
public class FatCowLicenseDialog extends JDialog
{
    private final static String FATCOW = "/javautils/licensing/FatCowIconsLicense.html";

    /***************************************************************************
     * Constructor
     * 
     * @param owner
     **************************************************************************/
    public FatCowLicenseDialog( Frame owner )
    {
        super( owner, true );

        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType( "text/html" );
        editorPane.setEditable( false );

        try
        {
            editorPane.setPage( Utils.loadResourceURL( FATCOW ) );
        } catch( Exception e )
        {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane( editorPane );

        setLayout( new BorderLayout() );
        add( scrollPane, BorderLayout.CENTER );

        setTitle( "Licensing info" );
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        setAlwaysOnTop( true );
        setPreferredSize( new Dimension( 500, 500 ) );
        pack();
        setLocationRelativeTo( owner );
    }

    /***************************************************************************
     * Demo
     * 
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            @Override
            public void run()
            {
                FatCowLicenseDialog dialog = new FatCowLicenseDialog( null );
                dialog.setVisible( true );
            }
        } );
    }
}
