package gool.ast.type;

import java.io.InputStream;

import gool.ast.constructs.ClassDef;
import gool.generator.GoolGeneratorController;

public class TypeScanner extends ReferenceType{
	
	
	
	private ClassDef classDef;
	
	public TypeScanner(){}

	public TypeScanner(TypeInputStream im) {
		this();
		addArgument(im);
	}
	
	public IType getElementType() {
		if (getTypeArguments().size() > 0) {
			return getTypeArguments().get(0);
		}
		return null;
	}
	
	public ClassDef getClassDef() {
		return classDef;
	}

	public void setClassDef(ClassDef classDef) {
		this.classDef = classDef;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TypeScanner && getName().equals(((IType)obj).getName());
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public String getName() {
		return this.callGetCode();
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}



}
