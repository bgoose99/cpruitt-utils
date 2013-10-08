
// local includes
#include "TestMetrics.h"

namespace StarcTest
{
   
   /****************************************************************************
    * 
    ***************************************************************************/
   TestMetrics::TestMetrics() :
      numPassed( 0 ),
      numFailed( 0 )
   {
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   TestMetrics::TestMetrics( const TestMetrics &that ) :
      numPassed( that.numPassed ),
      numFailed( that.numFailed )
   {
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   TestMetrics::~TestMetrics()
   {
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   TestMetrics &TestMetrics::operator=( const TestMetrics &that )
   {
      numPassed = that.numPassed;
      numFailed = that.numFailed;
      return *this;
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   TestMetrics &TestMetrics::operator+=( const TestMetrics &that )
   {
      numPassed += that.numPassed;
      numFailed += that.numFailed;
      return *this;
   }
   
   /****************************************************************************
    * 
    ***************************************************************************/
   double TestMetrics::getPercentPassed() const
   {
      int denom = getTotal();
      return denom == 0 ? 0.00 : ( numPassed * 1.0 / denom ) * 100.0;
   }
   
}
