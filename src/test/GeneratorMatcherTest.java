package gool.generator.common;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import gool.ast.core.ClassDef;
import gool.ast.core.Modifier;
import gool.ast.core.Package;
import gool.generator.GoolGeneratorController;
import gool.generator.java.JavaPlatform;

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
		String goolClass = "io.GoolFile";
		GoolGeneratorController.setCodeGenerator(JavaPlatform.getInstance().getCodePrinter().getCodeGenerator());
		GeneratorMatcher.init(JavaPlatform.getInstance());
		System.out.println(GeneratorMatcher.matchGoolClass("io.GoolFile"));
		System.out.println(GeneratorMatcher.matchGoolClass("io.GoolFileReader"));
		System.out.println(GeneratorMatcher.matchGoolClass("io.GoolFileWriter"));
		System.out.println(GeneratorMatcher.matchGoolClass("io.GoolBufferedReader"));
		System.out.println(GeneratorMatcher.matchGoolClass("io.GoolBufferedWriter"));
		
		System.out.println();
		
		for(String res : GeneratorMatcher.matchImports("io.GoolFile"))
			System.out.print(res+" , ");
		System.out.println();
		for(String res : GeneratorMatcher.matchImports("io.GoolFileReader"))
			System.out.print(res+" , ");
		System.out.println();
		for(String res : GeneratorMatcher.matchImports("io.GoolFileWriter"))
			System.out.print(res+" , ");
		System.out.println();
		for(String res : GeneratorMatcher.matchImports("io.GoolBufferedReader"))
			System.out.print(res+" , ");
		System.out.println();
		for(String res : GeneratorMatcher.matchImports("io.GoolBufferedWriter"))
			System.out.print(res+" , ");
		System.out.println();
		
		System.out.println();
		
		System.out.println(GeneratorMatcher.matchGoolClassImplementation("io.GoolFileReader", "GoolBufferedReaderImpl.java"));

	}

}
