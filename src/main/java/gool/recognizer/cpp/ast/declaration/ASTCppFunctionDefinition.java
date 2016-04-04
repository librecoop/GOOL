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

package gool.recognizer.cpp.ast.declaration;

import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTNode;

import gool.recognizer.cpp.visitor.DebugASTCpp;
import gool.recognizer.cpp.visitor.IVisitorASTCpp;
import gool.recognizer.cpp.visitor.DebugASTCpp.EASTstatu;

/**
 * Represents a node declaration(function) of the C++ AST.
 */
public class ASTCppFunctionDefinition extends ASTCppDeclaration {

	public ASTCppFunctionDefinition(IASTNode node) {
		super(node);
		setNode((IASTFunctionDefinition) node);
	}

	private IASTFunctionDefinition node ;
	
	
	public IASTFunctionDefinition getNode() {
		return node;
	}

	public void setNode(IASTFunctionDefinition node) {
		this.node = node;
	}
	
	@Override
	public Object accept(IVisitorASTCpp visitor, Object data) {
		DebugASTCpp.getInstance().printAstIfYouWant(EASTstatu.VISIT, "ASTCppFunctionDefinition",this);
		Object toReturn = visitor.visit(this, data);
		DebugASTCpp.getInstance().printAstIfYouWant(EASTstatu.LEAVE, "ASTCppFunctionDefinition",this);
		return toReturn;
	}
}
