package data;

public class ChatUser implements IUser
{
    private String displayName;
    private boolean available;

    public ChatUser( String name, boolean isAvailable )
    {
        this.displayName = name;
        this.available = isAvailable;
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
