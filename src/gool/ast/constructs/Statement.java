package gool.ast.constructs;


/**
 * This interface accounts for all statements of the intermediate language.
 */
public abstract class Statement implements INode {

	private Boolean semiColon = true;
	
	/**
	 * Gets a boolean specifying if the statement ends with a semicolon or not.
	 */
	public Boolean getSemiColon() {
		return semiColon;
	}
	
	/**
	 * Sets the value that specifies if the statement ends with a semicolon.
	 */
	public void setSemiColon(Boolean semiColon) {
		this.semiColon = semiColon;
	}
	
}
