
#ifndef __SocketThread__
#define __SocketThread__

// system includes
#include <string>

// local includes
#include "AbstractThread.h"
#include "Logger.h"
#include "Socket.h"

/******************************************************************************
 * This abstract class represents a single thread that handles communication
 * over a generic socket.
 *****************************************************************************/
class SocketThread : public AbstractThread
{
   public:
      
      /************************************************************************
       * Constructor
       *    ipAddress - socket ip address
       *    port      - socket port
       *    logger    - optional logger
       *    retry     - true if this object should repeatedly try to connect
       *                the socket, false otherwise
       ***********************************************************************/
      SocketThread( const std::string &ipAddress, const int &port, Logger *logger = 0, const bool &retry = false );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~SocketThread();
      
      /************************************************************************
       * Thread function.
       ***********************************************************************/
      virtual void threadFunction();
      
      /************************************************************************
       * Abstract message receive.
       ***********************************************************************/
      virtual void recvMessage( const char *buf, const int &size ) = 0;
      
      /************************************************************************
       * Sends a message.
       ***********************************************************************/
      virtual void sendMessage( const char *buf, const int &size );
      
   protected:
      
      bool    retry;
      Socket *socket;
      Logger *logger;
      
};

#endif
