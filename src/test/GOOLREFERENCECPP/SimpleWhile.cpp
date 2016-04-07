
#include <boost/any.hpp>
#include <boost/lexical_cast.hpp>
#include <iostream>
#include <string>



#include "SimpleWhile.h"

SimpleWhile::SimpleWhile() {
	
}

int main() {
	int i = 0;
	int total = 0;
	while ((i < 4)) {
		(total)++;
		(i)++;
	}
	std::cout << total << std::endl;
}


