
#ifndef __EventHandler__
#define __EventHandler__

// system includes
#include <vector>

// local includes
#include "Callback.h"
#include "Mutex.h"
#include "ScopedLock.h"

/******************************************************************************
 * EventHandler is a templated class that maintains a list of callbacks. These
 * can either be static function or member methods. When an event is signaled,
 * all registered callbacks are notified. Listeners can be added and removed
 * via several overloaded methods.
 *****************************************************************************/
template <typename Event>
class EventHandler
{
public:

   /***************************************************************************
    * Constructor
    **************************************************************************/
   EventHandler() {}

   /***************************************************************************
    * Destructor
    **************************************************************************/
   virtual ~EventHandler() { removeAllListeners(); }

   /***************************************************************************
    * Adds the supplied callback to the list of listeners.
    **************************************************************************/
   void addListener( Callback<void, Event> listener )
   {
      ScopedLock lock( listenerMutex );
      listeners.push_back( listener );
   }

   /***************************************************************************
    * Convenience overload that adds the supplied static function to the list
    * of listeners.
    **************************************************************************/
   void addListener( void( *function )( const Event & ) )
   {
      addListener( Callback<void, Event>( function ) );
   }

   /***************************************************************************
    * Convenience overload that adds the supplied member method to the list
    * of listeners.
    **************************************************************************/
   template <typename T, typename Method>
   void addListener( T *object, Method method )
   {
      addListener( Callback<void, Event>( object, method ) );
   }

   /***************************************************************************
    * Removes the supplied callback from the list of listeners.
    **************************************************************************/
   void removeListener( Callback<void, Event> listener )
   {
      ScopedLock lock( listenerMutex );
      listeners.erase( std::remove( listeners.begin(), listeners.end(), listener ), listeners.end() );
   }

   /***************************************************************************
    * Convenience overload that removes the supplied static function from the
    * list of listeners.
    **************************************************************************/
   void removeListener( void( *function )( const Event & ) )
   {
      removeListener( Callback<void, Event>( function ) );
   }

   /***************************************************************************
    * Convenience overload that removes the supplied member method from the
    * list of listeners.
    **************************************************************************/
   template <typename T, typename Method>
   void removeListener( T *object, Method method )
   {
      removeListener( Callback<void, Event>( object, method ) );
   }

   /***************************************************************************
    * Signals and event to all registered listeners.
    **************************************************************************/
   void signal( const Event &e )
   {
      ScopedLock lock( listenerMutex );
      for( int i = 0; i < listeners.size(); i++ ) listeners[i]( e );
   }

   /***************************************************************************
    * Removes all listeners.
    **************************************************************************/
   void removeAllListeners()
   {
      ScopedLock lock( listenerMutex );
      listeners.clear();
   }

private:

   // no copy or assignment
   EventHandler( const EventHandler & );
   EventHandler &operator=( const EventHandler & );

   Mutex                               listenerMutex;
   std::vector<Callback<void, Event> > listeners;
};

#endif
