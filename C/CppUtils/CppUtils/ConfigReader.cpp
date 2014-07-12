
// system includes
#include <cstdint>
#include <cstdio>
#include <cstring>
#include <stdexcept>
#include <stdlib.h>

// local includes
#include "ConfigReader.h"
#include "Logger.h"
#include "ScopedLock.h"
#include "StringUtils.h"

using namespace std;

using namespace StringUtils;

/******************************************************************************
 * 
 *****************************************************************************/
ConfigReader::ConfigReader( const std::string &filename, Logger *log ) :
   currentSection( "" ), valid( true ), log( log )
{
   in.open( filename.c_str() );
   if( !in.is_open() )
      throw runtime_error( "ConfigReader: Unable to open input file: " + filename );
}

/******************************************************************************
 * 
 *****************************************************************************/
ConfigReader::~ConfigReader()
{
   sectionMap.clear();
   in.close();
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::get( const std::string &section, const std::string &name, bool &value, const bool &defaultValue )
{
   value = defaultValue;
   string s;
   if( findValue( section, name, s ) )
   {
      toLowerCase( s );
      value = ( strcmp( s.c_str(), "true" ) == 0 || atoi( s.c_str() ) != 0 );
   }
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::get( const std::string &section, const std::string &name, uint16_t &value, const uint16_t &defaultValue )
{
   value = defaultValue;
   string s;
   if( findValue( section, name, s ) )
      value = atoi( s.c_str() );
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::get( const std::string &section, const std::string &name, int16_t &value, const int16_t &defaultValue )
{
   value = defaultValue;
   string s;
   if( findValue( section, name, s ) )
      value = atoi( s.c_str() );
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::get( const std::string &section, const std::string &name, uint32_t &value, const uint32_t &defaultValue )
{
   value = defaultValue;
   string s;
   if( findValue( section, name, s ) )
      value = atoi( s.c_str() );
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::get( const std::string &section, const std::string &name, int32_t &value, const int32_t &defaultValue )
{
   value = defaultValue;
   string s;
   if( findValue( section, name, s ) )
      value = atoi( s.c_str() );
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::get( const std::string &section, const std::string &name, uint64_t &value, const uint64_t &defaultValue )
{
   value = defaultValue;
   string s;
   if( findValue( section, name, s ) )
      value = atoi( s.c_str() );
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::get( const std::string &section, const std::string &name, int64_t &value, const int64_t &defaultValue )
{
   value = defaultValue;
   string s;
   if( findValue( section, name, s ) )
      value = atoi( s.c_str() );
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::get( const std::string &section, const std::string &name, float &value, const float &defaultValue )
{
   value = defaultValue;
   string s;
   if( findValue( section, name, s ) )
      value = (float)atof( s.c_str() );
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::get( const std::string &section, const std::string &name, double &value, const double &defaultValue )
{
   value = defaultValue;
   string s;
   if( findValue( section, name, s ) )
      value = atof( s.c_str() );
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::get( const std::string &section, const std::string &name, std::string &value, const std::string &defaultValue )
{
   value = defaultValue;
   string s;
   if( findValue( section, name, s ) )
      value = s;
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::get( const std::string &section, const std::string &name, char *value, const int &len, const char *defaultValue )
{
   #ifdef _WIN32
   _snprintf_s( value, len, _TRUNCATE, defaultValue );
   #else
   snprintf( value, len, defaultValue );
   #endif
   string s;
   if( findValue( section, name, s ) )
   {
      #ifdef _WIN32
      _snprintf_s( value, len, _TRUNCATE, defaultValue );
      #else
      snprintf( value, len, s.c_str() );
      #endif
   }
}

/******************************************************************************
 * 
 *****************************************************************************/
bool ConfigReader::good() const
{
   return valid;
}

/******************************************************************************
 * 
 *****************************************************************************/
const std::vector<std::string> ConfigReader::getErrorList() const
{
   return errorList;
}

/******************************************************************************
 * 
 *****************************************************************************/
bool ConfigReader::findValue( const std::string &section, const std::string &name, std::string &value )
{
   if( currentSection.compare( section ) != 0 )
      readSection( section );
      
   map<string, string>::const_iterator iter = sectionMap.find( name );
   if( iter != sectionMap.end() )
   {
      logParameter( iter->first, iter->second );
      value = iter->second;
      return true;
   }
   else
   {
      valid = false;
      errorList.push_back( "'" + name + "' not found in section '" + section + "'" );
      return false;
   }
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::readSection( const std::string &section )
{
   currentSection = section;
   sectionMap.clear();
   string line;
   string search( "[" + section + "]" );
   vector<string> strings;
   
   ScopedLock lock( mutex );
   
   // go to the appropriate section in the file
   in.clear();
   in.seekg( 0, ios::beg );
   while( in.good() )
   {
      getline( in, line );
      if( startsWith( line, search ) )
         break;
   }
   
   // read each line in the section
   while( in.good() )
   {
      getline( in, line );
      if( startsWith( line, "[" ) ) // beginning of new section
         break;
      
      // this is potentially a parameter
      int pos = line.find( "#" );
      if( pos >= 0 )
         line.erase( pos ); // chop off comments
      
      strings.clear();
      strings = split( line, "=" );
      if( strings.size() == 2 )
      {
         trimString( strings[0] );
         trimString( strings[1] );
         sectionMap[strings[0]] = strings[1];
      }
   }
}

/******************************************************************************
 * 
 *****************************************************************************/
void ConfigReader::logParameter( const std::string &parameter, const std::string &value )
{
   if( log != 0 )
      log->log( "ConfigReader", parameter + " = " + value );
}
