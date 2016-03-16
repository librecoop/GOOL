import GoolFileReaderForPY


class GoolBufferedReaderForPY:

    def __init__(self, reader):
        self.reader = reader
        self.fileR = open(reader.getName(), "r")

    def read(self):
        return self.fileR.read(1)

    def readLine(self):
        toReturn = self.fileR.readline().rstrip()
        if not toReturn:
            return None
        return toReturn

    def close(self):
        self.fileR.close()
