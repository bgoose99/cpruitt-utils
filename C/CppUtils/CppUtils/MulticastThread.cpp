
// local includes
#include "MulticastSocket.h"
#include "MulticastThread.h"

/******************************************************************************
 * 
 *****************************************************************************/
MulticastThread::MulticastThread( const std::string &ipAddress, const int &port, Logger *logger ) :
   SocketThread( ipAddress, port, logger )
{
   socket = new MulticastSocket( ipAddress, port, true );
}

/******************************************************************************
 * 
 *****************************************************************************/
MulticastThread::~MulticastThread()
{
}

