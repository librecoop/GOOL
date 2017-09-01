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

package gool.recognizer.cpp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.cdt.core.dom.ast.ASTTypeUtil;
import org.eclipse.cdt.core.dom.ast.IASTArrayDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTArrayModifier;
import org.eclipse.cdt.core.dom.ast.IASTArraySubscriptExpression;
import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTCastExpression;
import org.eclipse.cdt.core.dom.ast.IASTCompositeTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTConditionalExpression;
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
import org.eclipse.cdt.core.dom.ast.IASTFieldReference;
import org.eclipse.cdt.core.dom.ast.IASTForStatement;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTIfStatement;
import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNamedTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTPointerOperator;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTReturnStatement;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTSwitchStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit.IDependencyTree.IASTInclusionNode;
import org.eclipse.cdt.core.dom.ast.IASTUnaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTWhileStatement;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IField;
import org.eclipse.cdt.core.dom.ast.IFunction;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IVariable;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit.IDependencyTree;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCompositeTypeSpecifier.ICPPASTBaseSpecifier;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTCatchHandler;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTConstructorInitializer;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTTryBlockStatement;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTVisibilityLabel;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPClassType;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPMethod;

import gool.ast.core.ArrayAccess;
import gool.ast.core.Assign;
import gool.ast.core.BinaryOperation;
import gool.ast.core.Block;
import gool.ast.core.CastExpression;
import gool.ast.core.Catch;
import gool.ast.core.ClassDef;
import gool.ast.core.ClassNew;
import gool.ast.core.Constant;
import gool.ast.core.RecognizedDependency;
//import gool.ast.core.CustomDependency;
import gool.ast.core.Dec;
import gool.ast.core.Expression;
import gool.ast.core.ExpressionUnknown;
import gool.ast.core.Field;
import gool.ast.core.FieldAccess;
import gool.ast.core.For;
import gool.ast.core.Identifier;
import gool.ast.core.If;
import gool.ast.core.Meth;
import gool.ast.core.MethCall;
import gool.ast.core.Modifier;
import gool.ast.core.Operator;
import gool.ast.core.Return;
import gool.ast.core.Statement;
import gool.ast.core.This;
import gool.ast.core.Throw;
import gool.ast.core.Try;
import gool.ast.core.UnImplemented;
import gool.ast.core.UnaryOperation;
import gool.ast.core.UnrecognizedDependency;
import gool.ast.core.VarDeclaration;
import gool.ast.core.While;
import gool.ast.system.SystemOutDependency;
import gool.ast.system.SystemOutPrintCall;
import gool.ast.type.IType;
import gool.ast.type.TypeBool;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeNone;
import gool.ast.type.TypeString;
import gool.ast.type.TypeUnknown;
import gool.recognizer.common.RecognizerMatcher;
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
import logger.Log;



/**
 * This class is the recognizer for the language C++. It transforms
 * a C++ AST into a GOOL AST.
 */
public class CppRecognizer implements IVisitorASTCpp {

	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////   FIELDS   ///////////////////////////////////

	/**
	 * The helper used to help the recognizer.
	 */
	protected HelperCppRecognizer helper = new HelperCppRecognizer(this) ;

	/**
	 * The list of abstract GOOL classes and packages that will be generated.
	 */
	protected Map<gool.ast.type.IType, ClassDef> goolClasses = new HashMap<gool.ast.type.IType, ClassDef>();

	/**
	 * The stack of actives classes (used to know in which class insert a method or a field).
	 */
	protected Stack<ClassDef> stackClassActives = new Stack<ClassDef>();

	/**
	 * The active method (used to know in which method insert a statement).
	 * If no method active is available, methActive is null.
	 */
	protected Meth methActive = null ;





	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////   GET/SET   ///////////////////////////////////

	/**
	 * Gets the list of abstract GOOL classes and packages that will be generated.
	 * @return
	 * 		The list of abstract GOOL classes and packages that will be generated.
	 */
	public final Collection<ClassDef> getGoolClasses() {
		return goolClasses.values();
	}





	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////
	///////////////////////////  VISITORS OF AST VISITOR   ///////////////////////////

	@Override
	public Object visit(ASTCppTranslationUnit node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTTranslationUnit tu = node.getNode() ;

		// Create an new class with the name of the file.
		String className = helper.createClassNameFromFilename(tu.getContainingFilename());
		ClassDef unitaryClass = helper.getClassDef(className);

		// The class is added to goolClasses when it doesn't exist yet.
		if (unitaryClass == null){
			unitaryClass = helper.createClassDef(className);
			goolClasses.put(unitaryClass.getType(),unitaryClass);
		}

		// Add to stack active.
		stackClassActives.push(unitaryClass);

		// Initialized the recognizer matcher.
		RecognizerMatcher.init("cpp");

		// Visit the dependency.
		IDependencyTree tree = tu.getDependencyTree();
		for(IASTInclusionNode inc : tree.getInclusions()){
			helper.visitIncludeStatment(inc.getIncludeDirective(), this, data);
		}

		// Visit the declarations.
		helper.visitTranslationUnit(tu, this, new Context());

		// Visit errors.
		helper.visitErrors(tu.getChildren(), this, data);

		// Drop from stack active.
		stackClassActives.pop();
		return Log.MethodOut(Thread.currentThread(),null);
	}


	@Override
	public Object visit(ASTCppIncludeStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTPreprocessorIncludeStatement inc = node.getNode();

		// Visit the named include.
		String dependencyString = helper.visitName(inc.getName(), this, data);
		Log.d("<CppRecognizer - visit(ASTCppIncludeStatement...)> " + dependencyString);
		// Check this library.
		if (!RecognizerMatcher.matchImport(dependencyString)) {
			if(! dependencyString.contains("iostream")){
				stackClassActives.peek()
				.addDependency(new UnrecognizedDependency(dependencyString));
			}
		}
//		else{ 
//			stackClassActives.peek()
//			.addDependency(new RecognizedDependency(dependencyString));
//		}

		return Log.MethodOut(Thread.currentThread(),null);
	}

	@Override
	public Object visit(ASTCppDeclaration node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTDeclaration dec = node.getNode();

		// Add an error because unrecognized.
		helper.addAnError(dec.getRawSignature(), "This declaration");

		return Log.MethodOut(Thread.currentThread(),null);
	}

	@Override
	public Object visit(ASTCppSimpleDeclaration node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTSimpleDeclaration dec = node.getNode();

		// Visit the declaration specifier.
		helper.visitDeclSpecifier(dec.getDeclSpecifier(), this, data) ;

		// Visit and adds to return the declarators.
		List<Dec> decs = new ArrayList<Dec>();
		for(IASTNode decDecl : dec.getDeclarators()){
			decs.add(helper.visitDeclarator((IASTDeclarator) decDecl, this, data));
		}

		// Change the context
		Context context = (Context) data ;
		context.nameSpec = null ;

		// Return declarations.
		return Log.MethodOut(Thread.currentThread(), decs);
	}

	@Override
	public Object visit(ASTCppFunctionDefinition node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTFunctionDefinition dec = node.getNode() ;

		// Gets the context.
		Context context = ((Context)data) ;

		// Visit the declaration specifier.
		List<Modifier> listmodifiers = new ArrayList<Modifier>();
		Object modifiers = helper.visitDeclSpecifier(dec.getDeclSpecifier(), this, data) ;
		if(modifiers instanceof List<?>){
			listmodifiers.addAll((Collection<? extends Modifier>) modifiers);
		}

		// We resolved the binding to know the real type.
		IASTName functionDefintionName = dec.getDeclarator().getName();
		IBinding functionDefintionBind = functionDefintionName.resolveBinding() ;

		// Is a method definition.
		if(functionDefintionBind instanceof ICPPMethod){

			// Create and add the method.
			ICPPMethod method = (ICPPMethod)functionDefintionBind ;
			helper.createAndAddMethod(method, context.nameSpec);	
		}
		// Is a function definition.
		else if(functionDefintionBind instanceof IFunction)
		{
			// Create and add a static method associated.
			IFunction function = (IFunction) functionDefintionBind;
			helper.createAndAddMethod(function, context.nameSpec);
		}
		// Is not recognized (generate an error).
		else
		{
			stackClassActives.peek().addDependency(new UnImplemented(dec.getRawSignature(),
					"Undefined declaration"));
		}

		// Visit and add the statment to the method.
		Statement stmt = helper.visitStatement(dec.getBody(), this, data);
		if(methActive != null)
			methActive.addStatement(stmt);
		else
			Log.e("<CppRecognizer -- visit(ASTCppFunctionDefinition ...> Error, it is impossible to associate the statement.");

		// Visit the declarator (for example the parameters).
		helper.visitDeclarator(dec.getDeclarator(), this, data);

		// And of the function.
		methActive = null ;

		return Log.MethodOut(Thread.currentThread(),null);
	}

	@Override
	public Object visit(ASTCppVisibilityLabel node, Object data) {
		Log.MethodIn(Thread.currentThread());
		ICPPASTVisibilityLabel decSpec = node.getNode() ;

		// Update the visibility label.
		((Context)data).visibility = helper.visibilityLabelToModifier(decSpec.getVisibility());

		// Return the conversion of the visibility label.
		return Log.MethodOut(Thread.currentThread(),((Context)data).visibility);
	}

	@Override
	public Object visit(ASTCppDeclSpecifier node, Object data) {
		Log.MethodIn(Thread.currentThread());
		// Unused.
		return Log.MethodOut(Thread.currentThread(),null);
	}

	@Override
	public Object visit(ASTCppCompositeTypeSpecifier node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTCompositeTypeSpecifier decSpec = node.getNode() ;

		// Get the name of the composite type.
		String className = helper.visitName(decSpec.getName(), this, data);

		// Create class.
		ClassDef unitaryClass = helper.getClassDef(className);

		// The class is added to goolClasses if it does not exist.
		if (unitaryClass == null){
			unitaryClass = helper.createClassDef(className);
			goolClasses.put(unitaryClass.getType(),unitaryClass);
		}

		// Add to stack active.
		stackClassActives.push(unitaryClass);

		// Visit ICPPASTBaseSpecifier, ie. inherits declaration.
		helper.visitInherits(decSpec.getChildren(), this, data) ;


		// Get the visibility label (by default PUBLIC).
		Context context = (Context) data ;
		context.visibility = helper.visitDeclarationVisibilityLabel(decSpec.getMembers(), this, data);

		// Visit the declarations.
		helper.visitDeclarations(decSpec.getMembers(), this, context);

		// Drop from stack active.
		stackClassActives.pop();

		return Log.MethodOut(Thread.currentThread(),null);
	}

	@Override
	public Object visit(ASTCppSimpleDeclSpecifier node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTSimpleDeclSpecifier decSpec = node.getNode() ;

		// Gets the list of modifier (isLong, isImaginary, isVolatile ...)
		List<Modifier> toReturn = helper.getModifierDeclaration(decSpec);

		// Return the list of modifier associated.
		return Log.MethodOut(Thread.currentThread(), toReturn);
	}

	@Override
	public Object visit(ASTCppNamedTypeSpecifier node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTNamedTypeSpecifier decSpec = node.getNode() ;

		// Get and add to the context, the name of the specifier.
		Context context = (Context) data ;
		context.nameSpec = helper.visitName(decSpec.getName(), this, data);

		return Log.MethodOut(Thread.currentThread(), null); 
	}

	@Override
	public Object visit(ASTCppEnumerationSpecifier node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTEnumerationSpecifier decSpec = node.getNode();

		// Gets the name of the enumeration.
		String name = helper.visitName(decSpec.getName(), this, data);

		// Gets the list of enumerators associated. 
		List<Field> enumerators = helper.visitsEnumerators(
				decSpec.getEnumerators(), this, data);

		// Create and add the enumeration in the gool ast.
		helper.createAndAddEnum(name, enumerators);

		return Log.MethodOut(Thread.currentThread(), null);
	}

	@Override
	public Object visit(ASTCppEnumerator node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTEnumerationSpecifier.IASTEnumerator enumerator = node.getNode();

		// Gets the name of the enumerator.
		String name = helper.visitName(enumerator.getName(), this, data);

		// Gets the initial value of the enumerator.
		Expression exp = helper.visitExpression(enumerator.getValue(), this, data);

		// Create the field with modifiers PUBLIC and STATIC.
		Field toReturn = new Field(name, TypeInt.INSTANCE, exp) ;
		toReturn.addModifier(Modifier.PUBLIC);
		toReturn.addModifier(Modifier.STATIC);

		// Return the enumerator, as a field.
		return Log.MethodOut(Thread.currentThread(), toReturn);
	}

	@Override
	public Object visit(ASTCppBaseSpecifier node, Object data) {
		Log.MethodIn(Thread.currentThread());
		ICPPASTBaseSpecifier baseSpecifier = node.getNode();

		boolean correct = false ;
		IASTName name = baseSpecifier.getName();
		if (name.isReference()) {
			// Resolved bindind.
			// We recognized inheritance.
			IBinding b = name.resolveBinding();
			if (b instanceof ICPPClassType){
				if(stackClassActives.peek().getParentClass() == null)
					stackClassActives.peek().setParentClass(
							new gool.ast.type.TypeClass(name.toString()));
				else{
					// Unrecognozed this part because no multiple inheritance in gool ast.
					stackClassActives.peek().addDependency(
							new UnImplemented(baseSpecifier.getRawSignature(), 
									"Multiple inheritance"));
				}
				correct = true ;
			}
		}

		// Case error : unrecognized this part.
		if(!correct && name.isReference()){
			if(stackClassActives.peek().getParentClass() == null)
				stackClassActives.peek().setParentClass(new gool.ast.type.TypeUnknown(name.toString()));
			else
				stackClassActives.peek().addDependency(
						new UnImplemented(baseSpecifier.getRawSignature(), "Multiple inheritance (and Unknown type)"));
		}

		return Log.MethodOut(Thread.currentThread(), null);
	}

	@Override
	public Object visit(ASTCppDeclarator node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTDeclarator decl = node.getNode();

		// Gets the context.
		Context context = ((Context)data) ;

		// Get the visibility label.
		Modifier visibility = ((Context) data).visibility ;

		// To know is its a construction.
		String nameSpec = ((Context) data).nameSpec ;

		// Get the name of the declarator.
		String nameDec = helper.visitName(decl.getName(), this, data);

		// Get the initializer expression.
		Expression init = helper.visitInitializer(decl.getInitializer(), this, data) ;

		// Case : it is a construction.
		if(nameSpec != null){
			if(init instanceof ClassNew){
				((ClassNew)init).setType(new TypeClass(nameSpec));
			}
		}

		// We resolved the binding to get the real type.
		IBinding bind = decl.getName().resolveBinding();

		// Is a parameter definition.
		if(bind instanceof IParameter){
			// Add the parameter to method of the class.
			helper.createAndAddParameter((IParameter) bind, nameSpec);
		}
		// Is a field definition.
		else if(bind instanceof IField){
			// Add the field to the class.
			helper.createAndAddField(bind.getOwner().toString(), 
					(IField)bind, visibility, nameDec, init, context.nameSpec);
		}
		// Is a variable definition.
		else if(bind instanceof IVariable){
			// Add the field to the class or return the variable declaration.
			VarDeclaration var = helper.createAndAddVariable(
					(bind.getOwner() != null)? bind.getOwner().toString() : null, 
							(IVariable)bind, visibility, nameDec, 
							init, context.nameSpec);
			if(var != null)
				return Log.MethodOut(Thread.currentThread(), var);		
		}
		// Case unrecognized (generate an error).
		else if(!(bind instanceof IFunction)){
			// Add error.
			helper.addAnError(bind.getOwner().toString(), decl.getRawSignature(), 
					"Undefined declarator");
		}

		return Log.MethodOut(Thread.currentThread(), null);
	}

	@Override
	public Object visit(ASTCppArrayDeclarator node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTArrayDeclarator decl = node.getNode();

		// Get the visibility label.
		Modifier visibility = ((Context) data).visibility ;

		// Get the name of the declarator.
		String nameDec = helper.visitName(decl.getName(), this, data);


		// Get the initializer expression.
		Expression init = helper.visitInitializer(decl.getInitializer(), this, data) ;

		List<Expression> dims = new ArrayList<Expression>();


		for(IASTArrayModifier modifier : decl.getArrayModifiers()){
			dims.add(helper.visitExpression(modifier.getConstantExpression(), this, data));
		}

		// We resolved the binding to get the real type.
		IBinding bind = decl.getName().resolveBinding();

		// Is a parameter definition.
		if(bind instanceof IParameter){
			// Add the parameter to method of the class.
			helper.createAndAddArrayParameter((IParameter) bind, dims);
		}
		// Is a field definition.
		else if(bind instanceof IField){
			// Add the field to the class.
			helper.createAndAddArrayField(bind.getOwner().toString(), (IField)bind, 
					visibility, nameDec, init, dims);
		}
		// Is a variable definition.
		else if(bind instanceof IVariable){
			// Add the field to the class or return the variable declaration.
			VarDeclaration var = helper.createAndAddArrayVariable(
					bind.getOwner().toString(), (IVariable)bind, visibility, nameDec, init, dims);
			if(var != null)
				return Log.MethodOut(Thread.currentThread(), var);		
		}
		// Case unrecognized (generate an error).
		else if(!(bind instanceof IFunction)){
			// Add error.
			helper.addAnError(bind.getOwner().toString(), decl.getRawSignature(), "Undefined declarator");
		}

		// Generate error for pointer operators.
		for(IASTPointerOperator po : decl.getPointerOperators())
			methActive.getClassDef().addDependency(
					new UnImplemented(po.getRawSignature(), "Pointer operator"));

		return Log.MethodOut(Thread.currentThread(), null);
	}

	@Override
	public Object visit(ASTCppFieldDeclarator node, Object data) {
		Log.MethodIn(Thread.currentThread());
		// Unused.
		return Log.MethodOut(Thread.currentThread(), null);
	}

	@Override
	public Object visit(ASTCppFunctionDeclarator node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTFunctionDeclarator decl = node.getNode();

		// Visit the parameters, if exist.
		int params = helper.visitParameters(decl.getChildren(), this, data);


		// Generate error for pointer operators.
		for(IASTPointerOperator po : decl.getPointerOperators())
			methActive.getClassDef().addDependency(
					new UnImplemented(po.getRawSignature(), "Pointer operator"));

		// Case constructor. TODO : refactoring.
		if(params == 0 && ((Context)data).nameSpec != null){
			String name = helper.visitName(decl.getName(), this, data);
			VarDeclaration var = new VarDeclaration(
					new TypeClass(((Context)data).nameSpec), name);
			var.setInitialValue(new ClassNew(new TypeClass(((Context)data).nameSpec)));
			return Log.MethodOut(Thread.currentThread(), var);
		}

		return Log.MethodOut(Thread.currentThread(), null);
	}

	@Override
	public Object visit(ASTCppName node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTName name = node.getNode();
		// Return the name.
		return Log.MethodOut(Thread.currentThread(), name.toString());
	}

	@Override
	public Object visit(ASTCppStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTStatement stmt = node.getNode();

		// Error statement (generate error).
		return Log.MethodOut(Thread.currentThread(), 
				new ExpressionUnknown(TypeNone.INSTANCE, stmt.getRawSignature()));
	}

	@Override
	public Object visit(ASTCppCompoundStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTCompoundStatement stmts = node.getNode();

		// Create a block and add statements.
		Block toReturn = new Block();
		for(IASTStatement stmt : stmts.getStatements()){
			Statement stmtAdd = helper.visitStatement(stmt, this, data) ;
			if(stmtAdd != null)
				toReturn.addStatement(stmtAdd);
		}

		// Return the block of statements.
		return Log.MethodOut(Thread.currentThread(), toReturn);
	}

	@Override
	public Object visit(ASTCppDeclarationStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTDeclarationStatement stmt = node.getNode();

		// Get the list of declarations.
		List<Dec> decs = (List<Dec>) helper.visitDeclaration(stmt.getDeclaration(), this, data);

		if(decs.size() > 1){
			// Case : multiple declarations.
			Block toReturn = new Block();
			for(Dec decToAdd : decs)
				toReturn.addStatement(decToAdd);
			return Log.MethodOut(Thread.currentThread(), toReturn);
		}
		if(decs.size() == 1)
			// Case : one declaration.
			return Log.MethodOut(Thread.currentThread(), decs.get(0));
		// Case : no declaration.
		return Log.MethodOut(Thread.currentThread(), null);
	}

	@Override
	public Object visit(ASTCppDefaultStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTDefaultStatement stmt = node.getNode();

		// Unimplemented in GOOL (generate an error).
		return Log.MethodOut(Thread.currentThread(), 
				new ExpressionUnknown(TypeNone.INSTANCE, stmt.getRawSignature()));
	}

	@Override
	public Object visit(ASTCppDoStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTDoStatement stmt = node.getNode();

		// Unimplemented in GOOL (generate an error).
		return Log.MethodOut(Thread.currentThread(), 
				new ExpressionUnknown(TypeNone.INSTANCE, stmt.getRawSignature()));
	}

	@Override
	public Object visit(ASTCppExpressionStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTExpressionStatement stmt = node.getNode();

		// Visit the expression as a statement.
		Statement exp = helper.visitExpressionStatement(stmt.getExpression(),this,data);

		return Log.MethodOut(Thread.currentThread(), exp);
	}

	@Override
	public Object visit(ASTCppForStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTForStatement stmt = node.getNode();

		// Get the initializer (for(initializer;;)).
		Statement init = helper.visitStatement(stmt.getInitializerStatement(), this, data);
		// Get the condition (for(;condition;)).
		Expression cond = helper.visitExpression(stmt.getConditionExpression(), this, data);
		// Get the iterator (for(;;iterator)).
		Statement iter = helper.visitExpression(stmt.getIterationExpression(), this, data);
		// Get the body (for(;;){body}).
		Statement body = helper.visitStatement(stmt.getBody(), this, data);

		// Rebuild if empty.
		if(init == null) init = new Block();
		if(cond == null) cond = new Constant(TypeBool.INSTANCE, "");
		if(iter == null) iter= new Block();
		if(body == null) body = new Block();

		// Return a for statement.
		return Log.MethodOut(Thread.currentThread(), new For(init, cond, iter, body));
	}

	@Override
	public Object visit(ASTCppIfStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTIfStatement stmt = node.getNode();

		// Get the condition (if(condition){}else{}).
		Expression cond = helper.visitExpression(stmt.getConditionExpression(), this, data);

		// Get the then clause (if(){then clause}else{}).
		Statement thenC = helper.visitStatement(stmt.getThenClause(), this, data);

		// Get the else clause (if(){}else{else clause}).
		Statement elseC = helper.visitStatement(stmt.getElseClause(), this, data);

		// Return a if statement.
		return Log.MethodOut(Thread.currentThread(), new If(cond, thenC, elseC));
	}

	@Override
	public Object visit(ASTCppReturnStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTReturnStatement stmt = node.getNode();

		// Get the expression returned (return expression).
		Expression ret = helper.visitExpression(stmt.getReturnValue(), this, data);

		// Return a return statement.
		return Log.MethodOut(Thread.currentThread(), new Return(ret));
	}

	@Override
	public Object visit(ASTCppWhileStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTWhileStatement stmt = node.getNode();

		// Get the condition (while(condition){}).
		Expression cond = helper.visitExpression(stmt.getCondition(), this, data);

		// Get the body (while(){body}).
		Statement body = helper.visitStatement(stmt.getBody(), this, data);

		// Return a while statement.
		return Log.MethodOut(Thread.currentThread(), new While(cond, body));
	}

	@Override
	public Object visit(ASTCppSwitchStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTSwitchStatement stmt = node.getNode();

		// Unimplemented in GOOL (generate an error).
		return Log.MethodOut(Thread.currentThread(), 
				new ExpressionUnknown(TypeNone.INSTANCE, stmt.getRawSignature()));
	}

	@Override
	public Object visit(ASTCppTryBlockStatement node, Object data) {
		Log.MethodIn(Thread.currentThread());
		ICPPASTTryBlockStatement stmt = node.getNode();

		// Visit the block try.
		Statement tryStmt = helper.visitStatement(
				stmt.getTryBody(), this, data);

		// Visits the catches statements.
		// Normaly it is catch statement.
		List<Statement> stmts = helper.visitStatements(
				stmt.getCatchHandlers(), this, data) ; 

		// Check if its catch statements.
		List<Catch> catches = new ArrayList<Catch>();
		for(Statement catchStmt : stmts){
			if(catchStmt instanceof Catch)
				catches.add((Catch) catchStmt);
			else
				Log.w("Catches Error");
		}

		// Return the new try catch finally statement.
		return Log.MethodOut(Thread.currentThread(), 
				new Try(catches, (Block) tryStmt, new Block()));
	}

	@Override
	public Object visit(ASTCppCatchHandler node, Object data) {
		Log.MethodIn(Thread.currentThread());
		ICPPASTCatchHandler stmt = node.getNode();

		// Gets the parameters of the cath.
		List<VarDeclaration> parameters =  (List<VarDeclaration>) helper.visitDeclaration(
				stmt.getDeclaration(), this, data);

		// Gets the body associated of the catch.
		Statement catchStmt = helper.visitStatement(
				stmt.getCatchBody(), this, data);

		// Check the number of parameters.
		if(parameters.size() == 0){
			// 0 parameter, in the catch.
			// Return UKNOWN Catch.
			return Log.MethodOut(Thread.currentThread(), new Catch(new VarDeclaration(
					new TypeUnknown("UNKNOWN"), "UNKNOWN"), (Block) catchStmt));
		}
		else if(parameters.size() > 1){
			// Error, just one parameter possible.
			for(int i = 1 ; i < parameters.size() ; i++){
				helper.addAnError(parameters.get(i).toString(), 
						"Multiple parameter in a catch");
			}
		}

		// Return the catch statement.
		return Log.MethodOut(Thread.currentThread(), new Catch(parameters.get(0), (Block) catchStmt));
	}


	@Override
	public Object visit(ASTCppExpression node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTExpression exp = node.getNode();

		// Unimplemented in GOOL (generate an error).
		return Log.MethodOut(Thread.currentThread(), 
				new ExpressionUnknown(TypeNone.INSTANCE, exp.getRawSignature()));
	}

	@Override
	public Object visit(ASTCppArraySubscriptExpression node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTArraySubscriptExpression exp = node.getNode();

		// Gets the dims access.
		List<Expression> expsArray = helper.visitExpressionsParameters(
				exp.getChildren(), this, data);

		// Gets the array expression.
		Expression expArray = helper.visitExpression(exp.getArrayExpression(), this, data);

		// Return a array access.
		return Log.MethodOut(Thread.currentThread(), 
				new ArrayAccess(expArray, expsArray.get(1)));
	}

	@Override
	public Object visit(ASTCppBinaryExpression node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTBinaryExpression binExp = node.getNode();
		// Case : is the assign operator.
		if(helper.isAssignOperator(binExp.getOperator())){
			// Gets the left expression.
			Expression exp1 = helper.visitExpression(binExp.getOperand1(), this, data);

			// Gets the right expression.
			Expression exp2 = helper.visitExpression(binExp.getOperand2(), this, data);

			// Return a assign.
			return Log.MethodOut(Thread.currentThread(), new Assign(exp1, exp2));
		}
		// Case : is a binary assign operator('+=','*=',...).
		else if(helper.isBinaryAssignOperator(binExp.getOperator())){
			// Gets the operator, and its symbol, of the expression.
			Operator op = helper.convertBinaryOperator(binExp.getOperator());
			String symbol = helper.getSymboleBinaryOperator(binExp.getOperator()) ;
			org.eclipse.cdt.core.dom.ast.IType expType = binExp.getExpressionType();
			Log.d(String.format("<CppRecognizer -- visit(ASTCppBinaryExpression...)> %s",
					ASTTypeUtil.getType(expType)));
			// Gets the context.
			Context context = ((Context)data) ;
			// Gets the type of the expression.
			IType type = helper.convertTypeGool(expType, context.nameSpec);

			// Gets the left expression.
			Expression exp1 = helper.visitExpression(binExp.getOperand1(), this, data);

			// Gets the right expression.
			Expression exp2 = helper.visitExpression(binExp.getOperand2(), this, data);

			// Return a assign.
			return Log.MethodOut(Thread.currentThread(), 
					new Assign(exp1, new BinaryOperation(op, exp1, exp2, type, symbol)));
		}
		// Case : is an other binary operator.
		else{
			// Gets the operator, and its symbol, of the expression.
			Operator op = helper.convertBinaryOperator(binExp.getOperator());
			String symbol = helper.getSymboleBinaryOperator(binExp.getOperator()) ;
			org.eclipse.cdt.core.dom.ast.IType expType = binExp.getExpressionType();
			Log.d(String.format("<CppRecognizer -- visit(ASTCppBinaryExpression...)> %s",
					ASTTypeUtil.getType(expType)));
			// Gets the context.
			Context context = ((Context)data) ; 

			// Gets the type of the expression.
			IType type = helper.convertTypeGool(expType, context.nameSpec);

			// Gets the left expression.
			Expression exp1 = helper.visitExpression(binExp.getOperand1(), this, data);
			String exp1Code = binExp.getOperand1().getRawSignature();

			// Gets the right expression.
			Expression exp2 = helper.visitExpression(binExp.getOperand2(), this, data);
			String exp2Code = binExp.getOperand2().getRawSignature();

			Log.d(String.format("<CppRecognizer -- visit(ASTCppBinaryExpression...)> %s | %s | %s", 
					exp1Code, symbol.toString(), exp2Code));
			// Treat the case is System.out call.
			if(symbol.compareTo("<<") == 0){
				Log.d("<CppRecognizer -- visit(ASTCppBinaryExpression node, Object data)> << operator.");				
				// Case : add parameters.
				if(exp1 instanceof SystemOutPrintCall){
					Log.d("<CppRecognizer -- visit(ASTCppBinaryExpression node, Object data)> --> add parameters to a SystemOutPrintCall");
					SystemOutPrintCall sysPrint = ((SystemOutPrintCall)exp1);
					if(exp2Code.compareTo("std::endl") == 0){
						sysPrint.setEndofline(true);
					}
					//					sysPrint.getParameters().set(0,
					//							new BinaryOperation(Operator.PLUS, sysPrint.getParameters().get(0), 
					//									exp2, TypeString.INSTANCE, "+"));
					return Log.MethodOut(Thread.currentThread(), sysPrint);
				}
				// Case : create SystemOutPrintCall.
				else if(exp1Code.compareTo("std::cout") == 0){
					Log.d("<CppRecognizer -- visit(ASTCppBinaryExpression node, Object data)> --> Create a SystemOutPrintCall");
					methActive.getClassDef().addDependency(new SystemOutDependency());
					SystemOutPrintCall target = new SystemOutPrintCall();
					if(exp2Code.compareTo("std::endl") == 0){
						target.setEndofline(true);
					}
					Log.d("<CppRecognizer -- visit(ASTCppBinaryExpression node, Object data)> exp2 type : " + exp2.getType().toString());
					target.addParameter(exp2) ;
					return Log.MethodOut(Thread.currentThread(), target);
				}
			}

			// Return a binary expression.
			return Log.MethodOut(Thread.currentThread(),
					new BinaryOperation(op, exp1, exp2, type, symbol));
		}
	}

	@Override
	public Object visit(ASTCppCastExpression node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTCastExpression expCast = node.getNode();

		// Gets the context.
		Context context = ((Context)data) ; 

		// Get the type of the cast.
		IType targetType = helper.convertTypeGool(expCast.getExpressionType(), context.nameSpec);

		// Get the casted expression.
		Expression exp = helper.visitExpression(expCast.getOperand(), this, data);

		// Return a cast expression.
		return Log.MethodOut(Thread.currentThread(), new CastExpression(targetType, exp));
	}

	@Override
	public Object visit(ASTCppConditionalExpression node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTConditionalExpression exp = node.getNode();

		// Unimplemented in GOOL (generate an error).
		return Log.MethodOut(Thread.currentThread(),
				new ExpressionUnknown(TypeNone.INSTANCE, exp.getRawSignature()));
	}

	@Override
	public Object visit(ASTCppExpressionList node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTExpressionList exp = node.getNode();

		// Unimplemented in GOOL (generate an error).
		return Log.MethodOut(Thread.currentThread(),
				new ExpressionUnknown(TypeNone.INSTANCE, exp.getRawSignature()));
	}

	@Override
	public Object visit(ASTCppFieldReference node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTFieldReference exp = node.getNode();

		// Gets the context.
		Context context = ((Context)data) ; 

		// Gets the type of the expression.
		gool.ast.type.IType type = helper.convertTypeGool(
				exp.getExpressionType(), context.nameSpec);

		// Gets the name of the field.
		String member = helper.visitName(exp.getFieldName(), this, data);

		// Gets the target.
		Expression target = helper.visitExpression(exp.getFieldOwner(), this, data);

		// Case recognized method.
		String methodGool = RecognizerMatcher.matchMethod(
				helper.transformMethodMathcherInvocation(
						exp.getFieldOwner(), exp.getFieldName()));
		if(methodGool != null){
			context.methGool = methodGool ;
		}

		// Return a field access.
		return Log.MethodOut(Thread.currentThread(),
				new FieldAccess(type, target, member));
	}

	@Override
	public Object visit(ASTCppFunctionCallExpression node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTFunctionCallExpression exp = node.getNode();

		// Gets the context.
		Context context = ((Context)data) ; 

		// Gets the type of the expression.
		gool.ast.type.IType type = helper.convertTypeGool(
				exp.getExpressionType(), context.nameSpec);

		// The target object in the method invocation.
		Expression target = null;

		// The method invocation.
		Meth meth = null;

		// Visit expression.
		Expression expAccess = helper.visitExpression(
				exp.getFunctionNameExpression(), this, data);

		// Case : method.
		if(expAccess instanceof FieldAccess){
			meth = new Meth(type,((FieldAccess)expAccess).getMember());
			target = ((FieldAccess)expAccess).getTarget();
			Log.d("<CppRecognizer - visit(ASTCppFunctionCallExpression...)> Case FieldAccess: " + meth.getName());
		}
		// Case : function.
		else if(expAccess instanceof Identifier){
			meth = new Meth(type,((Identifier)expAccess).getName());
			target = helper.getIdentifierOfTransitionUnit(exp.getTranslationUnit());
			Log.d("<CppRecognizer - visit(ASTCppFunctionCallExpression...)> Case Identifier: " + meth.getName());
		}
		// Error return the expression.
		else
			return Log.MethodOut(Thread.currentThread(), expAccess);

		// Gets the parameters lists of the invocation.
		List<Expression> params = helper.visitExpressionsParameters(
				Arrays.copyOfRange(exp.getChildren(), 1, exp.getChildren().length), this, data) ;

		for(Expression p : params){
			Log.d("<CppRecognizer - visit(ASTCppFunctionCallExpression...)> Parameter : " + p.getType().getName());
		}
		// Create the method invocation.
		MethCall toReturn = MethCall.create(type, target, meth, null);
		toReturn.addParameters(params);

		// Case : the method is in the gool library.
		if(context.methGool != null){
			toReturn.setGoolLibraryMethod(context.methGool);
			context.methGool = null ; 
		}

		// Return a method invocation.
		return Log.MethodOut(Thread.currentThread(), toReturn);
	}

	@Override
	public Object visit(ASTCppIdExpression node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTIdExpression exp = node.getNode();

		// Gets the context.
		Context context = ((Context)data) ; 

		// Gets the type of the expression.
		gool.ast.type.IType type = helper.convertTypeGool(
				exp.getExpressionType(), context.nameSpec);

		// Gets the name of the identifier.
		String name = helper.visitName(exp.getName(), this, data);

		// Treat the case : static field access (refactoring).
		if(exp.getName().isReference()){
			IBinding bind = exp.getName().resolveBinding() ;
			if(bind instanceof IVariable){
				IVariable var = (IVariable) bind ;
				if(var.getOwner() == null){
					// Return static field access of the transitionUnit class.
					return Log.MethodOut(Thread.currentThread(), new FieldAccess(type, 
							helper.getIdentifierOfTransitionUnit(exp.getTranslationUnit()), 
							name));
				}
			}
		}

		// Return a identifier.
		return Log.MethodOut(Thread.currentThread(), new Identifier(type, name));
	}

	@Override
	public Object visit(ASTCppLiteralExpression node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTLiteralExpression exp = node.getNode();

		// Gets the context.
		Context context = ((Context)data) ; 

		// Case the expression is a this.
		if(exp.getRawSignature().toString().compareTo("this") == 0){
			// Return the this expression.
			return Log.MethodOut(Thread.currentThread(), new This(helper.convertTypeGool(
					exp.getExpressionType(), context.nameSpec)));
		}
		IType type = helper.convertTypeGool(exp.getExpressionType(), context.nameSpec);
		String code = exp.getRawSignature().toString();
		if (type instanceof TypeString){
			//Remove quotes
			code = code.substring(code.indexOf("\"")+1,code.lastIndexOf("\""));
		}

		Log.d(String.format("<CppRecognizer - visit(ASTCppLiteralExpression)> expression <%s> with type %s",
				code, type));
		// Return a constant expression.
		return Log.MethodOut(Thread.currentThread(), new Constant(type, code));
	}

	@Override
	public Object visit(ASTCppUnaryExpression node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTUnaryExpression unExp = node.getNode();

		// Gets the context.
		Context context = ((Context)data) ; 

		// Gets the operator, and its symbol, of the expression.
		Operator op = helper.convertUnaryOperator(unExp.getOperator());
		String symbol = helper.getSymboleUnaryOperator(unExp.getOperator()) ;

		// Gets the type of the expression.
		IType type = helper.convertTypeGool(
				unExp.getExpressionType(), context.nameSpec);

		// Gets the expression.
		Expression exp = helper.visitExpression(unExp.getOperand(), this, data);

		// Case : it is a throw.
		if(symbol.compareTo("throw") == 0){
			// Return a throw expression.
			return Log.MethodOut(Thread.currentThread(), new Throw(exp));
		}

		// Return a unary expression. 
		return Log.MethodOut(Thread.currentThread(), new UnaryOperation(op, exp, type, symbol));
	}

	@Override
	public Object visit(ASTCppInitializer node, Object data) {
		Log.MethodIn(Thread.currentThread());
		// TODO Auto-generated method stub
		return Log.MethodOut(Thread.currentThread(), null);
	}

	@Override
	public Object visit(ASTCppInitializerExpression node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTEqualsInitializer init = node.getNode();

		// Gets the initializer expression.
		Expression exp = helper.visitExpression(
				(IASTExpression) init.getChildren()[0], this, data);

		// Return a expression for an initializer.
		return Log.MethodOut(Thread.currentThread(), exp);
	}

	@Override
	public Object visit(ASTCppConstructorInitializer node, Object data) {
		Log.MethodIn(Thread.currentThread());
		ICPPASTConstructorInitializer init = node.getNode();

		// Visit expressions if exists and return a classNew.
		return Log.MethodOut(Thread.currentThread(),
				helper.visitExpressions(init.getChildren(), this, data));
	}

	@Override
	public Object visit(ASTCppParameterDeclaration node, Object data) {
		Log.MethodIn(Thread.currentThread());
		IASTParameterDeclaration dec = node.getNode();

		// Visit the declaration specifier of the parameter.
		helper.visitDeclSpecifier(dec.getDeclSpecifier(), this, data);

		// Visit the declarator of the parameter.
		helper.visitDeclarator(dec.getDeclarator(), this, data);

		return Log.MethodOut(Thread.currentThread(), null);
	}

	private class Context {
		Modifier visibility ;
		String nameSpec ;
		String methGool ;
	}

}
