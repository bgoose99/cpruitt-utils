package data;

import java.awt.Color;

/*******************************************************************************
 * User interface.
 ******************************************************************************/
public interface IUser
{
    /***************************************************************************
     * Returns the name of this user. This name will not be seen by other users.
     * Eg. johndoe@localhost
     * 
     * @return
     **************************************************************************/
    public String getName();

    /***************************************************************************
     * Returns the displayname of this user. This is the name that will be seen
     * by other users.
     * 
     * @return
     **************************************************************************/
    public String getDisplayName();

    /***************************************************************************
     * Sets the display name of this user.
     * 
     * @param displayName
     **************************************************************************/
    public void setDisplayName( String displayName );

    /***************************************************************************
     * Returns the display color this user has selected.
     * 
     * @return
     **************************************************************************/
    public Color getDisplayColor();

    /***************************************************************************
     * Sets the display color for this user.
     * 
     * @param color
     **************************************************************************/
    public void setDisplayColor( Color color );

    /***************************************************************************
     * Returns true if this user is available, false otherwise.
     * 
     * @return
     **************************************************************************/
    public boolean isAvailable();

    /***************************************************************************
     * Sets the availability of this user.
     * 
     * @param available
     **************************************************************************/
    public void setAvailable( boolean available );
}
