package javautils.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javautils.Utils;
import javautils.swing.validation.TextValidator;
import javautils.swing.validation.ValidityChangeListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/*******************************************************************************
 * This class is meant to replace a standard JTextField any time validation is
 * required. If no TextValidator is specified, it acts as a normal JTextField.
 * Multiple ValidityChangeListeners may be added.
 ******************************************************************************/
public class JValidatedTextField extends JTextField
{
    /** Valid background color */
    private Color validBackground;

    /** Invalid background color */
    private Color invalidBackground;

    /** Text validator */
    private TextValidator validator;

    /** List of validity change listeners */
    private List<ValidityChangeListener> validityChangeListeners;

    /** The state of this text field */
    private boolean valid;

    /** Flag indicating whether this field has been initialized or not */
    private boolean isInitialized = false;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public JValidatedTextField()
    {
        this( "" );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param string
     **************************************************************************/
    public JValidatedTextField( String string )
    {
        super( string );
        isInitialized = true;

        validBackground = this.getBackground();
        invalidBackground = Color.red;
        validator = null;
        valid = true;
        validityChangeListeners = new LinkedList<ValidityChangeListener>();
        this.getDocument().addDocumentListener( new DocumentListener()
        {
            public void removeUpdate( DocumentEvent e )
            {
                validateText();
            }

            public void insertUpdate( DocumentEvent e )
            {
                validateText();
            }

            public void changedUpdate( DocumentEvent e )
            {
                validateText();
            }
        } );
    }

    /***************************************************************************
     * Returns the validity of this text field.
     **************************************************************************/
    public boolean isInputValid()
    {
        return valid;
    }

    /***************************************************************************
     * Validates the text currently in this text field.
     **************************************************************************/
    private void validateText()
    {
        if( validator != null )
        {
            boolean oldValidity = valid;
            valid = validator.validateText( this.getText() );

            if( oldValidity != valid )
            {
                fireValidityChanged();
                setComponentValid( valid );
            }
        }
    }

    /***************************************************************************
     * Sets the validity of this text field.
     * 
     * @param valid
     **************************************************************************/
    protected void setComponentValid( boolean valid )
    {
        if( valid )
        {
            super.setBackground( validBackground );
        } else
        {
            super.setBackground( invalidBackground );
        }
    }

    /***************************************************************************
     * Notifies all listeners that the validity of this text field has changed.
     **************************************************************************/
    protected void fireValidityChanged()
    {
        for( ValidityChangeListener listener : validityChangeListeners )
        {
            listener.validityChanged( valid );
        }
    }

    /***************************************************************************
     * Adds a ValidityChangeListener to this text field.
     * 
     * @param listener
     **************************************************************************/
    public void addValidityChangeListener( ValidityChangeListener listener )
    {
        validityChangeListeners.add( 0, listener );
    }

    /***************************************************************************
     * Removes a ValidityChangeListener from this text field.
     * 
     * @param listener
     **************************************************************************/
    public void removeValidityChangeListener( ValidityChangeListener listener )
    {
        validityChangeListeners.remove( listener );
    }

    /***************************************************************************
     * Sets the background color of this text field.
     **************************************************************************/
    @Override
    public void setBackground( Color color )
    {
        validBackground = color;
        if( !isInitialized || valid )
            super.setBackground( color );
    }

    /***************************************************************************
     * Sets the TextValidator to be used with this text field.
     * 
     * @param validator
     **************************************************************************/
    public final void setValidator( TextValidator validator )
    {
        this.validator = validator;
        validateText();
    }

    /***************************************************************************
     * JValidatedTextField demo frame.
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
                final JFrame frame = new JFrame();
                JPanel panel = new JPanel();

                JLabel label1 = new JLabel( "Int (0-25)" );
                JLabel label2 = new JLabel( "Int [1-10]" );
                JLabel label3 = new JLabel( "Double (0.0-5.0)" );
                JLabel label4 = new JLabel( "The text 'clearcase sucks'" );

                final JValidatedTextField tf1 = new JValidatedTextField();
                final JValidatedTextField tf2 = new JValidatedTextField();
                final JValidatedTextField tf3 = new JValidatedTextField();
                final JValidatedTextField tf4 = new JValidatedTextField();

                final JButton okButton = new JButton( "OK" );
                JButton cancelButton = new JButton( "Cancel" );

                Dimension buttonSize = Utils
                        .getMaxComponentSize( new Component[] { okButton,
                                cancelButton } );

                TextValidator tv1 = new TextValidator()
                {
                    public boolean validateText( String text )
                    {
                        boolean valid = false;
                        int i = 0;
                        try
                        {
                            i = Integer.parseInt( text );
                            if( i >= 0 && i <= 25 )
                                valid = true;
                        } catch( Exception e )
                        {
                            valid = false;
                        }
                        return valid;
                    }
                };

                TextValidator tv2 = new TextValidator()
                {
                    public boolean validateText( String text )
                    {
                        boolean valid = false;
                        int i = 0;
                        try
                        {
                            i = Integer.parseInt( text );
                            if( i > 1 && i < 10 )
                                valid = true;
                        } catch( Exception e )
                        {
                            valid = false;
                        }
                        return valid;
                    }
                };

                TextValidator tv3 = new TextValidator()
                {
                    public boolean validateText( String text )
                    {
                        boolean valid = false;
                        double d = 0;
                        try
                        {
                            d = Double.parseDouble( text );
                            if( d >= 0.0 && d <= 5.0 )
                                valid = true;
                        } catch( Exception e )
                        {
                            valid = false;
                        }
                        return valid;
                    }
                };

                TextValidator tv4 = new TextValidator()
                {
                    public boolean validateText( String text )
                    {
                        return text.equals( "clearcase sucks" );
                    }
                };

                ValidityChangeListener validityListener = new ValidityChangeListener()
                {
                    public void validityChanged( boolean newValidity )
                    {
                        newValidity = tf1.isInputValid() && tf2.isInputValid()
                                && tf3.isInputValid() && tf4.isInputValid();
                        okButton.setEnabled( newValidity );
                    }
                };

                ActionListener actionListener = new ActionListener()
                {
                    public void actionPerformed( ActionEvent e )
                    {
                        frame.dispose();
                    }
                };

                panel.setLayout( new GridBagLayout() );

                okButton.setPreferredSize( buttonSize );
                okButton.addActionListener( actionListener );
                okButton.setEnabled( false );
                cancelButton.setPreferredSize( buttonSize );
                cancelButton.addActionListener( actionListener );

                tf1.setValidator( tv1 );
                tf1.addValidityChangeListener( validityListener );
                tf2.setValidator( tv2 );
                tf2.addValidityChangeListener( validityListener );
                tf3.setValidator( tv3 );
                tf3.addValidityChangeListener( validityListener );
                tf4.setValidator( tv4 );
                tf4.addValidityChangeListener( validityListener );

                panel.add( label1, new GridBagConstraints( 0, 0, 1, 1, 0.1,
                        0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( tf1, new GridBagConstraints( 1, 0, 3, 1, 0.9, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( label2, new GridBagConstraints( 0, 1, 1, 1, 0.1,
                        0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( tf2, new GridBagConstraints( 1, 1, 3, 1, 0.9, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( label3, new GridBagConstraints( 0, 2, 1, 1, 0.1,
                        0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( tf3, new GridBagConstraints( 1, 2, 3, 1, 0.9, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( label4, new GridBagConstraints( 0, 3, 1, 1, 0.1,
                        0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( tf4, new GridBagConstraints( 1, 3, 3, 1, 0.9, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );

                panel.add( Box.createHorizontalStrut( 0 ),
                        new GridBagConstraints( 1, 4, 1, 1, 1.0, 0.0,
                                GridBagConstraints.EAST,
                                GridBagConstraints.NONE,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( okButton, new GridBagConstraints( 2, 4, 1, 1, 0.0,
                        0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( cancelButton,
                        new GridBagConstraints( 3, 4, 1, 1, 0.0, 0.0,
                                GridBagConstraints.WEST,
                                GridBagConstraints.NONE,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );

                frame.setTitle( "JValidatedTextField demo" );
                frame.setContentPane( panel );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( 400, 200 );
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
