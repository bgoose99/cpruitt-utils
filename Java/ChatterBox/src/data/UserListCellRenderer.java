package data;

import java.awt.Component;

import javautils.IconManager;
import javautils.IconManager.IconSize;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;

public class UserListCellRenderer extends DefaultListCellRenderer
{
    private Icon userIcon;
    private Icon userAwayIcon;

    public UserListCellRenderer()
    {
        userIcon = IconManager.getIcon( IconManager.USER, IconSize.X32 );
        userAwayIcon = IconManager.getIcon( IconManager.USER_SILHOUETTE,
                IconSize.X32 );
    }

    @Override
    public Component getListCellRendererComponent( JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus )
    {
        super.getListCellRendererComponent( list, value, index, isSelected,
                cellHasFocus );

        IUser user = (IUser)value;

        setText( user.getDisplayName() );

        if( user.isAvailable() )
        {
            setIcon( userIcon );
        } else
        {
            setIcon( userAwayIcon );
        }

        return this;
    }
}
