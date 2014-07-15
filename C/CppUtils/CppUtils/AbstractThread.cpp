
// local includes
#include "AbstractThread.h"

/******************************************************************************
 *
 *****************************************************************************/
AbstractThread::AbstractThread() :
   isRunning( false ),
   thread( 0 )
{
}

/******************************************************************************
 *
 *****************************************************************************/
AbstractThread::~AbstractThread()
{
   shutdown();

#ifdef _WIN32
   if( thread != 0 )
   {
      delete thread;
      thread = 0;
   }
#endif
}

/******************************************************************************
 *
 *****************************************************************************/
void AbstractThread::start()
{
   if( !isRunning )
   {
      isRunning = true;

#ifdef _WIN32
      thread = new std::thread( &AbstractThread::threadFunction, this );
#else
      pthread_create( &thread, 0, AbstractThread::threadHelper, this );
#endif
   }
}

/******************************************************************************
 *
 *****************************************************************************/
void AbstractThread::join()
{
   if( isRunning )
   {
#ifdef _WIN32
      if( thread != 0 ) thread->join();
#else
      pthread_join( thread, 0 );
#endif
   }
   return;
}

/******************************************************************************
 *
 *****************************************************************************/
void AbstractThread::shutdown()
{
   if( isRunning )
   {
      isRunning = false;
#ifdef _WIN32
      if( thread != 0 ) thread->join();
#else
      pthread_join( thread, 0 );
#endif
   }
}

#ifndef _WIN32
/******************************************************************************
 *
 *****************************************************************************/
void *AbstractThread::threadHelper( void *arg )
{
   return ( reinterpret_cast< AbstractThread * >( arg ) )->threadFunction();
}
#endif
