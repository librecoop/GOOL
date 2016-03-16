#include "GoolFileForCPP.h"

GoolFileForCPP::GoolFileForCPP(std::string nameIn)
:
name(nameIn)
{}


bool GoolFileForCPP::createNewFile(){
	std::ofstream f;
	f.open (name.c_str(), std::ios::out);
	bool toReturn = (f.is_open());
	f.close();
	return toReturn;
}


bool GoolFileForCPP::exists(){
	std::ifstream f(name.c_str());
	if (f.good()) {
		f.close();
		return true;
	} else {
		f.close();
		return false;
	}
}


bool GoolFileForCPP::deleteFile(){
	return !std::remove(name.c_str());
}

std::string GoolFileForCPP::getName(){
	return this->name;
}
