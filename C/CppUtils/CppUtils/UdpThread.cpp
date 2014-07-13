
// local includes
#include "UdpSocket.h"
#include "UdpThread.h"

/******************************************************************************
 * 
 *****************************************************************************/
UdpThread::UdpThread( const std::string &myIpAddress,
                      const int         &myPort,
                      const std::string &destIpAddress,
                      const int         &destPort ) :
   SocketThread( myIpAddress, myPort )
{
   socket = new UdpSocket( myIpAddress, myPort, destIpAddress, destPort );
}

/******************************************************************************
 * 
 *****************************************************************************/
UdpThread::~UdpThread()
{
   shutdown();
}
