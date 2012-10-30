package gool.ast;

import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.TypeNone;

public class EnhancedForLoop extends Expression {

	private VarDeclaration varDec;
	private Expression expr;
	private Statement statements;

	private EnhancedForLoop() {
		super(TypeNone.INSTANCE);
	}

	public EnhancedForLoop(VarDeclaration varDec, Expression expr,
			Statement statements) {
		this();
		this.varDec = varDec;
		this.expr = expr;
		this.statements = statements;
	}
	
	public VarDeclaration getVarDec() {
		return varDec;
	}
	
	public Expression getExpression() {
		return expr;
	}
	
	public Statement getStatements() {
		return statements;
	}
	
	@Override
	public String toString() {
		return GoolGeneratorController.generator().getCode(this);		
	}

}
