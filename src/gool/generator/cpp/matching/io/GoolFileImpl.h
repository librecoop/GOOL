#include<iostream>
#ifndef __GOOLFILEIMPL_H
#define __GOOLFILEIMPL_H


#include "stdio.h"
#include "fstream"
#include "cstdio"




class GoolFileImpl  {

		public: 	GoolFileImpl(std::string* name);

		public: 	bool createNewFile();

		public: 	bool exists();

		public: 	bool deleteFile();

	
};
#endif
