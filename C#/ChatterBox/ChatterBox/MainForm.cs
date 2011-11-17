using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;

namespace ChatterBox
{
    /// <summary>
    /// Main application window.
    /// </summary>
    public partial class MainForm : Form
    {
        /// <summary>
        /// This delegate is used to update the main chat box from
        /// any thread.
        /// </summary>
        /// <param name="text"></param>
        private delegate void ChatBoxAppendDelegate( string text );

        /// <summary>
        /// Constructor
        /// </summary>
        public MainForm()
        {
            InitializeComponent();

            chatTextBox.ReadOnly = true;
            chatTextBox.BackColor = Color.White;

            // init our user's preferences
            string homePath = ( Environment.OSVersion.Platform == PlatformID.Unix ||
                Environment.OSVersion.Platform == PlatformID.MacOSX )
                ? Environment.GetEnvironmentVariable( "HOME" )
                : Environment.ExpandEnvironmentVariables( "%HOMEDRIVE%%HOMEPATH%" );
            Preferences.initialize( homePath + "/.ChatterBoxPrefs",
                new string[] {"user", "autoconnect", "host", "port", "color"} );
            if( !Preferences.exists() )
                showPreferenceDialog( null, null );
            Preferences.readPreferences();

            user = new ChatUser( Preferences.getPreference( "user" ) );

            bool autoConnect = false;
            try
            {
                autoConnect = bool.Parse( Preferences.getPreference( "autoconnect" ) );
            }
            catch {}
            
            if( autoConnect )
            {
                connect( null, null );
            }
            else
            {
                disonnectButton.Enabled = false;
            }
        }

        /// <summary>
        /// Exits the application.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void exitApplication( object sender, EventArgs e )
        {
            disconnect( null, null );
            System.Windows.Forms.Application.Exit();
        }

        /// <summary>
        /// Presents a preference dialog to the user.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void showPreferenceDialog( object sender, EventArgs e )
        {
            PreferenceForm prefForm = new PreferenceForm();
            if( prefForm.ShowDialog( this ) == DialogResult.OK )
            {
                Preferences.writePreferences();
            }
            prefForm.Dispose();
        }

        /// <summary>
        /// Sends a message.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void sendMessage( object sender, EventArgs e )
        {
            string messageText = messageTextBox.Text;
            if( messageText.Length > 0 )
            {
                messageTextBox.Text = "";
                IMessage msg = new ChatMessage( user.getName(), user.getDisplayName(), messageText );
                messageHandler.sendMessage( msg );
            }
        }

        /// <summary>
        /// Receives an individual message and updates the chat text
        /// accordingly.
        /// </summary>
        /// <param name="msg"></param>
        private void receiveMessage( IMessage msg )
        {
            string s = "(" + msg.getDateTime().ToShortTimeString() +
                    ") " + msg.getSender() + ": " + msg.getMessage() + "\n";
            if( chatTextBox.InvokeRequired )
            {
                ChatBoxAppendDelegate del = new ChatBoxAppendDelegate( chatTextBox.AppendText );
                chatTextBox.Invoke( del, s );
            }
            else
            {
                chatTextBox.AppendText( s );
            }
        }

        /// <summary>
        /// Connects on the user's preferred ip/port.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void connect( object sender, EventArgs e )
        {
            if( messageHandler != null )
                messageHandler.disconnect();
            
            if( messageThread != null && messageThread.IsAlive )
            {
                messageThread.Abort();
                messageThread.Join();
            }

            int port = 0;
            try
            {
                port = int.Parse( Preferences.getPreference( "port" ) );
            }
            catch( Exception )
            {
                MessageBox.Show( "Port '" + Preferences.getPreference( "port" ) +
                    "' is not a valid integer.\nUsing 6969 instead. Please change your preferences.",
                    "Error", MessageBoxButtons.OK, MessageBoxIcon.Exclamation );
                port = 6969;
            }

            messageHandler = new MessageHandler(
                new MessageUtils.ReceiveMessageDelegate( receiveMessage ),
                Preferences.getPreference( "host" ), port );
            messageThread = new Thread( new ThreadStart( messageHandler.startProcessing ) );
            messageThread.IsBackground = true;

            if( messageHandler.connect() )
            {
                messageThread.Start();
                connectButton.Enabled = false;
                disonnectButton.Enabled = true;
            }
            else
            {
                connectButton.Enabled = true;
                disonnectButton.Enabled = false;
            }
        }

        /// <summary>
        /// Disconnects from the currently connect socket, if necessary.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void disconnect( object sender, EventArgs e )
        {
            if( messageHandler != null )
                messageHandler.disconnect();

            if( messageThread != null && messageThread.IsAlive )
            {
                messageThread.Abort();
                messageThread.Join();
            }

            connectButton.Enabled = true;
            disonnectButton.Enabled = false;
        }

        /// <summary>
        /// Clears the conversation pane.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void clearConversation( object sender, EventArgs e )
        {
            chatTextBox.Clear();
        }
    }
}
