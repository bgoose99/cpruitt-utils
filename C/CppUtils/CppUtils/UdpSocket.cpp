
// system includes
#include <string.h>

// local includes
#include "UdpSocket.h"

/******************************************************************************
 *
 *****************************************************************************/
UdpSocket::UdpSocket( const std::string &myIpAddress,
                      const int         &myPort,
                      const std::string &destIpAddress,
                      const int         &destPort ) :
   Socket( myIpAddress, myPort ),
   destIpAddress( destIpAddress ),
   destPort( destPort )
{
}

/******************************************************************************
 *
 *****************************************************************************/
UdpSocket::~UdpSocket()
{
#ifdef _WIN32
#else
   if( destSock > 0 ) close( destSock );
#endif
}

/******************************************************************************
 *
 *****************************************************************************/
int UdpSocket::send( const char *msg, const int &size )
{
#ifdef _WIN32
   return -1;
#else
   return sendto( destSock, msg, size, 0, (struct sockaddr *)&destAddr, sizeof( destAddr ) );
#endif
}

/******************************************************************************
 *
 *****************************************************************************/
bool UdpSocket::connectSocket()
{
#ifdef _WIN32
   return false;
#else
   // set up our address
   memset( &myAddr, 0, sizeof( myAddr ) );
   myAddr.sin_family = AF_INET;
   myAddr.sin_port   = htons( port );

   int rval = inet_pton( AF_INET, ipAddress.c_str(), &(myAddr.sin_addr) );
   if( rval <= 0 )
   {
      errMsg = "Error initializing socket";
      return false;
   }

   sock = socket( AF_INET, SOCK_DGRAM, IPPROTO_UDP );
   if( sock < 0 )
   {
      errMsg = "Error opening socket";
      return false;
   }

   rval = bind( sock, (struct sockaddr *)&myAddr, sizeof( myAddr ) );
   if( rval < 0 )
   {
      errMsg = "Error binding socket";
      return false;
   }

   // set up destination
   memset( &destAddr, 0, sizeof( destAddr ) );
   destAddr.sin_family = AF_INET;
   destAddr.sin_port = htons( destPort );
   rval = inet_pton( AF_INET, destIpAddress.c_str(), &(destAddr.sin_addr) );
   if( rval <= 0 )
   {
      errMsg = "Error initializing destination socket";
      return false;
   }

   destSock = socket( AF_INET, SOCK_DGRAM, IPPROTO_UDP );
   if( destSock < 0 )
   {
      errMsg = "Error opening destination socket";
      return false;
   }

   return true;
#endif
}
