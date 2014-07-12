
#ifndef __QueuedBinaryLogger__
#define __QueuedBinaryLogger__

// system includes
#include <iostream>
#include <fstream>
#include <memory.h>
#include <queue>

// local includes
#include "AbstractThread.h"
#include "Condition.h"
#include "Mutex.h"

/******************************************************************************
 * This class represents a single binary log file. In order to separate 
 * potentially costly disk IO from processing, logged messages are put in a 
 * queue and processed in a separate thread. This class can be used when faster
 * logging calls are needed to increase performance.
 *****************************************************************************/
class QueuedBinaryLogger : public AbstractThread
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
      explicit QueuedBinaryLogger( const std::string &filename,
                                   const unsigned int &queueThreshold   = 20,
                                   const unsigned int &msgsPerIteration = 5,
                                   const unsigned int &condWaitMsec     = 100 );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~QueuedBinaryLogger();
      
      /************************************************************************
       * Thread function
       ***********************************************************************/
      virtual void threadFunction();
      
      /************************************************************************
       * Queues a single message for logging.
       ***********************************************************************/
      void log( const char *msg, const int &size );
      
   private:

      // no copy or assignment
      QueuedBinaryLogger( const QueuedBinaryLogger & );
      QueuedBinaryLogger &operator=( const QueuedBinaryLogger & );
      
      // simple message class to facilitate copying individual messages
      class BinaryMessage
      {
         public:
            
            BinaryMessage( const char *message, const int &size );
            ~BinaryMessage();
            const char *getMessage() const;
            const int getSize() const;
            
         private:
            
            char *msg;
            int size;
            
      };
      
      std::ofstream               out;
      Mutex                       queueMutex;
      Condition                   condition;
      std::queue<BinaryMessage *> msgQueue;
      const unsigned int          queueThreshold;
      const unsigned int          msgsPerIteration;
      const unsigned int          condWaitMsec;
      
      /************************************************************************
       * Empties the message queue.
       ***********************************************************************/
      void emptyQueue();
      
};

#endif
