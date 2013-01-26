package gool.generator.common;

import gool.ast.constructs.ClassDef;
import gool.generator.common.CodeGenerator;

/**
 * Implemented by generators that do not use a Velocity template
 * @author Nicolas Bouscarle
 *
 */
public interface CodeGeneratorNoVelocity extends CodeGenerator {
	
	/**
	 * Convert a GOOL representation of a class to code in the generated language
	 * @param pclass class to output
	 * @return the corresponding code
	 */
	String printClass(ClassDef pclass);

}
