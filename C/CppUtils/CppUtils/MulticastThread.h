
#ifndef __MulticastThread__
#define __MultecastThread__

// local includes
#include "SocketThread.h"

/******************************************************************************
 * This class represents a single thread that handles a communication via a
 * multicast socket.
 *****************************************************************************/
class MulticastThread : public SocketThread
{
   public:
      
      /************************************************************************
       * Constructor
       *    ipAddress - socket ip address
       *    port      - socket port
       *    logger    - optional logger
       ***********************************************************************/
      MulticastThread( const std::string &ipAddress, const int &port, Logger *logger = 0 );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~MulticastThread();
      
};

#endif
