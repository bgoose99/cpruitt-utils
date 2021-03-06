
#ifndef __AbstractThread__
#define __AbstractThread__

#ifdef _WIN32
#include <thread>
#else
// use pthreads
#include <pthread.h>
#endif

/******************************************************************************
 * This class represents an abstract threaded task.
 *****************************************************************************/
class AbstractThread
{
public:

   /***************************************************************************
    *
    **************************************************************************/
   virtual ~AbstractThread();

   /***************************************************************************
    *
    **************************************************************************/
   void start();

   /***************************************************************************
    *
    **************************************************************************/
   virtual void threadFunction() = 0;

   /***************************************************************************
    *
    **************************************************************************/
   void join();

   /***************************************************************************
    *
    **************************************************************************/
   void shutdown();

protected:

   /***************************************************************************
    *
    **************************************************************************/
   bool isRunning;

   /***************************************************************************
    *
    **************************************************************************/
   AbstractThread();

private:

   // no copy or assignment
   AbstractThread( const AbstractThread & );
   AbstractThread &operator=( const AbstractThread & );

#ifdef _WIN32
   std::thread *thread;
#else
   pthread_t thread;
#endif

#ifndef _WIN32
   static void *threadHelper( void *arg );
#endif
};

#endif
