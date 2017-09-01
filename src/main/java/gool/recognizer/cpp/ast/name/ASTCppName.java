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

package gool.recognizer.cpp.ast.name;

import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;

import gool.recognizer.cpp.ast.ASTCppNode;
import gool.recognizer.cpp.visitor.DebugASTCpp;
import gool.recognizer.cpp.visitor.DebugASTCpp.EASTstatu;
import gool.recognizer.cpp.visitor.IVisitorASTCpp;

/**
 * Represents a node name of the C++ AST.
 */
public class ASTCppName extends ASTCppNode {

	public ASTCppName(IASTNode node) {
		super(node);
		setNode((IASTName) node);
	}

	private IASTName node ;
	
	
	public IASTName getNode() {
		return node;
	}

	public void setNode(IASTName node) {
		this.node = node;
	}
	
	@Override
	public Object accept(IVisitorASTCpp visitor, Object data) {
		DebugASTCpp.getInstance().printAstIfYouWant(EASTstatu.VISIT, "ASTCppName",this);
		Object toReturn = visitor.visit(this, data);
		DebugASTCpp.getInstance().printAstIfYouWant(EASTstatu.LEAVE, "ASTCppName",this);
		return toReturn;
	}
}
