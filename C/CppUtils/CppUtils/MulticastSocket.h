
#ifndef __MulticastSocket__
#define __MulticastSocket__

// system includes
#ifdef _WIN32
// TODO: implement
#else
#include <arpa/inet.h>
#endif

// local includes
#include "Socket.h"

/******************************************************************************
 * Multicast socket class.
 *****************************************************************************/
class MulticastSocket : public Socket
{
public:

   /***************************************************************************
    *
    **************************************************************************/
   MulticastSocket( const std::string &ipAddress, const int &port, const bool &blocking = false );

   /***************************************************************************
    *
    **************************************************************************/
   virtual ~MulticastSocket();

   /***************************************************************************
    *
    **************************************************************************/
   virtual int send( const char *msg, const int &size );

   /***************************************************************************
    *
    **************************************************************************/
   virtual int recv( char *buffer, const int &size, int waitMs = 0 );

protected:

   /***************************************************************************
    *
    **************************************************************************/
   virtual bool connectSocket();

private:

   // no copy or assignment
   MulticastSocket( const MulticastSocket & );
   MulticastSocket &operator=( const MulticastSocket & );

#ifdef _WIN32
#else
   struct sockaddr_in myAddr;
   socklen_t          myAddrLen;
   struct sockaddr_in groupAddr;
#endif
};

#endif
