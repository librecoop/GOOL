import os


class GoolFileForPY:
    
    def __init__(self,name):
        self.name = name

    def exists(self):
        try:
            with open(self.name): pass
        except IOError:
            return False
        return True 

    def createNewFile(self):
        try:
            mfile = open(self.name, "w")
            mfile.close()
            return True
        except:
            return False

    def deleteFile(self):
        try:
            os.remove(self.name)
            return True
        except:
            return False