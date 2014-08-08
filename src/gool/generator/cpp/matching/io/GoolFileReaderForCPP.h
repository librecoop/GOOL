#ifndef __GOOL_FILE_READER_FOR_CPP_H
#define __GOOL_FILE_READER_FOR_CPP_H


#include <climits>
#include <cstring>



class GoolFileReaderForCPP  {
		private:	char* name = null ; 

		public: 	GoolFileReaderForCPP(std::string name);
				char* getName();
	
};
#endif
