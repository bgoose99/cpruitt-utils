
#ifndef __TaskScheduler__
#define __TaskScheduler__

// system includes
#include <map>
#include <tuple>
#include <vector>

// local includes
#include "AbstractThread.h"
#include "Callback.h"
#include "Mutex.h"
#include "ScopedLock.h"
#include "TimingUtils.h"

/******************************************************************************
 * This class encapsulates one or more threads that service a queue of
 * Callbacks. It is meant to be a simple fire-and-forget utility class that
 * allows a user to schedule function calls at an arbitrary future time.
 * NOTE: Because there is no way to return meaningful information back to the
 *       caller, all callbacks are assumed to return void. However, they can
 *       take any number of arguments.
 *****************************************************************************/
template <typename... TaskArgs>
class TaskScheduler
{
public:

   /***************************************************************************
    *
    **************************************************************************/
   TaskScheduler( const unsigned int &threadPoolSize = 1 )
   {
      for( unsigned int i = 0; i < ( threadPoolSize < 1 ? 1 : threadPoolSize ); ++i )
      {
         QueueServicer *t = new QueueServicer( *this );
         threads.push_back( t );
      }
   }

   /***************************************************************************
    *
    **************************************************************************/
   virtual ~TaskScheduler()
   {
      cancelAllTasks();

      for( unsigned int i = 0; i < threads.size(); ++i )
      {
         delete threads[i];
      }
      threads.clear();
   }

   /***************************************************************************
    * Add a single-shot task.
    *  - task      : the callback function to be called
    *  - taskArgs  : the argument(s) to be passed to the callback
    *  - firstTime : the time from now (in milliseconds) at which the
    *                callback will be called
    **************************************************************************/
   void addTask( Callback<void, TaskArgs...> task, const TaskArgs&... taskArgs, const long &firstTime )
   {
      long now = TimingUtils::getSystemMicros();
      long nextTime = now + ( firstTime < 0 ? 0 : firstTime * 1000 );
      Task t( task, taskArgs..., SINGLE_SHOT, nextTime, 0, 0 );

      ScopedLock lock( taskMutex );
      tasks.insert( std::make_pair( t.nextTime, t ) );
   }

   /***************************************************************************
    * Convenience overload for regular functions.
    **************************************************************************/
   void addTask( void( *function )( const TaskArgs&... taskArgs ), const long &firstTime )
   {
      addTask( Callback<void, TaskArgs...>( function ), taskArgs..., firstTime );
   }

   /***************************************************************************
    * Convenience overload for class member methods.
    **************************************************************************/
   template <typename ClassType, typename Method>
   void addTask( ClassType *object, Method method, const TaskArgs&... taskArgs, const long &firstTime )
   {
      addTask( Callback<void, TaskArgs...>( object, method ), taskArgs..., firstTime );
   }

   /***************************************************************************
    * Add a task that is repeated indefinitely.
    *  - task      : the callback function to be called
    *  - taskArgs  : the argument(s) to be passed to the callback
    *  - firstTime : the time from now (in milliseconds) at which the
    *                callback will be called
    *  - interval  : the interval (in milliseconds) at which the callback
    *                will be called
    **************************************************************************/
   void addInfiniteTask( Callback<void, TaskArgs...> task, const TaskArgs&... taskArgs, const long &firstTime, const long &interval )
   {
      long now = TimingUtils::getSystemMicros();
      long nextTime = now + ( firstTime < 0 ? 0 : firstTime * 1000 );
      Task t( task, taskArgs..., INFINITE_TASK, nextTime, 0, interval * 1000 );

      ScopedLock lock( taskMutex );
      tasks.insert( std::make_pair( t.nextTime, t ) );
   }

   /***************************************************************************
    * Convenience overload for regular functions.
    **************************************************************************/
   void addInfiniteTask( void( *function )( const TaskArgs&... taskArgs ), const long &firstTime, const long &interval )
   {
      addTask( Callback<void, TaskArgs...>( function ), taskArgs..., firstTime, interval );
   }

   /***************************************************************************
    * Convenience overload for class member methods.
    **************************************************************************/
   template <typename ClassType, typename Method>
   void addInfiniteTask( ClassType *object, Method method, const TaskArgs&... taskArgs, const long &firstTime, const long &interval )
   {
      addTask( Callback<void, TaskArgs...>( object, method ), taskArgs..., firstTime, interval );
   }

   /***************************************************************************
    * Add a recurring task that stops being called at a specified interval
    * until a certain amount of time has elapsed.
    *  - task      : the callback function to be called
    *  - taskArgs  : the argument(s) to be passed to the callback
    *  - firstTime : the time from now (in milliseconds) at which the
    *                callback will be called
    *  - interval  : the interval (in milliseconds) at which the callback
    *                will be called
    *  - maxTime   : maximum time from now (in milliseconds) at which the
    *                callback will be called
    **************************************************************************/
   void addRecurringTask( Callback<void, TaskArgs...> task, const TaskArgs&... taskArgs, const long &firstTime, const long &interval, const long &maxTime )
   {
      long now = TimingUtils::getSystemMicros();
      long nextTime = now + ( firstTime < 0 ? 0 : firstTime * 1000 );
      Task t( task, taskArgs..., RECURRING_TASK, nextTime, now + maxTime * 1000, interval * 1000 );

      ScopedLock lock( taskMutex );
      tasks.insert( std::make_pair( t.nextTime, t ) );
   }
   /***************************************************************************
    * Convenience overload for regular functions.
    **************************************************************************/
   void addRecurringTask( void( *function )( const TaskArgs&... taskArgs ), const long &firstTime, const long &interval, const long &maxTime )
   {
      addTask( Callback<void, TaskArgs...>( function ), taskArgs..., firstTime, interval, maxTime );
   }

   /***************************************************************************
    * Convenience overload for class member methods.
    **************************************************************************/
   template <typename ClassType, typename Method>
   void addRecurringTask( ClassType *object, Method method, const TaskArgs&... taskArgs, const long &firstTime, const long &interval, const long &maxTime )
   {
      addTask( Callback<void, TaskArgType>( object, method ), taskArgs..., firstTime, interval, maxTime );
   }

   /***************************************************************************
    * Cancels all tasks.
    **************************************************************************/
   void cancelAllTasks()
   {
      ScopedLock lock( taskMutex );
      tasks.clear();
   }

private:

   // no copy or assignment
   TaskScheduler( const TaskScheduler & );
   TaskScheduler &operator=( const TaskScheduler & );

   enum TaskType { SINGLE_SHOT, RECURRING_TASK, INFINITE_TASK };

   // This crafty bit of templating madness courtesy of the fine folks
   // on StackOverflow:
   // http://stackoverflow.com/questions/7858817/unpacking-a-tuple-to-call-a-matching-function-pointer/7858971#7858971
   // Basically, it's a recursive unfolding of the args present in the tuple
   template<int...>
   struct seq { };

   template <int N, int... S>
   struct gens : gens<N - 1, N - 1, S...> { };

   template <int... S>
   struct gens<0, S...>
   {
      typedef seq<S...> type;
   };

   struct Task
   {
      Callback<void, TaskArgs...> callback;
      std::tuple<TaskArgs...> taskArgs;
      TaskType type;
      long nextTime;
      long maxTime;
      long interval;

      explicit Task( Callback<void, TaskArgs...> callback,
                     const TaskArgs&...          taskArgs,
                     const TaskType             &type,
                     const long                 &nextTime,
                     const long                 &maxTime,
                     const long                 &interval ) :
         callback( callback ),
         taskArgs( taskArgs... ),
         type( type ),
         nextTime( nextTime ),
         maxTime( maxTime ),
         interval( interval )
      {}

      void callCallback()
      {
         callFunction( typename gens<sizeof...( TaskArgs )>::type() );
      }

      template<int... S>
      void callFunction( seq<S...> )
      {
         callback( std::get<S>( taskArgs )... );
      }
   };

   Mutex                     taskMutex;
   std::multimap<long, Task> tasks; // system time -> task

   class QueueServicer : public AbstractThread
   {
   public:
      explicit QueueServicer( TaskScheduler<TaskArgs...> &parent ) : parent( parent ) { start(); }
      virtual ~QueueServicer() { shutdown(); }
      virtual void threadFunction()
      {
         typename std::multimap<long, Task>::iterator iter;
         long now;
         ScopedLock lock( parent.taskMutex, false );

         while( isRunning )
         {
            lock.lock();
            iter = parent.tasks.begin();
            if( iter != parent.tasks.end() )
            {
               now = TimingUtils::getSystemMicros();
               if( now >= iter->first )
               {
                  Task t = iter->second;
                  parent.tasks.erase( iter );

                  switch( t.type )
                  {
                     case RECURRING_TASK:
                        t.nextTime += t.interval;
                        if( t.nextTime < t.maxTime )
                           parent.tasks.insert( std::make_pair( t.nextTime, t ) );
                        break;
                     case INFINITE_TASK:
                        t.nextTime += t.interval;
                        parent.tasks.insert( std::make_pair( t.nextTime, t ) );
                        break;
                     case SINGLE_SHOT:
                     default:
                        // do nothing
                        break;
                  }
                  lock.unlock();

                  t.callCallback();
               }
            }

            lock.unlock();
            Sleep( 1 );
         }
      }

   private:
      // no copy or assignment
      QueueServicer( const QueueServicer & );
      QueueServicer &operator=( const QueueServicer & );

      TaskScheduler<TaskArgs...> &parent;
   };

   std::vector<QueueServicer *> threads; // thread pool

};

#endif
