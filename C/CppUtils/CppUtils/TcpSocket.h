
#ifndef __TcpSocket__
#define __TcpSocket__

// local includes
#include "Socket.h"

/******************************************************************************
 * TCP socket class.
 *****************************************************************************/
class TcpSocket : public Socket
{
   public:
      
      /************************************************************************
       * 
       ***********************************************************************/
      TcpSocket( const std::string &ipAddress,
                 const int         &port,
                 const bool        &isServer,
                 const bool        &retry = false );
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual ~TcpSocket();
   
   protected:
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual bool connectSocket();
      
   private:
      
      bool isServer;
      bool retry;
      
      /************************************************************************
       * 
       ***********************************************************************/
      bool connectAsServer();
      
      /************************************************************************
       * 
       ***********************************************************************/
      bool connectAsClient();
};

#endif