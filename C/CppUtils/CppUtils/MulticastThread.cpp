
// local includes
#include "MulticastSocket.h"
#include "MulticastThread.h"

/******************************************************************************
 * 
 *****************************************************************************/
MulticastThread::MulticastThread( const std::string &ipAddress, const int &port ) :
   SocketThread( ipAddress, port )
{
   socket = new MulticastSocket( ipAddress, port, true );
}

/******************************************************************************
 * 
 *****************************************************************************/
MulticastThread::~MulticastThread()
{
   shutdown();
}

