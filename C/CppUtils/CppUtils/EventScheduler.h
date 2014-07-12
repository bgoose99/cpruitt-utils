
#ifndef __EventScheduler__
#define __EventScheduler__

// system includes
#include <map>
#include <vector>

// local includes
#include "AbstractThread.h"
#include "Mutex.h"

/*******************************************************************************
 * Simple delegate interface.
 ******************************************************************************/
class Delegate
{
   public:
      
      virtual void invoke() = 0;
      
   protected:
      
      Delegate() {}
      
      virtual ~Delegate() {}
      
   private:
      
      // no copy or assignment
      Delegate( const Delegate &that ) {}
      Delegate &operator=( const Delegate &that ) {}
};

/*******************************************************************************
 * This class encapsulates one or more threads that process a queue of events.
 * It is meant to be a fire-and-forget utility class that allows the user to
 * schedule arbitrary events at some point in the future.
 ******************************************************************************/
class EventScheduler
{
   public:
      
      /************************************************************************
       * Constructor
       ***********************************************************************/
      EventScheduler( const unsigned int &threadPoolSize = 1 );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~EventScheduler();
      
      /************************************************************************
       * Add a one-shot event.
       *  - delegate  : the delegate to be called
       *  - firstTime : the time from now (in milliseconds) at which the 
       *                delegate will be called
       ***********************************************************************/
      void addEvent( Delegate *delegate, const long &firstTime );
      
      /************************************************************************
       * Add an event that will be called indefinitely.
       *  - delegate  : the delegate to be called
       *  - firstTime : the time from now (in milliseconds) at which the 
       *                delegate will first be called
       *  - interval  : the interval (in milliseconds) at which the delegate
       *                will be called
       ***********************************************************************/
      void addInfiniteEvent( Delegate *delegate, const long &firstTime, const long &interval );
      
      /************************************************************************
       * Add a recurring event.
       *  - delegate  : the delegate to be called
       *  - firstTime : the time from now (in milliseconds) at which the 
       *                delegate will be called
       *  - interval  : the interval (in milliseconds) at which the delegate
       *                will be called
       *  - maxTime   : maximum time from now (in milliseconds) at which the
       *                delegate will be called
       ***********************************************************************/
      void addRecurringEvent( Delegate *delegate, const long &firstTime, const long &interval, const long &maxTime );
      
      /************************************************************************
       * Removes any events with the specified delegate from the queue.
       ***********************************************************************/
      void removeEvent( Delegate *delegate );
      
      /************************************************************************
       * Cancels all scheduled events.
       ***********************************************************************/
      void cancelAllEvents();
      
   private:
      
      enum EventType { EVENT_SINGLE, EVENT_RECURRING, EVENT_INFINITE };
      
      /************************************************************************
       * Simple event struct.
       ***********************************************************************/
      struct Event
      {
         EventType type;
         Delegate *delegate;
         long nextTime;
         long maxTime;
         long interval;
      };
      
      /************************************************************************
       * Simple thread for servicing the event queue.
       ***********************************************************************/
      class QueueServicer : public AbstractThread
      {
         public:
            QueueServicer( EventScheduler &parent );
            virtual ~QueueServicer();
            virtual void threadFunction();
         private:
            EventScheduler &parent;
      };
      
      std::vector<QueueServicer *> threads;
      
      Mutex                      eventMutex;
      std::multimap<long, Event> events;
};

#endif
