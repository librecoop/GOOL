#! /bin/bash

# Clean file
mv ClassWrapperOut.java ClassWrapperOut.tmp
rm *.java
rm *.jj
rm *.class
mv ClassWrapperOut.tmp ClassWrapperOut.java

# Compile JavaCC
java -cp javacc.jar jjtree ParserClassOutputTable.jjt
java -cp javacc.jar javacc ParserClassOutputTable.jj
#javac -cp .:javacc.jar *.java