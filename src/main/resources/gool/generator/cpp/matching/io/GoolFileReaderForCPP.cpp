#include "GoolFileReaderForCPP.h"

GoolFileReaderForCPP::GoolFileReaderForCPP(GoolFileForCPP * f){
	this->f = f;
}


std::string GoolFileReaderForCPP::getName(){
	return this->f->getName();
}
