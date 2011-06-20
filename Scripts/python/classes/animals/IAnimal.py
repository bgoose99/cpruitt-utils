
class IAnimal ( object ):
	"""Animal interface."""
	name = 'Unnamed'
	
	def __init__( self, myName ):
		self.name = myName
	
	def speak( self ):
		pass
