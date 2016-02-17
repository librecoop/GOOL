import GoolFileReaderForPY
class GoolBufferedReaderForPY:

    
    def __init__(self,reader):
		self.reader =  reader;
		self.fileR = open(self.reader, "r");
	
	def read(self):
		return self.fileR.read1();
		
	def readLine(self):
		self.fileR.readLine();
		
	def close(self)
		self.fileR.close();
