package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javautils.IconManager;
import javautils.IconManager.IconSize;
import javautils.io.Preferences;
import javautils.swing.JValidatedTextField;
import javautils.swing.validation.TextValidator;
import javautils.swing.validation.ValidityChangeListener;
import javautils.task.ICompletable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

/*******************************************************************************
 * This is a {@link JDialog} that allows the user to set up preferences for the
 * ChatterBox application.
 ******************************************************************************/
public class PreferenceDialog extends JDialog
{
    private ICompletable completable;
    private JLabel userLabel;
    private JValidatedTextField userTextField;
    private JLabel hostLabel;
    private JValidatedTextField hostTextField;
    private JLabel portLabel;
    private JValidatedTextField portTextField;
    private JCheckBox autoConnectCheckBox;
    private JButton acceptButton;
    private JButton cancelButton;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public PreferenceDialog( ICompletable completable )
    {
        this.completable = completable;
        acceptButton = new JButton( IconManager.getIcon( IconManager.ACCEPT,
                IconSize.X32 ) );
        acceptButton.addActionListener( new AcceptListener() );
        acceptButton.setBorderPainted( false );

        cancelButton = new JButton( IconManager.getIcon( IconManager.CANCEL,
                IconSize.X32 ) );
        cancelButton.addActionListener( new CancelListener() );
        cancelButton.setBorderPainted( false );

        userTextField = new JValidatedTextField();
        hostTextField = new JValidatedTextField();
        portTextField = new JValidatedTextField();

        TextValidator textValidator = new TextValidator()
        {
            @Override
            public boolean validateText( String text )
            {
                return !text.isEmpty();
            }
        };

        TextValidator portValidator = new TextValidator()
        {
            @Override
            public boolean validateText( String text )
            {
                boolean valid = false;
                int i = 0;
                try
                {
                    i = Integer.parseInt( text );
                    if( i >= 1 && i <= 65535 )
                        valid = true;
                } catch( Exception e )
                {
                    valid = false;
                }
                return valid;
            }
        };

        ValidityChangeListener validityListener = new ValidityChangeListener()
        {
            @Override
            public void validityChanged( boolean newValidity )
            {
                newValidity = userTextField.isInputValid()
                        && hostTextField.isInputValid()
                        && portTextField.isInputValid();
                acceptButton.setEnabled( newValidity );
            }
        };

        userLabel = new JLabel( "Display name" );
        userTextField.addValidityChangeListener( validityListener );
        userTextField.setValidator( textValidator );
        userTextField.setText( Preferences.getPreference( "user" ) );

        hostLabel = new JLabel( "Host name" );
        hostTextField.addValidityChangeListener( validityListener );
        hostTextField.setValidator( textValidator );
        hostTextField.setText( Preferences.getPreference( "host" ) );

        portLabel = new JLabel( "Port (1-65535)" );
        portTextField.addValidityChangeListener( validityListener );
        portTextField.setValidator( portValidator );
        portTextField.setText( Preferences.getPreference( "port" ) );

        validityListener.validityChanged( false );

        autoConnectCheckBox = new JCheckBox( "Auto-connect?" );
        autoConnectCheckBox.setSelected( Boolean.parseBoolean( Preferences
                .getPreference( "autoconnect" ) ) );

        setupDialog();
    }

    /***************************************************************************
     * Sets up this dialog.
     **************************************************************************/
    private void setupDialog()
    {
        setTitle( "ChatterBox Preferences" );
        setSize( 350, 200 );
        setLocationRelativeTo( null );
        setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
        setModal( true );
        setLayout( new GridBagLayout() );
        add( userLabel, new GridBagConstraints( 0, 0, 1, 1, 0.33, 0.01,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        3, 3, 1, 3 ), 0, 0 ) );
        add( userTextField, new GridBagConstraints( 1, 0, 1, 1, 0.67, 0.01,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 1, 3 ), 0, 0 ) );
        add( hostLabel, new GridBagConstraints( 0, 1, 1, 1, 0.33, 0.01,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        1, 3, 1, 3 ), 0, 0 ) );
        add( hostTextField, new GridBagConstraints( 1, 1, 1, 1, 0.67, 0.01,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 1, 3, 1, 3 ), 0, 0 ) );
        add( portLabel, new GridBagConstraints( 0, 2, 1, 1, 0.33, 0.01,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        1, 3, 1, 3 ), 0, 0 ) );
        add( portTextField, new GridBagConstraints( 1, 2, 1, 1, 0.67, 0.01,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 1, 3, 1, 3 ), 0, 0 ) );
        add( autoConnectCheckBox, new GridBagConstraints( 0, 3, 2, 1, 1.0,
                0.01, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 1, 3, 1, 3 ), 0, 0 ) );
        add( acceptButton, new GridBagConstraints( 0, 4, 1, 1, 1.0, 0.01,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        add( cancelButton, new GridBagConstraints( 1, 4, 1, 1, 1.0, 0.01,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
    }

    /***************************************************************************
     * Listener for the Accept button.
     **************************************************************************/
    private class AcceptListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            Preferences.setPreference( "user", userTextField.getText() );
            Preferences.setPreference( "host", hostTextField.getText() );
            Preferences.setPreference( "port", portTextField.getText() );
            Preferences.setPreference( "autoconnect",
                    Boolean.toString( autoConnectCheckBox.isSelected() ) );
            Preferences.writePreferences();
            PreferenceDialog.this.dispose();
            completable.notifyComplete();
        }
    }

    /***************************************************************************
     * Listener for the Cancel button.
     **************************************************************************/
    private class CancelListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            PreferenceDialog.this.dispose();
            completable.notifyCancelled();
        }
    }
}
