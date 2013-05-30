/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1 of this file.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2 of this file.    
 *
 * This file is part of GOOL.
 *
 * GOOL is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, version 3.
 *
 * GOOL is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License along with GOOL,
 * in the file COPYING.txt.  If not, see <http://www.gnu.org/licenses/>.
 */





package gool.ast.constructs;

import gool.generator.GoolGeneratorController;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing finally clause in a try/catch
 */
public class Finally extends Block {

	/**
	 * The list of statements.
	 */
	private List<Statement> statements = new ArrayList<Statement>();
	private Block b; 
	

	/**
	 * Creates a new block with the specified expression.R
	 * 
	 * @param expr
	 *            the expression to be returned by the {@link Catch}.
	 */
	public Finally(Statement statement) {
		statements.add(statement);
	}
	
	public Finally(Block block) {
		this.b=block;
		
	}

	public Finally() {
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


}
