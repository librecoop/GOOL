package gool.ast.type;

import gool.ast.constructs.ClassDef;
import gool.generator.GoolGeneratorController;

public class TypeBufferedReader extends ReferenceType {

	/**
	 * The class where the list was defined.
	 */
	private ClassDef classDef;
	
	public TypeBufferedReader(){}

	public TypeBufferedReader(IType elementType) {
		this();
		addArgument(elementType);
	}
	
	public IType getElementType() {
		if (getTypeArguments().size() > 0) {
			return getTypeArguments().get(0);
		}
		return null;
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
	public ClassDef getClassDef() {
		return classDef;
	}

	public void setClassDef(ClassDef classDef) {
		this.classDef = classDef;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TypeFile && getName().equals(((IType)obj).getName());
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public String getName() {
		return this.callGetCode();
	}

}