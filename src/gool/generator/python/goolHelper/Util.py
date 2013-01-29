class Scanner(object):
	def __init__(self, *args, **kwargs):
		if len(args) == 1 and isinstance(args[0], str):
			self.inializeFromStr()

	def inializeFromStr(self):
        	super(test, self).__init__()

	def nextLine(self):
		return raw_input()

	def nextInt(self):
		return int (raw_input())

	def nextDouble(self):
		return raw_input()
	
	def nexByte(self):
		return raw_input()
