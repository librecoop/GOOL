package gool.ast;

import gool.GoolGeneratorController;
import gool.ast.type.IType;

import java.util.Arrays;


/**
 * This class captures function invocation. Hence is is an OOTExpr.
 * 
 * @param T
 *            is the return type, , if known at compile time, otherwise put
 *            OOTType. That way java generics grant us some level of type
 *            checking of the generated code at compiler design time. Sometimes
 *            we will not be able to use this though, because we will not know T
 *            at compiler design time.
 */
public class MethCall extends Parameterizable {

	/**
	 * The target object.
	 */
	private Expression target;

	
	public MethCall(IType type, Expression target) {
		super(type);
		this.target = target;
	}

	private MethCall(IType type, Expression target, Expression param) {
		this(type, target);
		addParameter(param);
	}

	public MethCall(IType type, Expression target, String methodName, Expression... params) {
		this(type, new MemberSelect(type, target, methodName));
		if (params != null){
			addParameters(Arrays.asList(params));
		}
		
	}
	
	public MethCall(IType type, String name) {
		this(type, new Identifier(type, name));
	}

	public static MethCall  create(IType type,
			Expression target, Meth meth,
			Expression param) {
		return new MethCall(type, new FieldAccess(target.getType(), target, meth.getName()), param);
	}
	
	public static MethCall create(IType type, Expression expression) {
		return new MethCall(type, expression);
	}

	public Expression getTarget() {
		return target;
	}

	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
