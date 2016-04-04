#ifndef __GOOL_BUFFERED_WRITER_FOR_CPP_H
#define __GOOL_BUFFERED_WRITER_FOR_CPP_H

#include <cstdio>

#include "GoolFileWriterForCPP.h"

class GoolBufferedWriterForCPP{

private:

	GoolFileWriterForCPP * writer = NULL;
	FILE* file = NULL ;

public:

	GoolBufferedWriterForCPP(GoolFileWriterForCPP * writer );
	void write(char carac);
	void write(std::string toAdd,int start,int length);
	void close();

};
#endif
