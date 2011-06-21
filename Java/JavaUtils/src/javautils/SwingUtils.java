package javautils;

import javax.swing.Icon;
import javax.swing.JButton;

/*******************************************************************************
 * This class contains some useful Swing functions.
 ******************************************************************************/
public final class SwingUtils
{
    /***************************************************************************
     * Creates a {@link JButton} with settings that are common for toolbars.
     * 
     * @param tooltip
     * @param icon
     * @return
     **************************************************************************/
    public static JButton createToolbarButton( String tooltip, Icon icon )
    {
        JButton button = new JButton( "", icon );
        button.setFocusable( false );
        button.setBorderPainted( false );
        button.setToolTipText( tooltip );
        return button;
    }
}
