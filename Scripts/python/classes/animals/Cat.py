
from IAnimal import IAnimal

class Cat ( IAnimal ):
	"""Kitty class."""
	
	def __init__( self, myName ):
		"""Constructor."""
		super( Cat, self ).__init__( myName )
	
	def speak( self ):
		"""Make me say something."""
		print self.name, 'says Meow'
