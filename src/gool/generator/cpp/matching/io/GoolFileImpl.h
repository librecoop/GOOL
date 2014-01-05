#include<iostream>
#ifndef __GOOLFILEIMPL_H
#define __GOOLFILEIMPL_H


#include "stdio.h"
#include "fstream"
#include "cstdio"




class GoolFileImpl  {
    private:  FILE* outputlangobject;
    private:  std::string* filename;

		public: 	GoolFileImpl(std::string* name);


	
		public: 	bool createNewFile();


	
		public: 	bool exists();


	
		public: 	bool deleteFile();


	
		/* The GOOL method getOutputlangobject():GoolFileImpl has not been implemented yet for this output language. */


	
		/* The GOOL method getFilename():TypeString has not been implemented yet for this output language. */


	
};
#endif
