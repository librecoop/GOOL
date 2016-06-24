
#include <boost/any.hpp>
#include <boost/lexical_cast.hpp>
#include <string>
#include <iostream>



#include "SimpleFor.h"

SimpleFor::SimpleFor() {
	
}

int main() {
	int total = 0;
	for (int i = 0 ; (i < 4) ; (i)++) {
		(total)++;
	}
	std::cout << total << std::endl;
}


