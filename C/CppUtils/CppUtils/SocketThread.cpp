
// system includes

// local includes
#include "SocketThread.h"
#include "StringUtils.h"

using namespace StringUtils;

/******************************************************************************
 * 
 *****************************************************************************/
SocketThread::SocketThread( const std::string &ipAddress, const int &port, Logger *logger, const bool &retry ) :
   retry( retry ),
   socket( NULL ),
   logger( logger )
{
}

/******************************************************************************
 * 
 *****************************************************************************/
SocketThread::~SocketThread()
{
   shutdown();
   if( socket != NULL ) delete socket;
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
         if( logger != NULL )
         {
            logger->log( "SocketThread", "Unable to connect on ip " + socket->getIpAddress() + ", port " + toString( socket->getPort() ) );
            logger->log( "SocketThread", "Error from Socket: " + socket->getErrorMessage() );
         }
         
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
         if( logger != NULL )
            logger->log( "SocketThread", "Connected on ip " + socket->getIpAddress() + ", port " + toString( socket->getPort() ) + ", socket " + toString( socket->getSocket() ) );
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
void SocketThread::sendMessage( const char *buf, const int &size )
{
   if( isRunning )
   {
      int bytesSent = socket->send( buf, size );
      if( bytesSent < 0 )
      {
         if( logger != NULL )
            logger->log( "SocketThread", "Unable to send message (" + toString( size ) + " bytes)" );
      }
      else if( bytesSent != size )
      {
         if( logger != NULL )
            logger->log( "SocketThread", "Sent " + toString( bytesSent ) + " bytes; should have sent " + toString( size ) + " bytes." );
      }
      else
      {
         if( logger != NULL )
            logger->log( "SocketThread", "Sent " + toString( bytesSent ) + " bytes." );
      }
   }
}

