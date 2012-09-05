package gool.ast.type;

import gool.GoolGeneratorController;
import gool.ast.ClassDef;
import gool.ast.Constant;
import gool.ast.Expression;

/**
 * This is the basic type for classes defined in the intermediate language.
 */
public class TypeClass extends ReferenceType {

	/**
	 * The class' package.
	 */
	private String packageName;
	/**
	 * The name of the class.
	 */
	private String name;
	/**
	 * The class definition.
	 */
	private ClassDef classDef;
	
	private boolean isEnum;

	public TypeClass(String name) {
		this.name = name;
	}

	public TypeClass(String packageName, String name) {
		this(name);
		this.packageName = packageName;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the literal name of the class. It can be used as an expression to
	 * call static members.
	 * 
	 * @return
	 */
	public Expression getClassReference() {
		return new Constant(this, getName());
	}

	@Override
	public String getName() {
		return name;
	}
	
	public boolean isEnum() {
		return isEnum;
	}
	
	public void setIsEnum(boolean isEnum) {
		this.isEnum = isEnum;
	}

	public String getPackageName() {
		return packageName;
	}

	public ClassDef getClassDef() {
		return classDef;
	}

	public void setClassDef(ClassDef def) {
		this.classDef = def;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TypeClass
				&& getName().equals(((IType) obj).getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}

	@Override
	public boolean isValueType() {
		return !isReferenceType();
	}
	
	@Override
	public boolean isReferenceType() {
		return !isEnum();
	}
}