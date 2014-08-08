#! /bin/bash

# Clean file
mv ClassWrapperIn.java ClassWrapperIn.tmp
rm *.java
rm *.jj
rm *.class
mv ClassWrapperIn.tmp ClassWrapperIn.java

# Compile JavaCC
java -cp javacc.jar jjtree ParserClassInputTable.jjt
java -cp javacc.jar javacc ParserClassInputTable.jj
#javac -cp .:javacc.jar *.java