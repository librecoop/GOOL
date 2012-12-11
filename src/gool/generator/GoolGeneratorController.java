package gool.generator;

import gool.generator.common.CodeGenerator;
import gool.generator.java.JavaGenerator;


/**
 * Which CodeGenerator to use is specified by the CodePrinter, which is specified by the Platform, which is held at the level of the class that is being 
 * translated from abstract GOOL to concrete Target.
 * In order to remember which one this is, as we travel this class, we keep it here.
 * By default, the CodeGenerator is GoolGenerator, i.e. just the GOOL pretty printer.
 * @author parrighi
 */
public class GoolGeneratorController {
	/**
	 * The current code generator.
	 */
	private static CodeGenerator currentGenerator = new JavaGenerator();
	
	/**
	 * Specifies the new code generator.
	 * @param generator a new code generator.
	 */
	public static void setCodeGenerator(CodeGenerator generator) {
		currentGenerator = generator;
	}
	
	/**
	 * Gets the current code generator.
	 * @return the current code generator.
	 */
	public static CodeGenerator generator() {
		if (currentGenerator == null) {
			throw new IllegalStateException("The code generator is not properly initialized.");
		}
		return currentGenerator;
	}
	
	public static void reset() {
		currentGenerator = new JavaGenerator();
	}
}
