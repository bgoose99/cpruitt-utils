
#ifndef __Mutex__
#define __Mutex__

#ifdef _WIN32
#include <windows.h>
#else
#include <pthread.h>
#endif

/*******************************************************************************
 * Generic mutex wrapper.
 ******************************************************************************/
class Mutex
{
   public:
      
      /************************************************************************
       * Constructor
       ***********************************************************************/
      Mutex();
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~Mutex();
      
      /************************************************************************
       * Locks this mutex.
       ***********************************************************************/
      virtual void lock();
      
      /************************************************************************
       * Unlocks this mutex.
       ***********************************************************************/
      virtual void unlock();
      
   private:
      
      #ifdef _WIN32
      HANDLE mutex;
      #else
      pthread_mutex_t mutex;
      #endif
      
      // no copy constructor or assignment
      Mutex( const Mutex &mutex );
      Mutex &operator=( const Mutex &mutex );
      
      friend class Condition;
};

#endif
