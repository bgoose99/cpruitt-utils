
#ifndef __Logger__
#define __Logger__

// system includes
#include <iostream>
#include <fstream>
#include <string>

// local includes
#include "Mutex.h"

/******************************************************************************
 * This class represents a single, thread-safe log file.
 *****************************************************************************/
class Logger
{
   public:
      
      /************************************************************************
       * Constructor
       ***********************************************************************/
      Logger( const std::string &filename );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~Logger();
      
      /************************************************************************
       * Logs a single message.
       ***********************************************************************/
      void log( const std::string &msg, const std::string &prefix = "Unknown", const double &timestamp = 0.0 );
      
   private:
      
      // no copy or assignment
      Logger( const Logger & );
      Logger &operator=( const Logger & );
      
      const static int TIME_PAD;
      const static int PREF_PAD;
      
      std::ofstream out;
      Mutex         mutex;
      
};

#endif
