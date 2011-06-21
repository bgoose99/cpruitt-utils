package javautils.swing.validation;

/*******************************************************************************
 * This interface is used to validate text.
 ******************************************************************************/
public interface TextValidator
{
    /***************************************************************************
     * Validates a string of text.
     * 
     * @param text
     * @return
     **************************************************************************/
    public boolean validateText( String text );
}
