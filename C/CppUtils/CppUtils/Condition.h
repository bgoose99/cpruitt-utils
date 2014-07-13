
#ifndef __Condition__
#define __Condition__

// local includes
#include "Mutex.h"

/******************************************************************************
 * Generic condition wrapper.
 *****************************************************************************/
class Condition
{
   public:
      
      /************************************************************************
       * Constructor
       ***********************************************************************/
      Condition( Mutex &mutex );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~Condition();
      
      /************************************************************************
       * Waits for the condition to be met.
       ************************************************************************/
      void wait();
      
      /*************************************************************************
       * Waits the specified number of milliseconds for the condition to be met.
       ************************************************************************/
      void wait( const int &milliseconds );
      
      /*************************************************************************
       * Signals all waiting threads (broadcast) that the condition is met.
       ************************************************************************/
      void signal();
      
      /*************************************************************************
       * Resets the condition.
       ************************************************************************/
      void reset();
      
      /*************************************************************************
       * Returns true if the condition is met, false otherwise.
       * NOTE: Implementers should call this upon wakeup due to the possibility
       *       of spurious wakeups.
       ************************************************************************/
      bool isMet() const;
   
   private:
      
      // no copy or assignment
      Condition( const Condition & );
      Condition &operator=( const Condition & );
      
      Mutex &mutex;
      
      #ifdef _WIN32
      //CONDITION_VARIABLE condition;
      //CRITICAL_SECTION criticalSection;
      #else
      pthread_cond_t condition;
      #endif
      
      bool myCondition;
      
};

#endif
