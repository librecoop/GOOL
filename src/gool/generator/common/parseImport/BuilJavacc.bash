#! /bin/bash

# Clean file
mv ImportWrapperOut.java ImportWrapperOut.tmp
rm *.java
rm *.jj
rm *.class
mv ImportWrapperOut.tmp ImportWrapperOut.java

# Compile JavaCC
java -cp javacc.jar jjtree ParserImportOutputTable.jjt
java -cp javacc.jar javacc ParserImportOutputTable.jj
#javac -cp .:javacc.jar *.java