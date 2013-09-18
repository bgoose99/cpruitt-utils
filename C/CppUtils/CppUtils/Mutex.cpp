
// local includes
#include "Mutex.h"

/*******************************************************************************
 * 
 ******************************************************************************/
Mutex::Mutex()
{
   #ifdef _WIN32
   mutex = CreateMutex( NULL, false, NULL );
   #else
   pthread_mutex_init( &mutex, NULL );
   #endif
}

/*******************************************************************************
 * 
 ******************************************************************************/
Mutex::~Mutex()
{
   #ifdef _WIN32
   CloseHandle( mutex );
   #else
   pthread_mutex_destroy( &mutex );
   #endif
}

/*******************************************************************************
 * 
 ******************************************************************************/
void Mutex::lock()
{
   #ifdef _WIN32
   WaitForSingleObject( mutex, INFINITE );
   #else
   pthread_mutex_lock( &mutex );
   #endif
}

/*******************************************************************************
 * 
 ******************************************************************************/
void Mutex::unlock()
{
   #ifdef _WIN32
   ReleaseMutex( mutex );
   #else
   pthread_mutex_unlock( &mutex );
   #endif
}
