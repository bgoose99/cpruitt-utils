
// system includes

// local includes
#include "SocketThread.h"
#include "StringUtils.h"

using namespace StringUtils;

/******************************************************************************
 * 
 *****************************************************************************/
SocketThread::SocketThread( const std::string &ipAddress, const int &port, const bool &retry ) :
   retry( retry ),
   socket( 0 )
{
}

/******************************************************************************
 * 
 *****************************************************************************/
SocketThread::~SocketThread()
{
   shutdown();
   if( socket != 0 ) delete socket;
}

/******************************************************************************
 * 
 *****************************************************************************/
void SocketThread::threadFunction()
{
   while( isRunning )
   {
      if( !socket->connect() )
      {
         if( retry )
         {
            #ifdef _WIN32
            Sleep( 1000 );
            #else
            sleep( 1 );
            #endif
            continue;
         }
         else shutdown();
      }
      else
      {
         break;
      }
   }
   
   // message loop
   int bytesReceived;
   #ifdef _WIN32
   char buffer[1];
   #else
   char buffer[IP_MAXPACKET];
   #endif
   while( isRunning )
   {
      bytesReceived = socket->recv( buffer, sizeof( buffer ), 1 );
      if( bytesReceived > 0 )
         recvMessage( buffer, bytesReceived );
   }
}

/******************************************************************************
 * 
 *****************************************************************************/
int SocketThread::sendMessage( const char *buf, const int &size )
{
   if( isRunning )
   {
      return socket->send( buf, size );
   }
   return -1;
}

