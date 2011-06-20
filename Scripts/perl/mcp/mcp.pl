#!/usr/bin/env perl
use warnings;
use strict;

my $move;
my $searchStr;
my $replaceStr;

# display usage
sub usage
{
	print "\nMCP is a perl script that can be used to copy/move one\n";
	print "or more files, replacing a search string in the filename.\n";
	print "USAGE:\n";
	print "mcp [-m] SEARCH REPLACE FILES\n";
	print "\tWhere SEARCH is a string you are searching for,\n";
	print "\tREPLACE is a string you would like to replace\n";
	print "\tSEARCH with, and FILES is a list of files to search\n";
	print "\tthrough.\n";
	print "\tThe -m option specified that the files be moved instead of\n";
	print "\tcopied.\n\n";
}

# copies/moves a single file, if necessary
sub dupFile( $ )
{
	my $file = $_[0];
	
	if( $file =~ m/$searchStr/ )
	{
		my $newFile = "$file";
		$newFile =~ s/$searchStr/$replaceStr/;
		
		if( $move )
		{
			print "mv $file $newFile\n";
			system( "mv $file $newFile" );
		} else
		{
			print "cp $file $newFile\n";
			system( "cp $file $newFile" );
		}
	}
}

# make sure we have at least 3 arguments
if( scalar( @ARGV ) < 3 )
{
	usage();
} else
{
	if( $ARGV[0] eq "-m" )
	{
		$move = 1;
		shift( @ARGV );
	}
	
	$searchStr = shift( @ARGV );
	@replaceStr = shift( @ARGV );
	
	while( scalar( @ARGV ) )
	{
		dupFile( shift( @ARGV ) );
	}
}
