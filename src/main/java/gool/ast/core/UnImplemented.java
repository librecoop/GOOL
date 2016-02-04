package gool.ast.core;

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
	
}
