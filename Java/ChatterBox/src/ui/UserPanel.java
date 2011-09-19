package ui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.IUser;
import data.UserListCellRenderer;

public class UserPanel extends JPanel
{
    private DefaultListModel listModel;
    private JList list;
    private JScrollPane scrollPane;

    public UserPanel()
    {
        listModel = new DefaultListModel();
        list = new JList( listModel );
        UserListCellRenderer renderer = new UserListCellRenderer();
        list.setCellRenderer( renderer );
        scrollPane = new JScrollPane( list );

        setupPanel();
    }

    private void setupPanel()
    {
        setLayout( new BorderLayout() );
        add( scrollPane, BorderLayout.CENTER );
    }

    public void addUser( IUser user )
    {
        listModel.addElement( user );
        scrollPane.repaint();
    }

    public void removeUser( IUser user )
    {
        listModel.removeElement( user );
        scrollPane.repaint();
    }
}
