
// local includes
#include "TcpSocket.h"
#include "TcpThread.h"

/******************************************************************************
 * 
 *****************************************************************************/
TcpThread::TcpThread( const std::string &ipAddress,
                      const int         &port,
                      const bool        &isServer,
                      Logger            *logger,
                      const bool        &retry ) :
   SocketThread( ipAddress, port, logger, retry )
{
   socket = new TcpSocket( ipAddress, port, isServer, retry );
}

/******************************************************************************
 * 
 *****************************************************************************/
TcpThread::~TcpThread()
{
}

