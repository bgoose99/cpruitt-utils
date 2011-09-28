package data;

import java.awt.Color;

public class ChatUser implements IUser
{
    private String displayName;
    private Color displayColor;
    private boolean available;

    public ChatUser( String name, boolean isAvailable )
    {
        this( name, "255", isAvailable );
    }

    public ChatUser( String name, String colorRGB, boolean isAvailable )
    {
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
