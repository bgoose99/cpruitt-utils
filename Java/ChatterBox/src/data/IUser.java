package data;

public interface IUser
{
    public String getDisplayName();

    public void setDisplayName( String displayName );

    public boolean isAvailable();

    public void setAvailable( boolean available );
}
