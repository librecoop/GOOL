#include "GoolFileWriterForCPP.h"

GoolFileWriterForCPP::GoolFileWriterForCPP(std::string name){
	char filename[UINT_MAX];
	strcpy(filename, this->name.c_str());
	this->name=filename;
}

char* GoolFileWriterForCPP::getName(){
	return this->name;
}
