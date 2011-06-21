package javautils.wizard;

import java.util.LinkedList;
import java.util.List;

import javautils.swing.validation.ValidityChangeListener;

import javax.swing.JPanel;

/*******************************************************************************
 * This is a generic component that is displayed in a {@link JWizard}.
 ******************************************************************************/
public abstract class JWizardComponent extends JPanel
{
    private List<ValidityChangeListener> validityListeners;

    private boolean valid = false;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public JWizardComponent()
    {
        super();

        validityListeners = new LinkedList<ValidityChangeListener>();
    }

    /***************************************************************************
     * Returns the validity of this component's input.
     * 
     * @return
     **************************************************************************/
    public boolean isInputValid()
    {
        return valid;
    }

    /***************************************************************************
     * Adds a {@link ValidityChangeListener} to this component.
     * 
     * @param listener
     **************************************************************************/
    public void addValidityChangeListener( ValidityChangeListener listener )
    {
        validityListeners.add( 0, listener );
    }

    /***************************************************************************
     * Removes a {@link ValidityChangeListener} from this component.
     * 
     * @param listener
     **************************************************************************/
    public void removeValidityChangeListener( ValidityChangeListener listener )
    {
        validityListeners.remove( listener );
    }

    /***************************************************************************
     * Returns the help text associated with this component.
     * 
     * @return
     **************************************************************************/
    public abstract String getHelpText();

    /***************************************************************************
     * Refreshes this component's data when it is displayed.
     **************************************************************************/
    public void refresh()
    {
        ;
    }

    /***************************************************************************
     * Sets the validity of this component's input(s).
     * 
     * @param validity
     **************************************************************************/
    protected void setInputValidity( boolean validity )
    {
        valid = validity;
        fireValidityChanged();
    }

    /***************************************************************************
     * Notifies all listeners that this component's validity has changed.
     **************************************************************************/
    private void fireValidityChanged()
    {
        for( ValidityChangeListener listener : validityListeners )
        {
            listener.validityChanged( valid );
        }
    }
}
