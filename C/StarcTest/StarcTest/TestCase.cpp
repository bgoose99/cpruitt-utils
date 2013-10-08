
// system includes
#include <cmath>
#include <cstring>

// local includes
#include "TestCase.h"

namespace StarcTest
{
   /****************************************************************************
    * 
    ***************************************************************************/
   TestCase::TestCase( const std::string &name ) :
      name( name ),
      success( false )
   {
      errors.push_back( "No tests performed" );
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   TestCase::TestCase( const TestCase &that ) :
      name( that.name ),
      success( that.success ),
      errors( that.errors ),
      metrics( that.metrics )
   {
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   TestCase::~TestCase()
   {
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   TestCase &TestCase::operator=( const TestCase &that )
   {
      name    = that.name;
      success = that.success;
      errors  = that.errors;
      metrics = that.metrics;

      return *this;
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   bool TestCase::setUp()
   {
      return true;
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   void TestCase::tearDown()
   {
      return;
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   void TestCase::runTests()
   {
      try
      {
         success = setUp();
      }
      catch( ... )
      {
         success = false;
         errors.push_back( "Test setup threw unexpected exception." );
      }

      if( success )
      {
         errors.clear();
         try
         {
            runAllTests();
         }
         catch( ... )
         {
            success = false;
            errors.push_back( "Testing threw unexpected exception." );
         }

         try
         {
            tearDown();
         }
         catch( ... )
         {
            success = false;
            errors.push_back( "Test tear-down threw unexpected exception." );
         }
      }
      else
      {
         errors.push_back( "Set up failed." );
      }
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   void TestCase::verifyApproxEqual( const double      &d1,
                                     const double      &d2,
                                     const std::string &desc,
                                     const double      &TOL )
   {
      ( fabs( d1 - d2 ) <= TOL ) ? metrics.testPassed() : testFailed( desc );
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   void TestCase::verifyApproxNotEqual( const double      &d1,
                                        const double      &d2,
                                        const std::string &desc,
                                        const double      &TOL )
   {
      ( fabs( d1 - d2 ) > TOL ) ? metrics.testPassed() : testFailed( desc );
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   void TestCase::verifyEqual( const char        *str1,
                               const char        *str2,
                               const std::string &desc )
   {
      ( strcmp( str1, str2 ) == 0 ) ? metrics.testPassed() : testFailed( desc );
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   void TestCase::verifyNotEqual( const char        *str1,
                                  const char        *str2,
                                  const std::string &desc )
   {
      ( strcmp( str1, str2 ) != 0 ) ? metrics.testPassed() : testFailed( desc );
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   void TestCase::testFailed( const std::string &desc )
   {
      metrics.testFailed();
      success = false;
      errors.push_back( desc + " failed." );
   }
   
}
