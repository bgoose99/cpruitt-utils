package ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import data.AbstractProblem;
import data.ProblemFactory;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ProjectEulerFrame extends JFrame
{
    private DefaultListModel<AbstractProblem> problemListModel;
    private JList<AbstractProblem> problemList;
    private JScrollPane scrollPane;
    private JButton viewButton;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public ProjectEulerFrame()
    {
        problemListModel = new DefaultListModel<AbstractProblem>();
        for( AbstractProblem p : ProblemFactory.getProblems() )
        {
            problemListModel.addElement( p );
        }
        problemList = new JList<AbstractProblem>( problemListModel );
        problemList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        problemList.addMouseListener( new ListMouseAdapter() );
        scrollPane = new JScrollPane( problemList );
        viewButton = new JButton( "View" );
        viewButton.addActionListener( new ViewButtonListener() );

        setupFrame();
    }

    /***************************************************************************
     * Sets up this frame.
     **************************************************************************/
    private void setupFrame()
    {
        setTitle( "Project Euler Solver" );
        setSize( 400, 400 );

        setLayout( new GridBagLayout() );
        add( scrollPane, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.99,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        1, 1, 1, 1 ), 0, 0 ) );
        add( viewButton, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.01,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        1, 1, 1, 1 ), 0, 0 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ViewButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            AbstractProblem p = (AbstractProblem)problemList.getSelectedValue();
            ProblemDialog d = new ProblemDialog( ProjectEulerFrame.this, p );
            d.setVisible( true );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ListMouseAdapter extends MouseAdapter
    {
        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( e.getClickCount() == 2 && e.getSource() instanceof JList )
            {
                viewButton.doClick();
            }
        }
    }
}
