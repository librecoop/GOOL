package gool.ast.bufferedreader;

import gool.ast.constructs.BufferedReaderMethCall;
import gool.ast.constructs.Expression;
import gool.ast.type.TypeVoid;
import gool.generator.GoolGeneratorController;

public class BufferedReaderReadLineCall extends BufferedReaderMethCall {

	public BufferedReaderReadLineCall (Expression target) {
		super(TypeVoid.INSTANCE, target);
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
