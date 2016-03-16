#include "GoolBufferedWriterForCPP.h"

GoolBufferedWriterForCPP::GoolBufferedWriterForCPP(GoolFileWriterForCPP * writer){
	this->file = fopen(writer->getName().c_str(), "w");
}

void GoolBufferedWriterForCPP::write(char carac){
	fputc(carac, this->file);
}

void GoolBufferedWriterForCPP::write(std::string toAdd,int start,int length){
	if (length < 0)
		return;
	if (start > toAdd.size())
		return;
	if (start + length > toAdd.size())
		return;
	for(int i=start; i<length; i++)
		fputc(toAdd[i], this->file);
}

void GoolBufferedWriterForCPP::close(){
	fclose(this->file);
}
