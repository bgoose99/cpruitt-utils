package javautils.swing.validation;

/*******************************************************************************
 * This interface is used when an object can change validity.
 ******************************************************************************/
public interface ValidityChangeListener
{
    /***************************************************************************
     * Change this object's validity.
     * 
     * @param newValidity
     **************************************************************************/
    public void validityChanged( boolean newValidity );
}
