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

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class captures the "new" of the intermediate language, i.e object
 * 
 * instantiation from a class definition Hence is is an OOTExpr.
 * 
 * 
 * 
 * @param T
 * 
 *            is the return type In generic OO languages each object
 * 
 *            instantiations may generate new types, hence this is an OOType.
 */

public class ArrayNew extends Expression {

	private final List<Expression> dimesExpressions = new ArrayList<Expression>();
	private final List<Expression> initialiList = new ArrayList<Expression>();


	public ArrayNew(IType type, List<Expression> dimesExpressions,
			List<Expression> initialiList) {
				super(type);
				this.dimesExpressions.addAll(dimesExpressions);
				this.initialiList.addAll(initialiList);
	}

	public List<IType> getTypeArguments() {
		return getType().getTypeArguments();
	}
	
	public List<Expression> getDimesExpressions() {
		return dimesExpressions;
	}
	
	public List<Expression> getInitialiList() {
		return initialiList;
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}


}
