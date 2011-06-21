package javautils.wizard;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javautils.IconManager;
import javautils.IconManager.IconSize;
import javautils.Utils;
import javautils.swing.validation.ValidityChangeListener;
import javautils.task.ICompletable;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/*******************************************************************************
 * This class is a generic wizard-like panel that contains the necessary
 * functionality for presenting the user with a step-by-step process.
 ******************************************************************************/
public class JWizard extends JPanel implements ValidityChangeListener
{
    private JWizardPanel wizardPanel;

    private JButton helpButton;
    private JButton cancelButton;
    private JButton prevButton;
    private JButton nextButton;
    private JButton finishButton;

    private ICompletable completable;

    /***************************************************************************
     * Constructor
     * 
     * @param completable
     * @param wizardComponents
     **************************************************************************/
    public JWizard( ICompletable completable,
            JWizardComponent... wizardComponents )
    {
        this( completable, Arrays.asList( wizardComponents ) );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param completable
     * @param wizardComponents
     **************************************************************************/
    public JWizard( ICompletable completable,
            List<JWizardComponent> wizardComponents )
    {
        super();

        this.completable = completable;

        wizardPanel = new JWizardPanel( wizardComponents );

        for( JWizardComponent comp : wizardComponents )
        {
            comp.addValidityChangeListener( this );
        }

        helpButton = new JButton( IconManager.getIcon( IconManager.HELP,
                IconSize.X16 ) );
        helpButton.setFocusable( false );
        helpButton.setBorderPainted( false );
        helpButton.setContentAreaFilled( false );
        helpButton.addActionListener( new HelpButtonActionListener() );

        cancelButton = new JButton( "Cancel" );
        cancelButton.setIcon( IconManager.getIcon( IconManager.CANCEL,
                IconSize.X16 ) );
        cancelButton.setHorizontalTextPosition( SwingConstants.RIGHT );
        cancelButton.setIconTextGap( 10 );
        cancelButton.setFocusable( false );
        cancelButton.addActionListener( new CancelButtonActionListener() );

        prevButton = new JButton( "Back" );
        prevButton.setIcon( IconManager.getIcon( IconManager.ARROW_LEFT,
                IconSize.X16 ) );
        prevButton.setHorizontalTextPosition( SwingConstants.RIGHT );
        prevButton.setIconTextGap( 10 );
        prevButton.setEnabled( false );
        prevButton.setFocusable( false );
        prevButton.addActionListener( new PrevButtonActionListener() );

        nextButton = new JButton( "Next" );
        nextButton.setIcon( IconManager.getIcon( IconManager.ARROW_RIGHT,
                IconSize.X16 ) );
        nextButton.setHorizontalTextPosition( SwingConstants.LEFT );
        nextButton.setIconTextGap( 10 );
        nextButton.setEnabled( false );
        nextButton.setFocusable( false );
        nextButton.addActionListener( new NextButtonActionListener() );

        finishButton = new JButton( "Finish" );
        finishButton.setIcon( IconManager.getIcon( IconManager.ACCEPT,
                IconSize.X16 ) );
        finishButton.setHorizontalTextPosition( SwingConstants.LEFT );
        finishButton.setIconTextGap( 10 );
        finishButton.setEnabled( false );
        finishButton.setFocusable( false );
        finishButton.addActionListener( new FinishButtonActionListener() );

        Dimension d = Utils.getMaxComponentSize( cancelButton, prevButton,
                nextButton, finishButton );
        cancelButton.setPreferredSize( d );
        prevButton.setPreferredSize( d );
        nextButton.setPreferredSize( d );
        finishButton.setPreferredSize( d );

        setupPanel();

        checkButtons();
    }

    /***************************************************************************
     * Sets up this panel.
     **************************************************************************/
    private void setupPanel()
    {
        setLayout( new GridBagLayout() );

        JPanel panel = new JPanel( new GridBagLayout() );
        panel.add( helpButton, new GridBagConstraints( 0, 0, 1, 1, 0.01, 0.01,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                        0, 0, 0, 0 ), 0, 0 ) );

        // info panel
        add( panel, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.01,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        // wizard component
        add( wizardPanel, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.98,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        0, 0, 0, 0 ), 0, 0 ) );

        panel = new JPanel( new GridBagLayout() );
        panel.add( cancelButton, new GridBagConstraints( 0, 0, 1, 1, 0.01,
                0.01, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        panel.add( prevButton, new GridBagConstraints( 1, 0, 1, 1, 0.01, 0.01,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        panel.add( nextButton, new GridBagConstraints( 2, 0, 1, 1, 0.01, 0.01,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        panel.add( finishButton, new GridBagConstraints( 3, 0, 1, 1, 0.01,
                0.01, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );

        // button panel
        add( panel, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.01,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * Checks and sets the state of the buttons based on the current panel and
     * its validity.
     **************************************************************************/
    private void checkButtons()
    {
        JWizardComponent comp = wizardPanel.getCurrentCard();
        int index = wizardPanel.getIndex();
        if( comp.isInputValid() )
        {
            if( index == 0 )
            {
                if( wizardPanel.getPanelCount() > 1 )
                {
                    prevButton.setEnabled( false );
                    nextButton.setEnabled( true );
                    finishButton.setEnabled( false );
                } else
                {
                    prevButton.setEnabled( false );
                    nextButton.setEnabled( false );
                    finishButton.setEnabled( true );
                }
            } else if( index == ( wizardPanel.getPanelCount() - 1 ) )
            {
                prevButton.setEnabled( true );
                nextButton.setEnabled( false );
                finishButton.setEnabled( true );
            } else
            {
                prevButton.setEnabled( true );
                nextButton.setEnabled( true );
                finishButton.setEnabled( false );
            }
        } else
        {
            if( index == 0 )
            {
                prevButton.setEnabled( false );
                nextButton.setEnabled( false );
                finishButton.setEnabled( false );
            } else
            {
                prevButton.setEnabled( true );
                nextButton.setEnabled( false );
                finishButton.setEnabled( false );
            }
        }
    }

    /***************************************************************************
     * ActionListener for the help button.
     **************************************************************************/
    private class HelpButtonActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JOptionPane.showMessageDialog( JWizard.this, wizardPanel
                    .getCurrentCard().getHelpText(), "Help",
                    JOptionPane.INFORMATION_MESSAGE );
        }
    }

    /***************************************************************************
     * ActionListener for the cancel button.
     **************************************************************************/
    private class CancelButtonActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JWizard.this.removeAll();
            JWizard.this.setVisible( false );
            JWizard.this.completable.notifyCancelled();
        }
    }

    /***************************************************************************
     * ActionListener for the previous button.
     **************************************************************************/
    private class PrevButtonActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            wizardPanel.previous();
            checkButtons();
        }
    }

    /***************************************************************************
     * ActionListener for the next button.
     **************************************************************************/
    private class NextButtonActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            wizardPanel.next();
            checkButtons();
        }
    }

    /***************************************************************************
     * ActionListener for the finish button.
     **************************************************************************/
    private class FinishButtonActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( completable != null )
                completable.notifyComplete();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javautils.swing.validation.ValidityChangeListener#validityChanged(boolean
     * )
     */
    @Override
    public void validityChanged( boolean newValidity )
    {
        checkButtons();
    }
}
