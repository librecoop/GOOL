#include "GoolFileWriterForCPP.h"

GoolFileWriterForCPP::GoolFileWriterForCPP(GoolFileForCPP * f){
	this->f = f;
}

std::string GoolFileWriterForCPP::getName(){
	return this->f->getName();
}
