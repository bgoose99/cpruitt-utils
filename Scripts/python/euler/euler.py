#!/usr/bin/python

# sys imports
import sys

# local imports
import problems.ProblemFactory

def main():
   print ''
   print 'Welcome to the Python Project Euler solver.'
   print ''
   
   pf = problems.ProblemFactory.ProblemFactory()
   
   problemNum = -1
   
   while( True ):
      print 'Enter problem # (q to quit, l for list)'
      problemNum = raw_input( ' >>> ' )
      
      if( problemNum == 'q' ):
         break
      elif( problemNum == 'l' ):
         pf.printAvailableProblems()
         continue
      
      p = pf.getProblem( problemNum )
      
      if( p != 0 ):
         p.printDescription()
         p.solveProblem()
   
   sys.exit( 0 )

if __name__ == '__main__':
   main()
