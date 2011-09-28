package data;

import java.awt.Color;
import java.net.InetAddress;

public class ChatUser implements IUser
{
    private String name;
    private String displayName;
    private Color displayColor;
    private boolean available;

    public ChatUser( String name, boolean isAvailable )
    {
        this( name, "255", isAvailable );
    }

    public ChatUser( String name, String colorRGB, boolean isAvailable )
    {
        try
        {
            this.name = System.getProperty( "user.name" )
                    + InetAddress.getLocalHost().getHostName() + "@";
        } catch( Exception e )
        {
            this.name = System.getProperty( "user.name" ) + "@unknown.host";
        }

        this.displayName = name;
        this.available = isAvailable;
        try
        {
            displayColor = new Color( Integer.parseInt( colorRGB ) );
        } catch( Exception e )
        {
            displayColor = Color.black;
        }
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    @Override
    public Color getDisplayColor()
    {
        return displayColor;
    }

    @Override
    public void setDisplayColor( Color color )
    {
        displayColor = color;
    }

    @Override
    public boolean isAvailable()
    {
        return available;
    }

    @Override
    public void setAvailable( boolean available )
    {
        this.available = available;
    }
}
