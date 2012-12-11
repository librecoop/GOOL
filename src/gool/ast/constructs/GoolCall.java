package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;


public class GoolCall extends Parameterizable{




	private String method;


	protected GoolCall( IType type) {
		super(type);
	}


	public GoolCall(IType type, String method) {
		super(type);
		this.method = method;
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}
	
	public String getMethod() {
		return method;
	}
}
