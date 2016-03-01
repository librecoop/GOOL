// Platform: CPP


#include <boost/any.hpp>
#include <boost/lexical_cast.hpp>
#include <vector>;
#include <string>;
#include <iostream>;

#include <cmath>

#include "test.h"

	  test::test() {
		
	}

	int main() {
		try
		{
			std::vector<std::string>* list = (new std::vector<std::string>());
			std::vector<std::string>* list2 = (new std::vector<std::string>());
			list->push_back("toto");
			list->push_back("tata");
			list2->push_back("toto");
			list2->push_back("tata");
			if (list -> equals(list2)) {
				std::cout << "true" << std::endl;
			}else {
				std::cout << "false" << std::endl;
			};
			list2->push_back("titi");
			if (list -> equals(list2)) {
				std::cout << "true" << std::endl;
			}else {
				std::cout << "false" << std::endl;
			};
		}
		catch (std::exception * e)
		{
			std::cout << e->what() << std::endl;
		}
		;
		std::cout << (std::abs(-(20.5))) << std::endl;
	}


