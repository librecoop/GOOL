package gool.ast.constructs;

import gool.generator.GoolGeneratorController;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a list of statements with its optional 'return'
 * expression.
 */
public class Catch extends Block {

	/**
	 * The list of statements.
	 */
	private List<Statement> statements = new ArrayList<Statement>();
	private Block b; 
	private VarDeclaration singleParameter; //Currently only supports one parameter

	/**
	 * Creates a new block with the specified expression.R
	 * 
	 * @param expr
	 *            the expression to be returned by the {@link Catch}.
	 */
	public Catch(Statement statement) {
		statements.add(statement);
	}
	
	public Catch(Block block) {
		this.b=block;
		
	}

	public Catch() {
	}

	/**
	 * Appends a statement to the end of the statements' list.
	 * 
	 * @param statement
	 *            statement to be appended.
	 */
	public void addStatement(Statement statement) {
		statements.add(statement);
	}

	/**
	 * Appends a statement to the end of the statements' list.
	 * 
	 * @param statement
	 *            statement to be appended
	 * @param position
	 *            the position of the statement.
	 */
	public void addStatement(Statement statement, int position) {
		statements.add(position, statement);
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

	/**
	 * Returns the list of statements.
	 * 
	 * @return the list of statements.
	 */
	public List<Statement> getStatements() {
		return statements;
	}

	public void addStatements(List<Statement> statements) {
		this.statements.addAll(statements);
	}
	
	public Block getBlock()
	{
		
		return b;
	}

	public VarDeclaration getSingleParameter() {
		return singleParameter;
	}

	public void setSingleParameter(VarDeclaration singleParameter) {
		this.singleParameter = singleParameter;
	}
	
	


}