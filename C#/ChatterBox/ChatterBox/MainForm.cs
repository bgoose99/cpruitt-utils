using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;
using System.Timers;

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
        /// <param name="name"></param>
        /// <param name="color"></param>
        /// <param name="timestamp"></param>
        private delegate void ChatBoxAppendDelegate( DateTime timestamp, string name, Color color, string text );

        /// <summary>
        /// This delegate is used to update the user view.
        /// </summary>
        /// <param name="name"></param>
        /// <param name="isAvailable"></param>
        private delegate void UpdateUserViewDelegate( string name, bool isAvailable );

        /// <summary>
        /// Purges users who have not sent a heartbeat in an alotted time.
        /// </summary>
        private delegate void PurgeOldUserDelegate();

        /// <summary>
        /// Constructor
        /// </summary>
        public MainForm()
        {
            InitializeComponent();

            // set the default accept button to send
            AcceptButton = sendButton;

            // set up our user image list
            userView.SmallImageList = imageList1;
            imageList1.Images.Add( global::ChatterBox.Properties.Resources.user16 );
            imageList1.Images.Add( global::ChatterBox.Properties.Resources.user_silhouette16 );

            // set up our user list
            currentUserList = new List<ChatUserDisplay>();

            // set up our timer
            purgeUserTimer = new System.Timers.Timer( 1000.0 );
            purgeUserTimer.Elapsed += new ElapsedEventHandler( onTimedEvent );
            purgeUserTimer.Enabled = true;

            // set up our chat box
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

            // create our user
            localUser = new ChatUser( Preferences.getPreference( "user" ), Color.FromArgb( Int32.Parse( Preferences.getPreference( "color" ) ) ) );

            // connect, if necessary
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
                localUser.setDisplayName( Preferences.getPreference( "user" ) );
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
                IMessage msg = new ChatMessage( localUser.getName(), 
                    localUser.getDisplayName(), localUser.getPreferredColor(), messageText );
                if( messageHandler != null )
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
                    ChatBoxAppendDelegate chatBoxAppendDelegate = new ChatBoxAppendDelegate( appendToConversation );
                    this.Invoke( chatBoxAppendDelegate, chatMsg.getSendTime(), 
                        chatMsg.getUserDisplayName(), chatMsg.getDisplayColor(), chatMsg.getMessage() + "\n" );
                    break;
                case MessageUtils.MessageType.HEARTBEAT:
                    IHeartbeatMessage heartbeatMsg = (IHeartbeatMessage)msg;
                    string name = heartbeatMsg.getUserDisplayName();
                    UpdateUserViewDelegate updateUserViewDelegate = new UpdateUserViewDelegate( updateUserView );
                    this.Invoke( updateUserViewDelegate, heartbeatMsg.getUserDisplayName(), heartbeatMsg.isUserAvailable() );
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

            heartbeatTask = new HeartbeatSendTask( localUser,
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

            userView.Clear();
            currentUserList.Clear();
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
            localUser.setAvailable( !localUser.isAvailable() );
            onlineButton.Image = localUser.isAvailable() ? 
                global::ChatterBox.Properties.Resources.user_silhouette16 : 
                global::ChatterBox.Properties.Resources.user16;
        }

        /// <summary>
        /// Updates the user view.
        /// </summary>
        /// <param name="name"></param>
        /// <param name="isAvailable"></param>
        private void updateUserView( string name, bool isAvailable )
        {
            int index = isAvailable ? 0 : 1; // user image index
            bool found = false;
            if( !name.Equals( localUser.getDisplayName() ) )
            {
                foreach( ChatUserDisplay user in currentUserList )
                {
                    if( user.ListViewItem.Name.Equals( name ) )
                    {
                        // update
                        found = true;
                        user.LastUpdate = DateTime.Now;
                        if( user.ListViewItem.ImageIndex != index )
                            user.ListViewItem.ImageIndex = index;
                    }
                }
                if( !found )
                {
                    ChatUserDisplay user = new ChatUserDisplay( name );
                    currentUserList.Add( user );
                    userView.Items.Add( user.ListViewItem );
                    appendToConversation( DateTime.Now, null, Color.Black,
                        "User " + user.ListViewItem.Name + " has joined the conversation\n" );
                }
            }
        }

        /// <summary>
        /// Timer function.
        /// </summary>
        private void onTimedEvent( object source, ElapsedEventArgs e )
        {
            PurgeOldUserDelegate purgeOldUserDelegate = new PurgeOldUserDelegate( purgeOldUsers );
            this.Invoke( purgeOldUserDelegate );
        }

        /// <summary>
        /// Purges all users that have not sent a heartbeat in the last 5 seconds.
        /// </summary>
        private void purgeOldUsers()
        {
            foreach( ChatUserDisplay user in currentUserList.FindAll( isUserOffline ) )
            {
                userView.Items.Remove( user.ListViewItem );
                appendToConversation( DateTime.Now, null, Color.Black,
                    "User " + user.ListViewItem.Name + " has left the conversation\n" );
            }
            currentUserList.RemoveAll( isUserOffline );
        }

        /// <summary>
        /// Predicate for determining if a user has not sent a heartbeat in the last 5 seconds.
        /// </summary>
        /// <param name="user"></param>
        /// <returns></returns>
        private bool isUserOffline( ChatUserDisplay user )
        {
            TimeSpan threshold = new TimeSpan( 0, 0, 5 );
            DateTime now = DateTime.Now;
            return ( TimeSpan.Compare( now - user.LastUpdate, threshold ) > 0 );
        }

        /// <summary>
        /// Method to append text to the chat pane and scroll down, if necessary.
        /// </summary>
        /// <param name="timestamp"></param>
        /// <param name="name"></param>
        /// <param name="color"></param>
        /// <param name="text"></param>
        private void appendToConversation( DateTime timestamp, string name, Color color, string text )
        {
            string t = "(" + timestamp.ToShortTimeString() + ") " +
                ( name == null ? "" : name + ": " );
            int index = chatTextBox.TextLength;
            chatTextBox.AppendText( t );
            chatTextBox.AppendText( text );
            if( name != null )
            {
                chatTextBox.SelectionStart = chatTextBox.Find( t, index, RichTextBoxFinds.None );
                chatTextBox.SelectionLength = t.Length;
                chatTextBox.SelectionColor = color;
            }
            chatTextBox.Select( chatTextBox.Text.Length, 0 );
            chatTextBox.ScrollToCaret();
        }

        /// <summary>
        /// Shows a dialog allowing the user to select a preferred color.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void showColorDialog( object sender, EventArgs e )
        {
            // show dialog
            ColorDialog dialog = new ColorDialog();
            dialog.Color = localUser.getPreferredColor();

            if( dialog.ShowDialog() == DialogResult.OK )
            {
                localUser.setPreferredColor( dialog.Color );
                Preferences.setPreference( "color", localUser.getPreferredColor().ToArgb().ToString() );
                Preferences.writePreferences();
            }
        }

        /// <summary>
        /// This class represents a user that gets displayed in the user view.
        /// </summary>
        private class ChatUserDisplay
        {
            private ListViewItem listViewItem;
            private DateTime lastUpdate;

            /// <summary>
            /// Constructor
            /// </summary>
            /// <param name="name"></param>
            public ChatUserDisplay( string name ) : this( name, true )
            {
            }

            /// <summary>
            /// Constructor
            /// </summary>
            /// <param name="name"></param>
            /// <param name="isAvailable"></param>
            public ChatUserDisplay( string name, bool isAvailable )
            {
                listViewItem = new ListViewItem( name, isAvailable ? 0 : 1 );
                listViewItem.Name = name;
                lastUpdate = DateTime.Now;
            }

            /// <summary>
            /// The ListViewItem associated with this user.
            /// </summary>
            public ListViewItem ListViewItem
            {
                get { return listViewItem; }
                set { listViewItem = value; }
            }

            /// <summary>
            /// The last time a heartbeat was received for this user.
            /// </summary>
            public DateTime LastUpdate
            {
                get { return lastUpdate; }
                set { lastUpdate = value; }
            }
        }
    }
}
