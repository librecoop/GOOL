package gool.ast.constructs;

import gool.ast.type.IType;



/**
 * This interface is implemented by all things yielding a value
 * in the intermediate language.
 * Because expressions are frequently voided to be used as statements in 
 * OO languages, our expressions can also be statements
 * @param T is the type of the value, if known at compile time, otherwise put OOTType. 
 * That way java generics grant us some level of type checking of the generated code at compiler design time.
 * Sometimes we will not be able to use this though, because we will not know T at compiler design time.
 */
public abstract class Expression extends Statement {

	/**
	 * The return type.
	 */
	private IType type;
	
	protected Expression(IType type) {
		this.type = type;
	}
	
	public IType getType() {
		return type;
	}
	
	public void setType(IType type) {
		this.type = type;
	}
	


}
