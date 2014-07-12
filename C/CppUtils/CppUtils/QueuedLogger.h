
#ifndef __QueuedLogger__
#define __QueuedLogger__

// system includes
#include <iostream>
#include <fstream>
#include <queue>
#include <string>

// local includes
#include "AbstractThread.h"
#include "Condition.h"
#include "Mutex.h"

/******************************************************************************
 * This class represents a single log file. In order to separate potentially
 * costly disk IO from processing, logged messages are put in a queue and
 * processed in a separate thread. This class can be used when faster logging
 * calls are needed to increase performance.
 *****************************************************************************/
class QueuedLogger : public AbstractThread
{
   public:
      
      /************************************************************************
       * Constructor
       *    filename         - name of the log file
       *    queueThreshold   - how large the queue should get before being
       *                       processed
       *    msgsPerIteration - how many messages are pulled from the queue at
       *                       one time (this allows calling threads to place
       *                       more items in the queue while it is being 
       *                       processed)
       *    condWaitMsec     - how long the underlying thread should wait for
       *                       the condition signal
       * NOTE: This thread starts itself upon successful creation.
       ***********************************************************************/
      explicit QueuedLogger( const std::string &filename,
                             const unsigned int &queueThreshold   = 20,
                             const unsigned int &msgsPerIteration = 5,
                             const unsigned int &condWaitMsec     = 100 );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~QueuedLogger();
      
      /************************************************************************
       * Thread function.
       ***********************************************************************/
      virtual void threadFunction();
      
      /************************************************************************
       * Queue a single message for logging.
       ***********************************************************************/
      void log( const std::string &msg, const std::string &prefix = "Unknown", const double &timestamp = 0.0 );
      
   private:

      // no copy or assignment
      QueuedLogger( const QueuedLogger & );
      QueuedLogger &operator=( const QueuedLogger & );
      
      const static int TIME_PAD;
      const static int PREF_PAD;
      
      std::ofstream           out;
      Mutex                   queueMutex;
      Condition               condition;
      std::queue<std::string> msgQueue;
      const unsigned int      queueThreshold;
      const unsigned int      msgsPerIteration;
      const unsigned int      condWaitMsec;
      
      /************************************************************************
       * Empties the message queue.
       ***********************************************************************/
      void emptyQueue();
      
};

#endif
