package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javautils.IconManager;
import javautils.IconManager.IconSize;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

/*******************************************************************************
 * Simple {@link INavigator}.
 ******************************************************************************/
public class NavigatorPanel extends JPanel implements INavigator
{
    private static final Dimension PREFERRED_SIZE = new Dimension( 640, 80 );
    private JLabel label;
    private JButton prevButton;
    private JTextField gotoTextField;
    private JButton gotoButton;
    private JButton nextButton;
    private JProgressBar progressBar;

    /***************************************************************************
     * Constructor
     * 
     * @param prevActionListener
     * @param nextActionListener
     * @param gotoActionListener
     **************************************************************************/
    public NavigatorPanel( ActionListener prevActionListener,
            ActionListener nextActionListener, ActionListener gotoActionListener )
    {
        setupPanel();

        prevButton.addActionListener( prevActionListener );
        nextButton.addActionListener( nextActionListener );
        gotoButton.addActionListener( gotoActionListener );
    }

    /***************************************************************************
     * Sets up this panel.
     **************************************************************************/
    private void setupPanel()
    {
        label = new JLabel( "Showing item 0 of 0" );
        prevButton = new JButton( IconManager.getIcon( IconManager.ARROW_LEFT,
                IconSize.X16 ) );
        gotoTextField = new JTextField();
        gotoButton = new JButton( IconManager.getIcon( IconManager.BULLET_GO,
                IconSize.X16 ) );
        nextButton = new JButton( IconManager.getIcon( IconManager.ARROW_RIGHT,
                IconSize.X16 ) );
        progressBar = new JProgressBar();

        setLayout( new GridBagLayout() );
        add( label, new GridBagConstraints( 0, 0, 4, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( prevButton, new GridBagConstraints( 0, 1, 1, 1, 0.1, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        add( gotoTextField, new GridBagConstraints( 1, 1, 1, 1, 0.9, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( gotoButton, new GridBagConstraints( 2, 1, 1, 1, 0.1, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        add( nextButton, new GridBagConstraints( 3, 1, 1, 1, 0.1, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        add( progressBar, new GridBagConstraints( 0, 2, 4, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        setPreferredSize( PREFERRED_SIZE );
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.INavigator#getLabelText()
     */
    @Override
    public String getLabelText()
    {
        return label.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.INavigator#setLabelText(java.lang.String)
     */
    @Override
    public void setLabelText( String text )
    {
        label.setText( text );
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.INavigator#getGotoText()
     */
    @Override
    public String getGotoText()
    {
        return gotoTextField.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.INavigator#setGotoText(java.lang.String)
     */
    @Override
    public void setGotoText( String text )
    {
        gotoTextField.setText( text );
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.INavigator#getProgress()
     */
    @Override
    public int getProgress()
    {
        return progressBar.getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.INavigator#setProgress(int)
     */
    @Override
    public void setProgress( int value )
    {
        progressBar.setValue( value );
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.INavigator#getPrevEnabled()
     */
    @Override
    public boolean getPrevEnabled()
    {
        return prevButton.isEnabled();
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.INavigator#setPrevEnabled(boolean)
     */
    @Override
    public void setPrevEnabled( boolean value )
    {
        prevButton.setEnabled( value );
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.INavigator#getNextEnabled()
     */
    @Override
    public boolean getNextEnabled()
    {
        return nextButton.isEnabled();
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.INavigator#setNextEnabled(boolean)
     */
    @Override
    public void setNextEnabled( boolean value )
    {
        nextButton.setEnabled( value );
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.INavigator#getGotoEnabled()
     */
    @Override
    public boolean getGotoEnabled()
    {
        return gotoButton.isEnabled();
    }

    /*
     * (non-Javadoc)
     * 
     * @see view.INavigator#setGotoEnabled(boolean)
     */
    @Override
    public void setGotoEnabled( boolean value )
    {
        gotoButton.setEnabled( value );
    }

}
