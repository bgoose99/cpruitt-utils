

// local includes
#include "UdpSocket.h"
#include "UdpThread.h"

/******************************************************************************
 * 
 *****************************************************************************/
UdpThread::UdpThread( const std::string &myIpAddress,
                      const int         &myPort,
                      const std::string &destIpAddress,
                      const int         &destPort,
                      Logger            *logger ) :
   SocketThread( myIpAddress, myPort, logger )
{
   socket = new UdpSocket( myIpAddress, myPort, destIpAddress, destPort );
}

/******************************************************************************
 * 
 *****************************************************************************/
UdpThread::~UdpThread()
{
}
