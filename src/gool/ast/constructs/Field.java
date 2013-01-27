package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

import java.util.Collection;


/**
 * This class captures a field (i.e. an attribute) declaration in abstract GOOL.
 * Hence it is a Dec.
  */
public final class Field extends Dec {
	private Expression defaultValue;

	public Field(String name, IType type, Expression defaultValue) {
		super(type, name);
		this.defaultValue = defaultValue;
	}
	/**
	 * The type of the variable is T.
	 * @param modifiers codes for the visibility of the variable and so on
	 * @param name codes for the name of the variable
	 */
	public Field(Modifier modifier, String name, IType type, Expression defaultValue) {
		this(name, type, defaultValue);
		addModifier(modifier);
	}
	
	public Field(Collection<Modifier> listModifiers, String name, IType type,
			Expression initialValue) {
		this(name, type, initialValue);
		addModifiers(listModifiers);
	}


	public Field(Modifier modifier, String name, IType type) {
		this(modifier, name, type, null);
	}

	public Field(Collection<Modifier> modifiers, VarDeclaration var) {
		this(modifiers, var.getName(), var.getType(), var.getInitialValue());
	}
	
	public void setDefaultValue(Expression defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public Expression getDefaultValue() {
		return defaultValue;
	}
	
	@Override
	public boolean updateFrom(Dec source) {
		if (! super.updateFrom(source))
			return false;
		this.defaultValue = ((Field)source).getDefaultValue();
		return true;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
