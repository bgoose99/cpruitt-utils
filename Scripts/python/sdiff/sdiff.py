#!/usr/bin/python

import sys
import os

def usage():
   """
   Prints usage info.
   """
   print ''
   print 'SD is a simple Python script that performs a Subversion'
   print 'status (svn st -u) on the current directory anc checks'
   print 'the output for modified files. A difference report is'
   print 'then generated, comparing the modified version with the'
   print 'version in the HEAD of the repository.'
   print ''

def diffFile( filename ):
   """
   Performs a diff on the supplied file.
   """
   if( os.path.isfile( filename ) is not True ):
      return
   
   print 'Diffing', filename
   tempFile = '.sdiffTemp'
   cmd = '/usr/bin/svn cat -r HEAD ' + filename + ' > ' + tempFile
   os.system( cmd )
   cmd = '/usr/bin/meld ' + tempFile + ' ' + filename
   os.system( cmd )
   os.remove( tempFile )
   return

def diffFiles():
   """
   Gets a list of modified files from Subversion and diffs each one.
   """
   p = os.popen( '/usr/bin/svn st -u' )
   
   for line in p:
      strings = line.split()
      if( len( strings ) >= 3 ):
         if( strings[0].find( 'M' ) != -1 ):
            diffFile( strings[2] )
   
   p.close()

def main():
   """
   Entry point
   """
   # pop the first arg, which is the script name
   sys.argv.pop( 0 )
   
   if( len( sys.argv ) != 0 ):
      usage()
      sys.exit( 0 )
   
   print ''
   diffFiles()
   print 'Done'
   sys.exit( 0 )

# standard template to call main
if __name__ == '__main__':
   main()
