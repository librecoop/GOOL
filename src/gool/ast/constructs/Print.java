package gool.ast.constructs;


/**
 * This captures the if statements of the intermediate language.
 * Hence it is an OOTStatement.
 * Notice the type checking achieved through generics.
 */
public final class Print extends Statement {
	/**
	 * The expression to be printed.
	 */
	private Expression expr;
	
	private Print(Expression expr){
		this.expr=expr;
	}
	
	/**
	 * Return the expression to be printed.
	 * @return the expression to be printed.
	 */
	public Expression getExpression() {
		return getExpression();
	}

	@Override
	public String callGetCode() {
		return "System.out.println(" + expr + ")";
	}
	
	public static Print create(Expression expr){
		return new Print(expr);
	}
}
