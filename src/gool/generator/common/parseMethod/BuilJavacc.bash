#! /bin/bash

# Clean file
mv MethodWrapperOut.java MethodWrapperOut.tmp

rm *.java
rm *.jj
rm *.class

mv MethodWrapperOut.tmp MethodWrapperOut.java

# Compile JavaCC
java -cp javacc.jar jjtree ParserMethodOutputTable.jjt
java -cp javacc.jar javacc ParserMethodOutputTable.jj
#javac -cp .:javacc.jar *.java