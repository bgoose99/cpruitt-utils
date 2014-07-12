#include <stdio.h>
// local includes
#include "AbstractThread.h"

/*******************************************************************************
 *
 ******************************************************************************/
AbstractThread::AbstractThread() : isRunning( false )
{
}

/*******************************************************************************
 *
 ******************************************************************************/
AbstractThread::~AbstractThread()
{
   shutdown();

#ifdef _WIN32
   CloseHandle( thread );
#endif
}

/*******************************************************************************
 *
 ******************************************************************************/
void AbstractThread::start()
{
   if( !isRunning )
   {
      isRunning = true;

#ifdef _WIN32
      thread = CreateThread( 0, 0, AbstractThread::threadHelper, this, 0, &threadId );
#else
      pthread_create( &thread, 0, AbstractThread::threadHelper, this );
#endif
   }
}

/*******************************************************************************
 *
 ******************************************************************************/
void AbstractThread::join()
{
#ifdef _WIN32
   // TODO: wait for completion
#else
   pthread_join( thread, 0 );
#endif
   return;
}

/*******************************************************************************
 *
 ******************************************************************************/
void AbstractThread::shutdown()
{
   if( isRunning )
   {
      isRunning = false;
#ifdef _WIN32
      // TODO: wait for completion
#else
      pthread_join( thread, 0 );
#endif
   }
}

#ifdef _WIN32
/*******************************************************************************
 *
 ******************************************************************************/
DWORD WINAPI AbstractThread::threadHelper( LPVOID arg )
{
   AbstractThread *t = reinterpret_cast< AbstractThread * >( arg );
   if( t != 0 ) t->threadFunction();

   return 0;
}
#else
/*******************************************************************************
 *
 ******************************************************************************/
void *AbstractThread::threadHelper( void *arg )
{
   return ( reinterpret_cast< AbstractThread * >( arg ) )->threadFunction();
}
#endif
