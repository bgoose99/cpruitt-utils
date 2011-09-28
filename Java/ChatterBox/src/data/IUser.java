package data;

import java.awt.Color;

public interface IUser
{
    public String getName();

    public String getDisplayName();

    public void setDisplayName( String displayName );

    public Color getDisplayColor();

    public void setDisplayColor( Color color );

    public boolean isAvailable();

    public void setAvailable( boolean available );
}
