#! /bin/bash

# Clean file
mv MethodWrapperIn.java MethodWrapperIn.tmp

rm *.java
rm *.jj
rm *.class

mv MethodWrapperIn.tmp MethodWrapperIn.java

# Compile JavaCC
java -cp javacc.jar jjtree ParserMethodInputTable.jjt
java -cp javacc.jar javacc ParserMethodInputTable.jj
#javac -cp .:javacc.jar *.java