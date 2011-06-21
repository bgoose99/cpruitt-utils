package javautils.wizard;

import java.util.Arrays;
import java.util.List;

import javautils.swing.JFadePanel;

import javax.swing.JPanel;

/*******************************************************************************
 * This class contains the different panels presented to the user in a
 * {@link JWizard}. It allows switching of the panels in both directions.
 ******************************************************************************/
public class JWizardPanel extends JFadePanel
{
    /** */
    private int currentIndex;

    /***************************************************************************
     * Constructor
     * 
     * @param wizardComponents
     **************************************************************************/
    public JWizardPanel( JWizardComponent... wizardComponents )
    {
        this( Arrays.asList( wizardComponents ) );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param wizardPanels
     **************************************************************************/
    public JWizardPanel( List<JWizardComponent> wizardPanels )
    {
        super( wizardPanels );
        currentIndex = 0;
    }

    /***************************************************************************
     * Returns the panel at the specified index.
     * 
     * @param index
     * @return
     **************************************************************************/
    public JWizardComponent get( int index )
    {
        JPanel p = panels.get( index );
        return ( p instanceof JWizardComponent ) ? (JWizardComponent)p : null;
    }

    /***************************************************************************
     * Returns the current panel.
     * 
     * @return
     **************************************************************************/
    public JWizardComponent getCurrentCard()
    {
        JPanel p = panels.get( currentIndex );
        return ( p instanceof JWizardComponent ) ? (JWizardComponent)p : null;
    }

    /***************************************************************************
     * Returns the current index.
     * 
     * @return
     **************************************************************************/
    public int getIndex()
    {
        return currentIndex;
    }

    /***************************************************************************
     * Returns the total number of panels.
     * 
     * @return
     **************************************************************************/
    public int getPanelCount()
    {
        return panels.size();
    }

    /***************************************************************************
     * Shows the next panel.
     **************************************************************************/
    @Override
    public void next()
    {
        currentIndex++;
        super.next();
        getCurrentCard().refresh();
    }

    /***************************************************************************
     * Shows the last panel in the list.
     **************************************************************************/
    @Override
    public void last()
    {
        currentIndex = panels.size() - 1;
        super.last();
        getCurrentCard().refresh();
    }

    /***************************************************************************
     * Shows the first panel in the list.
     **************************************************************************/
    @Override
    public void first()
    {
        currentIndex = 0;
        super.first();
        getCurrentCard().refresh();
    }

    /***************************************************************************
     * Shows the previous panel.
     **************************************************************************/
    @Override
    public void previous()
    {
        currentIndex--;
        super.previous();
        getCurrentCard().refresh();
    }
}
