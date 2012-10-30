package gool.ast;

import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.IType;


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
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}
	
	public String getMethod() {
		return method;
	}
}
