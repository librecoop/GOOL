import os

class File:
	def __init__(self, *args):
		if len(args) == 1 and isinstance(args[0], str):
			self.inializeFromStr(args[0])

	def inializeFromStr(self, pathname):
		self.pathname = pathname

	def mkdir(self):
		if not os.path.exists(self.pathname):
			os.makedirs(self.pathname)
			return True
		return False

