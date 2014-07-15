
#ifndef __StringUtils__
#define __StringUtils__

// system includes
#include <cstdint>
#include <string>
#include <vector>

namespace StringUtils
{

   /***************************************************************************
    * Trims white space from the beginning and end of the supplied string.
    **************************************************************************/
   void trimString( std::string &str );

   /***************************************************************************
    * Converts a string to lowercase.
    **************************************************************************/
   void toLowerCase( std::string &str );

   /***************************************************************************
    * Converts a string to uppercase.
    **************************************************************************/
   void toUpperCase( std::string &str );

   /***************************************************************************
    * Returns true if the supplied string starts with the supplied prefix,
    * false otherwise.
    **************************************************************************/
   bool startsWith( const std::string &str, const std::string &prefix );

   /***************************************************************************
    * Returns true if the supplied string ends with the supplied suffix,
    * false otherwise.
    **************************************************************************/
   bool endsWith( const std::string &str, const std::string &suffix );

   /***************************************************************************
    * Converts the supplied integer to a string.
    **************************************************************************/
   std::string toString( const uint16_t &i );

   /***************************************************************************
    * Converts the supplied integer to a string.
    **************************************************************************/
   std::string toString( const int16_t &i );

   /***************************************************************************
    * Converts the supplied integer to a string.
    **************************************************************************/
   std::string toString( const uint32_t &i );

   /***************************************************************************
    * Converts the supplied integer to a string.
    **************************************************************************/
   std::string toString( const int32_t &i );

   /***************************************************************************
    * Converts the supplied integer to a string.
    **************************************************************************/
   std::string toString( const uint64_t &i );

   /***************************************************************************
    * Converts the supplied integer to a string.
    **************************************************************************/
   std::string toString( const int64_t &i );

   /***************************************************************************
    * Converts the supplied float to a string.
    **************************************************************************/
   std::string toString( const float &f, const int precision = 2 );

   /***************************************************************************
    * Converts the supplied double to a string.
    **************************************************************************/
   std::string toString( const double &d, const int precision = 2 );

   /***************************************************************************
    * Left-pads a string to the supplied length with the supplied character.
    **************************************************************************/
   std::string padStringLeft( const int &len, const char &pad, const std::string &str );

   /***************************************************************************
    * Right-pads a string to the supplied length with the supplied character.
    **************************************************************************/
   std::string padStringRight( const int &len, const char &pad, const std::string &str );

   /***************************************************************************
    * Splits a string based on the supplied delimiter.
    * If greedy is true, multiple delimiters are treated as one.
    **************************************************************************/
   std::vector<std::string> split( const std::string &str, const std::string &delim, const bool &greedy = false );

   /***************************************************************************
    * Splits a string on whitespace.
    * If greedy is true, multiple delimiters are treated as one.
    **************************************************************************/
   std::vector<std::string> split( const std::string &str, const bool &greedy = true );

}

#endif
