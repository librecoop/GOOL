package gool.generator.common;

import java.util.ArrayList;

import gool.ast.core.ClassDef;
import gool.ast.core.Modifier;
import gool.ast.core.Package;

public class GeneratorMatcherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		String goolClass = "gool.io.GoolFile";
		ClassDef GoolClassAST = new ClassDef(goolClass.substring(goolClass
				.lastIndexOf(".") + 1));
		GoolClassAST.setIsGoolLibraryClass(true);
		GoolClassAST.setIsEnum(false);
		GoolClassAST.setIsInterface(false);
		GoolClassAST.addModifier(Modifier.PUBLIC);
		GoolClassAST.setPpackage(new Package(goolClass.substring(0,
				goolClass.lastIndexOf("."))));
		
		System.out.println(GoolClassAST.getName());
		System.out.println(GoolClassAST.getPackageName());
		ArrayList<String> imports = GeneratorMatcher.matchImports(GoolClassAST.getPackageName()+"."+GoolClassAST.getName());
		
		System.exit(0);*/
		String goolClass = "gool.io.GoolFile";
		System.out.println(GeneratorMatcher.matchGoolClass("gool.io.GoolFile"));
		System.out.println(GeneratorMatcher.matchGoolClass("gool.io.GoolFileReader"));
		System.out.println(GeneratorMatcher.matchGoolClass("gool.io.GoolFileWriter"));
		System.out.println(GeneratorMatcher.matchGoolClass("gool.io.GoolBufferedReader"));
		System.out.println(GeneratorMatcher.matchGoolClass("gool.io.GoolBufferedWriter"));
		
		System.out.println();
		
		for(String res : GeneratorMatcher.matchImports("gool.io.GoolFile"))
			System.out.print(res+" , ");
		System.out.println();
		for(String res : GeneratorMatcher.matchImports("gool.io.GoolFileReader"))
			System.out.print(res+" , ");
		System.out.println();
		for(String res : GeneratorMatcher.matchImports("gool.io.GoolFileWriter"))
			System.out.print(res+" , ");
		System.out.println();
		for(String res : GeneratorMatcher.matchImports("gool.io.GoolBufferedReader"))
			System.out.print(res+" , ");
		System.out.println();
		for(String res : GeneratorMatcher.matchImports("gool.io.GoolBufferedWriter"))
			System.out.print(res+" , ");
		System.out.println();
		
		System.out.println();
		
		System.out.println(GeneratorMatcher.matchGoolClassImplementation("gool.io.GoolFileReader", "GoolBufferedReaderImpl.java"));

	}

}
