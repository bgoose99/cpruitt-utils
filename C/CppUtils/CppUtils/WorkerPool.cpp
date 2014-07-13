
// system includes
#ifdef _WIN32
#include <windows.h>
#else
#include <unistd.h>
#endif

// local includes
#include "ScopedLock.h"
#include "WorkerPool.h"

/******************************************************************************
 * 
 *****************************************************************************/
WorkerPool::WorkerPool( const unsigned int &threadPoolSize )
{
   for( unsigned int i = 0; i < ( threadPoolSize < 1 ? 1 : threadPoolSize ); i++ )
   {
      WorkerThread *t = new WorkerThread( *this );
      threads.push_back( t );
   }
}

/******************************************************************************
 * 
 *****************************************************************************/
WorkerPool::~WorkerPool()
{
   stopWork();
}

/******************************************************************************
 * 
 *****************************************************************************/
void WorkerPool::addWorker( Worker *worker )
{
   ScopedLock lock( workerMutex );
   workers.push( worker );
}

/******************************************************************************
 * 
 *****************************************************************************/
void WorkerPool::stopWork()
{
   for( unsigned int i = 0; i < threads.size(); i++ )
   {
      threads[i]->shutdown();
      threads[i]->join();
      delete threads[i];
   }
   threads.clear();
   
   ScopedLock lock( workerMutex );
   while( !workers.empty() )
   {
      delete workers.front();
      workers.pop();
   }
}

/******************************************************************************
 * 
 *****************************************************************************/
WorkerPool::WorkerThread::WorkerThread( WorkerPool &parent ) : parent( parent )
{
   start();
}

/******************************************************************************
 * 
 *****************************************************************************/
WorkerPool::WorkerThread::~WorkerThread()
{
   shutdown();
   join();
}

/******************************************************************************
 * 
 *****************************************************************************/
void WorkerPool::WorkerThread::threadFunction()
{
   Worker *worker;
   ScopedLock lock( parent.workerMutex, false );
   while( isRunning )
   {
      lock.lock();
      if( !parent.workers.empty() )
      {
         worker = parent.workers.front();
         parent.workers.pop();
         lock.unlock();
         
         worker->doWork();
         delete worker;
      }
      else
      {
         lock.unlock();
      }
      
      #ifdef _WIN32
      Sleep( 1 );
      #else
      usleep( 1000 );
      #endif
   }
   return;
}

