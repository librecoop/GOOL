package gool.ast.constructs;

import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.IType;

/**
 * Represents member invocation over class members. For example, in Java they are known as static methods or attributes.
 */

public class FieldAccess extends Expression {

	/**
	 * The target object.
	 */
	private Expression target;
	/**
	 * The target member to be called.
	 */
	private String member;
	/**
	 * The parameters needed to call the member.
	 */
	
	public FieldAccess(IType type, Expression target, String member) {
		super(type);
		this.target = target;
		this.member = member;
	}
	
	public String getMember() {
		return member;
	}

	public Expression getTarget() {
		return target;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
