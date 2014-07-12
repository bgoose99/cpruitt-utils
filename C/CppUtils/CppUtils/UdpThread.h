
#ifndef __UdpThread__
#define __UdpThread__

// local includes
#include "SocketThread.h"

/******************************************************************************
 * This class represents a single thread that handles a communication via a
 * UDP socket.
 *****************************************************************************/
class UdpThread : public SocketThread
{
   public:
      
      /************************************************************************
       * Constructor
       *    myIpAddress   - sender ip address
       *    myPort        - sender port
       *    destIpAddress - destination ip address
       *    destPort      - destination port
       ***********************************************************************/
      UdpThread( const std::string &myIpAddress,
                 const int         &myPort,
                 const std::string &destIpAddress,
                 const int         &destPort );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~UdpThread();
      
};

#endif
