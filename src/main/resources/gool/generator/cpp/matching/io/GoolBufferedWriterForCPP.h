#ifndef __GOOL_BUFFERED_WRITER_FOR_CPP_H
#define __GOOL_BUFFERED_WRITER_FOR_CPP_H

#include "GoolFileWriterForCPP.h"
#include <cstdio>



class GoolBufferedWriterForCPP  {
		private:
				FILE* file = null ;

		public: 	GoolBufferedWriterForCPP(GoolFileWriterForCPP writer );
				void write(int carac);
				void write(std::string toAdd,int start,int length);
				void close();
	
};
#endif
