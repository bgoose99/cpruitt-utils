#!/usr/bin/python

import sys

# local imports
import utils
import io.fileutils

def main():
	print 'fileutils example'
	
	print
	
	print 'interrogate example'
	utils.interrogate( utils.interrogate )
	print
	
	sys.exit( 0 )

if __name__ == '__main__':
	main()