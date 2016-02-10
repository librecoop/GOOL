#include "GoolBufferedReaderForCPP.h"

GoolBufferedReaderForCPP::GoolBufferedReaderForCPP(GoolFileReaderForCPP reader ){
	file = fopen(reader.getName(), "r");
}

int GoolBufferedReaderForCPP::read(){
	return fgetc(file);
}

std::string GoolBufferedReaderForCPP::readLine(){
	char str[UINT_MAX];
	std::string toReturn = std::string ( fgets ( str, UINT_MAX, file ));
	return toReturn;
}

void GoolBufferedReaderForCPP::close(){
	fclose(this->file);
}

