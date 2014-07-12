
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
   long getSystemMillis( )
   {
#ifdef _WIN32
      static FILETIME ft;
      static long long now;
      GetSystemTimeAsFileTime( &ft );
      now = (LONGLONG)ft.dwLowDateTime + ( (LONGLONG)( ft.dwHighDateTime ) << 32LL );
      return (long)( now / 10000 );
#else
      static struct timeval tv;
      gettimeofday( &tv, NULL );
      return ( tv.tv_usec + ( tv.tv_sec * 1000000 ) ) / 1000;
#endif
   }

}
