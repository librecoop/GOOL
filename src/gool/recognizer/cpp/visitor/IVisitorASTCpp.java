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
	
	//---- TRANSITION UNIT ----//
	
	/**
	 * When you visit a transition unit : a file c++.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppTranslationUnit node, Object data);
	
	/**
	 * When you visit an include statement : a #include.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppIncludeStatement node, Object data);
	

	//---- DECLARATION ----//
	
	/**
	 * When you visit a declaration : class, method, function,...
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppDeclaration node, Object data);
	
	/**
	 * When you visit a declaration : class
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppSimpleDeclaration node, Object data);
	
	/**
	 * When you visit a declaration : function, method
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppFunctionDefinition node, Object data);
	
	/**
	 * When you visit a declaration : visibility label (public, private, protected)
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppVisibilityLabel node, Object data);

	
	//---- DECLARATION SPECIFIER ----//

	/**
	 * When you visit a declaration specifier.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppDeclSpecifier node, Object data);
	
	/**
	 * When you visit a declaration specifier as a class.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppCompositeTypeSpecifier node, Object data);
	
	/**
	 * When you visit a declaration specifier as modifiers.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppSimpleDeclSpecifier node, Object data);
	
	/**
	 * When you visit a declaration specifier as a name.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppNamedTypeSpecifier node, Object data);
	
	/**
	 * When you visit a declaration specifier as an enumeration.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppEnumerationSpecifier node, Object data);

	
	//---- ENUMERATION ----//

	/**
	 * When you visit an enumerator : case of enum.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppEnumerator node, Object data);

	
	//---- BASE SPECIFIER ----//
	
	/**
	 * When you visit a base specifier as a Class inheritance.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppBaseSpecifier node, Object data);
	
	
	//---- DECLARATOR ----//
	
	/**
	 * When you visit a declarator as a variable, field.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppDeclarator node, Object data);
	
	/**
	 * When you visit a declarator as an array variable, array field.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppArrayDeclarator node, Object data);
	
	/**
	 * When you visit a declarator as a field.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppFieldDeclarator node, Object data);
	
	/**
	 * When you visit a declarator as a function, method.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppFunctionDeclarator node, Object data);

	
	//---- NAME ----//
	
	/**
	 * When you visit a name.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppName node, Object data);

	
	//---- STATEMENT ----//
	
	/**
	 * When you visit a statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppStatement node, Object data);
	
	/**
	 * When you visit a statement as a block statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppCompoundStatement node, Object data);
	
	/**
	 * When you visit a statement as a declaration statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppDeclarationStatement node, Object data);
	
	/**
	 * When you visit a statement as a default (case) statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppDefaultStatement node, Object data);
	
	/**
	 * When you visit a statement as a do-while statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppDoStatement node, Object data);
	
	/**
	 * When you visit a statement as a expression statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppExpressionStatement node, Object data);
	
	/**
	 * When you visit a statement as a for statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppForStatement node, Object data);
	
	/**
	 * When you visit a statement as an if-then-else statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppIfStatement node, Object data);
	
	/**
	 * When you visit a statement as a return statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppReturnStatement node, Object data);
	
	/**
	 * When you visit a statement as a while statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppWhileStatement node, Object data);
	
	/**
	 * When you visit a statement as a switch statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppSwitchStatement node, Object data);
	
	/**
	 * When you visit a statement as a try statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppTryBlockStatement node, Object data);
	
	/**
	 * When you visit a statement as a catch statement.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppCatchHandler node, Object data);

	
	//---- EXPRESSION ----//
	
	/**
	 * When you visit an expression.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppExpression node, Object data);
	
	/**
	 * When you visit an expression as an array access expression.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppArraySubscriptExpression node, Object data);
	
	/**
	 * When you visit an expression as a binary expression.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppBinaryExpression node, Object data);
	
	/**
	 * When you visit an expression as a cast expression.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppCastExpression node, Object data);
	
	/**
	 * When you visit an expression as a conditional expression.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppConditionalExpression node, Object data);
	
	/**
	 * When you visit an expression as an expression list.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppExpressionList node, Object data);
	
	/**
	 * When you visit an expression as a field reference expression.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppFieldReference node, Object data);
	
	/**
	 * When you visit an expression as a call expression.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppFunctionCallExpression node, Object data);
	
	/**
	 * When you visit an expression as an id expression.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppIdExpression node, Object data);
	
	/**
	 * When you visit an expression as a constant expression.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppLiteralExpression node, Object data);
	
	/**
	 * When you visit an expression as an unary expression.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppUnaryExpression node, Object data);

	
	//---- INITIALIZER ----//
	
	/**
	 * When you visit an initializer.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppInitializer node, Object data);
	
	/**
	 * When you visit an initializer as an expression initializer.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppInitializerExpression node, Object data);
	
	/**
	 * When you visit an initializer as a contructor initializer.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppConstructorInitializer node, Object data);

	
	//---- PARAMETER DECLARATION ----//

	/**
	 * When you visit a parameter declaration.
	 * @param node
	 * 		: The node c++.
	 * @param data
	 * 		: The data which can be used.
	 * @return
	 * 		: The GOOL AST which is built, for this part.
	 */
	public Object visit(ASTCppParameterDeclaration node, Object data);
	
}
