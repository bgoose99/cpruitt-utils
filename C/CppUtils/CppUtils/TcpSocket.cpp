
// system includes
#ifdef _WIN32
#else
#include <fcntl.h>
#include <arpa/inet.h>
#endif
#include <memory.h>

// local includes
#include "TcpSocket.h"

/******************************************************************************
 * 
 *****************************************************************************/
TcpSocket::TcpSocket( const std::string &ipAddress,
                      const int         &port,
                      const bool        &isServer,
                      const bool        &retry ) :
   Socket( ipAddress, port ),
   isServer( isServer ),
   retry( retry )
{
}

/******************************************************************************
 * 
 *****************************************************************************/
TcpSocket::~TcpSocket()
{
}

/******************************************************************************
 * 
 *****************************************************************************/
bool TcpSocket::connectSocket()
{
   return isServer ? connectAsServer() : connectAsClient();
}

/******************************************************************************
 * 
 *****************************************************************************/
bool TcpSocket::connectAsServer()
{
   #ifdef _WIN32
   return false;
   #else
   struct sockkaddr_in myAddr;
   struct in_addr      clientAddr;
   socklen_t           clientLen = sizeof( clientAddr );
   int tempSock = socket( AF_INET, SOCK_STREAM, IPPROTO_TCP );
   if( tempSock < 0 )
   {
      errMsg = "Error creating socket";
      return false;
   }
   
   memset( &myAddr, 0, sizeof( myAddr ) );
   myAddr.sin_addr.s_addr = htonl( INADDR_ANY );
   myAddr.sin_family      = AF_INET;
   myAddr.sin_port        = htons( port );
   
   if( bind( tempSock, (struct sockaddr *)&myAddr, sizeof( myAddr ) ) < 0 )
   {
      errMsg = "Error binding socket";
      return false;
   }
   
   if( listen( tempSock, 1024 ) < 0 )
   {
      errMsg = "Error listening on socket";
      return false;
   }
   
   int flags;
   if( ( flags = fcntl( tempSock, F_GETFL, 0 ) ) == -1 ) flags = 0;
   fcntl( tempSock, F_SETFL, flags | O_NONBLOCK );
   
   while( 1 )
   {
      sock = accept( tempSock, (struct sockaddr *)&clientAddr, &clientLen );
      if( sock >= 0 ) break;
      sleep( 1 );
   }
   
   return true;
   }
   #endif
}

/******************************************************************************
 * 
 *****************************************************************************/
bool TcpSocket::connectAsClient()
{
   #ifdef _WIN32
   return false;
   #else
   struct sockaddr_in clientAddr;
   sock = socket( AF_INET, SOCK_STREAM, IPPROTO_TCP );
   if( sock < 0 )
   {
      errMsg = "Error creating socket";
      return false;
   }
   
   memset( (char*)&clientAddr, 0, sizeof( clientAddr ) );
   clientAddr.sin_addr.s_addr = htonl( INADDR_ANY );
   clientAddr.sin_family      = AF_INET;
   clientAddr.sin_port        = htons( port );
   
   if( ::connect( sock, (struct sockaddr *)&clientAddr, sizeof( clientAddr ) ) < 0 )
   {
      errMsg = "Error connecting on socket";
      return false;
   }
   
   return true;
   #endif
}

