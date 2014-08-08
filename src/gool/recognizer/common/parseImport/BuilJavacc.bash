#! /bin/bash

# Clean file
mv ImportWrapperIn.java ImportWrapperIn.tmp
rm *.java
rm *.jj
rm *.class
mv ImportWrapperIn.tmp ImportWrapperIn.java

# Compile JavaCC
java -cp javacc.jar jjtree ParserImportInputTable.jjt
java -cp javacc.jar javacc ParserImportInputTable.jj
#javac -cp .:javacc.jar *.java