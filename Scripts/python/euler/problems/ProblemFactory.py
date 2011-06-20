
import problems.Problem001
import problems.Problem002

class ProblemFactory( object ):
   problems = {}
   
   def __init__( self ):
      p1 = problems.Problem001.Problem001()
      p2 = problems.Problem002.Problem002()
      
      self.problems[p1.number] = p1
      self.problems[p2.number] = p2
   
   def printAvailableProblems( self ):
      print ''
      print '################################################################################'
      print 'Available problems:'
      keyList = self.problems.keys()
      for item in keyList:
         print '   ', item
      print '################################################################################'
      print ''
   
   def getProblem( self, number ):
      if( ( number in self.problems ) == True ):
         return self.problems[number]
      else:
         print 'Invalid key:', number
         return 0
