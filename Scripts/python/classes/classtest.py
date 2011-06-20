#!/usr/bin/python

# sys imports
import sys

# local imports
import animals.Cat
import animals.Dog

def main():
	# package.module.classname
	cat = animals.Cat.Cat( 'Tommy' )
	cat.speak()
	
	dog = animals.Dog.Dog( 'Muttly' )
	dog.speak()
	
	sys.exit( 0 )

if __name__ == '__main__':
	main()