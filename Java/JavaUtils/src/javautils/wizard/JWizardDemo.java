package javautils.wizard;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javautils.swing.FrameRunner;
import javautils.task.ICompletable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

/*******************************************************************************
 * This is a demonstration of how one might use a {@link JWizard}.
 ******************************************************************************/
public class JWizardDemo extends FrameRunner
{
    /**
     * @param args
     */
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new JWizardDemo() );
    }

    /***************************************************************************
     * Sample {@link ICompletable}
     **************************************************************************/
    private static class Completable implements ICompletable
    {
        @Override
        public void notifyComplete()
        {
            System.out
                    .println( "Completing something would normally be done here" );
            System.exit( 0 );
        }

        @Override
        public void notifyCancelled()
        {
            System.out
                    .println( "Cancelling something would normally be done here" );
            System.exit( 0 );
        }
    }

    /***************************************************************************
     * Sample {@link JWizardComponent}
     **************************************************************************/
    private static class WizComp01 extends JWizardComponent
    {
        private JToggleButton tb;
        private JTextArea ta;
        private JScrollPane sp;

        public WizComp01()
        {
            tb = new JToggleButton( "Toggle me to go to the next step" );
            tb.addActionListener( new ActionListener()
            {
                @Override
                public void actionPerformed( ActionEvent arg0 )
                {
                    // input validity only depends on the toggle button
                    setInputValidity( tb.isSelected() );
                }
            } );
            ta = new JTextArea( "Text in this area does not matter" );
            sp = new JScrollPane( ta );

            setLayout( new GridBagLayout() );
            add( tb, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.1,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets( 3, 3, 3, 3 ), 0, 0 ) );
            add( sp, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.9,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        }

        @Override
        public String getHelpText()
        {
            return "Sample JWizard component.\nHelp would normally go here.";
        }
    }

    /***************************************************************************
     * Sample {@link JWizardComponent}
     **************************************************************************/
    private static class WizComp02 extends JWizardComponent
    {
        private JLabel l;
        private JCheckBox cb;
        private JToggleButton tb;
        private JRadioButton rb;

        public WizComp02()
        {
            l = new JLabel( "In order to proceed:" );

            ActionListener al = new ActionListener()
            {
                @Override
                public void actionPerformed( ActionEvent arg0 )
                {
                    // input validity depends on all three buttons
                    setInputValidity( cb.isSelected() && tb.isSelected()
                            && rb.isSelected() );
                }
            };

            cb = new JCheckBox( "I must be selected" );
            cb.addActionListener( al );
            tb = new JToggleButton( "Me too" );
            tb.addActionListener( al );
            rb = new JRadioButton( "And me as well" );
            rb.addActionListener( al );

            setLayout( new GridBagLayout() );
            add( l, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.01,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets( 3, 3, 3, 3 ), 0, 0 ) );
            add( cb, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.01,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets( 3, 3, 3, 3 ), 0, 0 ) );
            add( tb, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.01,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets( 3, 3, 3, 3 ), 0, 0 ) );
            add( rb, new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.9,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        }

        @Override
        public String getHelpText()
        {
            return "Another JWizard component.\nHelp would normally go here.";
        }
    }

    /***************************************************************************
     * Sample {@link JWizardComponent}
     **************************************************************************/
    private static class WizComp03 extends JWizardComponent
    {
        private JLabel l;
        private JButton b;
        private JCheckBox cb;

        public WizComp03()
        {
            // input is always valid
            setInputValidity( true );

            l = new JLabel( "This panel is always valid" );
            b = new JButton( "This button does nothing" );
            cb = new JCheckBox( "This is a parameter that doesn't matter" );

            setLayout( new GridBagLayout() );
            add( l, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.01,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                    new Insets( 3, 3, 3, 3 ), 0, 0 ) );
            add( b, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.01,
                    GridBagConstraints.WEST, GridBagConstraints.NONE,
                    new Insets( 3, 3, 3, 3 ), 0, 0 ) );
            add( cb, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.98,
                    GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                    new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        }

        @Override
        public String getHelpText()
        {
            return "Yet another JWizard component.\nThis one is always valid.";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.swing.FrameRunner#createFrame()
     */
    @Override
    protected JFrame createFrame()
    {
        JFrame frame = new JFrame();

        Completable completable = new Completable();

        WizComp01 comp01 = new WizComp01();
        WizComp02 comp02 = new WizComp02();
        WizComp03 comp03 = new WizComp03();

        JWizard wiz = new JWizard( completable, comp01, comp02, comp03 );

        frame.setLayout( new BorderLayout() );
        frame.add( wiz );
        frame.setTitle( "JWizard Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 300 );
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );

        return frame;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javautils.swing.FrameRunner#validate()
     */
    @Override
    protected boolean validate()
    {
        return true;
    }
}
