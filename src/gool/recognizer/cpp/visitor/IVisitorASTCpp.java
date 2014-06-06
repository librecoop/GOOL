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

package gool.recognizer.cpp.visitor;

import gool.recognizer.cpp.ast.declaration.ASTCppDeclaration;
import gool.recognizer.cpp.ast.declaration.ASTCppFunctionDefinition;
import gool.recognizer.cpp.ast.declaration.ASTCppSimpleDeclaration;
import gool.recognizer.cpp.ast.declaration.ASTCppVisibilityLabel;
import gool.recognizer.cpp.ast.declarator.ASTCppArrayDeclarator;
import gool.recognizer.cpp.ast.declarator.ASTCppDeclarator;
import gool.recognizer.cpp.ast.declarator.ASTCppFieldDeclarator;
import gool.recognizer.cpp.ast.declarator.ASTCppFunctionDeclarator;
import gool.recognizer.cpp.ast.declspecifier.ASTCppCompositeTypeSpecifier;
import gool.recognizer.cpp.ast.declspecifier.ASTCppDeclSpecifier;
import gool.recognizer.cpp.ast.declspecifier.ASTCppEnumerationSpecifier;
import gool.recognizer.cpp.ast.declspecifier.ASTCppNamedTypeSpecifier;
import gool.recognizer.cpp.ast.declspecifier.ASTCppSimpleDeclSpecifier;
import gool.recognizer.cpp.ast.expression.ASTCppArraySubscriptExpression;
import gool.recognizer.cpp.ast.expression.ASTCppBinaryExpression;
import gool.recognizer.cpp.ast.expression.ASTCppCastExpression;
import gool.recognizer.cpp.ast.expression.ASTCppConditionalExpression;
import gool.recognizer.cpp.ast.expression.ASTCppExpression;
import gool.recognizer.cpp.ast.expression.ASTCppExpressionList;
import gool.recognizer.cpp.ast.expression.ASTCppFieldReference;
import gool.recognizer.cpp.ast.expression.ASTCppFunctionCallExpression;
import gool.recognizer.cpp.ast.expression.ASTCppIdExpression;
import gool.recognizer.cpp.ast.expression.ASTCppLiteralExpression;
import gool.recognizer.cpp.ast.expression.ASTCppUnaryExpression;
import gool.recognizer.cpp.ast.initializer.ASTCppConstructorInitializer;
import gool.recognizer.cpp.ast.initializer.ASTCppInitializer;
import gool.recognizer.cpp.ast.initializer.ASTCppInitializerExpression;
import gool.recognizer.cpp.ast.name.ASTCppName;
import gool.recognizer.cpp.ast.other.ASTCppBaseSpecifier;
import gool.recognizer.cpp.ast.other.ASTCppEnumerator;
import gool.recognizer.cpp.ast.other.ASTCppIncludeStatement;
import gool.recognizer.cpp.ast.other.ASTCppParameterDeclaration;
import gool.recognizer.cpp.ast.other.ASTCppTranslationUnit;
import gool.recognizer.cpp.ast.statement.ASTCppCatchHandler;
import gool.recognizer.cpp.ast.statement.ASTCppCompoundStatement;
import gool.recognizer.cpp.ast.statement.ASTCppDeclarationStatement;
import gool.recognizer.cpp.ast.statement.ASTCppDefaultStatement;
import gool.recognizer.cpp.ast.statement.ASTCppDoStatement;
import gool.recognizer.cpp.ast.statement.ASTCppExpressionStatement;
import gool.recognizer.cpp.ast.statement.ASTCppForStatement;
import gool.recognizer.cpp.ast.statement.ASTCppIfStatement;
import gool.recognizer.cpp.ast.statement.ASTCppReturnStatement;
import gool.recognizer.cpp.ast.statement.ASTCppStatement;
import gool.recognizer.cpp.ast.statement.ASTCppSwitchStatement;
import gool.recognizer.cpp.ast.statement.ASTCppTryBlockStatement;
import gool.recognizer.cpp.ast.statement.ASTCppWhileStatement;

/**
 * Interface pattern visitor for a C++ AST.
 */
public interface IVisitorASTCpp {
	public Object visit(ASTCppTranslationUnit node, Object data);
	
	public Object visit(ASTCppIncludeStatement node, Object data);
	
	public Object visit(ASTCppDeclaration node, Object data);
	public Object visit(ASTCppSimpleDeclaration node, Object data);
	public Object visit(ASTCppFunctionDefinition node, Object data);
	public Object visit(ASTCppVisibilityLabel node, Object data);
	
	public Object visit(ASTCppDeclSpecifier node, Object data);
	public Object visit(ASTCppCompositeTypeSpecifier node, Object data);
	public Object visit(ASTCppSimpleDeclSpecifier node, Object data);
	public Object visit(ASTCppNamedTypeSpecifier node, Object data);
	public Object visit(ASTCppEnumerationSpecifier node, Object data);
	
	public Object visit(ASTCppEnumerator node, Object data);
	
	public Object visit(ASTCppBaseSpecifier node, Object data);
	
	public Object visit(ASTCppDeclarator node, Object data);
	public Object visit(ASTCppArrayDeclarator node, Object data);
	public Object visit(ASTCppFieldDeclarator node, Object data);
	public Object visit(ASTCppFunctionDeclarator node, Object data);
	
	public Object visit(ASTCppName node, Object data);
	
	public Object visit(ASTCppStatement node, Object data);
	public Object visit(ASTCppCompoundStatement node, Object data);
	public Object visit(ASTCppDeclarationStatement node, Object data);
	public Object visit(ASTCppDefaultStatement node, Object data);
	public Object visit(ASTCppDoStatement node, Object data);
	public Object visit(ASTCppExpressionStatement node, Object data);
	public Object visit(ASTCppForStatement node, Object data);
	public Object visit(ASTCppIfStatement node, Object data);
	public Object visit(ASTCppReturnStatement node, Object data);
	public Object visit(ASTCppWhileStatement node, Object data);
	public Object visit(ASTCppSwitchStatement node, Object data);
	public Object visit(ASTCppTryBlockStatement node, Object data);
	public Object visit(ASTCppCatchHandler node, Object data);
			
	public Object visit(ASTCppExpression node, Object data);
	public Object visit(ASTCppArraySubscriptExpression node, Object data);
	public Object visit(ASTCppBinaryExpression node, Object data);
	public Object visit(ASTCppCastExpression node, Object data);
	public Object visit(ASTCppConditionalExpression node, Object data);
	public Object visit(ASTCppExpressionList node, Object data);
	public Object visit(ASTCppFieldReference node, Object data);
	public Object visit(ASTCppFunctionCallExpression node, Object data);
	public Object visit(ASTCppIdExpression node, Object data);
	public Object visit(ASTCppLiteralExpression node, Object data);
	public Object visit(ASTCppUnaryExpression node, Object data);
	
	public Object visit(ASTCppInitializer node, Object data);
	public Object visit(ASTCppInitializerExpression node, Object data);
	public Object visit(ASTCppConstructorInitializer node, Object data);
	
	public Object visit(ASTCppParameterDeclaration node, Object data);
}
