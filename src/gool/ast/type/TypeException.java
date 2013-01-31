package gool.ast.type;

import gool.generator.GoolGeneratorController;

public class TypeException extends ReferenceType{

	private String message;
	
	public TypeException(String message){
		this.message = message;
	}
	
	@Override
	public String getName() {
		return this.callGetCode();
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
