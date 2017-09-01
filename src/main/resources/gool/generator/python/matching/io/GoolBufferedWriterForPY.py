import GoolFileWriterForPY

class GoolBufferedWriterForPY:

    def __init__(self, writer):
        self.writer = writer
        self.fileW = open(self.writer.getName(), "w")

    def write(self, carac, start=0, length=1):
        self.fileW.write(carac[start:start+length])

    def close(self):
        self.fileW.close()
