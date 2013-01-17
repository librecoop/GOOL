package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


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
	
	private Collection<Modifier> modifiers;
	
	public MethCall(IType type, Collection<Modifier> modifiers, Expression target) {
		super(type);
		this.target = target;
		this.modifiers = modifiers;
	}

	private MethCall(IType type, Collection<Modifier> modifiers, Expression target, Expression param) {
		this(type, modifiers, target);
		addParameter(param);
	}

	public MethCall(IType type, Collection<Modifier> modifiers, Expression target, String methodName, Expression... params) {
		this(type, modifiers, new MemberSelect(type, target, methodName));
		if (params != null){
			addParameters(Arrays.asList(params));
		}
		
	}
	
	public MethCall(IType type, Collection<Modifier> modifiers, String name) {
		this(type, modifiers, new Identifier(type, name));
	}

	public static MethCall  create(IType type,
			Expression target, Meth meth,
			Expression param) {
		return new MethCall(type, meth.getModifiers(), new FieldAccess(target.getType(), target, meth.getName()), param);
	}
	
	public static MethCall create(IType type, Collection<Modifier> modifiers, Expression expression) {
		return new MethCall(type,modifiers, expression);
	}

	public Expression getTarget() {
		return target;
	}
	
	public Collection<Modifier> getModifiers(){
		return modifiers;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
