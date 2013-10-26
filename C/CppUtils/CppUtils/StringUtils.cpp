
// system includes
#include <stdio.h>
#include <ctype.h>
#include <iomanip>
#include <sstream>
#include <iterator>

// local includes
#include "StringUtils.h"

using namespace std;

namespace StringUtils
{

   /***************************************************************************
    * 
    **************************************************************************/
   void trimString( std::string &str )
   {
      size_t pos = str.find_first_not_of( " \t\n\r" );
      str.erase( 0, pos );
      pos = str.find_last_not_of( " \t\n\r" );
      str.erase( pos + 1 );
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   void toLowerCase( std::string &str )
   {
      for( unsigned int i = 0; i < str.length(); i++ )
      {
         int c = str[i];
         str[i] = tolower( c );
      }
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   void toUpperCase( std::string &str )
   {
      for( unsigned int i = 0; i < str.length(); i++ )
      {
         int c = str[i];
         str[i] = toupper( c );
      }
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   bool startsWith( const std::string &str, const std::string &prefix )
   {
      if( str.length() < prefix.length() )
         return false;
      return ( str.find( prefix ) == 0 );
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   bool endsWith( const std::string &str, const std::string &suffix )
   {
      if( str.length() < suffix.length() )
         return false;
      return ( str.rfind( suffix ) == ( str.length() - suffix.length() ) );
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   std::string toString( const uint16_t &i )
   {
      stringstream ss;
      ss << i;
      return ss.str();
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   std::string toString( const int16_t &i )
   {
      stringstream ss;
      ss << i;
      return ss.str();
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   std::string toString( const uint32_t &i )
   {
      stringstream ss;
      ss << i;
      return ss.str();
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   std::string toString( const int32_t &i )
   {
      stringstream ss;
      ss << i;
      return ss.str();
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   std::string toString( const uint64_t &i )
   {
      stringstream ss;
      ss << i;
      return ss.str();
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   std::string toString( const int64_t &i )
   {
      stringstream ss;
      ss << i;
      return ss.str();
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   std::string toString( const float &f, const int precision )
   {
      stringstream ss;
      ss << std::fixed << std::setprecision( precision ) << f;
      return ss.str();
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   std::string toString( const double &d, const int precision )
   {
      stringstream ss;
      ss << std::fixed << std::setprecision( precision ) << d;
      return ss.str();
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   std::string padStringLeft( const int &len, const char &pad, const std::string &str )
   {
      stringstream ss;
      int numChars = len - str.length();
      
      for( int i = 0; i < numChars; i++ )
         ss << pad;
      
      ss << str;
      return ss.str();
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   std::string padStringRight( const int &len, const char &pad, const std::string &str )
   {
      stringstream ss;
      ss << str;
      int numChars = len - str.length();
      
      for( int i = 0; i < numChars; i++ )
         ss << pad;
      
      return ss.str();
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   std::vector<std::string> split( const std::string &str, const std::string &delim, const bool &greedy )
   {
      string temp( str );
      vector<string> vec;
      
      int pos = temp.find( delim );
      while( pos >= 0 )
      {
         vec.push_back( temp.substr( 0, pos ) );
         
         temp.erase( 0, pos + delim.size() );
         if( greedy )
         {
            while( startsWith( temp, delim ) )
               temp.erase( 0, delim.size() );
         }
         
         pos = temp.find( delim );
      }
      
      vec.push_back( temp );
      return vec;
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   std::vector<std::string> split( const std::string &str, const bool &greedy )
   {
      string temp( str );
      if( greedy ) trimString( temp );
      vector<string> vec;
      
      size_t pos = temp.find_first_of( " \t" );
      while( pos != string::npos )
      {
         vec.push_back( temp.substr( 0, pos ) );
         
         temp.erase( 0, pos + 1 );
         if( greedy )
         {
            while( startsWith( temp, " " ) || startsWith( temp, "\t" ) )
               temp.erase( 0, 1 );
         }
         
         pos = temp.find_first_of( " \t" );
      }
      
      vec.push_back( temp );
      return vec;
   }

}
