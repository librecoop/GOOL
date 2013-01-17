package gool.ast.constructs;

import gool.ast.type.IType;


public abstract class InitCall extends MethCall {
	protected InitCall(IType type, Expression target) {
		super(type, null, target);
	}
}
