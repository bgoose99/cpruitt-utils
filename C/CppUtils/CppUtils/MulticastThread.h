
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

   /***************************************************************************
    * Constructor
    *    ipAddress - socket ip address
    *    port      - socket port
    **************************************************************************/
   MulticastThread( const std::string &ipAddress, const int &port );

   /***************************************************************************
    * Destructor
    **************************************************************************/
   virtual ~MulticastThread();

private:

   // no copy or assignment
   MulticastThread( const MulticastThread & );
   MulticastThread &operator=( const MulticastThread & );

};

#endif
