import os
class GoolFileForPY:

    
    def __init__(self,name):
		self.name = name ;
		
    def exists(self):
		try:
			with open(self.name): pass
		except IOError:
			return false;
		return true; 

	def createNewFile(self):
		 mfile = open(self.name, "w");
		 mfile.close();
	
	def deleteFile(self):
		remove(self.name);
