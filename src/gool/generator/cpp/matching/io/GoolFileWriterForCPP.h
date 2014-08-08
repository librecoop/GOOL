#ifndef __GOOL_FILE_WRITER_FOR_CPP_H
#define __GOOL_FILE_WRITER_FOR_CPP_H


#include <climits>
#include <cstring>



class GoolFileWriterForCPP  {
		private:	char* name = null ; 

		public: 	GoolFileWriterForCPP(std::string name);
				char* getName();
	
};
#endif
