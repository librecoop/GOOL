package gool.ast.core;

import gool.generator.GoolGeneratorController;
import gool.generator.common.CodeGenerator;

public class UnImplemented extends Dependency {
	
	/**
	 * The code of the unimplemented part.
	 */
	private String code;
	
	/**
	 * The comment associate with the unimplemented part.
	 */
	private String comment;
	
	public UnImplemented(String code, String comment){
		this.code = code ;
		this.comment = comment ;
	}
	
	public String getCodeUnImplemented(){
		return code ;
	}
	
	public String getCommentUnImplemented(){
		return comment ;
	}
	
	@Override
	public String callGetCode() {
		CodeGenerator cg;
		try{
			cg = GoolGeneratorController.generator();
		}catch (IllegalStateException e){
			return this.getClass().getSimpleName();
		}
		return cg.getCode(this);
	}

}
