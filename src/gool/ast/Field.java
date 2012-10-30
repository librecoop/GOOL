package gool.ast;

import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.IType;

import java.util.Collection;


/**
 * This class captures a variable declaration in the intermediate language.
 * Hence it is a Dec.
 * But because declarations are usually OK statements in OO Languages it is also a Statement
 * @param T is the type of the declared variable, if known at compile time, otherwise put IType. 
 * That way java generics grant us some level of type checking of the generated code at compiler design time.
 * Sometimes we will not be able to use this though, because we will not know T at compiler design time.
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
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
