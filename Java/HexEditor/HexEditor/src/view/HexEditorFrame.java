package view;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;
import java.util.prefs.Preferences;

import javautils.IconManager;
import javautils.IconManager.IconSize;
import javautils.hex.HexUtils;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

/*******************************************************************************
 * Main frame for the HexEditor application.
 ******************************************************************************/
public class HexEditorFrame extends JFrame
{
    private final static String FRAME_TITLE = "HexEditor";
    private final static String PREF_FILE_OPEN_DIR = "fileOpenDir";
    private final static String PREF_FILE_SAVE_DIR = "fileSaveDir";

    private JToolBar toolbar;

    private NavPanel navPanel;

    private Preferences preferences;

    private Action newAction;
    private Action openAction;
    private Action closeAction;
    private Action saveAction;
    private Action saveAsAction;
    private Action exitAction;
    private Action gotoOffsetAction;
    private Action findAction;
    private Action findNextAction;
    private Action findPrevAction;
    private Action addBytesAction;
    private Action removeBytesAction;
    private Action gotoBlockAction;
    private Action gotoPrevBlockAction;
    private Action gotoNextBlockAction;
    private Action asciiDialogAction;
    private Action aboutDialogAction;

    private HexEditorTable hexTable;

    private File saveFile;
    private File selectedFile;

    private byte searchByte;
    private int searchIndex;

    private boolean unsavedEdits;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public HexEditorFrame()
    {
        preferences = Preferences.userRoot().node( this.getClass().getName() );
        saveFile = null;
        selectedFile = null;
        searchByte = -1;
        searchIndex = -1;
        unsavedEdits = false;

        hexTable = new HexEditorTable();

        // set up notification for edits
        hexTable.addPropertyChangeListener( new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                if( e.getPropertyName().equals( hexTable.EDIT ) )
                {
                    unsavedEdits = true;
                    updateUI();
                }
            }
        } );

        // set up notification for selection changes
        hexTable.addPropertyChangeListener( new PropertyChangeListener()
        {
            @Override
            public void propertyChange( PropertyChangeEvent e )
            {
                if( e.getPropertyName().equals( hexTable.SELECTION ) )
                {
                    navPanel.setCurrentByte( hexTable.getFirstSelectedByte() );
                    navPanel.setOffset( hexTable.getFirstSelectedOffset() );
                }
            }
        } );

        setupActions();
        setupMenu();
        setupToolbar();
        setupFrame();
    }

    /***************************************************************************
     * Updates the user interface. This function is used any time there is a
     * block or selection change, so that current information is displayed to
     * the user.
     **************************************************************************/
    private void updateUI()
    {
        setDisplayInfo();
        setActionStatus();
        hexTable.revalidate();
        hexTable.repaint();
    }

    /***************************************************************************
     * Sets the display information (selection, offset, etc.).
     **************************************************************************/
    private void setDisplayInfo()
    {
        if( selectedFile == null )
        {
            navPanel.setFileDetails( "<none>", 0 );
            navPanel.setBlock( 0, 0 );
        } else
        {
            navPanel.setFileDetails( selectedFile.getName(), hexTable
                    .getHexTableModel().getTotalBytes() );
            navPanel.setBlock( hexTable.getHexTableModel()
                    .getCurrentBlockIndex(), hexTable.getHexTableModel()
                    .getBlockCount() );
        }
        setTitle( ( unsavedEdits ? "*" : "" ) + FRAME_TITLE );
    }

    /***************************************************************************
     * Enables or disables all actions depending on current status.
     **************************************************************************/
    private void setActionStatus()
    {
        boolean b = ( selectedFile != null );

        saveAction.setEnabled( unsavedEdits );
        saveAsAction.setEnabled( unsavedEdits );
        closeAction.setEnabled( b );
        gotoOffsetAction.setEnabled( b );
        findAction.setEnabled( b );
        findNextAction.setEnabled( searchIndex != -1 );
        findPrevAction.setEnabled( searchIndex != -1 );
        addBytesAction.setEnabled( b );
        removeBytesAction.setEnabled( b );
        gotoBlockAction.setEnabled( b );
        gotoNextBlockAction.setEnabled( hexTable.getHexTableModel().hasNext() );
        gotoPrevBlockAction.setEnabled( hexTable.getHexTableModel()
                .hasPrevious() );
    }

    /***************************************************************************
     * Initializes all actions.
     **************************************************************************/
    private void setupActions()
    {
        newAction = new NewAction();
        openAction = new OpenAction();
        closeAction = new CloseAction();
        saveAction = new SaveAction();
        saveAsAction = new SaveAsAction();
        exitAction = new ExitAction();
        gotoOffsetAction = new GotoOffsetAction();
        findAction = new FindAction();
        findNextAction = new FindNextAction();
        findPrevAction = new FindPrevAction();
        addBytesAction = new AddBytesAction();
        removeBytesAction = new RemoveBytesAction();
        gotoBlockAction = new GotoBlockAction();
        gotoPrevBlockAction = new GotoPrevBlockAction();
        gotoNextBlockAction = new GotoNextBlockAction();
        asciiDialogAction = new AsciiDialogAction();
        aboutDialogAction = new AboutDialogAction();
    }

    /***************************************************************************
     * Sets up the main application menu.
     **************************************************************************/
    private void setupMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu( "File" );
        fileMenu.add( newAction );
        fileMenu.add( openAction );
        fileMenu.addSeparator();
        fileMenu.add( closeAction );
        fileMenu.addSeparator();
        fileMenu.add( saveAction );
        fileMenu.add( saveAsAction );
        fileMenu.addSeparator();
        fileMenu.add( exitAction );

        JMenu editMenu = new JMenu( "Edit" );
        editMenu.add( gotoOffsetAction );
        editMenu.add( findAction );
        editMenu.add( findNextAction );
        editMenu.add( findPrevAction );
        editMenu.add( addBytesAction );
        editMenu.add( removeBytesAction );

        JMenu helpMenu = new JMenu( "Help" );
        helpMenu.add( asciiDialogAction );
        helpMenu.add( aboutDialogAction );

        menuBar.add( fileMenu );
        menuBar.add( editMenu );
        menuBar.add( helpMenu );

        this.setJMenuBar( menuBar );
    }

    /***************************************************************************
     * Sets up the toolbar.
     **************************************************************************/
    private void setupToolbar()
    {
        toolbar = new JToolBar();
        toolbar.setFocusable( false );
        toolbar.setFloatable( false );
        toolbar.add( newAction ).setFocusable( false );
        toolbar.add( openAction ).setFocusable( false );
        toolbar.addSeparator();
        toolbar.add( closeAction ).setFocusable( false );
        toolbar.addSeparator();
        toolbar.add( saveAction ).setFocusable( false );
        toolbar.addSeparator();
        toolbar.add( gotoOffsetAction ).setFocusable( false );
        toolbar.add( findAction ).setFocusable( false );
        toolbar.add( addBytesAction ).setFocusable( false );
        toolbar.add( removeBytesAction ).setFocusable( false );
    }

    /***************************************************************************
     * Sets up this frame.
     **************************************************************************/
    private void setupFrame()
    {
        setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        addWindowListener( new WindowAdapter()
        {
            @Override
            public void windowClosing( WindowEvent e )
            {
                if( confirmAction() )
                    dispose();
                else
                    return;
            }
        } );

        setTitle( FRAME_TITLE );
        setIconImages( Arrays.asList(
                IconManager.getImage( IconManager.YINYANG, IconSize.X16 ),
                IconManager.getImage( IconManager.YINYANG, IconSize.X32 ),
                IconManager.getImage( IconManager.YINYANG, IconSize.X64 ) ) );
        setPreferredSize( new Dimension( 800, 690 ) );
        setMinimumSize( getPreferredSize() );
        setSize( getPreferredSize() );
        setLocationRelativeTo( null );

        navPanel = new HexNavPanel();
        navPanel.setGotoAction( gotoBlockAction );
        navPanel.setGotoNextAction( gotoNextBlockAction );
        navPanel.setGotoPrevAction( gotoPrevBlockAction );

        setLayout( new BorderLayout() );
        add( toolbar, BorderLayout.PAGE_START );
        add( new JScrollPane( hexTable ), BorderLayout.CENTER );
        add( navPanel.getComponent(), BorderLayout.PAGE_END );

        updateUI();
    }

    /***************************************************************************
     * Returns true if there are unsaved edits the user wishes to discard. If
     * there are no unsaved edits, returns true.
     * 
     * @return
     **************************************************************************/
    private boolean confirmAction()
    {
        if( unsavedEdits )
        {
            if( JOptionPane.showConfirmDialog( this,
                    "There are unsaved edits.\n" + "Do you wish to continue?",
                    "Continue?", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE ) != JOptionPane.YES_OPTION )
            {
                return false;
            }
        }

        return true;
    }

    /***************************************************************************
     * Returns true if the current file has been closed, false otherwise.
     * 
     * @return
     **************************************************************************/
    private boolean closeFile()
    {
        if( selectedFile != null )
        {
            if( !confirmAction() )
                return false;

            saveFile = null;
            selectedFile = null;
            unsavedEdits = false;
            hexTable.clearSelection();
            hexTable.getHexTableModel().clear();
            updateUI();
        }

        return true;
    }

    /**************************************************************************/
    private class NewAction extends AbstractAction
    {
        public NewAction()
        {
            super( "New", IconManager.getIcon( IconManager.PAGE_ADD,
                    IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "New empty file" );
            putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_N ) );
            putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_N,
                    java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            if( closeFile() )
            {
                hexTable.getHexTableModel().newData();
                try
                {
                    selectedFile = File.createTempFile( "untitled", ".bin" );
                    unsavedEdits = true;
                } catch( Exception e )
                {
                    e.printStackTrace();
                }
                updateUI();
            }
        }
    }

    /**************************************************************************/
    private class OpenAction extends AbstractAction
    {

        public OpenAction()
        {
            super( "Open...", IconManager.getIcon( IconManager.FOLDER,
                    IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "Open file for viewing/editing" );
            putValue( MNEMONIC_KEY, new Integer( KeyEvent.VK_O ) );
            putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_O,
                    java.awt.event.InputEvent.CTRL_DOWN_MASK ) );

        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            // show open file dialog
            JFileChooser chooser = new JFileChooser( preferences.get(
                    PREF_FILE_OPEN_DIR, "." ) );
            chooser.setMultiSelectionEnabled( false );
            if( chooser.showOpenDialog( HexEditorFrame.this ) == JFileChooser.APPROVE_OPTION )
            {
                preferences.put( PREF_FILE_OPEN_DIR, chooser.getSelectedFile()
                        .getAbsolutePath() );
                if( closeFile() )
                {
                    selectedFile = chooser.getSelectedFile();

                    try
                    {
                        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
                        hexTable.getHexTableModel().readData( selectedFile );
                        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                        unsavedEdits = false;
                    } catch( Exception ex )
                    {
                        JOptionPane.showMessageDialog(
                                HexEditorFrame.this,
                                "Error reading file: '"
                                        + selectedFile.getAbsolutePath()
                                        + "'\nERROR: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE );
                        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                        return;
                    }

                    updateUI();
                }
            }
        }
    }

    /**************************************************************************/
    private class CloseAction extends AbstractAction
    {
        public CloseAction()
        {
            super( "Close", IconManager.getIcon( IconManager.FOLDER_DELETE,
                    IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "Close current file" );
            putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_W,
                    java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
            setEnabled( false );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            closeFile();
        }
    }

    /**************************************************************************/
    private class SaveAction extends AbstractAction
    {
        public SaveAction()
        {
            super( "Save", IconManager.getIcon( IconManager.DISK,
                    IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "Save current file" );
            putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_S,
                    java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
            setEnabled( false );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            if( saveFile == null )
            {
                saveAsAction.actionPerformed( null );
            } else
            {
                try
                {
                    hexTable.getHexTableModel().writeData( saveFile );
                    unsavedEdits = false;
                    selectedFile = saveFile;
                    updateUI();
                } catch( Exception e )
                {
                    JOptionPane.showMessageDialog(
                            HexEditorFrame.this,
                            "Error writing file: '"
                                    + saveFile.getAbsolutePath() + "'\nERROR: "
                                    + e.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE );
                    return;
                }
            }
        }
    }

    /**************************************************************************/
    private class SaveAsAction extends AbstractAction
    {
        public SaveAsAction()
        {
            super( "Save as...", IconManager.getIcon( IconManager.DISK,
                    IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "Save file as..." );
            putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_S,
                    java.awt.event.InputEvent.CTRL_DOWN_MASK
                            | java.awt.event.InputEvent.SHIFT_DOWN_MASK ) );
            setEnabled( false );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            // show open file dialog
            JFileChooser chooser = new JFileChooser( preferences.get(
                    PREF_FILE_SAVE_DIR, "." ) );
            chooser.setMultiSelectionEnabled( false );
            if( chooser.showSaveDialog( HexEditorFrame.this ) == JFileChooser.APPROVE_OPTION )
            {
                preferences.put( PREF_FILE_SAVE_DIR, chooser.getSelectedFile()
                        .getAbsolutePath() );
                saveFile = chooser.getSelectedFile();
                saveAction.actionPerformed( null );
            }
        }
    }

    /**************************************************************************/
    private class ExitAction extends AbstractAction
    {
        public ExitAction()
        {
            super( "Exit", IconManager.getIcon( IconManager.DOOR_IN,
                    IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "Exit the application" );
            putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_Q,
                    java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            if( confirmAction() )
                HexEditorFrame.this.dispose();
        }
    }

    /**************************************************************************/
    private class GotoOffsetAction extends AbstractAction
    {
        public GotoOffsetAction()
        {
            super( "Goto offset...", IconManager.getIcon(
                    IconManager.BULLET_GO, IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "Goto user-provided byte offset" );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            if( confirmAction() )
            {
                String input = JOptionPane.showInputDialog(
                        HexEditorFrame.this, "Input offset:", "Go to offset",
                        JOptionPane.QUESTION_MESSAGE );
                if( input.isEmpty() )
                    return;

                int offset = 0;
                try
                {
                    offset = HexUtils.parseHexString( input );
                } catch( Exception e )
                {
                    JOptionPane.showMessageDialog( HexEditorFrame.this,
                            "Could not parse input; please try again.",
                            "Invalid input", JOptionPane.ERROR_MESSAGE );
                    return;
                }

                if( offset < 0
                        || offset >= hexTable.getHexTableModel()
                                .getTotalBytes() )
                {
                    JOptionPane.showMessageDialog( HexEditorFrame.this,
                            "Offset is not valid.", "Invalid input",
                            JOptionPane.ERROR_MESSAGE );
                    return;
                }

                int offsetInBlock = hexTable.getHexTableModel().gotoOffset(
                        offset );
                hexTable.setSelection( offsetInBlock );
                updateUI();
            }
        }
    }

    /**************************************************************************/
    private class FindAction extends AbstractAction
    {
        public FindAction()
        {
            super( "Find byte...", IconManager.getIcon( IconManager.FIND,
                    IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "Find byte in current block" );
            putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_F,
                    java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            String input = JOptionPane.showInputDialog( HexEditorFrame.this,
                    "Find:", "Find", JOptionPane.QUESTION_MESSAGE );
            if( input.isEmpty() )
                return;

            int offset = 0;
            try
            {
                offset = HexUtils.parseHexString( input );
                if( offset < 0 || offset > 0xFF )
                {
                    throw new Exception();
                }
                searchByte = (byte)offset;
            } catch( Exception e )
            {
                JOptionPane.showMessageDialog( HexEditorFrame.this,
                        "Could not parse input; please try again.",
                        "Invalid input", JOptionPane.ERROR_MESSAGE );
                return;
            }

            offset = hexTable.find( 0, searchByte );
            if( offset >= 0 )
            {
                searchIndex = offset;
                updateUI();
            } else
            {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    /**************************************************************************/
    private class FindNextAction extends AbstractAction
    {
        public FindNextAction()
        {
            super( "Find next" );
            putValue( SHORT_DESCRIPTION, "Find next" );
            putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_G,
                    java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
            setEnabled( false );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            int index = hexTable.find( ( searchIndex + 1 ), searchByte );
            if( index >= 0 )
            {
                searchIndex = index;
                updateUI();
            } else
            {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    /**************************************************************************/
    private class FindPrevAction extends AbstractAction
    {
        public FindPrevAction()
        {
            super( "Find previous" );
            putValue( SHORT_DESCRIPTION, "Find previous" );
            putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke( KeyEvent.VK_G,
                    java.awt.event.InputEvent.CTRL_DOWN_MASK
                            | java.awt.event.InputEvent.SHIFT_DOWN_MASK ) );
            setEnabled( false );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            int index = hexTable.rfind( ( searchIndex - 1 ), searchByte );
            if( index >= 0 )
            {
                searchIndex = index;
                updateUI();
            } else
            {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    /**************************************************************************/
    private class AddBytesAction extends AbstractAction
    {
        public AddBytesAction()
        {
            super( "Add bytes...", IconManager.getIcon( IconManager.BRICK_ADD,
                    IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "Add bytes to current block" );
            putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                    KeyEvent.VK_PLUS, java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
            setEnabled( false );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            if( hexTable.addBytes( 1 ) )
            {
                unsavedEdits = true;
                updateUI();
            }
        }
    }

    /**************************************************************************/
    private class RemoveBytesAction extends AbstractAction
    {
        public RemoveBytesAction()
        {
            super( "Remove bytes...", IconManager.getIcon(
                    IconManager.BRICK_DELETE, IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "Remove bytes from current block" );
            putValue( ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke( KeyEvent.VK_MINUS,
                            java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
            setEnabled( false );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            if( hexTable.deleteBytes( 1 ) )
            {
                unsavedEdits = true;
                updateUI();
            }
        }
    }

    /**************************************************************************/
    private class GotoBlockAction extends AbstractAction
    {
        public GotoBlockAction()
        {
            super( "Go" );
            putValue( SHORT_DESCRIPTION, "Go to specified block" );
            setEnabled( false );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            if( confirmAction() )
            {
                hexTable.getHexTableModel().gotoBlock(
                        navPanel.getUserSpecifiedBlock() );
                hexTable.clearSelection();
                updateUI();
                searchIndex = 0;
            }
        }
    }

    /**************************************************************************/
    private class GotoPrevBlockAction extends AbstractAction
    {
        public GotoPrevBlockAction()
        {
            super( "", IconManager.getIcon( IconManager.ARROW_LEFT,
                    IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "Go to previous block" );
            putValue( ACCELERATOR_KEY, KeyStroke.getKeyStroke(
                    KeyEvent.VK_LEFT, java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
            setEnabled( false );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            if( confirmAction() )
            {
                hexTable.getHexTableModel().previous();
                hexTable.clearSelection();
                updateUI();
                searchIndex = 0;
            }
        }
    }

    /**************************************************************************/
    private class GotoNextBlockAction extends AbstractAction
    {
        public GotoNextBlockAction()
        {
            super( "", IconManager.getIcon( IconManager.ARROW_RIGHT,
                    IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "Go to next block" );
            putValue( ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke( KeyEvent.VK_RIGHT,
                            java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
            setEnabled( false );
        }

        @Override
        public void actionPerformed( ActionEvent arg0 )
        {
            if( confirmAction() )
            {
                hexTable.getHexTableModel().next();
                hexTable.clearSelection();
                updateUI();
                searchIndex = 0;
            }
        }
    }

    /**************************************************************************/
    private class AsciiDialogAction extends AbstractAction
    {
        public AsciiDialogAction()
        {
            super( "ASCII Codes...", IconManager.getIcon( IconManager.KEY_A,
                    IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION,
                    "Present a dialog with all the ASCII character codes." );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            JDialog dialog = new AsciiDialog();
            dialog.setVisible( true );
        }
    }

    /**************************************************************************/
    private class AboutDialogAction extends AbstractAction
    {
        public AboutDialogAction()
        {
            super( "About", IconManager.getIcon( IconManager.HELP,
                    IconManager.IconSize.X16 ) );
            putValue( SHORT_DESCRIPTION, "About HexEditor" );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            JDialog dialog = new AboutDialog();
            dialog.setVisible( true );
        }
    }
}
