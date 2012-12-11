package gool.ast.type;

import gool.ast.constructs.ClassDef;
import gool.ast.constructs.Constant;
import gool.ast.constructs.Expression;
import gool.ast.printer.GoolGeneratorController;

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
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}