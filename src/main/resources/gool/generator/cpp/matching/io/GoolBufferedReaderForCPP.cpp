#include "GoolBufferedReaderForCPP.h"

GoolBufferedReaderForCPP::GoolBufferedReaderForCPP(GoolFileReaderForCPP * reader ){
	file = fopen(reader->getName().c_str(), "r");
}

int GoolBufferedReaderForCPP::read(){
	return fgetc(file);
}

std::string GoolBufferedReaderForCPP::readLine(){
	char str[999999];
	char * toReturnChar = fgets ( str, 999999, file );
	if (toReturnChar == NULL)
		return "";
	std::string toReturn(toReturnChar);
	return toReturn.substr(0, toReturn.size()-1);
}

void GoolBufferedReaderForCPP::close(){
	fclose(this->file);
}

