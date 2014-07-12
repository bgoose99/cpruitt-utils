
// local includes
#include "TcpSocket.h"
#include "TcpThread.h"

/******************************************************************************
 * 
 *****************************************************************************/
TcpThread::TcpThread( const std::string &ipAddress,
                      const int         &port,
                      const bool        &isServer,
                      const bool        &retry ) :
   SocketThread( ipAddress, port, retry )
{
   socket = new TcpSocket( ipAddress, port, isServer, retry );
}

/******************************************************************************
 * 
 *****************************************************************************/
TcpThread::~TcpThread()
{
   shutdown();
}

