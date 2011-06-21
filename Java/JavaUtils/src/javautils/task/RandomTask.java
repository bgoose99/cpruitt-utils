package javautils.task;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javautils.swing.FrameRunner;
import javautils.swing.IToggleable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*******************************************************************************
 * This class contains a sample implementation of a <code>WorkerThread</code>.
 * 
 * @see WorkerThread
 ******************************************************************************/
public class RandomTask extends WorkerThread
{
    /***************************************************************************
     * Constructor
     **************************************************************************/
    public RandomTask()
    {

    }

    /***************************************************************************
     * Overridden <code>doWorkerTask()</code> method. This is where all the
     * thread processing goes on.
     **************************************************************************/
    @Override
    protected void doWorkerTask() throws Exception
    {
        int numTasks = 5;
        setNumTasks( numTasks );

        for( int task = 0; task < numTasks; task++ )
        {
            for( int i = 0; i < 100; i++ )
            {
                publish( "Doing simple task number: " + ( i + 1 ) );

                try
                {
                    Thread.sleep( 10 );
                } catch( Exception e )
                {
                    ;
                }

                setProgress( ( ( i + 1 ) * 100 ) / 100 );
            }

            taskDone();
        }
    }

    /***************************************************************************
     * RandomTask demo frame.
     * 
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        class DemoRunner extends FrameRunner
        {
            @Override
            protected JFrame createFrame()
            {
                final JFrame frame = new JFrame();
                JPanel panel = new JPanel();

                final JButton button1 = new JButton( "Push me..." );
                button1.setFocusable( false );

                final IToggleable toggle1 = new IToggleable()
                {
                    @Override
                    public void toggle( boolean newState )
                    {
                        button1.setEnabled( newState );
                    }
                };

                button1.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        RandomTask task = new RandomTask();
                        WorkerDialog dialog = new WorkerDialog( frame, task,
                                "Special processing", toggle1 );
                        dialog.startThread();
                    }
                } );

                final JButton button2 = new JButton( "Or me..." );
                button2.setFocusable( false );

                final IToggleable toggle2 = new IToggleable()
                {
                    @Override
                    public void toggle( boolean newState )
                    {
                        button2.setEnabled( newState );
                    }
                };

                button2.addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed( ActionEvent e )
                    {
                        RandomTask task = new RandomTask();
                        WorkerDialog dialog = new WorkerDialog( frame, task,
                                "Other processing", toggle2 );
                        dialog.startThread();
                    }
                } );

                panel.setLayout( new GridBagLayout() );

                panel.add( button1,
                        new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.NONE,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
                panel.add( button2,
                        new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
                                GridBagConstraints.CENTER,
                                GridBagConstraints.NONE,
                                new Insets( 3, 3, 3, 3 ), 0, 0 ) );

                frame.setTitle( "WorkerDialog / WorkerThread demo" );
                frame.setContentPane( panel );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( 400, 100 );
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );

                return frame;
            }

            @Override
            protected boolean validate()
            {
                return true;
            }
        }

        SwingUtilities.invokeLater( new DemoRunner() );
    }
}
