#include <iostream>
#include "test.h"

test::test(){}

std::string test::toString(){
	return "Hello World.";
}

int main(int argc, char *argv[]) {
	test t = new test();
	std::cout << t.toString() << std::endl;
	return 0;
}


