
#ifdef _WIN32
#else
#include <sys/time.h>
#endif

// local includes
#include "Condition.h"

/*******************************************************************************
 * 
 ******************************************************************************/
Condition::Condition( Mutex &mutex ) :
   mutex( mutex ),
   myCondition( false )
{
   #ifdef _WIN32
   //InitializeConditionVariable( &condition );
   //InitializeCriticalSection( &criticalSection );
   ExitProcess( 1 );
   #else
   pthread_cond_init( &condition, NULL );
   #endif
}

/*******************************************************************************
 * 
 ******************************************************************************/
Condition::~Condition()
{
   #ifdef _WIN32
   // TODO: implement
   ExitProcess( 1 );
   #else
   pthread_cond_destroy( &condition );
   #endif
}

/*******************************************************************************
 * 
 ******************************************************************************/
void Condition::wait()
{
   #ifdef _WIN32
   // TODO: implement
   //while( !myCondition ) 
   ExitProcess( 1 );
   #else
   while( !myCondition ) pthread_cond_wait( &condition, &mutex.mutex );
   #endif
}

/*******************************************************************************
 * 
 ******************************************************************************/
void Condition::wait( const int &milliseconds )
{
   #ifdef _WIN32
   // TODO: implement
   ExitProcess( 1 );
   #else
   static struct timespec ts;
   static struct timeval  tv;
   gettimeofday( &tv, NULL );
   ts.tv_sec = tv.tv_sec;
   ts.tv_nsec = ( tv.tv_usec * 1000 ) + ( milliseconds * 1000000 );
   
   // add seconds if necessary
   if( ts.tv_nsec > 1000000000 )
   {
      ts.tv_sec += ts.tv_nsec / 1000000000;
      ts.tv_nsec = ts.tv_nsec % 1000000000;
   }
   
   pthread_cond_timedwait( &condition, &mutex.mutex, &ts );
   #endif
}

/*******************************************************************************
 * 
 ******************************************************************************/
void Condition::signal()
{
   myCondition = true;
   #ifdef _WIN32
   // TODO: implement
   //WakeAllConditionVariable( &condition );
   ExitProcess( 1 );
   #else
   pthread_cond_broadcast( &condition );
   #endif
}

/*******************************************************************************
 * 
 ******************************************************************************/
void Condition::reset()
{
   myCondition = false;
}

/*******************************************************************************
 * 
 ******************************************************************************/
bool Condition::isMet() const
{
   return myCondition;
}

