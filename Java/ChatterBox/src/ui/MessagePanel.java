package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javautils.IconManager;
import javautils.IconManager.IconSize;
import javautils.Utils;
import javautils.message.DefaultChatMessage;
import javautils.message.IMessageHandler;
import javautils.message.MessageFormatOption;
import javautils.message.MessageFormatType;
import javautils.swing.JAppendableTextPane;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import data.IUser;
import data.IUserActivityMonitor;

/*******************************************************************************
 * This panel provides a place for the user to type a message.
 ******************************************************************************/
public class MessagePanel extends JPanel
{
    private JToolBar toolbar;
    private JToggleButton boldButton;
    private JToggleButton italicButton;
    private JToggleButton underlineButton;
    private JButton lowercaseButton;
    private JButton uppercaseButton;
    private MessageFormatListener messageFormatListener;
    private JAppendableTextPane textPane;
    private JScrollPane scrollPane;
    private IMessageHandler messageHandler;
    private IUser localUser;
    private IUserActivityMonitor activityMonitor = null;
    private SimpleAttributeSet currentAttributes;

    /***************************************************************************
     * Constructor
     * 
     * @param localUser
     **************************************************************************/
    public MessagePanel( IUser localUser )
    {
        this.localUser = localUser;

        currentAttributes = new SimpleAttributeSet();
        StyleConstants.setFontFamily( currentAttributes, "Dialog" );
        StyleConstants.setFontSize( currentAttributes, 12 );

        messageFormatListener = new MessageFormatListener();

        textPane = new JAppendableTextPane();
        textPane.addKeyListener( new MessagePanelKeyListener() );
        textPane.getDocument().addDocumentListener( messageFormatListener );

        scrollPane = new JScrollPane( textPane );

        setupToolbar();
        setupPanel();
    }

    /***************************************************************************
     * Sets up the toolbar for this panel.
     **************************************************************************/
    private void setupToolbar()
    {
        boldButton = new JToggleButton( IconManager.getIcon(
                IconManager.TEXT_BOLD, IconSize.X16 ) );
        boldButton.setFocusable( false );
        boldButton.addActionListener( new BoldActionListener() );

        italicButton = new JToggleButton( IconManager.getIcon(
                IconManager.TEXT_ITALIC, IconSize.X16 ) );
        italicButton.setFocusable( false );
        italicButton.addActionListener( new ItalicActionListener() );

        underlineButton = new JToggleButton( IconManager.getIcon(
                IconManager.TEXT_UNDERLINE, IconSize.X16 ) );
        underlineButton.setFocusable( false );
        underlineButton.addActionListener( new UnderlineActionListener() );

        lowercaseButton = new JButton( IconManager.getIcon(
                IconManager.TEXT_LOWERCASE, IconSize.X16 ) );
        lowercaseButton.setFocusable( false );
        lowercaseButton.addActionListener( new LowercaseActionListener() );

        uppercaseButton = new JButton( IconManager.getIcon(
                IconManager.TEXT_UPPERCASE, IconSize.X16 ) );
        uppercaseButton.setFocusable( false );
        uppercaseButton.addActionListener( new UppercaseActionListener() );

        toolbar = new JToolBar();
        toolbar.setFloatable( false );
        toolbar.setBorderPainted( false );
        toolbar.add( boldButton );
        toolbar.add( italicButton );
        toolbar.add( underlineButton );
        toolbar.addSeparator();
        toolbar.add( lowercaseButton );
        toolbar.add( uppercaseButton );
    }

    /***************************************************************************
     * Sets up this panel.
     **************************************************************************/
    private void setupPanel()
    {
        setLayout( new BorderLayout() );
        add( toolbar, BorderLayout.PAGE_START );
        add( scrollPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * Sets the activity monitor for this panel.
     * 
     * @param monitor
     **************************************************************************/
    public void setActivityMonitor( IUserActivityMonitor monitor )
    {
        this.activityMonitor = monitor;
    }

    /***************************************************************************
     * Returns the user's message.
     * 
     * @return
     **************************************************************************/
    public String getMessage()
    {
        return textPane.getText();
    }

    /***************************************************************************
     * Clears the message field.
     **************************************************************************/
    public void clearMessage()
    {
        textPane.setText( "" );
    }

    /***************************************************************************
     * Sends the user's message.
     **************************************************************************/
    private void sendMessage()
    {
        String s = getMessage();
        if( messageHandler != null )
        {
            try
            {
                DefaultChatMessage msg = new DefaultChatMessage(
                        localUser.getName(), localUser.getDisplayName(),
                        localUser.getDisplayColor(), s,
                        messageFormatListener.getFormattingOptions() );
                messageHandler.sendMessage( msg );
            } catch( Exception e )
            {
                e.printStackTrace();
            }
        }
        clearMessage();
        messageFormatListener.clearFormatting();
    }

    /***************************************************************************
     * Sets the message handler associated with this panel.
     * 
     * @param handler
     **************************************************************************/
    public void setMessageHandler( IMessageHandler handler )
    {
        this.messageHandler = handler;
    }

    /***************************************************************************
     * Updates the attributes on the currently selected text in the message
     * pane. E.g. If the user selects a word, then clicks the bold button, we
     * want to make the text bold.
     **************************************************************************/
    private void updateAttributesOnSelectedText()
    {
        String s = textPane.getSelectedText();
        if( s != null )
        {
            int offset = textPane.getSelectionStart();
            int len = textPane.getSelectionEnd() - offset;
            textPane.replace( s, offset, len, currentAttributes );
        }
    }

    /***************************************************************************
     * Custom {@link KeyAdapter} used to alter the behavior of the Enter key.
     **************************************************************************/
    private class MessagePanelKeyListener extends KeyAdapter
    {
        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
         */
        @Override
        public void keyTyped( KeyEvent e )
        {
            if( activityMonitor != null )
                activityMonitor.updateActivity();

            if( e.getKeyChar() == '\n' )
            {
                e.consume();
                if( e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK )
                    textPane.append( "\n" );
                else
                    sendMessage();
            } else
            {
                // consume the event to override default behavior
                e.consume();
                String s = e.getKeyChar() + "";
                if( Utils.isPrintableChar( e.getKeyChar() ) )
                {
                    if( textPane.getSelectedText() == null )
                    {
                        textPane.insert( s, textPane.getCaretPosition(),
                                currentAttributes );
                    } else
                    {
                        int offset = textPane.getSelectionStart();
                        int len = textPane.getSelectionEnd() - offset;
                        textPane.replace( s, offset, len, currentAttributes );
                    }
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class MessageFormatListener implements DocumentListener
    {
        /***********************************************************************
         * Each MessageFormatType in this list will correspond to a character in
         * the potential message the user is assembling. When necessary, these
         * formatting options can be resolved to a list of options.
         **********************************************************************/
        private List<MessageFormatType> formatting;

        /***********************************************************************
         * Constructor
         **********************************************************************/
        public MessageFormatListener()
        {
            formatting = new ArrayList<MessageFormatType>();
        }

        /***********************************************************************
         * Returns a {@link List} of {@link MessageFormatOption}s that represent
         * the current message formatting.
         * 
         * @return
         **********************************************************************/
        public List<MessageFormatOption> getFormattingOptions()
        {
            if( formatting.size() < 1 )
                return null;

            List<MessageFormatOption> options = new ArrayList<MessageFormatOption>();
            MessageFormatType previousType = formatting.get( 0 );
            int length = 1;
            int offset = 0;

            for( int i = 1; i < formatting.size(); i++ )
            {
                MessageFormatType currentType = formatting.get( i );
                if( currentType == previousType )
                {
                    length++;
                } else
                {
                    MessageFormatOption o = new MessageFormatOption(
                            previousType, offset, length );
                    options.add( o );
                    previousType = currentType;
                    length = 1;
                    offset = i;
                }
            }
            MessageFormatOption o = new MessageFormatOption( previousType,
                    offset, length );
            options.add( o );

            return options;
        }

        /***********************************************************************
         * Clears the current formatting.
         **********************************************************************/
        public void clearFormatting()
        {
            formatting.clear();
        }

        /***********************************************************************
         * Returns the current formatting, determined by the state of the format
         * buttons.
         * 
         * @return
         **********************************************************************/
        private MessageFormatType getCurrentFormatting()
        {
            boolean b = boldButton.isSelected();
            boolean i = italicButton.isSelected();
            boolean u = underlineButton.isSelected();
            if( b && i && u )
                return MessageFormatType.BOLD_ITALIC_AND_UNDERLINE;
            else if( b && i )
                return MessageFormatType.BOLD_AND_ITALIC;
            else if( b && u )
                return MessageFormatType.BOLD_AND_UNDERLINE;
            else if( i && u )
                return MessageFormatType.ITALIC_AND_UNDERLINE;
            else if( b )
                return MessageFormatType.BOLD;
            else if( i )
                return MessageFormatType.ITALIC;
            else if( u )
                return MessageFormatType.UNDERLINE;
            else
                return MessageFormatType.NONE;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.DocumentListener#changedUpdate(javax.swing.event
         * .DocumentEvent)
         */
        @Override
        public void changedUpdate( DocumentEvent e )
        {
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.DocumentListener#insertUpdate(javax.swing.event
         * .DocumentEvent)
         */
        @Override
        public void insertUpdate( DocumentEvent e )
        {
            // user added text
            MessageFormatType type = getCurrentFormatting();
            for( int i = 0; i < e.getLength(); i++ )
            {
                formatting.add( e.getOffset(), type );
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.swing.event.DocumentListener#removeUpdate(javax.swing.event
         * .DocumentEvent)
         */
        @Override
        public void removeUpdate( DocumentEvent e )
        {
            // user removed text
            for( int i = 0; i < e.getLength(); i++ )
            {
                formatting.remove( e.getOffset() );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class BoldActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            StyleConstants.setBold( currentAttributes, boldButton.isSelected() );
            updateAttributesOnSelectedText();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ItalicActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            StyleConstants.setItalic( currentAttributes,
                    italicButton.isSelected() );
            updateAttributesOnSelectedText();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class UnderlineActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            StyleConstants.setUnderline( currentAttributes,
                    underlineButton.isSelected() );
            updateAttributesOnSelectedText();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class LowercaseActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            String s = textPane.getSelectedText();
            if( s != null )
            {
                s = s.toLowerCase();
                int offset = textPane.getSelectionStart();
                int len = textPane.getSelectionEnd() - offset;
                textPane.replace( s, offset, len, currentAttributes );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class UppercaseActionListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            String s = textPane.getSelectedText();
            if( s != null )
            {
                s = s.toUpperCase();
                int offset = textPane.getSelectionStart();
                int len = textPane.getSelectionEnd() - offset;
                textPane.replace( s, offset, len, currentAttributes );
            }
        }
    }
}
