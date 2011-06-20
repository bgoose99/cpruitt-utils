
from AbstractProblem import AbstractProblem

class Problem001( AbstractProblem ):
   number = '1'
   desc = 'If we list all the natural number below 10 that are multiples\n' \
      'of 3 or 5, we get 3, 5, 6, and 9. The sum of these multiples is 23.\n' \
      'Find the sum of all the multiples of 3 or 5 below 1000.\n'
   
   def __init__( self ):
      super( Problem001, self ).__init__( self.number, self.desc )
   
   def solveProblemInternal( self ):
      ans = 0
      for i in range( 3, 1000 ):
         if( ( i % 3 == 0 ) or ( i % 5 == 0 ) ):
            ans += i
      
      print 'Answer:', ans
