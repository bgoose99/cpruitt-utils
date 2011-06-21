package ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javautils.Utils;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import data.AbstractProblem;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ProblemDialog extends JDialog
{
    private JEditorPane editorPane;
    private JScrollPane scrollPane;
    private JButton solveButton;
    private JButton closeButton;

    /***************************************************************************
     * Constructor
     * 
     * @param owner
     * @param problem
     **************************************************************************/
    public ProblemDialog( Frame owner, final AbstractProblem problem )
    {
        super( owner, problem.toString() );

        editorPane = new JEditorPane();
        editorPane.setContentType( "text/html" );
        editorPane.setEditable( false );
        editorPane.setText( problem.getDescription() );
        editorPane.setCaretPosition( 0 );
        scrollPane = new JScrollPane( editorPane );
        scrollPane.setPreferredSize( new Dimension( 400, 400 ) );
        solveButton = new JButton( "Solve" );
        solveButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                long start = System.currentTimeMillis();
                String answer = problem.getSolution();
                double elapsed = ( System.currentTimeMillis() - start ) / 1000.0;

                JOptionPane.showMessageDialog(
                        ProblemDialog.this,
                        answer + "\n\n(Time elapsed: "
                                + String.format( "%.3f", elapsed )
                                + " seconds)", "Solution",
                        JOptionPane.INFORMATION_MESSAGE );
            }
        } );

        closeButton = new JButton( "Close" );
        closeButton.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent arg0 )
            {
                dispose();
            }
        } );

        Dimension prefSize = Utils.getMaxComponentSize( solveButton,
                closeButton );
        solveButton.setPreferredSize( prefSize );
        closeButton.setPreferredSize( prefSize );

        setupDialog();
    }

    /***************************************************************************
     * Sets up this dialog.
     **************************************************************************/
    private void setupDialog()
    {
        setModal( true );
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        setLayout( new GridBagLayout() );

        add( scrollPane, new GridBagConstraints( 0, 0, 2, 1, 1.0, 0.99,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        5, 5, 5, 5 ), 0, 0 ) );
        add( solveButton, new GridBagConstraints( 0, 1, 1, 1, 0.5, 0.01,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                        1, 1, 1, 1 ), 0, 0 ) );
        add( closeButton, new GridBagConstraints( 1, 1, 1, 1, 0.5, 0.01,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        1, 1, 1, 1 ), 0, 0 ) );

        pack();
        setLocationRelativeTo( getOwner() );
    }
}
