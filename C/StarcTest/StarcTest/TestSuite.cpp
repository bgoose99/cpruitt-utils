
// system includes
#include <iomanip>

// local includes
#include "TestSuite.h"

using std::endl;

namespace StarcTest
{
   
   /****************************************************************************
    * 
    ***************************************************************************/
   TestSuite::TestSuite( const std::string &name ) :
      name( name )
   {
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   TestSuite::~TestSuite()
   {
      for( unsigned int i = 0; i < suites.size(); i++ )
      {
         delete suites[i];
      }
      suites.clear();
      
      for( unsigned int i = 0; i < cases.size(); i++ )
      {
         delete cases[i];
      }
      cases.clear();
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   void TestSuite::runTests()
   {
      for( unsigned int i = 0; i < suites.size(); i++ )
      {
         suites[i]->runTests();
         metrics += suites[i]->getTestMetrics();
      }
      
      for( unsigned int i = 0; i < cases.size(); i++ )
      {
         cases[i]->runTests();
         metrics += cases[i]->getTestMetrics();
      }
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   void TestSuite::testResultsToStream( std::ostream &stream ) const
   {
      stream << name << " Results" << endl;
      if( !suites.empty() )
      {
         stream << "Tested " << suites.size() << " test suites." << endl;
         for( unsigned int i = 0; i < suites.size(); i++ )
         {
            suites[i]->testResultsToStream( stream );
         }
         stream << endl;
      }
      
      stream << "Tested " << cases.size() << " test cases." << endl;
      for( unsigned int i = 0; i < cases.size(); i++ )
      {
         if( cases[i]->getNumErrors() == 0 )
         {
            stream << cases[i]->getName() << " passed." << endl;
         }
         else
         {
            stream << cases[i]->getName() << " failed. Errors: " << endl;
            std::vector<std::string> errors = cases[i]->getErrors();
            for( unsigned int j = 0; j < errors.size(); j++ )
            {
               stream << "  " << errors[j] << endl;
            }
         }
      }
      stream << endl;
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   void TestSuite::testMetricsToStream( std::ostream &stream ) const
   {
      stream << name << " Metrics" << endl;
      stream << "Number of tests run:    " << metrics.getTotal() << endl;
      stream << "Number of tests passed: " << metrics.getNumPassed() << endl;
      stream << "Number of tests failed: " << metrics.getNumFailed() << endl;
      stream << "Percent passed:         " << std::fixed << std::setprecision( 2 )
             << metrics.getPercentPassed() << "%" << endl;
      stream << endl;
   }
   
}
