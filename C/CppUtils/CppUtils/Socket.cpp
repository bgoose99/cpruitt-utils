
// system includes
#ifdef _WIN32
#include <winsock.h>
#else
#include <netinet/ip.h>
#endif

// local includes
#include "Socket.h"

/******************************************************************************
 *
 *****************************************************************************/
Socket::Socket( const std::string &ipAddress, const int &port, const bool &blocking ) :
   ipAddress( ipAddress ),
   errMsg( "None" ),
   port( port ),
   sock( -1 ),
   blocking( blocking )
{
#ifdef _WIN32
   errMsg = "Windows sockets not supported yet.";
#else
   pollIn.events = POLLIN;
   recvFlag = ( blocking ? MSG_WAITALL : MSG_DONTWAIT );
#endif
}

/******************************************************************************
 *
 *****************************************************************************/
Socket::~Socket()
{
   disconnect();
}

/******************************************************************************
 *
 *****************************************************************************/
bool Socket::connect()
{
   bool connected = connectSocket();
#ifdef _WIN32
   return false;
#else
   if( connected ) pollIn.fd = sock;
#endif
   return connected;
}

/******************************************************************************
 *
 *****************************************************************************/
void Socket::disconnect()
{
#ifdef _WIN32
#else
   if( sock > 0 ) close( sock );
#endif
}

/******************************************************************************
 *
 *****************************************************************************/
int Socket::send( const char *msg, const int &size )
{
#ifdef _WIN32
   return -1;
#else
   return ::send( sock, msg, size, 0 );
#endif
}

/******************************************************************************
 *
 *****************************************************************************/
int Socket::recv( char *buffer, const int &size, int waitMs )
{
#ifdef _WIN32
   return 0;
#else
   if( poll( &pollIn, 1, waitMs ) > 0 )
      return recvfrom( sock, buffer, size, recvFlag, 0, 0 );
#endif
}

