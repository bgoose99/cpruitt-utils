package ui;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;

import javautils.IconManager;
import javautils.IconManager.IconSize;
import javautils.SwingUtils;
import javautils.Utils;
import javautils.hex.HexUtils;
import javautils.io.Preferences;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

/*******************************************************************************
 * This class contains the main frame for the HexEditor application.
 ******************************************************************************/
public class HexEditorFrame extends JFrame
{
    private final static String FRAME_TITLE = "HexEditor";

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openMenuItem;
    private JMenuItem closeMenuItem;
    private JMenuItem newMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem exitMenuItem;
    private JMenu editMenu;
    private JMenuItem gotoMenuItem;
    private JMenuItem findMenuItem;
    private JMenuItem findNextMenuItem;
    private JMenuItem findPrevMenuItem;
    private JMenuItem addBytesMenuItem;
    private JMenuItem deleteBytesMenuItem;
    private JMenu helpMenu;
    private JMenuItem aboutMenuItem;
    private JMenuItem asciiTableMenuItem;

    private JToolBar toolbar;
    private JButton newButton;
    private JButton openButton;
    private JButton closeButton;
    private JButton saveButton;
    private JButton gotoButton;
    private JButton findButton;
    private JButton addByteButton;
    private JButton deleteByteButton;

    private JPanel infoPanel;
    private JLabel infoLabel;
    private JLabel gotoBlockLabel;
    private JTextField gotoBlockTextField;
    private JButton gotoBlockButton;
    private JButton prevButton;
    private JButton nextButton;

    private JScrollPane scrollPane;
    private HexTable hexTable;

    private File saveFile;
    private File selectedFile;

    private boolean unsavedEdits;

    private byte findKey;
    private int findIndex;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public HexEditorFrame()
    {
        Preferences.initialize( System.getProperty( "user.home" )
                + "/.HexEditorPrefs", "filedir", "savedir" );
        Preferences.readPreferences();

        menuBar = new JMenuBar();
        setupFileMenu();
        setupEditMenu();
        setupHelpMenu();
        menuBar.add( fileMenu );
        menuBar.add( editMenu );
        menuBar.add( Box.createHorizontalGlue() );
        menuBar.add( helpMenu );

        setupToolbar();

        infoPanel = new JPanel();
        infoLabel = new JLabel();
        gotoBlockLabel = new JLabel( "Go to block" );
        gotoBlockTextField = new JTextField();
        gotoBlockButton = new JButton( "Go" );
        gotoBlockButton.setFocusable( false );
        gotoBlockButton.setBorderPainted( false );
        gotoBlockButton.setEnabled( false );
        gotoBlockButton.addActionListener( new GotoBlockActionListener() );
        prevButton = new JButton( IconManager.getIcon( IconManager.ARROW_LEFT,
                IconSize.X16 ) );
        prevButton.setFocusable( false );
        prevButton.setBorderPainted( false );
        prevButton.setEnabled( false );
        prevButton.addActionListener( new PrevActionListener() );
        nextButton = new JButton( IconManager.getIcon( IconManager.ARROW_RIGHT,
                IconSize.X16 ) );
        nextButton.setFocusable( false );
        nextButton.setBorderPainted( false );
        nextButton.setEnabled( false );
        nextButton.addActionListener( new NextActionListener() );

        hexTable = new HexTable();
        scrollPane = new JScrollPane( hexTable );

        hexTable.addPropertyChangeListener( HexTable.EDIT,
                new TableEditListener() );

        selectedFile = null;
        saveFile = null;

        unsavedEdits = false;

        findKey = -1;
        findIndex = -1;

        setupFrame();
    }

    /***************************************************************************
     * Sets up the File menu for this frame.
     **************************************************************************/
    private void setupFileMenu()
    {
        fileMenu = new JMenu( "File" );
        fileMenu.setMnemonic( 'F' );
        newMenuItem = new JMenuItem( "New", IconManager.getIcon(
                IconManager.PAGE_ADD, IconSize.X16 ) );
        newMenuItem.setMnemonic( 'N' );
        newMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_N,
                java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        newMenuItem.addActionListener( new NewActionListener() );
        openMenuItem = new JMenuItem( "Open...", IconManager.getIcon(
                IconManager.FOLDER, IconSize.X16 ) );
        openMenuItem.setMnemonic( 'O' );
        openMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O,
                java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        openMenuItem.addActionListener( new OpenActionListener() );
        closeMenuItem = new JMenuItem( "Close", IconManager.getIcon(
                IconManager.FOLDER_DELETE, IconSize.X16 ) );
        closeMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_W,
                java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        closeMenuItem.addActionListener( new CloseActionListener() );
        saveMenuItem = new JMenuItem( "Save", IconManager.getIcon(
                IconManager.DISK, IconSize.X16 ) );
        saveMenuItem.setMnemonic( 'S' );
        saveMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S,
                java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        saveMenuItem.addActionListener( new SaveActionListener() );
        saveAsMenuItem = new JMenuItem( "Save as...", IconManager.getIcon(
                IconManager.DISK, IconSize.X16 ) );
        saveAsMenuItem.addActionListener( new SaveAsActionListener() );
        exitMenuItem = new JMenuItem( "Exit", IconManager.getIcon(
                IconManager.DOOR_IN, IconSize.X16 ) );
        exitMenuItem.setMnemonic( 'x' );
        exitMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Q,
                java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        exitMenuItem.addActionListener( new ExitActionListener() );

        fileMenu.add( newMenuItem );
        fileMenu.add( openMenuItem );
        fileMenu.addSeparator();
        fileMenu.add( closeMenuItem );
        fileMenu.addSeparator();
        fileMenu.add( saveMenuItem );
        fileMenu.add( saveAsMenuItem );
        fileMenu.addSeparator();
        fileMenu.add( exitMenuItem );
    }

    /***************************************************************************
     * Sets up the Edit menu for this frame.
     **************************************************************************/
    private void setupEditMenu()
    {
        editMenu = new JMenu( "Edit" );
        gotoMenuItem = new JMenuItem( "Goto byte...", IconManager.getIcon(
                IconManager.BULLET_GO, IconSize.X16 ) );
        gotoMenuItem.addActionListener( new GotoActionListener() );
        findMenuItem = new JMenuItem( "Find...", IconManager.getIcon(
                IconManager.FIND, IconSize.X16 ) );
        findMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F,
                java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        findMenuItem.addActionListener( new FindActionListener() );
        findNextMenuItem = new JMenuItem( "Find next" );
        findNextMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_G,
                java.awt.event.InputEvent.CTRL_DOWN_MASK ) );
        findNextMenuItem.addActionListener( new FindNextActionListener() );
        findPrevMenuItem = new JMenuItem( "Find prev" );
        findPrevMenuItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_G,
                java.awt.event.InputEvent.CTRL_DOWN_MASK
                        | java.awt.event.InputEvent.SHIFT_DOWN_MASK ) );
        findPrevMenuItem.addActionListener( new FindPrevActionListener() );
        addBytesMenuItem = new JMenuItem( "Add byte(s)...",
                IconManager.getIcon( IconManager.BRICK_ADD, IconSize.X16 ) );
        addBytesMenuItem.addActionListener( new AddBytesActionListener() );
        deleteBytesMenuItem = new JMenuItem( "Delete byte(s)...",
                IconManager.getIcon( IconManager.BRICK_DELETE, IconSize.X16 ) );
        deleteBytesMenuItem.addActionListener( new DeleteBytesActionListener() );
        editMenu.add( gotoMenuItem );
        editMenu.add( findMenuItem );
        editMenu.add( findNextMenuItem );
        editMenu.add( findPrevMenuItem );
        editMenu.add( addBytesMenuItem );
        editMenu.add( deleteBytesMenuItem );
    }

    /***************************************************************************
     * Sets up the Help menu for this frame.
     **************************************************************************/
    private void setupHelpMenu()
    {
        helpMenu = new JMenu( "Help" );
        aboutMenuItem = new JMenuItem( "About HexEditor", IconManager.getIcon(
                IconManager.HELP, IconSize.X16 ) );
        aboutMenuItem.addActionListener( new AboutActionListener() );
        asciiTableMenuItem = new JMenuItem( "ASCII codes...",
                IconManager.getIcon( IconManager.KEY_A, IconSize.X16 ) );
        asciiTableMenuItem.addActionListener( new AsciiActionListener() );
        helpMenu.add( aboutMenuItem );
        helpMenu.add( asciiTableMenuItem );
    }

    /***************************************************************************
     * Sets up the toolbar for this frame.
     **************************************************************************/
    private void setupToolbar()
    {
        toolbar = new JToolBar();
        toolbar.setFocusable( false );
        toolbar.setFloatable( false );

        newButton = SwingUtils.createToolbarButton( "New",
                IconManager.getIcon( IconManager.PAGE_ADD, IconSize.X16 ) );
        newButton.addActionListener( new NewActionListener() );
        openButton = SwingUtils.createToolbarButton( "Open",
                IconManager.getIcon( IconManager.FOLDER, IconSize.X16 ) );
        openButton.addActionListener( new OpenActionListener() );
        closeButton = SwingUtils.createToolbarButton( "Close",
                IconManager.getIcon( IconManager.FOLDER_DELETE, IconSize.X16 ) );
        closeButton.addActionListener( new CloseActionListener() );
        saveButton = SwingUtils.createToolbarButton( "Save",
                IconManager.getIcon( IconManager.DISK, IconSize.X16 ) );
        saveButton.addActionListener( new SaveActionListener() );
        gotoButton = SwingUtils.createToolbarButton( "Go to byte",
                IconManager.getIcon( IconManager.BULLET_GO, IconSize.X16 ) );
        gotoButton.addActionListener( new GotoActionListener() );
        findButton = SwingUtils.createToolbarButton( "Find",
                IconManager.getIcon( IconManager.FIND, IconSize.X16 ) );
        findButton.addActionListener( new FindActionListener() );
        addByteButton = SwingUtils.createToolbarButton( "Add byte",
                IconManager.getIcon( IconManager.BRICK_ADD, IconSize.X16 ) );
        addByteButton.addActionListener( new AddByteActionListener() );
        deleteByteButton = SwingUtils.createToolbarButton( "Delete byte",
                IconManager.getIcon( IconManager.BRICK_DELETE, IconSize.X16 ) );
        deleteByteButton.addActionListener( new DeleteByteActionListener() );

        toolbar.add( newButton );
        toolbar.add( openButton );
        toolbar.addSeparator();
        toolbar.add( closeButton );
        toolbar.addSeparator();
        toolbar.add( saveButton );
        toolbar.addSeparator();
        toolbar.add( gotoButton );
        toolbar.add( findButton );
        toolbar.add( addByteButton );
        toolbar.add( deleteByteButton );
    }

    /***************************************************************************
     * Sets up this frame.
     **************************************************************************/
    private void setupFrame()
    {
        setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        addWindowListener( new WindowCloseAdapter() );
        setTitle( FRAME_TITLE );
        setIconImages( Arrays.asList(
                IconManager.getImage( IconManager.YINYANG, IconSize.X16 ),
                IconManager.getImage( IconManager.YINYANG, IconSize.X32 ),
                IconManager.getImage( IconManager.YINYANG, IconSize.X64 ) ) );
        setJMenuBar( menuBar );
        setSize( 800, 710 );
        setLocationRelativeTo( null );
        getRootPane().setDefaultButton( gotoBlockButton );

        infoPanel.setLayout( new GridBagLayout() );
        infoPanel.add( infoLabel, new GridBagConstraints( 0, 0, 1, 1, 0.45,
                0.01, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        infoPanel.add( gotoBlockLabel, new GridBagConstraints( 1, 0, 1, 1,
                0.04, 0.01, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        infoPanel
                .add( gotoBlockTextField, new GridBagConstraints( 2, 0, 1, 1,
                        0.45, 0.01, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL,
                        new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        infoPanel.add( gotoBlockButton, new GridBagConstraints( 3, 0, 1, 1,
                0.04, 0.01, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        infoPanel.add( prevButton, new GridBagConstraints( 4, 0, 1, 1, 0.01,
                1.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        infoPanel.add( nextButton, new GridBagConstraints( 5, 0, 1, 1, 0.01,
                1.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );

        setLayout( new GridBagLayout() );
        add( toolbar, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.01,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 3, 3, 3, 3 ), 0, 0 ) );
        add( infoPanel, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.05,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );
        add( scrollPane, new GridBagConstraints( 0, 2, 1, 1, 1.0, 0.94,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
                        3, 3, 3, 3 ), 0, 0 ) );

        updateUI();
    }

    /***************************************************************************
     * Updates the interface. This should be called any time a significant
     * action takes place.
     **************************************************************************/
    private void updateUI()
    {
        setInfo();
        checkButtons();
        hexTable.revalidate();
        hexTable.repaint();
    }

    /***************************************************************************
     * Opens a new file.
     **************************************************************************/
    private void newFile()
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

    /***************************************************************************
     * Presents the user with an open dialog.
     **************************************************************************/
    private void openFile()
    {
        if( closeFile() )
        {
            JFileChooser chooser = new JFileChooser(
                    Preferences.getPreference( "filedir" ) );
            chooser.setMultiSelectionEnabled( false );
            if( chooser.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION )
            {
                selectedFile = chooser.getSelectedFile();
                Preferences.setPreference( "filedir", selectedFile
                        .getParentFile().getAbsolutePath() );

                try
                {
                    setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
                    hexTable.getHexTableModel().readData( selectedFile );
                    setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                    unsavedEdits = false;
                } catch( Exception e )
                {
                    JOptionPane.showMessageDialog(
                            this,
                            "Error reading file: '"
                                    + selectedFile.getAbsolutePath()
                                    + "'\nERROR: " + e.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE );
                    setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
                    return;
                }

                updateUI();
            }
        }
    }

    /***************************************************************************
     * Closes the currently open file and clears/resets all data.
     * 
     * @return true if the file was closed, false otherwise
     **************************************************************************/
    private boolean closeFile()
    {
        if( selectedFile != null )
        {
            if( unsavedEdits )
            {
                if( JOptionPane.showConfirmDialog( this,
                        "There are unsaved edits.\n"
                                + "Do you wish to continue?", "Open?",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE ) != JOptionPane.YES_OPTION )
                {
                    return false;
                }
            }

            saveFile = null;
            selectedFile = null;
            unsavedEdits = false;
            hexTable.clearSelection();
            hexTable.getHexTableModel().clear();
            updateUI();
        }

        return true;
    }

    /***************************************************************************
     * Saves the table data to file.
     **************************************************************************/
    private void saveFile()
    {
        if( saveFile == null )
        {
            saveAsMenuItem.doClick();
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
                JOptionPane.showMessageDialog( this,
                        "Error writing file: '" + saveFile.getAbsolutePath()
                                + "'\nERROR: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE );
                return;
            }
        }
    }

    /***************************************************************************
     * Presents the user with a save dialog, then call save if a file is
     * selected.
     **************************************************************************/
    private void saveFileAs()
    {
        JFileChooser chooser = new JFileChooser(
                Preferences.getPreference( "savedir" ) );
        chooser.setMultiSelectionEnabled( false );
        if( chooser.showSaveDialog( this ) == JFileChooser.APPROVE_OPTION )
        {
            saveFile = chooser.getSelectedFile();
            Preferences.setPreference( "savedir", saveFile.getParentFile()
                    .getAbsolutePath() );
            saveFile();
        }
    }

    /***************************************************************************
     * Sets pertinent information displayed to the user.
     **************************************************************************/
    private void setInfo()
    {
        String s;
        if( selectedFile == null )
        {
            gotoBlockTextField.setText( "" );
            s = "No file loaded";
        } else
        {
            gotoBlockTextField.setText( ""
                    + ( hexTable.getHexTableModel().getCurrentBlockIndex() ) );
            s = "<html>"
                    + selectedFile.getName()
                    + "<br>Showing block <b>"
                    + Utils.formatNumber( ( hexTable.getHexTableModel()
                            .getCurrentBlockIndex() ) )
                    + "</b> of <b>"
                    + Utils.formatNumber( hexTable.getHexTableModel()
                            .getBlockCount() )
                    + "</b>"
                    + "<br>(Total bytes: <b>"
                    + Utils.formatNumber( hexTable.getHexTableModel()
                            .getTotalBytes() ) + "</b>)</html>";
        }
        infoLabel.setText( s );
        setTitle( ( unsavedEdits ? "*" : "" ) + FRAME_TITLE );
    }

    /***************************************************************************
     * Checks the status of pertinent buttons.
     **************************************************************************/
    private void checkButtons()
    {
        boolean b = ( selectedFile != null );

        saveMenuItem.setEnabled( unsavedEdits );
        saveAsMenuItem.setEnabled( unsavedEdits );
        closeMenuItem.setEnabled( b );
        gotoMenuItem.setEnabled( b );
        findMenuItem.setEnabled( b );
        findNextMenuItem.setEnabled( findIndex != -1 );
        findPrevMenuItem.setEnabled( findIndex != -1 );
        addBytesMenuItem.setEnabled( b );
        deleteBytesMenuItem.setEnabled( b );

        saveButton.setEnabled( unsavedEdits );
        closeButton.setEnabled( b );
        gotoButton.setEnabled( b );
        findButton.setEnabled( b );
        addByteButton.setEnabled( b );
        deleteByteButton.setEnabled( b );

        gotoBlockTextField.setEnabled( b );
        gotoBlockButton.setEnabled( b );
        nextButton.setEnabled( hexTable.getHexTableModel().hasNext() );
        prevButton.setEnabled( hexTable.getHexTableModel().hasPrevious() );
    }

    /***************************************************************************
     * Finds a byte in the data model, if it exists.
     * 
     * @param next
     **************************************************************************/
    private void find( boolean next, boolean reverse )
    {
        int offset;
        if( !next )
        {
            offset = 0;
            String s = JOptionPane.showInputDialog( this,
                    "Input a hex search key:", "Find",
                    JOptionPane.QUESTION_MESSAGE );
            if( s == null )
            {
                return;
            }

            try
            {
                int i = HexUtils.parseHexString( s );
                if( i < 0 || i > 0xff )
                {
                    throw new Exception();
                }
                findKey = (byte)i;
            } catch( Exception e )
            {
                JOptionPane.showMessageDialog( this,
                        "You must input a single hex value to search for.",
                        "Error", JOptionPane.ERROR_MESSAGE );
                return;
            }
        } else
        {
            offset = findIndex + ( reverse ? -1 : 1 );
            if( findKey < 0 )
            {
                Toolkit.getDefaultToolkit().beep();
            }

        }

        int idx = ( reverse ? hexTable.rfind( offset, findKey ) : hexTable
                .find( offset, findKey ) );
        if( idx >= 0 )
        {
            findIndex = idx;
            updateUI();
        } else
        {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /***************************************************************************
     * Present the user with a dialog to enter an offset and advance to that
     * offset in the current file.
     **************************************************************************/
    private void showGotoDialog()
    {
        String s = JOptionPane.showInputDialog( this, "Go to offset:", "GoTo",
                JOptionPane.QUESTION_MESSAGE );
        if( s == null )
        {
            return;
        }

        int offset = -1;
        try
        {
            offset = HexUtils.parseHexString( s );
        } catch( Exception e )
        {
            JOptionPane.showMessageDialog( this,
                    "You must input a hex or integer value to go to.", "Error",
                    JOptionPane.ERROR_MESSAGE );
            return;
        }

        if( offset < 0 || offset >= hexTable.getHexTableModel().getTotalBytes() )
        {
            JOptionPane.showMessageDialog( this,
                    "Supplied offset does not exist.", "Error",
                    JOptionPane.ERROR_MESSAGE );
            return;
        }

        hexTable.gotoOffset( offset );
        updateUI();
    }

    /***************************************************************************
     * Determines if a block change should take place. (E.g. in the event of the
     * go to block button, or the prev/next block buttons) If there are no
     * un-saved edits, returns true. Otherwise, prompts the user to discard
     * un-saved edits.
     * 
     * @return
     **************************************************************************/
    private boolean shouldChangeBlock()
    {
        if( unsavedEdits )
        {
            if( JOptionPane.showConfirmDialog( this,
                    "There are unsaved edits in this block.\n"
                            + "Do you wish to continue anyway?\n"
                            + "(Unsaved edits will be lost.)", "Continue?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE ) != JOptionPane.YES_OPTION )
            {
                return false;
            } else
            {
                // discard edits
                unsavedEdits = false;
                return true;
            }
        } else
        {
            return true;
        }
    }

    /***************************************************************************
     * Parses the value in the goto text field and attempts to go to that block
     * of data.
     **************************************************************************/
    private void gotoBlock()
    {
        if( shouldChangeBlock() )
        {
            String input = gotoBlockTextField.getText();
            if( input == null )
                return;

            try
            {
                int i = Integer.parseInt( input );
                if( hexTable.getHexTableModel().gotoBlock( i ) )
                {
                    updateUI();
                } else
                {
                    throw new Exception();
                }
            } catch( Exception e )
            {
                JOptionPane.showMessageDialog( this, "Invalid block number: "
                        + input, "Error", JOptionPane.ERROR_MESSAGE );
                gotoBlockTextField
                        .setText( ""
                                + ( hexTable.getHexTableModel()
                                        .getCurrentBlockIndex() ) );
            }
        }
    }

    /***************************************************************************
     * Presents the user with a dialog to input the number of bytes to
     * add/delete.
     * 
     * @param add
     **************************************************************************/
    private void showNumBytesDialog( boolean add )
    {
        String s = JOptionPane.showInputDialog( this, "Number of bytes to "
                + ( add ? "add:" : "delete:" ), ( add ? "Add byte(s)"
                : "Delete byte(s)" ), JOptionPane.QUESTION_MESSAGE );
        if( s == null )
            return;

        int num = -1;
        try
        {
            num = Integer.parseInt( s );
        } catch( Exception e )
        {
            JOptionPane.showMessageDialog( this, "Input was not an integer.",
                    "Error", JOptionPane.ERROR_MESSAGE );
            return;
        }

        if( num <= 0 )
        {
            JOptionPane.showMessageDialog( this,
                    "Input must be a positive integer.", "Error",
                    JOptionPane.ERROR_MESSAGE );
            return;
        }

        if( add )
        {
            addBytes( num );
        } else
        {
            deleteBytes( num );
        }
    }

    /***************************************************************************
     * Adds bytes to the underlying table model.
     * 
     * @param offset
     * @param number
     **************************************************************************/
    private void addBytes( int number )
    {
        if( hexTable.addBytes( number ) )
        {
            unsavedEdits = true;
            updateUI();
        }
    }

    /***************************************************************************
     * Removes bytes from the underlying table model.
     * 
     * @param offset
     * @param number
     **************************************************************************/
    private void deleteBytes( int number )
    {
        if( hexTable.deleteBytes( number ) )
        {
            unsavedEdits = true;
            updateUI();
        }
    }

    /***************************************************************************
     * Show the dialog that displays information about this application.
     **************************************************************************/
    private void showAboutDialog()
    {
        JDialog dialog = new AboutDialog();
        dialog.setVisible( true );
    }

    /***************************************************************************
     * Show the dialog that displays information about this application.
     **************************************************************************/
    private void showAsciiDialog()
    {
        JDialog dialog = new AsciiDialog();
        dialog.setVisible( true );
    }

    /***************************************************************************
     * Exits the application.
     **************************************************************************/
    private boolean exit()
    {
        if( unsavedEdits )
        {
            if( JOptionPane.showConfirmDialog( this,
                    "There are unsaved edits.\n"
                            + "Do you wish to exit anyway?", "Exit?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE ) != JOptionPane.YES_OPTION )
            {
                return false;
            }
        }

        Preferences.writePreferences();
        return true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NewActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            newFile();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class OpenActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            openFile();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class CloseActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            closeFile();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SaveActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            saveFile();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SaveAsActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            saveFileAs();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ExitActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( exit() )
                HexEditorFrame.this.dispose();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class FindActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            find( false, false );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class FindNextActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            find( true, false );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class FindPrevActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            find( true, true );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class GotoActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showGotoDialog();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class GotoBlockActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            gotoBlock();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class NextActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( shouldChangeBlock() )
            {
                hexTable.getHexTableModel().next();
                hexTable.clearSelection();
                updateUI();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class PrevActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            if( shouldChangeBlock() )
            {
                hexTable.getHexTableModel().previous();
                hexTable.clearSelection();
                updateUI();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class AddByteActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            addBytes( 1 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class DeleteByteActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            deleteBytes( 1 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class AddBytesActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showNumBytesDialog( true );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class DeleteBytesActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showNumBytesDialog( false );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class AboutActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showAboutDialog();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class AsciiActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            showAsciiDialog();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class TableEditListener implements PropertyChangeListener
    {
        @Override
        public void propertyChange( PropertyChangeEvent arg0 )
        {
            unsavedEdits = true;
            updateUI();
        }
    }

    /***************************************************************************
     * Listener for window closing events.
     **************************************************************************/
    private class WindowCloseAdapter extends WindowAdapter
    {
        @Override
        public void windowClosing( WindowEvent e )
        {
            if( !exit() )
                return;
            else
                HexEditorFrame.this.dispose();
        }
    }
}
