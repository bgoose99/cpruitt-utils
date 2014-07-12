
#ifndef __UdpSocket__
#define __UdpSocket__

// system includes
#ifdef _WIN32
// TODO: implement
#else
#include <arpa/inet.h>
#endif

// local includes
#include "Socket.h"

/******************************************************************************
 * UDP socket class.
 *****************************************************************************/
class UdpSocket : public Socket
{
   public:
      
      /************************************************************************
       * 
       ***********************************************************************/
      UdpSocket( const std::string &myIpAddress,
                 const int         &myPort,
                 const std::string &destIpAddress,
                 const int         &destPort );
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual ~UdpSocket();
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual int send( const char *msg, const int &size );
   
   protected:
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual bool connectSocket();
   
   private:

      // no copy or assignment
      UdpSocket( const UdpSocket & );
      UdpSocket &operator=( const UdpSocket & );
      
      std::string destIpAddress;
      int destPort;
      int destSock;
      
      #ifdef _WIN32
      #else
      struct sockaddr_in myAddr;
      struct sockaddr_in destAddr;
      #endif
};

#endif
