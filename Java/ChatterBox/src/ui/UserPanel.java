package ui;

import java.awt.BorderLayout;
import java.awt.Point;

import javautils.swing.PopupMessage;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.IUser;
import data.IUserDisplay;
import data.UserListCellRenderer;

public class UserPanel extends JPanel implements IUserDisplay
{
    private DefaultListModel<IUser> listModel;
    private JList<IUser> list;
    private JScrollPane scrollPane;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public UserPanel()
    {
        listModel = new DefaultListModel<IUser>();
        list = new JList<IUser>( listModel );
        UserListCellRenderer renderer = new UserListCellRenderer();
        list.setCellRenderer( renderer );
        scrollPane = new JScrollPane( list );

        setupPanel();
    }

    /***************************************************************************
     * Sets up this panel.
     **************************************************************************/
    private void setupPanel()
    {
        setLayout( new BorderLayout() );
        add( scrollPane, BorderLayout.CENTER );
    }

    /*
     * (non-Javadoc)
     * 
     * @see data.IUserDisplay#addUser(data.IUser)
     */
    @Override
    public void addUser( IUser user )
    {
        listModel.addElement( user );
        scrollPane.repaint();

        Point p = getLocationOnScreen();
        p.y -= 20;
        PopupMessage popup = new PopupMessage( user.getDisplayName()
                + " has joined the conversation", 2000, p );
        popup.showPopup();
    }

    /*
     * (non-Javadoc)
     * 
     * @see data.IUserDisplay#removeUser(data.IUser)
     */
    @Override
    public void removeUser( IUser user )
    {
        listModel.removeElement( user );
        scrollPane.repaint();

        Point p = getLocationOnScreen();
        p.y -= 20;
        PopupMessage popup = new PopupMessage( user.getDisplayName()
                + " has left the conversation", 2000, p );
        popup.showPopup();
    }

    /*
     * (non-Javadoc)
     * 
     * @see data.IUserDisplay#updateDisplay()
     */
    @Override
    public void updateDisplay()
    {
        scrollPane.repaint();
    }
}
