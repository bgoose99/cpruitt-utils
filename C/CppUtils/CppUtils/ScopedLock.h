
#ifndef __ScopedLock__
#define __ScopedLock__

#include "Mutex.h"

/******************************************************************************
 * This class provides a wrapper around a Mutex, assuring that the state of
 * the Mutex is always well-defined. Specifically, when a ScopedLock object
 * goes out of scope and is destroyed, the Mutex it wraps is unlocked, if
 * necessary.
 *****************************************************************************/
class ScopedLock
{
   public:
      
      /************************************************************************
       * Constructor
       ***********************************************************************/
      explicit ScopedLock( Mutex &mutex, bool lockMutex = true );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~ScopedLock();
      
      /************************************************************************
       * Locks the mutex.
       ***********************************************************************/
      void lock();
      
      /************************************************************************
       * Unlocks the mutex.
       ***********************************************************************/
      void unlock();
      
   private:

      // no copy or assignment
      ScopedLock( const ScopedLock & );
      ScopedLock &operator=( const ScopedLock & );
      
      Mutex &mutex;
      bool   locked;
      
};

#endif
