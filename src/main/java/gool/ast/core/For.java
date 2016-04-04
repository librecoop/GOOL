/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2.    
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

package gool.ast.core;

import gool.generator.GoolGeneratorController;
import gool.generator.common.CodeGenerator;

/**
 *  This class captures the "for loop" of the intermediate language.
 *  For example, this class captures
 *  {@code
 *  for( initializer ; condition ; updater ){
 *  	statements
 *  }
 */
public class For extends While {

	/**
	 * The initializer statement in the "for loop".
	 */
	private Statement initializer;
	
	/**
	 * The updater statement in the "for loop".
	 */
	private Statement updater;

	/**
	 * The constructor of an "for loop" representation.
	 * @param initializer
	 * 		: The initializer statement in the "for loop".
	 * @param condition
	 * 		: The condition expression in the "for loop".
	 * @param updater
	 * 		: The updater statement in the "for loop".
	 * @param statements
	 * 		: The statements in the "for loop".
	 */
	public For(Statement initializer, Expression condition, Statement updater,
			Statement statements) {
		super(condition, statements);
		this.initializer = initializer;
		this.updater = updater;
	}

	/**
	 * Gets the initializer statement in the "for loop".
	 * @return
	 * 		The initializer statement in the "for loop".
	 */
	public Statement getInitializer() {
		return initializer;
	}

	/**
	 * Gets the updater statement in the "for loop".
	 * @return
	 * 		The updater statement in the "for loop".
	 */
	public Statement getUpdater() {
		return updater;
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
