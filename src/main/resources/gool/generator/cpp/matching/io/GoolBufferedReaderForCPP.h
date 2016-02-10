#ifndef __GOOL_BUFFERED_READER_FOR_CPP_H
#define __GOOL_BUFFERED_READER_FOR_CPP_H

#include "GoolFileReaderForCPP.h"
#include <cstdio>



class GoolBufferedReaderForCPP  {
		private:
				FILE* file = null ;

		public: 	GoolBufferedReaderForCPP(GoolFileReaderForCPP reader );
				int read();
				std::string readLine();
				void close();
	
};
#endif
