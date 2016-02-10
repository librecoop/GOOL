#include "GoolFileForCPP.h"

GoolFileForCPP::GoolFileForCPP(std::string name){
	this->name=name;
}


bool GoolFileForCPP::createNewFile(){
	char filename[UINT_MAX];
	strcpy(filename, this->name.c_str());
	FILE* file = fopen (filename, "w+");
	bool toReturn = (file == null);
	fclose(file);
	return toReturn;
}


bool GoolFileForCPP::exists(){
	char filename[UINT_MAX];
	strcpy(filename, this->name.c_str());
	FILE* file = fopen (filename, "w");
	bool toReturn = (file == null);
	fclose(file);
	return toReturn;
}


bool GoolFileForCPP::deleteFile(){
	char filename[UINT_MAX];
	strcpy(filename, this->name.c_str());
	return remove(filename) != -1;
}
