package gool.ast.printer;

import gool.generator.common.CodeGenerator;

public final class GoolGeneratorController {
	/**
	 * The current code generator.
	 */
	private static CodeGenerator currentGenerator = new GoolGenerator();
	
	private GoolGeneratorController() {}
	
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
		currentGenerator = new GoolGenerator();
	}
}
