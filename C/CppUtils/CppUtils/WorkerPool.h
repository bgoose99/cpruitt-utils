
#ifndef __WorkerPool__
#define __WorkerPool__

// system includes
#include <queue>
#include <vector>

// local includes
#include "AbstractThread.h"
#include "Mutex.h"

/******************************************************************************
 * Simple worker interface.
 *****************************************************************************/
class Worker
{
   public:
      
      virtual ~Worker() {}
      virtual void doWork() = 0;
      
   protected:
      
      Worker() {}

   private:
      
      // no copy or assignment
      Worker( const Worker &that ) {}
      Worker &operator=( const Worker &that ) {}
};

/******************************************************************************
 * This class encapsulates one or more threads that execute Workers as needed.
 * IMPORTANT NOTE: This class assumes ownership of all Workers.
 *****************************************************************************/
class WorkerPool
{
   public:
      
      /************************************************************************
       * Constructor
       ***********************************************************************/
      WorkerPool( const unsigned int &threadPoolSize = 1 );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~WorkerPool();
      
      /************************************************************************
       * Adds a worker to the queue that will be serviced as soon as a thread
       * is available for work.
       * NOTE: This pool assumes ownership of the supplied worker. As soon as
       *       the work is completed, 'delete' will be called on the supplied
       *       pointer.
       ***********************************************************************/
      void addWorker( Worker *worker );
      
   private:

      // no copy or assignment
      WorkerPool( const WorkerPool & );
      WorkerPool &operator=( const WorkerPool & );
      
      /************************************************************************
       * Simple worker thread.
       ***********************************************************************/
      class WorkerThread : public AbstractThread
      {
         public:
            
            WorkerThread( WorkerPool &parent );
            virtual ~WorkerThread();
            virtual void threadFunction();
            
         private:
            
            // no copy or assignment
            WorkerThread( const WorkerThread &);
            WorkerThread &operator=( const WorkerThread &);

            WorkerPool &parent;
      };
      
      // thread pool
      std::vector<WorkerThread *> threads;
      
      // workers
      Mutex                workerMutex;
      std::queue<Worker *> workers;
      
      /************************************************************************
       * Stops all worker threads and clears the queue.
       ***********************************************************************/
      void stopWork();
};

#endif
