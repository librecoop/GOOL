package gool.ast.exception;

import gool.ast.constructs.ExceptionMethCall;
import gool.ast.constructs.Expression;
import gool.ast.constructs.BufferedReaderMethCall;
import gool.ast.type.TypeVoid;
import gool.generator.GoolGeneratorController;

public class ExceptionPrintStackTraceCall extends ExceptionMethCall {

	public ExceptionPrintStackTraceCall(Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
