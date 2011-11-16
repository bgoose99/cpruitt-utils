using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net.Sockets;
using System.Net;
using System.Windows.Forms;

namespace ChatterBox
{
    class MessageHandler
    {
        private MessageUtils.ReceiveMessageDelegate processMessage;

        private bool connected = false;
        private Socket receiveSocket;
        private Socket sendSocket;
        private string ipAddress;
        private int port;
        private IPEndPoint remoteEP;
        private IPEndPoint localEP;
        
        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="receiveMessage"></param>
        /// <param name="ipAddress"></param>
        /// <param name="port"></param>
        public MessageHandler( MessageUtils.ReceiveMessageDelegate receiveMessage, string ipAddress, int port )
        {
            this.processMessage = receiveMessage;
            this.ipAddress = ipAddress;
            this.port = port;
        }

        /// <summary>
        /// Starts this thread, which will listen for messages until the
        /// application exits, or the socket is disconnected.
        /// </summary>
        public void startProcessing()
        {
            // open socket and start listening for messages
            if( !connected )
                connect();

            byte[] rawMessage = new byte[MessageUtils.MAX_MESSAGE_SIZE];
            receiveSocket.ReceiveTimeout = 2000;
            int bytesReceived = 0;
            EndPoint endPoint = (EndPoint)localEP;
            while( connected )
            {
                try
                {
                    bytesReceived = receiveSocket.ReceiveFrom( rawMessage, ref endPoint );
                    if( bytesReceived > 0 )
                    {
                        processMessage( MessageUtils.decodeMessage( rawMessage ) );
                    }
                }
                catch( Exception )
                {
                    continue;
                }
            }

        }

        /// <summary>
        /// Attempts to connect on the address/port supplied in the constructor.
        /// </summary>
        public void connect()
        {
            try
            {
                IPAddress ip = IPAddress.Parse( ipAddress );
                remoteEP = new IPEndPoint( ip, port );
                localEP = new IPEndPoint( IPAddress.Any, port );
                
                receiveSocket = new Socket( AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp );
                receiveSocket.SetSocketOption( SocketOptionLevel.Socket, SocketOptionName.ReuseAddress, true );
                receiveSocket.Bind( localEP );
                receiveSocket.SetSocketOption( SocketOptionLevel.IP, SocketOptionName.AddMembership, new MulticastOption( ip ) );
                receiveSocket.SetSocketOption( SocketOptionLevel.IP, SocketOptionName.MulticastTimeToLive, 20 );
                
                sendSocket = new Socket( AddressFamily.InterNetwork, SocketType.Dgram, ProtocolType.Udp );
                sendSocket.SetSocketOption( SocketOptionLevel.Socket, SocketOptionName.ReuseAddress, true );
                sendSocket.Connect( remoteEP );
                sendSocket.SetSocketOption( SocketOptionLevel.IP, SocketOptionName.AddMembership, new MulticastOption( ip ) );
                sendSocket.SetSocketOption( SocketOptionLevel.IP, SocketOptionName.MulticastTimeToLive, 20 );
                
                connected = true;
            }
            catch( Exception e )
            {
                MessageBox.Show( e.Message );
                return;
            }
        }

        /// <summary>
        /// Attempts to disconnect from the currently connected socket, if one exists.
        /// </summary>
        public void disconnect()
        {
            try
            {
                receiveSocket.Close();
                sendSocket.Close();
                connected = false;
            }
            catch( Exception e )
            {
                MessageBox.Show( e.Message );
                return;
            }
        }

        /// <summary>
        /// Sends a message, if connected.
        /// </summary>
        /// <param name="msg"></param>
        public void sendMessage( IMessage msg )
        {
            // send message over socket
            if( connected )
            {
                int bytesSent = sendSocket.Send( MessageUtils.encodeMessage( msg ) );
            }
        }
    }
}
