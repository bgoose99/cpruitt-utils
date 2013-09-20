
// system includes
#ifdef _WIN32
#include <windows.h>
#else
#include <sys/time.h>
#include <unistd.h>
#endif
#include <utility>

// local includes
#include "EventScheduler.h"
#include "ScopedLock.h"

using namespace std;

/*******************************************************************************
 * 
 ******************************************************************************/
EventScheduler::EventScheduler( const unsigned int &threadPoolSize )
{
   for( unsigned int i = 0; i < ( threadPoolSize < 1 ? 1 : threadPoolSize ); i++ )
   {
      QueueServicer *t = new QueueServicer( *this );
      threads.push_back( t );
   }
}

/*******************************************************************************
 * 
 ******************************************************************************/
EventScheduler::~EventScheduler()
{
   for( unsigned int i = 0; i < threads.size(); i++ )
   {
      threads[i]->shutdown();
      threads[i]->join();
      delete threads[i];
   }
   threads.clear();
   
   cancelAllEvents();
}

/*******************************************************************************
 * 
 ******************************************************************************/
void EventScheduler::addEvent( Delegate *delegate, const long &firstTime )
{
   long now = getSystemMillis();
   Event e;
   e.type     = EVENT_SINGLE;
   e.delegate = delegate;
   e.nextTime = now + ( firstTime < 0 ? 0 : firstTime );
   
   ScopedLock lock( eventMutex );
   events.insert( make_pair( e.nextTime, e ) );
   
   return;
}

/*******************************************************************************
 * 
 ******************************************************************************/
void EventScheduler::addInfiniteEvent( Delegate *delegate, const long &firstTime, const long &interval )
{
   long now = getSystemMillis();
   Event e;
   e.type     = EVENT_INFINITE;
   e.delegate = delegate;
   e.nextTime = now + ( firstTime < 0 ? 0 : firstTime );
   e.interval = ( interval <= 0 ? 1 : interval );
   
   ScopedLock lock( eventMutex );
   events.insert( make_pair( e.nextTime, e ) );
   
   return;
}

/*******************************************************************************
 * 
 ******************************************************************************/
void EventScheduler::addRecurringEvent( Delegate *delegate, const long &firstTime, const long &interval, const long &maxTime )
{
   long now = getSystemMillis();
   Event e;
   e.type     = EVENT_RECURRING;
   e.delegate = delegate;
   e.nextTime = now + ( firstTime < 0 ? 0 : firstTime );
   e.interval = ( interval <= 0 ? 1 : interval );
   e.maxTime  = now + maxTime;
   
   ScopedLock lock( eventMutex );
   events.insert( make_pair( e.nextTime, e ) );
   
   return;
}

/*******************************************************************************
 * 
 ******************************************************************************/
void EventScheduler::removeEvent( Delegate *delegate )
{
   ScopedLock lock( eventMutex );
   std::multimap<long, Event>::iterator iter = events.begin();
   for( ; iter != events.end(); ++iter )
   {
      if( iter->second.delegate == delegate ) events.erase( iter );
   }
}

/*******************************************************************************
 * 
 ******************************************************************************/
void EventScheduler::cancelAllEvents()
{
   ScopedLock lock( eventMutex );
   events.clear();
}

/*******************************************************************************
 * 
 ******************************************************************************/
long EventScheduler::getSystemMillis()
{
   #ifdef _WIN32
   static FILETIME ft;
   static long long now;
   GetSystemTimeAsFileTime( &ft );
   now = (LONGLONG)ft.dwLowDateTime + ((LONGLONG)(ft.dwHighDateTime) << 32LL);
   return (long)( now / 10000 );
   #else
   static struct timeval tv;
   gettimeofday( &tv, NULL );
   return ( tv.tv_usec + ( tv.tv_sec * 1000000 ) ) / 1000;
   #endif
}

/*******************************************************************************
 * 
 ******************************************************************************/
EventScheduler::QueueServicer::QueueServicer( EventScheduler &parent ) :
   parent( parent )
{
   start();
}

/*******************************************************************************
 * 
 ******************************************************************************/
EventScheduler::QueueServicer::~QueueServicer()
{
   shutdown();
   join();
}

/*******************************************************************************
 * 
 ******************************************************************************/
void EventScheduler::QueueServicer::threadFunction()
{
   std::multimap<long, Event>::iterator iter;
   long now;
   ScopedLock lock( parent.eventMutex, false );
   
   while( isRunning )
   {
      lock.lock();
      iter = parent.events.begin();
      if( iter != parent.events.end() )
      {
         now = parent.getSystemMillis();
         if( now >= iter->first )
         {
            Event e = iter->second;
            parent.events.erase( iter );
            
            switch( e.type )
            {
               case EVENT_RECURRING:
                  {
                     e.nextTime += e.interval;
                     if( e.nextTime < e.maxTime ) parent.events.insert( make_pair( e.nextTime, e ) );
                  }
                  break;
               case EVENT_INFINITE:
                  {
                     e.nextTime += e.interval;
                     parent.events.insert( make_pair( e.nextTime, e ) );
                  }
                  break;
            }
            lock.unlock();
            
            e.delegate->invoke();
         }
      }
      lock.unlock();
      
      #ifdef _WIN32
      Sleep( 1 );
      #else
      usleep( 1000 );
      #endif
   }
}


