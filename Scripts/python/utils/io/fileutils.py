
def echoFile( filename ):
	"""Echoes a file to stdout."""
	try:
		try:
			f = open( filename )
			for line in f:
				print line,
		except:
			print 'Error!'
	finally:
		f.close()
		