#!/bin/sh

os=`uname`
if [ "$os" != "Linux" ]
then
	echo "This command must be run on a Linux machine."
	exit -1
fi

BASE=".svndiffBase"
HEAD=".svndiffHead"
REV1=".svndiffRev1"
REV2=".svndiffRev2"

if [ $# == 1 ]
then
	
	# run standard BASE / HEAD / MINE diff
	/usr/bin/svn cat -r BASE "$1" > "$BASE"
	/usr/bin/svn cat -r HEAD "$1" > "$HEAD"
	/usr/bin/meld "$BASE" "$HEAD" "$1"
	rm "$BASE" "$HEAD"
	
elif [ $# == 2 ]
then
	
	# run diff with only BASE or HEAD, dictated by $1
	if [ $1 == "-h" ]
	then
		
		/usr/bin/svn cat -r HEAD "$2" > "$HEAD"
		/usr/bin/meld "$HEAD" "$2"
		rm "$HEAD"
		
	elif [ $1 == "-b" ]
	then
		
		/usr/bin/svn cat -r BASE "$2" > "$BASE"
		/usr/bin/meld "$BASE" "$2"
		rm "$BASE"
		
	else
		echo "Option $1 not supported."
		exit -1
	fi
	
elif [ $# == 3 ]
then
	
	# run diff against version $2 and $3
	if [ $1 != "-p" ]
	then
		echo "Option $1 not supported."
		exit -1
	fi
	
	/usr/bin/svn cat -r "$2" "$3" > "$REV1"
	/usr/bin/meld "$REV1" "$3"
	rm "$REV1"
	
elif [ $# == 4 ]
then
	
	# run diff against version $2 and $3
	if [ $1 != "-r" ]
	then
		echo "Option $1 not supported."
		exit -1
	fi
	
	/usr/bin/svn cat -r "$2" "$4" > "$REV1"
	/usr/bin/svn cat -r "$3" "$4" > "$REV2"
	/usr/bin/meld "$REV1" "$REV2"
	rm "$REV1" "$REV2"
	
else
	echo ""
	echo "****************************************************************"
	echo "USAGE: $0 [flags] [flag options] <filename>"
	echo ""
	echo "Valid flags:"
	echo "-h               : diff with only the HEAD"
	echo "-b               : diff with only the BASE"
	echo "-p [rev]         : diff with the specified previous version"
	echo "-r [rev1] [rev2] : diff the two specified revisions"
	echo "****************************************************************"
	echo ""
	exit 0
fi