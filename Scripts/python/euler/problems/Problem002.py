
from AbstractProblem import AbstractProblem

class Problem002( AbstractProblem ):
   number = '2'
   desc = 'Each new term in the Fibonacci sequence is generated by\n' \
      'adding the previous two terms. By starting with 1 and 2, the\n' \
      'first 10 terms will be:\n' \
      '1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...\n' \
      'By considering the terms in the Fibonacci sequence whose values\n' \
      'do not exceed four million, find the sum of the even-valued terms.\n'
   
   def __init__( self ):
      super( Problem002, self ).__init__( self.number, self.desc )
   
   def solveProblemInternal( self ):
      ans = 0
      prevTerm = 1
      nextTerm = 2
      temp = 0
      while( nextTerm < 4000000 ):
         if( nextTerm % 2 == 0 ):
            ans += nextTerm
         temp = prevTerm + nextTerm
         prevTerm = nextTerm
         nextTerm = temp
      
      print 'Answer:', ans
