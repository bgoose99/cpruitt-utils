
from IAnimal import IAnimal

class Dog ( IAnimal ):
	"""Doggy class."""
	
	def __init__( self, myName ):
		"""Constructor."""
		super( Dog, self ).__init__( myName )
	
	def speak( self ):
		"""Make me say something."""
		print self.name, 'says Woof'
