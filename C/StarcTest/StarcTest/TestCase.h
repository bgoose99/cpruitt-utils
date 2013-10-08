
#ifndef __TestCase__
#define __TestCase__

// system includes
#include <string>
#include <vector>

// local includes
#include "TestMetrics.h"

namespace StarcTest
{
   /****************************************************************************
    * This is the abstract base class for all tests.
    ***************************************************************************/
   class TestCase
   {
      public:
         
         /**********************************************************************
          * Constructor
          *********************************************************************/
         TestCase( const std::string &name );
         
         /**********************************************************************
          * Copy constructor
          *********************************************************************/
         TestCase( const TestCase &that );
         
         /**********************************************************************
          * Destructor
          *********************************************************************/
         virtual ~TestCase();
         
         /**********************************************************************
          * Assignment operator
          *********************************************************************/
         TestCase &operator=( const TestCase &that );
         
         /**********************************************************************
          * Returns the name/description of this test case.
          *********************************************************************/
         inline std::string getName() const { return name; }
         
         /**********************************************************************
          * Returns true if all tests have passed.
          *********************************************************************/
         inline bool good() const { return success; }
         
         /**********************************************************************
          * This function can be overridden to perform any initialization
          * necessary before testing occurs.
          *********************************************************************/
         virtual bool setUp();
         
         /**********************************************************************
          * This function can be overridden to clean up any resources after
          * testing has completed.
          *********************************************************************/
         virtual void tearDown();
         
         /**********************************************************************
          * Returns a list of all errors encountered during testing.
          *********************************************************************/
         inline std::vector<std::string> getErrors() const { return errors; }
         
         /**********************************************************************
          * Returns the number of error encountered during testing.
          *********************************************************************/
         inline int getNumErrors() const { return errors.size(); }
         
         /**********************************************************************
          * Returns metrics associated with this test.
          *********************************************************************/
         inline TestMetrics getTestMetrics() const { return metrics; }
         
         /**********************************************************************
          * Sets up this test case, runs all associated tests, and performs any
          * clean up.
          *********************************************************************/
         void runTests();
         
      protected:
         
         /**********************************************************************
          * To be implemented by concrete test class.
          *********************************************************************/
         virtual void runAllTests() = 0;
         
         /**********************************************************************
          * Registers an error if arg1 != arg2.
          *********************************************************************/
         template <typename T>
         void verifyEqual( const T           &arg1,
                           const T           &arg2,
                           const std::string &desc )
         {
            ( arg1 == arg2 ) ? metrics.testPassed() : testFailed( desc );
            return;
         }
         
         /**********************************************************************
          * Registers an error if arg1 == arg2.
          *********************************************************************/
         template <typename T>
         void verifyNotEqual( const T           &arg1,
                              const T           &arg2,
                              const std::string &desc )
         {
            ( arg1 != arg2 ) ? metrics.testPassed() : testFailed( desc );
         }
         
         /**********************************************************************
          * Registers an error if arg1 >= arg2.
          *********************************************************************/
         template <typename T>
         void verifyLessThan( const T           &arg1,
                              const T           &arg2,
                              const std::string &desc )
         {
            ( arg1 < arg2 ) ? metrics.testPassed() : testFailed( desc );
         }
         
         /**********************************************************************
          * Registers an error if arg1 > arg2.
          *********************************************************************/
         template <typename T>
         void verifyLessThanOrEqual( const T           &arg1,
                                     const T           &arg2,
                                     const std::string &desc )
         {
            ( arg1 <= arg2 ) ? metrics.testPassed() : testFailed( desc );
         }
         
         /**********************************************************************
          * Registers an error if arg1 <= arg2.
          *********************************************************************/
         template <typename T>
         void verifyGreaterThan( const T           &arg1,
                                 const T           &arg2,
                                 const std::string &desc )
         {
            ( arg1 > arg2 ) ? metrics.testPassed() : testFailed( desc );
         }
         
         /**********************************************************************
          * Registers an error if arg1 < arg2.
          *********************************************************************/
         template <typename T>
         void verifyGreaterThanOrEqual( const T           &arg1,
                                        const T           &arg2,
                                        const std::string &desc )
         {
            ( arg1 >= arg2 ) ? metrics.testPassed() : testFailed( desc );
         }
         
         /**********************************************************************
          * Tests two doubles to see if they are equal, given a precision
          * tolerance.
          *********************************************************************/
         void verifyApproxEqual( const double      &d1,
                                 const double      &d2,
                                 const std::string &desc,
                                 const double      &TOL = 1.0e-6 );
         
         /**********************************************************************
          * Tests two doubles to see if they are not equal, given a precision
          * tolerance.
          *********************************************************************/
         void verifyApproxNotEqual( const double      &d1,
                                    const double      &d2,
                                    const std::string &desc,
                                    const double      &TOL = 1.0e-6 );
         
         /**********************************************************************
          * Registers an error if str1 != str2.
          *********************************************************************/
         void verifyEqual( const char        *str1,
                           const char        *str2,
                           const std::string &desc );
         
         /**********************************************************************
          * Registers an error if str1 == str2.
          *********************************************************************/
         void verifyNotEqual( const char        *str1,
                              const char        *str2,
                              const std::string &desc );
         
      private:
         
         /**********************************************************************
          * Updates metrics and pushed error string onto list of errors.
          *********************************************************************/
         void testFailed( const std::string &desc );
         
         std::string              name;
         bool                     success;
         std::vector<std::string> errors;
         TestMetrics              metrics;
         
   };
}

#endif
