#ifndef __GOOL_FILE_FOR_CPP_H
#define __GOOL_FILE_FOR_CPP_H


#include <cstdio>
#include <climits>
#include <cstring>



class GoolFileForCPP  {
		private:	std::string name = null ; 

		public: 	GoolFileImpl(std::string name);

		public: 	bool createNewFile();

		public: 	bool exists();

		public: 	bool deleteFile();

	
};
#endif
