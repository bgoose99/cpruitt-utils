
// system includes
#include <iostream>

// StarcTest includes
#include "TestSuite.h"

// local includes
#include "PropagatorTest.h"

int main( int argc, char **argv )
{
   StarcTest::TestSuite mainSuite( "CppUtils Test Suite" );
   
   // add all test cases
   mainSuite.addTestCase( new PropagatorTest() );

   // run tests
   mainSuite.runTests();

   // output results
   mainSuite.testResultsToStream( std::cout );
   mainSuite.testMetricsToStream( std::cout );

   getchar();
   return 0;
}
