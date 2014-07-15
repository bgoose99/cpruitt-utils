
// system includes
#include <algorithm>
#include <stdexcept>

// local includes
#include "Logger.h"
#include "ScopedLock.h"
#include "StringUtils.h"

using namespace std;

using namespace StringUtils;

const int Logger::TIME_PAD = 10;
const int Logger::PREF_PAD = 20;

/******************************************************************************
 *
 *****************************************************************************/
Logger::Logger( const std::string &filename )
{
   out.open( filename.c_str() );
   if( !out.is_open() )
      throw runtime_error( "Logger: Unable to open output file: " + filename );
}

/******************************************************************************
 *
 *****************************************************************************/
Logger::~Logger()
{
   out.close();
}

/******************************************************************************
 *
 *****************************************************************************/
void Logger::log( const std::string &msg, const std::string &prefix, const double &timestamp )
{
   ScopedLock lock( mutex );
   out << padStringLeft( TIME_PAD, ' ', toString( timestamp ) ) << ": [" <<
      padStringLeft( PREF_PAD, ' ', prefix ) << "] - " << msg << endl;
}

