#include "GoolBufferedWriterForCPP.h"

GoolBufferedWriterForCPP::GoolBufferedWriterForCPP(GoolFileWriterForCPP writer ){
	file = fopen(writer.getName(), "w");
}

void GoolBufferedWriterForCPP::write(int carac){
	fput(carac);
}

void GoolBufferedWriterForCPP::write(std::string toAdd,int start,int length){
	char realtoAdd[UINT_MAX];
	strcpy(realtoAdd, this->toAdd.c_str());
	FILE* fileAdd = fopen(writer.getName(), "rw");
	int i = 0;
	for(;i<start;i++){
		fget(fileAdd);
	}
	fwrite ( realtoAddconst, length, sizeof(char), fileAdd );
	fclose(fileAdd);
}

void GoolBufferedWriterForCPP::close(){
	fclose(this->file);
}
