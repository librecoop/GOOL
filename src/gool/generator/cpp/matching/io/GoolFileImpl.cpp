#include<iostream>

#include <boost/any.hpp>
#include <boost/lexical_cast.hpp>
//using boost::any_cast;

#include "GoolFileImpl.h"

#include "stdio.h"
#include "fstream"
#include "cstdio"




	GoolFileImpl::GoolFileImpl(std::string* name){
		const char *tmp = name->c_str();
		outputlangobject = fopen(tmp, "rw");
		filename = name;
	}


	bool GoolFileImpl::createNewFile(){
		//const char *tmp = filename->c_str();
		//fopen(tmp, "rw");
		//std::ifstream fichier(tmp);
		//return !fichier.fail();
		if(exists())
			return false;
		const char *tmp = filename->c_str();
		outputlangobject=fopen(tmp,"rw");
		return true;
	}


	bool GoolFileImpl::exists(){
		const char *tmp = filename->c_str();
		std::ifstream tmpfile(tmp);
		return !tmpfile.fail();
	}


	bool GoolFileImpl::deleteFile(){
		const char *tmp = filename->c_str();
		if(remove(tmp)==0)
			return true;
		else
			return false;
	}


	/* The GOOL method getOutputlangobject():GoolFileImpl has not been implemented yet for this output language. */

	/* The GOOL method getFilename():TypeString has not been implemented yet for this output language. */


