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
            
            // debug
            user = new ChatUser( "bug" );
            messageHandler = new MessageHandler( new MessageUtils.ReceiveMessageDelegate( receiveMessage ), "224.0.0.0", 6969 );
            messageHandler.connect();
            messageThread = new Thread( new ThreadStart( messageHandler.startProcessing ) );
            messageThread.IsBackground = true;
            messageThread.Start();
        }

        /// <summary>
        /// Exits the application.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void exitToolStripMenuItem_Click( object sender, EventArgs e )
        {
            System.Windows.Forms.Application.Exit();
        }

        /// <summary>
        /// Presents a preference dialog to the user.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void preferencesToolStripMenuItem_Click( object sender, EventArgs e )
        {
            // show preferences form
        }

        /// <summary>
        /// Sends a message.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void SendButton_Click( object sender, EventArgs e )
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
    }
}
