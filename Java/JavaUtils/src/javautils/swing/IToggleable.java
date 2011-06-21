package javautils.swing;

/*******************************************************************************
 * This interface should be used any time an object has a state that is
 * toggle-able.
 ******************************************************************************/
public interface IToggleable
{
    /***************************************************************************
     * Toggles the state of this object.
     * 
     * @param newState
     **************************************************************************/
    public void toggle( boolean newState );
}
