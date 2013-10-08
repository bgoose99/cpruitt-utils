
#ifndef __TestSuite__
#define __TestSuite__

// system includes
#include <ostream>

// local includes
#include "TestCase.h"

namespace StarcTest
{
   /****************************************************************************
    * A TestSuite is the top-level test container. It may contain other
    * TestSuites and/or TestCases.
    * NOTE: This object becomes the owner of all TestSuites and TestCases it
    *       contains, ensuring they are deleted when this object is destroyed.
    ***************************************************************************/
   class TestSuite
   {
      public:
         
         /**********************************************************************
          * Constructor
          *********************************************************************/
         TestSuite( const std::string &name );
         
         /**********************************************************************
          * Destructor
          *********************************************************************/
         virtual ~TestSuite();
         
         /**********************************************************************
          * Returns the name of this suite.
          *********************************************************************/
         inline std::string getName() const { return name; }
         
         /**********************************************************************
          * Adds a TestSuite to this suite.
          *********************************************************************/
         inline void addSuite( TestSuite *suite ) { suites.push_back( suite ); }
         
         /**********************************************************************
          * Adds a TestCase to this suite.
          *********************************************************************/
         inline void addTestCase( TestCase *testCase ) { cases.push_back( testCase ); }
         
         /**********************************************************************
          * Returns the metrics associated with this suite.
          *********************************************************************/
         inline TestMetrics getTestMetrics() const { return metrics; }
         
         /**********************************************************************
          * Runs all tests associated with this suite.
          *********************************************************************/
         void runTests();
         
         /**********************************************************************
          * Writes the detailed test results to the supplied stream.
          *********************************************************************/
         void testResultsToStream( std::ostream &stream ) const;
         
         /**********************************************************************
          * Writes the test metrics to the supplied stream.
          *********************************************************************/
         void testMetricsToStream( std::ostream &stream ) const;
         
      private:
         
         // no copy constructor or assignment
         TestSuite( const TestSuite &that ) {}
         TestSuite &operator=( const TestSuite &that ) {}
         
         std::string              name;
         std::vector<TestSuite *> suites;
         std::vector<TestCase *>  cases;
         TestMetrics              metrics;
         
   };
}

#endif
