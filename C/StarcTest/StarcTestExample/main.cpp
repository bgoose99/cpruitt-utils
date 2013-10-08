
// system includes
#include <iostream>

// StarcTest includes
#include "TestSuite.h"

// contrived example class
class Foo
{
   public:
      Foo() {};
      ~Foo() {};
      int addIntegers( const int &a, const int &b ) const { return a + b; }
      double addDoubles( const double &a, const double &b ) const { return a + b; }
};

// contrived test class
class FooTest : public StarcTest::TestCase
{
   public:
      FooTest() : StarcTest::TestCase( "Foo Test" ) {}
      ~FooTest() {}
      bool setUp()
      {
         testInt = 0;
         testDouble = 0.0;
         return true;
      }
      void tearDown()
      {
         testInt = 0;
         testDouble = 0.0;
      }
   protected:
      void runAllTests()
      {
         // test integer addition
         verifyEqual( f.addIntegers(  1, 2 ), 3, "Integer addition test 1" );
         verifyEqual( f.addIntegers( -1, 6 ), 5, "Integer addition test 2" );
         verifyEqual( f.addIntegers(  5, 0 ), 5, "Integer addition test 3" );
         verifyNotEqual( f.addIntegers( 5, 5 ), 1, "Integer addition test 4" );

         // test double addition
         verifyEqual( f.addDoubles( 1.0, 6.0 ),  7.0, "Double addition test 1" );
         verifyEqual( f.addDoubles( 5.6, 7.1 ), 12.7, "Double addition test 2" );
         verifyEqual( f.addDoubles( 1, 1/3.0 ), 0.33, "Double addition test 3" ); // failure
         verifyApproxEqual( f.addDoubles( 0.123, 0.333333333 ), 0.456333333, "Double addition test 4" );
      }
   private:
      int testInt;
      double testDouble;
      Foo f;
};

// program entry point
int main( int argc, char **argv )
{
   StarcTest::TestSuite mainSuite( "StarcTest Example Suite" );

   mainSuite.addTestCase( new FooTest() );

   mainSuite.runTests();

   mainSuite.testResultsToStream( std::cout );
   mainSuite.testMetricsToStream( std::cout );

   getchar();
   return 0;
}
