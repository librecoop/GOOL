#ifndef __GOOL_FILE_FOR_CPP_H
#define __GOOL_FILE_FOR_CPP_H


#include <cstdio>
#include <cstring>
#include <string>
#include <climits>
#include <fstream>
#include <iostream>


class GoolFileForCPP  {

private:
	std::string name;

public:

	GoolFileForCPP(std::string name);
	bool createNewFile();
	bool exists();
	bool deleteFile();
	std::string getName();
};
#endif
