
#ifndef __TcpThread__
#define __TcpThread__

// local includes
#include "SocketThread.h"

/******************************************************************************
 * This class represents a single thread that handles communication via a
 * TCP socket.
 *****************************************************************************/
class TcpThread : public SocketThread
{
   public:
      
      /************************************************************************
       * Constructor
       *    ipAddress - tcp ip address
       *    port      - tcp port
       *    isServer  - true if this object is the server, false otherwise
       *    retry     - true if this object should repeatedly try to connect
       *                the socket, false otherwise
       ***********************************************************************/
      TcpThread( const std::string &ipAddress,
                 const int         &port,
                 const bool        &isServer,
                 const bool        &retry = false );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~TcpThread();
      
};

#endif
