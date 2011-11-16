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
    public partial class MainForm : Form
    {
        private delegate void ChatBoxAppendDelegate( string text );
        
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

        private void exitToolStripMenuItem_Click( object sender, EventArgs e )
        {
            // exit
            System.Windows.Forms.Application.Exit();
        }

        private void preferencesToolStripMenuItem_Click( object sender, EventArgs e )
        {
            // show preferences form
        }

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
