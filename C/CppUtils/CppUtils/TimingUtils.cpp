
// system includes
#ifdef _WIN32
#include <windows.h>
#else
#include <sys/time.h>
#include <unistd.h>
#endif
#include <utility>

// local includes
#include "TimingUtils.h"

namespace TimingUtils
{

   /***************************************************************************
    *
    **************************************************************************/
   long getSystemMicros()
   {
#ifdef _WIN32
      static FILETIME ft;
      static long long now;
      GetSystemTimeAsFileTime( &ft );
      now = (LONGLONG)ft.dwLowDateTime + ( (LONGLONG)( ft.dwHighDateTime ) << 32LL );
      return (long)( now / 10 ); // 100-nanosecond intervals in a microsecond
#else
      static struct timeval tv;
      gettimeofday( &tv, 0 );
      return ( tv.tv_usec + ( tv.tv_sec * 1000000 ) );
#endif
   }

   /***************************************************************************
    *
    **************************************************************************/
   long getSystemMillis()
   {
      return getSystemMicros() / 1000;
   }

   /***************************************************************************
    *
    **************************************************************************/
   void spinSleep( const long &microseconds )
   {
      long now = getSystemMicros();
      long later = now + microseconds;
      long maxSleep = later - now;

      while( now < later )
      {
         now = getSystemMicros();
         if( ( later - now ) > maxSleep ) return; // time has elapsed, wake up
      }
   }

}
