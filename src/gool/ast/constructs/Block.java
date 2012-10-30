package gool.ast.constructs;

import gool.ast.printer.GoolGeneratorController;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a list of statements with its optional 'return'
 * expression.
 */
public class Block extends Statement {

	/**
	 * The list of statements.
	 */
	private List<Statement> statements = new ArrayList<Statement>();

	/**
	 * Creates a new block with the specified expression.
	 * 
	 * @param expr
	 *            the expression to be returned by the {@link Block}.
	 */
	public Block(Statement statement) {
		statements.add(statement);
	}

	public Block() {
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
	public String toString() {
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

}