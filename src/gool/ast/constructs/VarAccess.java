package gool.ast.constructs;

import gool.generator.GoolGeneratorController;


/**
 * This class captures variable content invocation.
 * Hence is is an Expression.
 * This has to be distinguished from VarDeclaration because later it does not compile in the same way.
 */
public class VarAccess extends Expression {

	/**
	 * The declared variable.
	 */
	private Dec var;

	/**
	 * The type of the return value is T
	 * @param method is a pointer to the previous declaration of the method to be invoked.
	 * @param params 
	 */
	public VarAccess(Dec var){
		super(var.getType());
		this.var=var;
	}

	public Dec getDec() {
		return var;
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
