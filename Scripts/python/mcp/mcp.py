#!/usr/bin/python

import sys

from os import rename
from shutil import copyfile

def usage():
	"""
	Prints usage info.
	"""
	print '\nMCP is a python script that can be used to copy/move one'
	print 'or more files, replacing a search string in the filename.'
	print 'USAGE:'
	print 'mcp [-m] SEARCH REPLACE FILES'
	print '\tWhere SEARCH is a string you are searching for,'
	print '\tREPLACE is a string you would like to replace'
	print '\tSEARCH with, and FILES is a list of files to search'
	print '\tthrough.'
	print '\tThe -m option specified that the files be moved instead of'
	print '\tcopied.\n'

def dupFile( move, search, replace, filename ):
	"""
	Copies or moves a file, if necessary.
	"""
	if( filename.find( search ) != -1:
		newFilename = filename.replace( search, replace )
		
		if move:
			print 'mv', filename, '->', newFilename
			rename( filename, newFilename )
		else:
			print 'cp', filename, '->', newFilename
			copyfile( filename, newFilename )
	
	return False

def main():
	"""
	Entry point
	"""
	# Pop the first arg, which is the script name
	sys.argv.pop( 0 )
	
	# Make sure we have at least three args
	if( len( sys.argv ) < 3:
		usage()
	else
		move = False
		
		if sys.argv[0] == '-m':
			move = True
			sys.argv.pop( 0 )
		
		searchStr = sys.argv.pop( 0 )
		replaceStr = sys.argv.pop( 0 )
		
		for s in sys.argv[:]:
			dupFile( move, searchStr, replaceStr, s )
				
	sys.exit( 0 )

# Standard template to call the main function
if __name__ == '__main__'
	main()
