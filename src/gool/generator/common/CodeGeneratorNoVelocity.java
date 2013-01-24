package gool.generator.common;

import gool.ast.constructs.ClassDef;
import gool.generator.common.CodeGenerator;

public interface CodeGeneratorNoVelocity extends CodeGenerator {
	
	String printClass(ClassDef pclass);

}
