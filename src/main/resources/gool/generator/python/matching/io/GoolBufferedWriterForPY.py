import GoolFileWriterForPY
class GoolBufferedWriterForPY:

    
    def __init__(self,writer):
		self.writer =  writer;
		self.fileW = open(self.writer, "w");
	
	def write(self,carac):
		self.fileW.write(carac);
		
	def write(self,carac,start,length):
		self.fileW.write(carac,start,length);
		
	def close(self)
		self.fileW.close();
