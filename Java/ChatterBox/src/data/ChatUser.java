package data;

import java.awt.Color;
import java.net.InetAddress;

public class ChatUser implements IUser
{
    private String name;
    private String displayName;
    private Color displayColor;
    private boolean available;

    /***************************************************************************
     * Constructor
     * 
     * @param displayName
     * @param isAvailable
     **************************************************************************/
    public ChatUser( String displayName, boolean isAvailable )
    {
        this( displayName, "255", isAvailable );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param displayName
     * @param colorRGB
     * @param isAvailable
     **************************************************************************/
    public ChatUser( String displayName, String colorRGB, boolean isAvailable )
    {
        try
        {
            this.name = System.getProperty( "user.name" )
                    + InetAddress.getLocalHost().getHostName() + "@";
        } catch( Exception e )
        {
            this.name = System.getProperty( "user.name" ) + "@unknown.host";
        }

        this.displayName = displayName;
        this.available = isAvailable;
        try
        {
            displayColor = new Color( Integer.parseInt( colorRGB ) );
        } catch( Exception e )
        {
            displayColor = Color.black;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see data.IUser#getName()
     */
    @Override
    public String getName()
    {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see data.IUser#getDisplayName()
     */
    @Override
    public String getDisplayName()
    {
        return displayName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see data.IUser#setDisplayName(java.lang.String)
     */
    @Override
    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see data.IUser#getDisplayColor()
     */
    @Override
    public Color getDisplayColor()
    {
        return displayColor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see data.IUser#setDisplayColor(java.awt.Color)
     */
    @Override
    public void setDisplayColor( Color color )
    {
        displayColor = color;
    }

    /*
     * (non-Javadoc)
     * 
     * @see data.IUser#isAvailable()
     */
    @Override
    public boolean isAvailable()
    {
        return available;
    }

    /*
     * (non-Javadoc)
     * 
     * @see data.IUser#setAvailable(boolean)
     */
    @Override
    public void setAvailable( boolean available )
    {
        this.available = available;
    }
}
