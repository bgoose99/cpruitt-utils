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

            // set the default accept button to send
            AcceptButton = sendButton;

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
                disconnectButton.Enabled = false;
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
                // update user display name, if necessary
                user.setDisplayName( Preferences.getPreference( "user" ) );
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
            switch( msg.getMessageHeader().getMessageType() )
            {
                case MessageUtils.MessageType.CHAT:
                    IChatMessage chatMsg = (IChatMessage)msg;
                    string s = "(" + chatMsg.getSendTime().ToShortTimeString() +
                            ") " + chatMsg.getUserDisplayName() + ": " + chatMsg.getMessage() + "\n";
                    if( chatTextBox.InvokeRequired )
                    {
                        ChatBoxAppendDelegate del = new ChatBoxAppendDelegate( chatTextBox.AppendText );
                        chatTextBox.Invoke( del, s );
                    }
                    else
                    {
                        chatTextBox.AppendText( s );
                    }
                    break;
                case MessageUtils.MessageType.HEARTBEAT:
                    IHeartbeatMessage heartbeatMsg = (IHeartbeatMessage)msg;
                    break;
                default:
                    System.Diagnostics.Debug.WriteLine( "Received message of unknown type: {0}", msg.getMessageHeader().getMessageType() );
                    break;
            }
        }

        /// <summary>
        /// Connects on the user's preferred ip/port.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void connect( object sender, EventArgs e )
        {
            disconnect( this, null );
            
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

            heartbeatTask = new HeartbeatSendTask( user,
                new MessageUtils.SendMessageDelegate( messageHandler.sendMessage ) );

            if( messageHandler.connect() )
            {
                messageThread.Start();
                heartbeatTask.startSending();
                connectButton.Enabled = false;
                disconnectButton.Enabled = true;
            }
            else
            {
                connectButton.Enabled = true;
                disconnectButton.Enabled = false;
            }
        }

        /// <summary>
        /// Disconnects from the currently connect socket, if necessary.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void disconnect( object sender, EventArgs e )
        {
            if( heartbeatTask != null )
                heartbeatTask.stopSending();
            
            if( messageHandler != null )
                messageHandler.disconnect();

            if( messageThread != null && messageThread.IsAlive )
            {
                messageThread.Abort();
                messageThread.Join();
            }

            connectButton.Enabled = true;
            disconnectButton.Enabled = false;
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

        /// <summary>
        /// Toggle the user's online status.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void toggleStatus( object sender, EventArgs e )
        {
            user.setAvailable( !user.isAvailable() );
            onlineButton.Image = user.isAvailable() ? 
                global::ChatterBox.Properties.Resources.user_silhouette16 : 
                global::ChatterBox.Properties.Resources.user16;
        }
    }
}
