
#ifndef __ConfigReader__
#define __ConfigReader__

#include <iostream>
#include <fstream>
#include <map>
#include <string>
#include <vector>

#include "Mutex.h"

class Logger;

/******************************************************************************
 * This class handles reading items from a config file formatted like a 
 * standard INI file.
 * E.g.
 * [Section 1]
 * parameter1 = value1
 * parameter2 = value2
 * ...
 * [Section n]
 * parametern = valuen
 *
 * White space will be ignored, and comments start with a '#'
 *****************************************************************************/
class ConfigReader
{
   public:
      
      /************************************************************************
       * Constructor
       ***********************************************************************/
      ConfigReader( const std::string &filename, Logger *log = 0 );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~ConfigReader();
      
      /************************************************************************
       * Gets a boolean.
       ***********************************************************************/
      void get( const std::string &section, const std::string &name, bool &value, const bool &defaultValue = false );
      
      /************************************************************************
       * Gets an integer.
       ***********************************************************************/
      void get( const std::string &section, const std::string &name, uint16_t &value, const uint16_t &defaultValue = 0 );
      
      /************************************************************************
       * Gets an integer.
       ***********************************************************************/
      void get( const std::string &section, const std::string &name, int16_t &value, const int16_t &defaultValue = 0 );
      
      /************************************************************************
       * Gets an integer.
       ***********************************************************************/
      void get( const std::string &section, const std::string &name, uint32_t &value, const uint32_t &defaultValue = 0 );
      
      /************************************************************************
       * Gets an integer.
       ***********************************************************************/
      void get( const std::string &section, const std::string &name, int32_t &value, const int32_t &defaultValue = 0 );
      
      /************************************************************************
       * Gets an integer.
       ***********************************************************************/
      void get( const std::string &section, const std::string &name, uint64_t &value, const uint64_t &defaultValue = 0 );
      
      /************************************************************************
       * Gets an integer.
       ***********************************************************************/
      void get( const std::string &section, const std::string &name, int64_t &value, const int64_t &defaultValue = 0 );
      
      /************************************************************************
       * Gets a float.
       ***********************************************************************/
      void get( const std::string &section, const std::string &name, float &value, const float &defaultValue = 0.0f );
      
      /************************************************************************
       * Gets a double.
       ***********************************************************************/
      void get( const std::string &section, const std::string &name, double &value, const double &defaultValue = 0.0 );
      
      /************************************************************************
       * Gets a string.
       ***********************************************************************/
      void get( const std::string &section, const std::string &name, std::string &value, const std::string &defaultValue = "" );
      
      /************************************************************************
       * Gets a character array.
       ***********************************************************************/
      void get( const std::string &section, const std::string &name, char *value, const int &len, const char *defaultValue = "" );
      
      /************************************************************************
       * Returns true if all reads have been successful, false otherwise.
       ***********************************************************************/
      bool good() const;
      
      /************************************************************************
       * Returns a list of all current read errors.
       ***********************************************************************/
      const std::vector<std::string> getErrorList() const;
      
   private:
      
      // no copy or assignment
      ConfigReader( const ConfigReader & );
      ConfigReader &operator=( const ConfigReader & );
      
      Logger                            *log;            // optional log file
      bool                               valid;          // true if all parameters are found, false otherwise
      std::ifstream                      in;             // input file
      Mutex                              mutex;          // mutex; this reader must be thread safe
      std::string                        currentSection; // current section name
      std::map<std::string, std::string> sectionMap;     // current section; parameter -> value
      std::vector<std::string>           errorList;      // list of encountered errors
      
      /************************************************************************
       * Retrieves a value from the file, if possible. Returns true if the
       * parameter is found (and the value is placed in 'value'), false
       * otherwise.
       ***********************************************************************/
      bool findValue( const std::string &section, const std::string &name, std::string &value );
      
      /************************************************************************
       * Reads an entire section from the file and stores it in the map.
       ***********************************************************************/
      void readSection( const std::string &section );
      
      /************************************************************************
       * Logs a parameter/value pair in the log file.
       ***********************************************************************/
      void logParameter( const std::string &parameter, const std::string &value );
      
};

#endif
