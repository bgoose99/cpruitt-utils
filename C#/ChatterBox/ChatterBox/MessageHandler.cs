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
        
        public MessageHandler( MessageUtils.ReceiveMessageDelegate receiveMessage, string ipAddress, int port )
        {
            this.processMessage = receiveMessage;
            this.ipAddress = ipAddress;
            this.port = port;
        }

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

        public void disconnect()
        {
            try
            {
                receiveSocket.Close();
                connected = false;
            }
            catch( Exception e )
            {
                MessageBox.Show( e.Message );
                return;
            }
        }

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
