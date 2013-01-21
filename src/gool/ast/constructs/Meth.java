package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

import java.util.ArrayList;
import java.util.List;

/**
 * This class accounts for method declarations in the intermediate language.
 * Hence it is an OOTDec.
 * @param T is the return type, if known at compile time, otherwise put OOTType. 
 * That way java generics grant us some level of type checking of the generated code at compiler design time.
 * Sometimes we will not be able to use this though, because we will not know T at compiler design time.
 */
public class Meth extends Dec {
	/**
	 * The method body instructions.
	 */
	private Block block = new Block();
	/**
	 * The list of parameters.
	 */
	private List<VarDeclaration> parameters = new ArrayList<VarDeclaration>();
	/**
	 * The list of generic types (specific to the method).
	 */
	private List<IType> genericTypes = new ArrayList<IType>();

	/**
	 * Indicates if this method is inherited.
	 */
	private boolean inherited;
	
	private ClassDef classDef;
	
	public Meth(IType returnType, Modifier modifier, String name) {
		super(returnType, name);
		addModifier(modifier);
	}

	public Meth(IType returnType, String name) {
		super(returnType, name);
		addModifier(Modifier.PUBLIC);
	}

	
	public final void addParameter(VarDeclaration varParam){
		parameters.add(varParam);
	}
	
	public final void addStatement(Statement statement){
		block.addStatement(statement);
	}
	
	public final void addStatements(List<Statement> statements){
		block.addStatements(statements);
	}
	
	public final void addGenericType(IType type) {
		genericTypes.add(type);
	}

	public boolean isMainMethod() {
		return false;
	}

	public Block getBlock() {
		return block;
	}
	
	public ClassDef getClassDef() {
		return classDef;
	}
	
	public void setClassDef(ClassDef classDef) {
		this.classDef = classDef;
	}
	
	public List<VarDeclaration> getParams() {
		return parameters;
	}

	public boolean isInherited() {
		return inherited;
	}

	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}
	
	public List<IType> getGenericTypes() {
		return genericTypes;
	}

	public boolean isConstructor(){
		return false;
	}

	
	public boolean isAbstract(){
		return getModifiers().contains(Modifier.ABSTRACT);
	}

	
	public String getHeader() {
		return GoolGeneratorController.generator().getCode(this);
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}
}
