using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Timers;

namespace ChatterBox
{
    /// <summary>
    /// This class contains the thread function for sending a heartbeat message
    /// across the connected socket via a delegate.
    /// </summary>
    class HeartbeatSendTask
    {
        public const double INTERVAL = 2000.0;
        
        private IUser localUser;
        private MessageUtils.SendMessageDelegate sendMessage;
        private Timer timer;

        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="localUser"></param>
        /// <param name="sendDelegate"></param>
        public HeartbeatSendTask( IUser localUser, MessageUtils.SendMessageDelegate sendDelegate )
        {
            this.localUser = localUser;
            this.sendMessage = sendDelegate;
            timer = new Timer( INTERVAL );
            timer.Elapsed += new ElapsedEventHandler( onTimedEvent );
        }

        /// <summary>
        /// Starts sending heartbeat messages every INTERVAL seconds.
        /// </summary>
        public void startSending()
        {
            timer.Enabled = true;
        }

        /// <summary>
        /// Stops sending heartbeat messages.
        /// </summary>
        public void stopSending()
        {
            timer.Enabled = false;
        }

        /// <summary>
        /// Timer event method.
        /// </summary>
        /// <param name="source"></param>
        /// <param name="e"></param>
        private void onTimedEvent( object source, ElapsedEventArgs e )
        {
            HeartbeatMessage msg = new HeartbeatMessage( localUser.getName(),
                localUser.getDisplayName(), localUser.isAvailable() );
            sendMessage( msg );
        }
    }
}
