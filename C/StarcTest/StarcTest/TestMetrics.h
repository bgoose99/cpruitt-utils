
#ifndef __TestMetrics__
#define __TestMetrics__

namespace StarcTest
{
   /****************************************************************************
    * This class simply keeps track of the number of passed/failed tests
    * for reporting purposes.
    ***************************************************************************/
   class TestMetrics
   {
      public:
         
         /**********************************************************************
          * Constructor
          *********************************************************************/
         TestMetrics();
         
         /**********************************************************************
          * Copy constructor
          *********************************************************************/
         TestMetrics( const TestMetrics &that );
         
         /**********************************************************************
          * Destructor
          *********************************************************************/
         ~TestMetrics();
         
         /**********************************************************************
          * Assignment operator
          *********************************************************************/
         TestMetrics &operator=( const TestMetrics &that );
         
         /**********************************************************************
          * Addition operator.
          *********************************************************************/
         TestMetrics &operator+=( const TestMetrics &that );
         
         /**********************************************************************
          * Increments the number of passed tests.
          *********************************************************************/
         inline void testPassed() { numPassed++; }
         
         /**********************************************************************
          * Increments the number of failed tests.
          *********************************************************************/
         inline void testFailed() { numFailed++; }
         
         /**********************************************************************
          * Returns the number of passed tests.
          *********************************************************************/
         inline int getNumPassed() const { return numPassed; }
         
         /**********************************************************************
          * Returns the number of failed tests.
          *********************************************************************/
         inline int getNumFailed() const { return numFailed; }
         
         /**********************************************************************
          * Returns the total number of tests executed.
          *********************************************************************/
         inline int getTotal() const { return numPassed + numFailed; }
         
         /**********************************************************************
          * Returns the percentage of tests that passed.
          *********************************************************************/
         double getPercentPassed() const;
         
      private:
         
         int numPassed;
         int numFailed;
         
   };
}

#endif
