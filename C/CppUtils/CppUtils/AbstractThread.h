
#ifndef __AbstractThread__
#define __AbstractThread__

#ifdef _WIN32
// use windows threads
#include <windows.h>
#else
// use pthreads
#include <pthread.h>
#endif

/*******************************************************************************
 * This class represents an abstract threaded task.
 ******************************************************************************/
class AbstractThread
{
   public:
      virtual ~AbstractThread();
      void start();
      virtual void threadFunction() = 0;
      void join();
      void shutdown();
   protected:
      bool isRunning;
      AbstractThread();
   private:
      #ifdef _WIN32
      HANDLE thread;
      DWORD threadId;
      #else
      pthread_t thread;
      #endif
      
      #ifdef _WIN32
      static DWORD WINAPI threadHelper( LPVOID arg );
      #else
      static void *threadHelper( void *arg );
      #endif
};

#endif
