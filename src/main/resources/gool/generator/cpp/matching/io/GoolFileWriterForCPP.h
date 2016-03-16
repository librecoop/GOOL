#ifndef __GOOL_FILE_WRITER_FOR_CPP_H
#define __GOOL_FILE_WRITER_FOR_CPP_H

#include <string>

#include "GoolFileForCPP.h"


class GoolFileWriterForCPP{

private:

	GoolFileForCPP * f = NULL;

public:

	GoolFileWriterForCPP(GoolFileForCPP * f);
	std::string getName();

};
#endif
