GOOL
====

Description
==

The most widely used Object-Oriented Programming languages (Java, C++, C#, Android, Objective C) are all roughly the same; so long as one does not enter device-dependent programming (GUI, H/W, ...). The core constructs of these languages can be translated into one another.

GOOL stands for General Object-Oriented Language. It is not yet another concrete language, but rather, an abstract intermediate language which stands for whatever is common in all these Object-Oriented Programmming (OOP) languages. At the moment the GOOL system takes a Java input and produces a C++, C# output. Thus, as it stands, the GOOL program is a lightweight core-Java to core- C++, C# translator generating clean, readable code.

However, everything is done so that, in the future, the GOOL program takes other OOP languages as inputs, and produces other OOP languages as outputs. Indeed, the structure of the project is so that more input/output languages can be added, and more primitives be recognized/generated.

Still, the final objective is not to reach a comprehensive translator. The aim is to provide a tool that lets you write device-independent pieces of programs for many output languages, in one go. The device-dependent pieces of program do not prevent you from using GOOL; although they will not be implemented into the output language, they will still be translated in a generic, syntactic manner.


Read More at the project Wiki: https://github.com/librecoop/GOOL/wiki
