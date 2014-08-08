#include "GoolFileReaderForCPP.h"

GoolFileReaderForCPP::GoolFileReaderForCPP(std::string name){
	char filename[UINT_MAX];
	strcpy(filename, this->name.c_str());
	this->name=filename;
}

char* GoolFileReaderForCPP::getName(){
	return this->name;
}
