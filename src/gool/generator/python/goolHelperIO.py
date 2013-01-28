import os

class File(object):
	def __init__(self, *args, **kwargs):
		if len(args) == 1 and isinstance(args[0], str):
			self.inializeFromStr(args[0])

	def inializeFromStr(self, pathname):
		self.pathname = pathname

	def getAbsolutePath(self):
		os.path.abspath(self.pathname)

	def getName(self):
		return self.pathname

	def mkdir(self):
		if not os.path.exists(self.pathname):
			os.makedirs(self.pathname)
			return True
		return False

class FileReader(object):
	def __init__(self, *args, **kwargs):
		if len(args) == 1:
			if isinstance(args[0], str):
				self.inializeFromStr(args[0])
			elif isinstance(args[0], File):
				self.inializeFromFile(args[0])

	def inializeFromStr(self, pathname):
			self.file = open(pathname, 'r')

	def inializeFromFile(self, filee):
			self.file = open(filee.getName(), 'r')

	def read(self):
		return self.file.read(1)

	def getFile(self):
		return self.file

	def close(self):
		return self.file.close()

class FileWriter(object):
	def __init__(self, *args, **kwargs):
		if len(args) == 1:
			if isinstance(args[0], str):
				self.inializeFromStr(args[0])
			elif isinstance(args[0], File):
				self.inializeFromFile(args[0])

	def inializeFromStr(self, pathname):
			self.file = open(pathname, 'w')

	def inializeFromFile(self, filee):
			self.file = open(filee.getName(), 'w')

	def write(self, *args, **kwargs):
		nbArgs = len(args)
		if nbArgs == 1:
			self.writeStr(args[0])
		elif nbArgs==3 and isinstance(args[0], str) and isinstance(args[1], int) and isinstance(args[2], int):
			self.writeStr(args[0][args[1]:args[2]])

	def writeStr(self, string):
		self.file.write(string)

	def getFile(self):
		return self.file

	def close(self):
		return self.file.close()


class BufferedReader(object):
	def __init__(self, *args, **kwargs):
		if len(args) == 1:
			if isinstance(args[0], FileReader):
				self.inializeFromStr(args[0])

	def inializeFromStr(self, reader):
			self.reader = reader

	def read(self):
		return self.reader.read()

	def readLine(self):
		return self.reader.getFile().readline()

	def close(self):
		return self.reader.close()
