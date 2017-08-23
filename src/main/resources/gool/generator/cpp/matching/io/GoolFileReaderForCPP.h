#ifndef __GOOL_FILE_READER_FOR_CPP_H
#define __GOOL_FILE_READER_FOR_CPP_H

#include <string>

#include "GoolFileForCPP.h"


class GoolFileReaderForCPP{

private:

	GoolFileForCPP * f;

public:

	GoolFileReaderForCPP(GoolFileForCPP * f);
	std::string getName();

};
#endif
