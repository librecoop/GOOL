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
