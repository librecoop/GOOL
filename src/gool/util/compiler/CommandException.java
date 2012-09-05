package gool.util.compiler;

public class CommandException extends RuntimeException {
	/**
	 * Serialization id. 
	 */
	private static final long serialVersionUID = 6093262615166731596L;
	
	public CommandException(String message) {
		super(message);
	}
	
	public CommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandException(Throwable cause) {
		super(cause);
	}

}
