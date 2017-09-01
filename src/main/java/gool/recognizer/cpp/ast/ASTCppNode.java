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

package gool.recognizer.cpp.ast;

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
import gool.recognizer.cpp.visitor.IVisitorASTCpp;

import org.eclipse.cdt.core.dom.ast.IASTArrayDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTArraySubscriptExpression;
import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTCastExpression;
import org.eclipse.cdt.core.dom.ast.IASTCompositeTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTConditionalExpression;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarationStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTDefaultStatement;
import org.eclipse.cdt.core.dom.ast.IASTDoStatement;
import org.eclipse.cdt.core.dom.ast.IASTEnumerationSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTEqualsInitializer;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpressionList;
import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTFieldDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFieldReference;
import org.eclipse.cdt.core.dom.ast.IASTForStatement;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTIfStatement;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNamedTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTReturnStatement;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTSwitchStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IASTUnaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTWhileStatement;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCatchHandler;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCompositeTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTConstructorInitializer;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTTryBlockStatement;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTVisibilityLabel;

/**
 * Represents a node of the C++ AST.
 */
public abstract class ASTCppNode {

	public abstract Object accept(IVisitorASTCpp visitor, Object data);
	
	public ASTCppNode(IASTNode node) {
	}
	
	public static ASTCppNode transforme(IASTNode node){
		return mapNode(node);
	}
	
	/**
	 * Transforms a C++ CDT node in a C++ node.
	 * @param node
	 * @return
	 */
	private static ASTCppNode mapNode(IASTNode node){

		if(node instanceof IASTTranslationUnit) return new ASTCppTranslationUnit(node);

		if(node instanceof IASTPreprocessorIncludeStatement) return new ASTCppIncludeStatement(node);
		
		if(node instanceof IASTSimpleDeclaration) return new ASTCppSimpleDeclaration(node);
		if(node instanceof IASTFunctionDefinition) return new ASTCppFunctionDefinition(node);
		if(node instanceof ICPPASTVisibilityLabel) return new ASTCppVisibilityLabel(node);
		if(node instanceof IASTDeclaration) return new ASTCppDeclaration(node);

		if(node instanceof IASTCompositeTypeSpecifier) return new ASTCppCompositeTypeSpecifier(node);
		if(node instanceof IASTSimpleDeclSpecifier) return new ASTCppSimpleDeclSpecifier(node);
		if(node instanceof IASTNamedTypeSpecifier) return new ASTCppNamedTypeSpecifier(node);
		if(node instanceof IASTEnumerationSpecifier) return new ASTCppEnumerationSpecifier(node);
		if(node instanceof IASTDeclSpecifier) return new ASTCppDeclSpecifier(node);
		
		if(node instanceof IASTEnumerationSpecifier.IASTEnumerator) return new ASTCppEnumerator(node);
		
		if(node instanceof ICPPASTCompositeTypeSpecifier.ICPPASTBaseSpecifier) return new ASTCppBaseSpecifier(node);
		
		if(node instanceof IASTArrayDeclarator) return new ASTCppArrayDeclarator(node);
		if(node instanceof IASTFieldDeclarator) return new ASTCppFieldDeclarator(node);
		if(node instanceof IASTFunctionDeclarator) return new ASTCppFunctionDeclarator(node);
		if(node instanceof IASTDeclarator) return new ASTCppDeclarator(node);
		
		if(node instanceof IASTName) return new ASTCppName(node);

		if(node instanceof IASTCompoundStatement) return new ASTCppCompoundStatement(node);
		if(node instanceof IASTDeclarationStatement) return new ASTCppDeclarationStatement(node);
		if(node instanceof IASTDefaultStatement) return new ASTCppDefaultStatement(node);
		if(node instanceof IASTDoStatement) return new ASTCppDoStatement(node);
		if(node instanceof IASTExpressionStatement) return new ASTCppExpressionStatement(node);
		if(node instanceof IASTForStatement) return new ASTCppForStatement(node);
		if(node instanceof IASTIfStatement) return new ASTCppIfStatement(node);
		if(node instanceof IASTReturnStatement) return new ASTCppReturnStatement(node);
		if(node instanceof IASTWhileStatement) return new ASTCppWhileStatement(node);
		if(node instanceof IASTSwitchStatement) return new ASTCppSwitchStatement(node);
		if(node instanceof ICPPASTTryBlockStatement) return new ASTCppTryBlockStatement(node);
		if(node instanceof ICPPASTCatchHandler) return new ASTCppCatchHandler(node);
		if(node instanceof IASTStatement) return new ASTCppStatement(node);

		if(node instanceof IASTArraySubscriptExpression) return new ASTCppArraySubscriptExpression(node);
		if(node instanceof IASTBinaryExpression) return new ASTCppBinaryExpression(node);
		if(node instanceof IASTCastExpression) return new ASTCppCastExpression(node);
		if(node instanceof IASTConditionalExpression) return new ASTCppConditionalExpression(node);
		if(node instanceof IASTExpressionList) return new ASTCppExpressionList(node);
		if(node instanceof IASTFieldReference) return new ASTCppFieldReference(node);
		if(node instanceof IASTFunctionCallExpression) return new ASTCppFunctionCallExpression(node);
		if(node instanceof IASTIdExpression) return new ASTCppIdExpression(node);
		if(node instanceof IASTLiteralExpression) return new ASTCppLiteralExpression(node);
		if(node instanceof IASTUnaryExpression) return new ASTCppUnaryExpression(node);
		if(node instanceof IASTExpression) return new ASTCppExpression(node);
		
		if(node instanceof IASTEqualsInitializer) return new ASTCppInitializerExpression(node);
		if(node instanceof ICPPASTConstructorInitializer) return new ASTCppConstructorInitializer(node);
		if(node instanceof IASTInitializer) return new ASTCppInitializer(node);
		
		if(node instanceof IASTParameterDeclaration) return new ASTCppParameterDeclaration(node);
				
		return null ;
	}
}
