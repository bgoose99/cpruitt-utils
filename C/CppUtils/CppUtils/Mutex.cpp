
// local includes
#include "Mutex.h"

/******************************************************************************
 *
 *****************************************************************************/
Mutex::Mutex()
{
#ifdef _WIN32
   mutex = CreateMutex( 0, false, 0 );
#else
   pthread_mutex_init( &mutex, 0 );
#endif
}

/******************************************************************************
 *
 *****************************************************************************/
Mutex::~Mutex()
{
#ifdef _WIN32
   CloseHandle( mutex );
#else
   pthread_mutex_destroy( &mutex );
#endif
}

/******************************************************************************
 *
 *****************************************************************************/
void Mutex::lock()
{
#ifdef _WIN32
   WaitForSingleObject( mutex, INFINITE );
#else
   pthread_mutex_lock( &mutex );
#endif
}

/******************************************************************************
 *
 *****************************************************************************/
void Mutex::unlock()
{
#ifdef _WIN32
   ReleaseMutex( mutex );
#else
   pthread_mutex_unlock( &mutex );
#endif
}
