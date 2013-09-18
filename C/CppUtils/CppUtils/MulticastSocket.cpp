
// system include
#ifdef _WIN32
#else
#include <errno.h>
#include <fcntl.h>
#endif
#include <string.h>

// local includes
#include "MulticastSocket.h"

/*******************************************************************************
 * 
 ******************************************************************************/
MulticastSocket::MulticastSocket( const std::string &ipAddress, const int &port, const bool &blocking ) :
   Socket( ipAddress, port, blocking )
{
}

/*******************************************************************************
 * 
 ******************************************************************************/
MulticastSocket::~MulticastSocket()
{
}

/*******************************************************************************
 * 
 ******************************************************************************/
int MulticastSocket::send( const char *msg, const int &size )
{
   int bytesSent = -1;
   #ifdef _WIN32
   #else
   bytesSent = sendto( sock, msg, size, 0, (struct sockaddr *)&groupAddr, sizeof( groupAddr ) );
   if( bytesSent < 0 && errno == EAGAIN )
   {
      // try to send one more time
      errMsg = "First send failed";
      struct pollfd p;
      p.fd = sock;
      p.events = POLLOUT;
      if( poll( &p, 1, 5 ) > 0 )
         bytesSent = sendto( sock, msg, size, 0, (struct sockaddr *)&groupAddr, sizeof( groupAddr ) );
   }
   #endif
   return bytesSent;
}

/*******************************************************************************
 * 
 ******************************************************************************/
bool MulticastSocket::connectSocket()
{
   #ifdef _WIN32
   return false;
   #else
   sock = socket( AF_INET, SOCK_DGRAM, 0 );
   if( sock < 0 )
   {
      errMsg = "Unable to open socket";
      return false;
   }
   
   u_int i = 1;
   int rval = setsockopt( sock, SOL_SOCKET, SO_REUSEADDR, &i, sizeof( i ) );
   if( rval < 0 )
   {
      errMsg = "Unable to set first socket options";
      return false;
   }
   
   memset( (char*)&myAddr, 0, sizeof( myAddr ) );
   myAddr.sin_addr.s_addr = htonl( INADDR_ANY );
   myAddr.sin_family      = AF_INET;
   myAddr.sin_port        = htons( port );
   
   rval = bind( sock, (struct sockaddr *)&myAddr, sizeof( myAddr ) );
   if( rval < 0 )
   {
      errMsg = "Unable to bind socket";
      return false;
   }
   
   memset( (char*)&groupAddr, 0, sizeof( groupAddr ) );
   groupAddr.sin_addr.s_addr = inet_addr( ipAddress.c_str() );
   groupAddr.sin_family      = AF_INET;
   groupAddr.sin_port        = htons( port );
   
   struct ip_mreq mreq;
   mreq.imr_multiaddr.s_addr = groupAddr.sin_addr.s_addr;
   mreq.imr_interface.s_addr = myAddr.sin_addr.s_addr;
   rval = setsockopt( sock, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof( mreq ) );
   if( rval < 0 )
   {
      errMsg = "Unable to set second socket options";
      return false;
   }
   
   i = 16;
   rval = setsockopt( sock, IPPROTO_IP, IP_MULTICAST_TTL, &i, sizeof( i ) );
   if( rval < 0 )
   {
      errMsg = "Unable to set TTL";
      return false;
   }
   
   int flags;
   if( ( flags = fcntl( sock, F_GETFL< 0 ) ) == -1 ) flags = 0;
   flags = blocking ? ( flags & (~O_NONBLOCK) ) : ( flags | O_NONBLOCK );
   fcntl( sock, F_SETFL, flags );
   
   return true;
   #endif
}
