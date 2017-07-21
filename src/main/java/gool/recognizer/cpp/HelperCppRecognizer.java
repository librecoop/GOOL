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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.cdt.core.dom.ast.ASTTypeUtil;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTEnumerationSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTEnumerationSpecifier.IASTEnumerator;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IField;
import org.eclipse.cdt.core.dom.ast.IFunction;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IProblemType;
import org.eclipse.cdt.core.dom.ast.IVariable;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPMethod;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTBinaryExpression;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTCompositeTypeSpecifier;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTUnaryExpression;

import gool.ast.core.ArrayNew;
import gool.ast.core.ClassDef;
import gool.ast.core.ClassNew;
import gool.ast.core.Constructor;
import gool.ast.core.Dec;
import gool.ast.core.Expression;
import gool.ast.core.ExpressionUnknown;
import gool.ast.core.Field;
import gool.ast.core.Identifier;
import gool.ast.core.MainMeth;
import gool.ast.core.Meth;
import gool.ast.core.Modifier;
import gool.ast.core.Operator;
import gool.ast.core.RecognizedDependency;
import gool.ast.core.Statement;
import gool.ast.core.UnImplemented;
import gool.ast.core.VarDeclaration;
import gool.ast.type.IType;
import gool.ast.type.TypeBool;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeGoolLibraryClass;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeNone;
import gool.ast.type.TypeString;
import gool.ast.type.TypeUnknown;
import gool.ast.type.TypeVar;
import gool.ast.type.TypeVoid;
import gool.recognizer.common.RecognizerMatcher;
import gool.recognizer.cpp.ast.ASTCppNode;
import gool.recognizer.cpp.ast.declaration.ASTCppDeclaration;
import gool.recognizer.cpp.ast.declaration.ASTCppVisibilityLabel;
import gool.recognizer.cpp.ast.declarator.ASTCppDeclarator;
import gool.recognizer.cpp.ast.declspecifier.ASTCppDeclSpecifier;
import gool.recognizer.cpp.ast.expression.ASTCppExpression;
import gool.recognizer.cpp.ast.initializer.ASTCppInitializer;
import gool.recognizer.cpp.ast.name.ASTCppName;
import gool.recognizer.cpp.ast.other.ASTCppBaseSpecifier;
import gool.recognizer.cpp.ast.other.ASTCppEnumerator;
import gool.recognizer.cpp.ast.other.ASTCppIncludeStatement;
import gool.recognizer.cpp.ast.other.ASTCppParameterDeclaration;
import gool.recognizer.cpp.ast.statement.ASTCppStatement;
import gool.recognizer.cpp.visitor.IVisitorASTCpp;
import logger.Log;

public class HelperCppRecognizer {

	private CppRecognizer recognizer = null ;

	public HelperCppRecognizer(CppRecognizer recognizer){
		this.recognizer = recognizer ;
	}





	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////   VISITORS   //////////////////////////////////

	public void visitTranslationUnit(IASTTranslationUnit tuAst, IVisitorASTCpp visistor, Object data){
		visitDeclarations(tuAst.getDeclarations(), visistor, data);
	}

	public void visitIncludeStatment(IASTPreprocessorIncludeStatement incAst, IVisitorASTCpp visistor, Object data){
		ASTCppNode incNode = ASTCppNode.transforme(incAst) ;
		if(incNode instanceof ASTCppIncludeStatement){
			((ASTCppIncludeStatement)incNode).accept(visistor, data);
		}
		else
			Log.w("Impossible de visiter : " + incAst.getClass());
	}

	public List<Dec> visitDeclarations(IASTDeclaration[] decsAst, IVisitorASTCpp visistor, Object data){
		List<Dec> toReturn = new ArrayList<Dec>();
		for(IASTDeclaration dec : decsAst){
			Object decToAdd = visitDeclaration(dec, visistor, data);
			if(decToAdd instanceof Dec)
				toReturn.add((Dec) decToAdd);
		}
		return toReturn;
	}

	public Object visitDeclaration(IASTDeclaration decAst, IVisitorASTCpp visistor, Object data){
		ASTCppNode decNode = ASTCppNode.transforme(decAst) ;
		if(decNode instanceof ASTCppDeclaration){
			return ((ASTCppDeclaration)decNode).accept(visistor, data);
		}
		else
			Log.w("Impossible de visiter : " + decAst.getClass());
		return null ;
	}

	public Modifier visitDeclarationVisibilityLabel(IASTDeclaration[] decsAst, IVisitorASTCpp visistor, Object data){
		Modifier toReturn = Modifier.PUBLIC ;
		for(IASTDeclaration dec : decsAst){
			ASTCppNode decNode = ASTCppNode.transforme(dec) ;
			if(decNode instanceof ASTCppVisibilityLabel){
				return (Modifier) ((ASTCppVisibilityLabel)decNode).accept(visistor, data);
			}
			else if(!(decNode instanceof ASTCppDeclaration))
				Log.w("Impossible de visiter : " + dec.getClass());
		}
		return toReturn ;
	}

	public Dec visitDeclarator(IASTDeclarator decAst, IVisitorASTCpp visistor, Object data){
		ASTCppNode decNode = ASTCppNode.transforme(decAst) ;
		if(decNode instanceof ASTCppDeclarator){
			return ((Dec) ((ASTCppDeclarator)decNode).accept(visistor, data));
		}
		else
			Log.w("Impossible de visiter : " + decAst.getClass());
		return null ;
	}

	public Object visitDeclSpecifier(IASTDeclSpecifier decSpecAst, IVisitorASTCpp visistor, Object data){
		ASTCppNode decSpecNode = ASTCppNode.transforme(decSpecAst) ;
		if(decSpecNode instanceof ASTCppDeclSpecifier){
			return ((ASTCppDeclSpecifier)decSpecNode).accept(visistor, data);
		}
		else
			Log.w("Impossible de visiter : " + decSpecAst.getClass());
		return null ;
	}

	public Expression visitExpression(IASTExpression expAst, IVisitorASTCpp visistor, Object data){
		if(expAst == null)
			return null ;
		ASTCppNode expNode = ASTCppNode.transforme(expAst);
		if(expNode instanceof ASTCppExpression){
			return (Expression) ((ASTCppExpression)expNode).accept(visistor, data);
		}
		else
			Log.w("Impossible de visiter : " + expAst.getClass());
		return null ;
	}

	public Statement visitExpressionStatement(IASTExpression expAst, IVisitorASTCpp visistor, Object data){
		if(expAst == null)
			return null ;
		ASTCppNode expNode = ASTCppNode.transforme(expAst);
		if(expNode instanceof ASTCppExpression){
			return (Statement) ((ASTCppExpression)expNode).accept(visistor, data);
		}
		else
			Log.w("Impossible de visiter : " + expAst.getClass());
		return null ;
	}

	public ClassNew visitExpressions(IASTNode[] childrenAst, IVisitorASTCpp visistor, Object data){
		if(childrenAst == null)
			return null ;
		ClassNew toReturn = new ClassNew(TypeNone.INSTANCE);
		for(IASTNode child : childrenAst){
			ASTCppNode expNode = ASTCppNode.transforme(child);
			if(expNode instanceof ASTCppExpression){
				toReturn.addParameter((Expression) ((ASTCppExpression)expNode).accept(visistor, data));
			}
		}
		return toReturn ;
	}

	public List<Expression> visitExpressionsParameters(IASTNode[] childrenAst, IVisitorASTCpp visistor, Object data){
		if(childrenAst == null)
			return null ;
		List<Expression> toReturn = new ArrayList<Expression>() ;
		for(IASTNode child : childrenAst){
			ASTCppNode expNode = ASTCppNode.transforme(child);
			if(expNode instanceof ASTCppExpression){
				toReturn.add((Expression) ((ASTCppExpression)expNode).accept(visistor, data));
			}
		}
		return toReturn ;
	}
	public List<Field> visitsEnumerators( IASTEnumerationSpecifier.IASTEnumerator[] enumsAst, 
			IVisitorASTCpp visistor, Object data){
		if(enumsAst == null)
			return null ;
		List<Field> toReturn = new ArrayList<Field>();
		for(IASTEnumerator enumAst : enumsAst){
			ASTCppNode enumNode = ASTCppNode.transforme(enumAst);
			if(enumNode instanceof ASTCppEnumerator){
				toReturn.add((Field) ((ASTCppEnumerator)enumNode).accept(visistor, data));
			}
			else
				Log.w("Impossible de visiter : " + enumAst.getClass());
		}
		return toReturn ;
	}

	public List<Statement> visitStatements(IASTStatement[] stmtsAst, IVisitorASTCpp visistor, Object data){
		if(stmtsAst == null)
			return null ;
		List<Statement> toReturn = new ArrayList<Statement>();
		for(IASTStatement stmtAst : stmtsAst){
			ASTCppNode stmtNode = ASTCppNode.transforme(stmtAst);
			if(stmtNode instanceof ASTCppStatement){
				toReturn.add((Statement) ((ASTCppStatement)stmtNode).accept(visistor, data));
			}
			else
				Log.w("Impossible de visiter : " + stmtAst.getClass());
		}
		return toReturn ;
	}

	public Statement visitStatement(IASTStatement stmtAst, IVisitorASTCpp visistor, Object data){
		if(stmtAst == null)
			return null ;
		ASTCppNode stmtNode = ASTCppNode.transforme(stmtAst);
		if(stmtNode instanceof ASTCppStatement){
			return (Statement) ((ASTCppStatement)stmtNode).accept(visistor, data);
		}
		else
			Log.w("Impossible de visiter : " + stmtAst.getClass());
		return null ;
	}

	public String visitName(IASTName nameAst, IVisitorASTCpp visistor, Object data){
		ASTCppNode nameNode = ASTCppNode.transforme(nameAst);
		if(nameNode instanceof ASTCppName){
			return (String) ((ASTCppName)nameNode).accept(visistor, data);
		}
		else
			Log.w("Impossible de visiter : " + nameAst.getClass());
		return null ;
	}

	public Expression visitInitializer(IASTInitializer initAst, IVisitorASTCpp visistor, Object data){
		ASTCppNode initNode = ASTCppNode.transforme(initAst);
		if(initNode instanceof ASTCppInitializer){
			return (Expression) ((ASTCppInitializer)initNode).accept(visistor, data);
		}
		else if(initNode != null)
			Log.w("Impossible de visiter : " + initAst.getClass());
		return null ;
	}

	public int visitParameters(IASTNode[] childresAst, IVisitorASTCpp visistor, Object data){
		int toReturn = 0 ;
		for(IASTNode child : childresAst){
			ASTCppNode paramNode = ASTCppNode.transforme(child);
			if(paramNode instanceof ASTCppParameterDeclaration){
				((ASTCppParameterDeclaration)paramNode).accept(visistor, data);
				toReturn++;
			}
		}
		return toReturn;
	}

	public void visitInherits(IASTNode[] childresAst, IVisitorASTCpp visistor, Object data){
		for(IASTNode child : childresAst){
			ASTCppNode inherit = ASTCppNode.transforme(child);
			if(inherit instanceof ASTCppBaseSpecifier){
				((ASTCppBaseSpecifier)inherit).accept(visistor, data);
			}
		}
	}

	public void visitErrors(IASTNode[] childresAst, IVisitorASTCpp visistor, Object data){
		for(IASTNode child : childresAst){
			ASTCppNode error = ASTCppNode.transforme(child);
			if(error == null){
				addAnError(null, child.getRawSignature(), "Unrealisable");
			}
		}
	}





	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////   CREATORS   //////////////////////////////////


	//////////////////////////////////
	//////////////////////////////////
	//// CREATE AND ADD :

	/**
	 * Create and add a method in the gool ast.
	 * @param method
	 * 		: The method CDT to add.
	 * @param typeName
	 * 		: The name of the type of the return method.
	 */
	public void createAndAddMethod(ICPPMethod method, String typeName){

		// The method to add in the class.
		Meth toAdd = null ;

		// The class definition of the method.
		ClassDef classdef = getClassDef(method.getOwner().toString());

		// Case : the method is a constructor CPP.
		if(isAConstructor(method, classdef))
			toAdd = createConstructor(method) ;
		else
			toAdd = createMethod(method,typeName);

		// Activated the method.
		recognizer.methActive = toAdd ;

		// Case : the method is a destructor CPP.
		if(method.isDestructor())
			classdef.addDependency(new UnImplemented(method.getName(), "Destructor"));

		// Add the method to the class.
		classdef.addMethod(toAdd);
	}

	/**
	 * Create and add a method (as a function CPP) in the gool ast.
	 * The function is converted in a static method.
	 * @param function
	 * 		: The function CDT to add.
	 * @param typeName
	 * 		: The name of the type of the return function.
	 */
	public void createAndAddMethod(IFunction function, String typeName){

		// The method to add in the class.
		Meth toAdd = null ;

		// Case : it is the main method.
		if(isTheMainFunction(function.getName(), function.getType())){
			Log.d(String.format(
					"<HelperCppRecognizer - createAndAddMethod> function %s is the main function.",
					function.getName()));
			toAdd = new MainMeth() ;
		}
		else{
			Log.d(String.format(
					"<HelperCppRecognizer - createAndAddMethod> function %s is not the main function.",
					function.getName()));
			toAdd = createMethod(function, typeName);
		}

		// Activated the method.
		recognizer.methActive = toAdd ;

		// Add the method to the class.
		if(function.getOwner() != null){
			ClassDef classdef = getClassDef(function.getOwner().toString());
			if(classdef == null)
				recognizer.stackClassActives.peek().addMethod(toAdd);
			else
				classdef.addMethod(toAdd);
		}
		else{
			ClassDef classdef = recognizer.stackClassActives.peek();
			Log.d("<HelperCppRecognizer - createAndAddMethod> method is added to classdef " + classdef.getName() + " with id " + System.identityHashCode(classdef));
			classdef.addMethod(toAdd);
		}
			
	}

	/**
	 * Create and add a parameter of a method in the gool ast.
	 * @param param
	 * 		: The parameter CDT to add.
	 * @param typeName
	 * 		: The name of the type of the parameter.
	 */
	public void createAndAddParameter(IParameter param, String typeName){

		// Add the parameter to the method active.
		if(recognizer.methActive != null)
			recognizer.methActive.addParameter(createParameter(param, typeName));
	}

	/**
	 * Create and add a field of a class definition in the gool ast.
	 * @param owner
	 * 		: The class definition name of the field.
	 * @param field
	 * 		: The field CDT to add.
	 * @param visibility
	 * 		: The visibility of the field.
	 * @param nameDec
	 * 		: The name of the field.
	 * @param init
	 * 		: The initialized expression of the field.
	 * @param typeName
	 * 		: The name of the type of the field.
	 */
	public void createAndAddField(String owner, IField field, 
			Modifier visibility, String nameDec, Expression init, String typeName){

		// The class definition where is the field.
		ClassDef classdef = getClassDef(owner);

		// The field definition, if exist.
		Field fieldtoAdd = getField(classdef, field.getName());

		if(fieldtoAdd == null){
			// Case : the field doesn't exist.
			// We create the field.
			classdef.addField(createField(field, visibility, field.getName(), init, typeName));
		}
		else{
			// Case : the field exist.
			// We update the initiale value of the field.
			fieldtoAdd.setDefaultValue(init);
		}			
	}

	/**
	 * Create and add a variable (as a field) of a class definition in the gool ast.
	 * Or return a variable for the method active (if exit a method active).
	 * @param owner
	 * 		: The class definition name of the variable (maybe null).
	 * @param var
	 * 		: The variable CDT to add.
	 * @param visibility
	 * 		: The visibility of the variable (maybe null).
	 * @param nameDec
	 * 		: The name of the variable.
	 * @param init
	 * 		: The initialized expression of the variable.
	 * @param typeName
	 * 		: The name of the type of the variable.
	 * @return
	 * 		If doesn't exit a method active, return the declaration of the
	 * 		variable, otherwise return null.
	 */
	public VarDeclaration createAndAddVariable(String owner, IVariable var, 
			Modifier visibility, String nameDec, Expression init, String typeName){

		// Case : the variable as a field.
		if(recognizer.methActive == null){
			// Add the field to the class active, ie. to the fileClass.
			if(var.getOwner() == null) 
				recognizer.stackClassActives.peek().addField(
						createField(var, nameDec, init, typeName)) ;
		}
		// Return the variable for the method active.
		else{
			return (createVariable(var, nameDec, init, typeName));
		}
		return null ;
	}

	/**
	 * Create and add a enumeration in the gool ast.
	 * @param 
	 * 		: The parameter CDT to add.
	 * @param typeName
	 * 		: The name of the type of the parameter.
	 */
	public void createAndAddEnum(String name, List<Field> enumerators){

		// Create a classdef as an enum.
		ClassDef toAdd = createEnumClassDef(name, enumerators);

		// Add the enum to the stack of classdef.
		recognizer.goolClasses.put(toAdd.getType(), toAdd);
	}


	//////////////////////////////////
	//////////////////////////////////
	//// CREATE :

	/**
	 * This method give a class name like "Class" 
	 * from a string like "class.cpp".
	 * @param path
	 * 		: The filename used.
	 * @return
	 * 		A class name like of the filename.
	 */
	public String createClassNameFromFilename(String path){
		String filename = new File(path).getName();
		String className = filename.split("\\.")[0];	
		return className.substring(0, 1).toUpperCase() + className.substring(1);// + "FileClass";
	}

	/**
	 * This method can create a classdef, simply.
	 * The classdef created is "public".
	 * @param className
	 * 		: The name of the class (used in his creation).
	 * @return
	 * 		The new classdef.
	 */
	public ClassDef createClassDef (String className) {
		ClassDef classdef = new ClassDef(Modifier.PUBLIC, className);
		Log.d("<HelperCppRecognizer : createClassDef> creation of classdef " + classdef.getName() + " with id " + System.identityHashCode(classdef));
		IType type  = new TypeClass(className);
		classdef.setType(type);
		return classdef;
	}

	/**
	 * This method can create a classdef (as an enumeration), simply.
	 * @param name
	 * 		: The name of the class (used in his creation), 
	 * 		ie. the name of the enumeration.
	 * @param enumerators
	 * 		: The list of field (enumerator) to add in the class.
	 * @return
	 * 		The new classdef as an enumeration.
	 */
	public ClassDef createEnumClassDef(String name, List<Field> enumerators){

		// Create the classdef with modifier PUBLIC.
		ClassDef classdef = new ClassDef(Modifier.PUBLIC, name);
		IType type  = new TypeClass(name);
		classdef.setType(type);

		// Add field in the classdef, ie. add static field with initial value.
		// Transformed, if it is necessary the initial value of fields.
		//Expression init = new Constant(TypeInt.INSTANCE, "0");
		for(Field enumerator : enumerators){
			// It isn't necessary to create an initial value.
			//if(enumerator.getDefaultValue() != null)
			//	init = enumerator.getDefaultValue();
			// It is necessary to create an initial value.
			// So, the initial value create is (init + 1).
			//else{
			//	init = new BinaryOperation(
			//			Operator.PLUS, init, 
			//			new Constant(TypeInt.INSTANCE, "1"), 
			//			TypeInt.INSTANCE, "+");
			//	enumerator.setDefaultValue(init);
			//}
			// Change the field.
			enumerator.setType(new TypeVar(name));
			enumerator.setDefaultValue(new ClassNew(type));

			// Add the field in the class.
			classdef.addField(enumerator);
		}

		// Return the classdef.
		return classdef;
	}

	/**
	 * This method can create a field, simply.
	 * @param field
	 * 		: The field CDT to create.
	 * @param visibility
	 * 		: The visibility of the field.
	 * @param fieldName
	 * 		: The name of the field.
	 * @param init
	 * 		: The initialized expression of the field.
	 * @param typeName
	 * 		: The name of the type of the field.
	 * @return
	 * 		The new field.
	 */
	public Field createField(IField field, Modifier visibility,
			String fieldName, Expression init, String typeName){

		// Create the modifier of the field.
		List<Modifier> listModifiers = new ArrayList<Modifier>();
		listModifiers.add(visibility);
		listModifiers.addAll(getModifierOfField(field));

		// Create the field.
		return new Field(listModifiers, fieldName, 
				convertTypeGool(field.getType(), typeName), init);
	}

	/**
	 * This method can create a field (as a global variable), simply.
	 * @param field
	 * 		: The variable CDT to create.
	 * @param fieldName
	 * 		: The name of the variable.
	 * @param init
	 * 		: The initialized expression of the variable.
	 * @param typeName
	 * 		: The name of the type of the variable.
	 * @return
	 * 		The new field with modifiers PUBLIC and STATIC.
	 */
	public Field createField(IVariable field, String fieldName, 
			Expression init, String typeName){
		List<Modifier> listModifiers = new ArrayList<Modifier>();

		// Create the modifier of the field.
		listModifiers.add(Modifier.PUBLIC);
		listModifiers.add(Modifier.STATIC);
		// TODO: GET MODIFIER UNKNOWN

		// Create the field.
		return new Field(listModifiers, fieldName, 
				convertTypeGool(field.getType(), typeName), init);
	}

	/**
	 * This method can create a variable, simply.
	 * @param var
	 * 		: The variable CDT to create.
	 * @param varName
	 * 		: The name of the variable.
	 * @param init
	 * 		: The initialized expression of the variable.
	 * @param typeName
	 * 		: The name of the type of the variable.
	 * @return
	 * 		The new variable.
	 */
	public VarDeclaration createVariable(IVariable var, String varName, 
			Expression init, String typeName){

		// Create the modifier of the variable.
		List<Modifier> listModifiers = new ArrayList<Modifier>();
		// TODO : GET MODIFIER UNKNOWN

		// Create the variable.
		VarDeclaration toReturn = new VarDeclaration(
				convertTypeGool(var.getType(), typeName), varName);

		// Add the initial value.
		toReturn.setInitialValue(init);

		// Return the variable.
		return toReturn ;
	}

	/**
	 * This method can create a parameter of a method, simply.
	 * @param param
	 * 		: The parameter CDT.
	 * @param typeName
	 * 		: The name of the type of the parameter.
	 * @return
	 * 		The new parameter.
	 */
	public VarDeclaration createParameter(IParameter param, String typeName){

		// Create type of the parameter.
		gool.ast.type.IType type = convertTypeGool(
				param.getType(), typeName);

		// Create the parameter.
		VarDeclaration toReturn = new VarDeclaration(
				type, param.getName());

		// Treat the modifiers, of the parameter.
		toReturn.addModifiers(getModifierOfParam(param));

		// Return the parameter.
		return toReturn ;
	}

	/**
	 * This method can create a method for a classdef, simply.
	 * @param method
	 * 		: The method CDT in a classdef.
	 * @param typeName
	 * 		: The name of the type of the return method.
	 * @return
	 * 		The new method for a classdef.
	 */
	public Meth createMethod(ICPPMethod method, String typeName) {

		// Create type returned of the method.
		gool.ast.type.IType type = convertTypeGool(
				method.getType().getReturnType(), typeName);

		// Create the method.
		Meth toReturn = new Meth(
				type, method.getName());

		// Treat the modifiers, of the method.
		// Set start with empty modifier.
		toReturn.setModifiers(new ArrayList<Modifier>());  
		toReturn.addModifier(visibilityLabelToModifier(method.getVisibility()));
		toReturn.addModifiers(getModifierOfMethod(method));

		// Return the method.
		return toReturn ;
	}

	/**
	 * This method can create a constructor for a classdef, simply.
	 * @param method
	 * 		: The method CDT (as a constructor) in a classdef.
	 * @return
	 * 		The new constructor for a classdef.
	 */
	public Constructor createConstructor(ICPPMethod method) {

		// Create the constructor.
		Constructor toReturn = new Constructor();

		// Treat the modifiers, of the method.
		// Set start with empty modifier.
		toReturn.setModifiers(new ArrayList<Modifier>());
		toReturn.addModifier(visibilityLabelToModifier(method.getVisibility()));
		toReturn.addModifiers(getModifierOfMethod(method));

		// Return the constructor.
		return toReturn ;
	}

	/**
	 * This method can create a method for a classdef, simply.
	 * By default, the method is "PUBLIC" and "STATIC".
	 * @param method
	 * 		: The function CDT in a classdef.
	 * @return
	 * 		The new method for a classdef, result of conversion about a function.
	 */
	public Meth createMethod(IFunction method, String typeName) {

		// Create type returned of the method.
		gool.ast.type.IType type = convertTypeGool(
				method.getType().getReturnType(), typeName);

		// Create the method.
		Meth toReturn = new Meth(
				type, method.getName());

		// Treat the modifiers, of the method
		// Set start with empty modifier.
		toReturn.setModifiers(new ArrayList<Modifier>());
		toReturn.addModifier(Modifier.PUBLIC);
		toReturn.addModifier(Modifier.STATIC);
		toReturn.addModifiers(getModifierOfFunction(method));

		// Return the method.
		return toReturn ;
	}





	/////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////   TOOLS   ///////////////////////////////////


	//////////////////////////////////
	//////////////////////////////////
	//// CHECKOR :

	/**
	 * Checks if a function definition is the main of the program.
	 * @param functionName
	 * 		: The name of the function.
	 * @param functionType
	 * 		: The type associate to the function.
	 * @return
	 * 		True if it is the main, otherwise, return false.
	 */
	public boolean isTheMainFunction(String functionName, org.eclipse.cdt.core.dom.ast.IType functionType){		
		// Check the name of the function :
		if(!functionName.toLowerCase().equals("main")){
			return false ;
		}
		// Check the parameter :
//		if(ASTTypeUtil.getType(functionType).toString().compareTo("int (int, char * *)") == 0)
//			return true ;
//		if(ASTTypeUtil.getType(functionType).toString().compareTo("int ()") == 0)
//			return true ;
//		if(ASTTypeUtil.getType(functionType).toString().compareTo("int (void)") == 0)
//			return true ;
//		return false ;
		return true;
	}

	/**
	 * Checks if a method definition is the constructor of a classdef.
	 * @param method
	 * 		: The method definition.
	 * @param classdef
	 * 		: The class definition.
	 * @return
	 * 		True if the method is a consrtuctor of the classdef, 
	 * 		otherwise, return false.
	 */
	public boolean isAConstructor(ICPPMethod method, ClassDef classdef){
		return method.getName().compareTo(classdef.getName()) == 0 ;
	}

	/**
	 * Checks if an operator (binary) is the assign operator.
	 * @param operator
	 * 		: The operator CDT.
	 * @return
	 * 		True if the operator is the assign operator,
	 * 		otherwise, return false.
	 */
	public boolean isAssignOperator(int operator){
		return operator == CPPASTBinaryExpression.op_assign ;
	}

	/**
	 * Checks if an operator (binary) is the assign binary operator
	 * ie. the operator is '+=', '*=', '%=' , ...
	 * @param operator
	 * 		: The operator CDT.
	 * @return
	 * 		True if the operator is the assign binary operator,
	 * 		otherwise, return false.
	 */
	public boolean isBinaryAssignOperator(int operator){
		return operator == CPPASTBinaryExpression.op_binaryAndAssign
				|| operator == CPPASTBinaryExpression.op_binaryOrAssign
				|| operator == CPPASTBinaryExpression.op_binaryXorAssign
				|| operator == CPPASTBinaryExpression.op_divideAssign
				|| operator == CPPASTBinaryExpression.op_minusAssign
				|| operator == CPPASTBinaryExpression.op_moduloAssign
				|| operator == CPPASTBinaryExpression.op_multiplyAssign
				|| operator == CPPASTBinaryExpression.op_plusAssign
				|| operator == CPPASTBinaryExpression.op_shiftLeftAssign
				|| operator == CPPASTBinaryExpression.op_shiftRightAssign ;
	}


	//////////////////////////////////
	//////////////////////////////////
	//// GETOR :

	/**
	 * Gets the class name of a transition unit.
	 * @param tu
	 * 		: The transition unit CDT.
	 * @return
	 * 		The class name of a transition unit as an Identifier.
	 */
	public Identifier getIdentifierOfTransitionUnit(IASTTranslationUnit tu){
		// Gets the name of the class generated.
		String className = createClassNameFromFilename(tu.getContainingFilename());

		// Return the Identifier of the class.
		return new Identifier(new TypeClass(className), className);
	}

	/**
	 * This method is used to test if a class already exists,
	 * and so to get his definition.
	 * @param className
	 * 		: The name of the class (used in his creation).
	 * @return
	 * 		If a class already exists, return the class definition, otherwise
	 * 		return null.
	 */
	public ClassDef getClassDef (String className){
		if(className == null)
			return null ;
		// Visit and check all class definitions.
		Iterator<ClassDef> it = recognizer.getGoolClasses().iterator();
		Log.d("<HelperCppRecognition - getClassDef> : comparison of " + className);
		while (it.hasNext()){			
			ClassDef tmp = it.next();
			Log.d("<HelperCppRecognition - getClassDef> : comparison of " + className +
					" with " + tmp.getName());
			// The class definition exists.
			if (tmp.getName().compareTo(className) == 0){
				Log.d("<HelperCppRecognition - getClassDef> : they are the same.");
				return tmp ;
			}
		}
		// The class definition doesn't exist.
		return null ;
	}

	/**
	 * This method is used to test if a field already exists in a class,
	 * and so to get his definition.
	 * @param classdef
	 * 		: The classdef where is the field.
	 * @param fieldName
	 * 		: The name of the field (used in his creation).
	 * @return
	 * 		If a field already exists in the classdef, return the class 
	 * 		definition, otherwise return null.
	 */
	public Field getField(ClassDef classdef, String fieldName){
		if(fieldName == null)
			return null ;
		// Visit and check all field definitions.
		Iterator<Field> it = classdef.getFields().iterator();
		while (it.hasNext()){
			Field tmp = it.next();
			// The field definition exists.
			if (tmp.getName().compareTo(fieldName) == 0){
				return tmp ;
			}
		}
		// The field definition doesn't exist.
		return null ;
	}





	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////// CONVERSIONS ///////////////////////////////////

	public String transformMethodMathcherInvocation(IASTExpression acesss, IASTName field){
		String toReturn = "";
		IBinding bind = field.resolveBinding();
		org.eclipse.cdt.core.dom.ast.IType typeMeth = 
				(bind instanceof ICPPMethod)? ((ICPPMethod)bind).getType() : null ; 
				if(typeMeth != null){
					toReturn+=ASTTypeUtil.getType(acesss.getExpressionType()) ;
					toReturn+=".";
					toReturn+=field.toString();

					String tmp = ASTTypeUtil.getType(typeMeth);

					String returnType = "";
					String paramstype = "";
					int i=0;
					for(i=0; i < tmp.length() ; i++){
						if(tmp.charAt(i) == ' '){
							i++;
							break ;
						}
						returnType+=tmp.charAt(i);
					}
					for(; i < tmp.length() ; i++){
						if(tmp.charAt(i) != ' ')
							paramstype+=tmp.charAt(i);
					}
					toReturn+=paramstype;
					toReturn+=":";
					toReturn+=returnType;
				}
				return toReturn ;
	}

	public IType convertTypePrimitiveGool(org.eclipse.cdt.core.dom.ast.IType typeAst, 
			String typeName, boolean typeUnknown){

		String type = "" ;

		if(typeName == null)
			type = ASTTypeUtil.getType(typeAst);
		else
			type = typeName;

		Log.d("<HelperCppRecognizer -- convertTypePrimitiveGool> type = <" + type + ">");

		if (type.compareTo("?") == 0) return new TypeUnknown("UNKNOWN");
		if (type.compareTo("int") == 0) return TypeInt.INSTANCE;
		if (type.compareTo("const int") == 0) return TypeInt.INSTANCE;
		if (type.compareTo("void") == 0) return TypeVoid.INSTANCE;
		if (type.compareTo("const void") == 0) return TypeVoid.INSTANCE;
		if (type.compareTo("char") == 0) return TypeChar.INSTANCE;
		if (type.compareTo("const char") == 0) return TypeChar.INSTANCE;
		if (type.replaceAll("\\s+","").startsWith("constchar[")) return TypeString.INSTANCE;
		if (type.compareTo("std::string") == 0) return TypeString.INSTANCE;
		if (type.compareTo("string") == 0) return TypeString.INSTANCE;
		if (type.compareTo("const std::string") == 0) return TypeString.INSTANCE;
		if (type.compareTo("const string") == 0) return TypeString.INSTANCE;
		if (type.compareTo("short") == 0) return TypeInt.INSTANCE; /* short -> int */ 
		if (type.compareTo("const short") == 0) return TypeInt.INSTANCE; /* short -> int */ 
		if (type.compareTo("long") == 0) return TypeInt.INSTANCE; /* long -> int */ 
		if (type.compareTo("const long") == 0) return TypeInt.INSTANCE; /* long -> int */ 
		if (type.compareTo("float") == 0) return TypeDecimal.INSTANCE; /* float -> decimal */
		if (type.compareTo("const float") == 0) return TypeDecimal.INSTANCE; /* float -> decimal */ 
		if (type.compareTo("double") == 0) return TypeDecimal.INSTANCE; /* double -> decimal */  
		if (type.compareTo("const double") == 0) return TypeDecimal.INSTANCE; /* double -> decimal */ 
		if (type.compareTo("signed") == 0) return TypeInt.INSTANCE; /* signed -> int */ 
		if (type.compareTo("const signed") == 0) return TypeInt.INSTANCE; /* signed -> int */ 
		if (type.compareTo("unsigned") == 0) return TypeInt.INSTANCE; /*unsigned -> int */ 
		if (type.compareTo("const unsigned") == 0) return TypeInt.INSTANCE; /*unsigned -> int */ 
		if (type.compareTo("const bool") == 0) return TypeBool.INSTANCE;
		if (type.compareTo("bool") == 0) return TypeBool.INSTANCE;

		if(type.contains("*"))
			return new TypeUnknown(type);

		if(typeUnknown && typeName == null)
			return new TypeUnknown("UNKNOWN");
		if(typeUnknown)
			return new TypeUnknown(typeName);

		Log.w("<HelperCppRecognizer -- convertTypePrimitiveGool> Cannot recognize " + type);

		return new TypeVar(typeName);
	}
	/**
	 * Convertion function of type : string like "int" 
	 * to GOOL type like TypeInt.INSTANCE.
	 * @param type
	 * 		: The type who wants to convert, as a string.
	 * @return
	 * 		The conversion between a CDT type and a GOOL type.
	 * 		If don't match, return null.
	 */
	public IType convertTypeGool(org.eclipse.cdt.core.dom.ast.IType typeAst, String typeName){
		if (typeAst instanceof IProblemType){
			String className = RecognizerMatcher.matchClass(typeName);
			if(className == null)
				return convertTypePrimitiveGool(typeAst,typeName,true);
			else{
				recognizer.stackClassActives.peek()
				.addDependency(new RecognizedDependency(className));
				return new TypeGoolLibraryClass(className) ;
			}	
		}
		String className = RecognizerMatcher.matchClass(typeName);
		if(className == null)
			return convertTypePrimitiveGool(typeAst,typeName,false);
		else{
			recognizer.stackClassActives.peek()
			.addDependency(new RecognizedDependency(className));
			return new TypeGoolLibraryClass(className) ;
		}	
	}

	public String getPartNameTypeOfArray(String arrayType){
		String toReturn = "" ;
		for(int i = 0 ; i < arrayType.length() ; i++){
			if(arrayType.charAt(i) == '*' || arrayType.charAt(i) == '[')
				return toReturn;
			toReturn+=arrayType.charAt(i);
		}
		return toReturn;
	}

	public IType convertArrayTypeGool(String type){
		if (type == null)
			return new TypeUnknown(type);
		String typeElement = getPartNameTypeOfArray(type);

		if (typeElement.compareTo("int") == 0) return new gool.ast.type.TypeArray(TypeInt.INSTANCE);
		if (typeElement.compareTo("void") == 0) return new gool.ast.type.TypeArray(TypeVoid.INSTANCE);
		if (typeElement.compareTo("char") == 0) return new gool.ast.type.TypeArray(TypeChar.INSTANCE);
		if (typeElement.compareTo("short") == 0) return new gool.ast.type.TypeArray(TypeInt.INSTANCE); /* short -> int */ 
		if (typeElement.compareTo("long") == 0) return new gool.ast.type.TypeArray(TypeInt.INSTANCE); /* long -> int */ 
		if (typeElement.compareTo("float") == 0) return new gool.ast.type.TypeArray(TypeDecimal.INSTANCE); /* float -> decimal */ 
		if (typeElement.compareTo("double") == 0) return new gool.ast.type.TypeArray(TypeDecimal.INSTANCE); /* double -> decimal */ 
		if (typeElement.compareTo("signed") == 0) return new gool.ast.type.TypeArray(TypeInt.INSTANCE); /* signed -> int */ 
		if (typeElement.compareTo("unsigned") == 0) return new gool.ast.type.TypeArray(TypeInt.INSTANCE); /*unsigned -> int */ 
		if (typeElement.compareTo("boolean") == 0) return new gool.ast.type.TypeArray(TypeBool.INSTANCE);
		return new gool.ast.type.TypeArray(new TypeVar(typeElement));
	}


	/**
	 * This method can convert a representation of a visibility label CDT
	 * into a gool modifier.
	 * @param visibility
	 * 		: The visibility label CDT (v_private, v_protected, v_public).
	 * @return
	 * 		The conversion into a gool modifier. By default return Modifier.PUBLIC.
	 */
	public Modifier visibilityLabelToModifier(int visibility){
		switch (visibility) {
		case CPPASTCompositeTypeSpecifier.ICPPASTBaseSpecifier.v_private: return Modifier.PRIVATE ;
		case CPPASTCompositeTypeSpecifier.ICPPASTBaseSpecifier.v_protected: return Modifier.PROTECTED ;
		case CPPASTCompositeTypeSpecifier.ICPPASTBaseSpecifier.v_public: return Modifier.PUBLIC ;
		default: return Modifier.PUBLIC ;
		}
	}



	/**
	 * Gets the list of modifiers associated at the definition
	 * of an method in CDT.
	 * declaration.
	 * @return
	 * 		The conversion between the CDT modifiers and 
	 * 		the GOOL modifiers, about the method definition.
	 */
	public List<Modifier> getModifierOfMethod(ICPPMethod method){
		List<Modifier> toReturn = new LinkedList<Modifier>();
		if(method.isConstexpr() 
				|| method.isFinal()) toReturn.add(Modifier.FINAL);
		if(method.isOverride()) toReturn.add(Modifier.OVERRIDE);
		if(method.isPureVirtual()
				|| method.isVirtual()) toReturn.add(Modifier.VIRTUAL);
		if(method.isStatic()) toReturn.add(Modifier.STATIC);
		if(method.isAuto()
				|| method.isDeleted()
				/*|| method.isDestructor()*/
				/*|| method.isInline()*/
				|| method.isMutable()
				|| method.isRegister()) toReturn.add(Modifier.NATIVE);
		return toReturn ;
	}

	/**
	 * Gets the list of modifiers associated at the definition
	 * of an function in CDT.
	 * declaration.
	 * @return
	 * 		The conversion between the CDT modifiers and 
	 * 		the GOOL modifiers, about the function definition.
	 */
	public List<Modifier> getModifierOfFunction(IFunction method){
		List<Modifier> toReturn = new LinkedList<Modifier>();
		if(method.isStatic()) toReturn.add(Modifier.STATIC);
		if(method.isAuto()
				|| method.isInline()
				|| method.isRegister()) toReturn.add(Modifier.NATIVE);
		return toReturn ;
	}

	public List<Modifier> getModifierOfField(IField field){
		List<Modifier> toReturn = new LinkedList<Modifier>();
		if(field.isAuto()
				|| field.isRegister()
				// || field.isExtern()
				) toReturn.add(Modifier.NATIVE);
		if(field.isStatic()) toReturn.add(Modifier.STATIC);
		return toReturn ;
	}

	/**
	 * Gets the list of modifiers associated at the definition
	 * of a parmeter of an method in CDT.
	 * declaration.
	 * @return
	 * 		The conversion between the CDT modifiers and 
	 * 		the GOOL modifiers, about the parameter definition.
	 */
	public List<Modifier> getModifierOfParam(IParameter param){
		List<Modifier> toReturn = new LinkedList<Modifier>();
		if(param.isStatic()) toReturn.add(Modifier.STATIC);
		if(param.isAuto()
				|| param.isRegister()) toReturn.add(Modifier.NATIVE);
		return toReturn ;
	}

	public List<Modifier> getModifierDeclaration(IASTSimpleDeclSpecifier decSpec){
		return new ArrayList<Modifier>();
	}


	public Operator convertBinaryOperator(int operator){
		switch (operator) {
		case CPPASTBinaryExpression.op_assign: return Operator.UNKNOWN; // assignment =
		case CPPASTBinaryExpression.op_binaryAnd: return Operator.UNKNOWN; // binary and &
		case CPPASTBinaryExpression.op_binaryAndAssign: return Operator.UNKNOWN; // binary and assign &=
		case CPPASTBinaryExpression.op_binaryOr: return Operator.UNKNOWN; // binary Or |
		case CPPASTBinaryExpression.op_binaryOrAssign: return Operator.UNKNOWN; // binary Or assign |=
		case CPPASTBinaryExpression.op_binaryXor: return Operator.UNKNOWN; // binary Xor ^
		case CPPASTBinaryExpression.op_binaryXorAssign: return Operator.UNKNOWN; // binary Xor assign ^=
		case CPPASTBinaryExpression.op_divide: return Operator.DIV; // divide /
		case CPPASTBinaryExpression.op_divideAssign: return Operator.DIV; // divide assignemnt /=
		case CPPASTBinaryExpression.op_equals: return Operator.EQUAL; // equals ==
		case CPPASTBinaryExpression.op_greaterEqual: return Operator.GEQ; // greater than or equals >=
		case CPPASTBinaryExpression.op_greaterThan: return Operator.GT; // greater than >
		case CPPASTBinaryExpression.op_lessEqual: return Operator.LEQ; // less than or equals <=
		case CPPASTBinaryExpression.op_lessThan: return Operator.LT; // less than <
		case CPPASTBinaryExpression.op_logicalAnd: return Operator.AND; // logical and &&
		case CPPASTBinaryExpression.op_logicalOr: return Operator.OR; // logical or ||
		case CPPASTBinaryExpression.op_max: return Operator.UNKNOWN; // For g++, only.
		case CPPASTBinaryExpression.op_min: return Operator.UNKNOWN; // For g++, only.
		case CPPASTBinaryExpression.op_minus: return Operator.MINUS; // minus -
		case CPPASTBinaryExpression.op_minusAssign: return Operator.MINUS; // minus assignment -=
		case CPPASTBinaryExpression.op_modulo: return Operator.UNKNOWN; // modulo %
		case CPPASTBinaryExpression.op_moduloAssign: return Operator.UNKNOWN; // modulo assignment %=
		case CPPASTBinaryExpression.op_multiply: return Operator.MULT; // multiply *
		case CPPASTBinaryExpression.op_multiplyAssign: return Operator.MULT; // multiply assignment *=
		case CPPASTBinaryExpression.op_notequals: return Operator.NOT_EQUAL; // not equals !
		case CPPASTBinaryExpression.op_plus: return Operator.PLUS; // plus +
		case CPPASTBinaryExpression.op_plusAssign: return Operator.PLUS; // plus assignment +=
		case CPPASTBinaryExpression.op_pmarrow: return Operator.UNKNOWN; // For c++, only.
		case CPPASTBinaryExpression.op_pmdot: return Operator.UNKNOWN; // For c==, only.
		case CPPASTBinaryExpression.op_shiftLeft: return Operator.UNKNOWN; // shift left <<
		case CPPASTBinaryExpression.op_shiftLeftAssign: return Operator.UNKNOWN; // shift left assignment <<=
		case CPPASTBinaryExpression.op_shiftRight: return Operator.UNKNOWN; // shift right >>
		case CPPASTBinaryExpression.op_shiftRightAssign: return Operator.UNKNOWN; // shift right assign >>=
		default: return Operator.UNKNOWN;
		}
	}

	public Operator convertUnaryOperator(int operator){
		switch (operator) {
		case CPPASTUnaryExpression.op_amper: return Operator.UNKNOWN; // Operator ampersand.
		case CPPASTUnaryExpression.op_bracketedPrimary: return Operator.UNKNOWN; // A bracketed expression.
		case CPPASTUnaryExpression.op_minus: return Operator.UNKNOWN; // Operator minus.
		case CPPASTUnaryExpression.op_not: return Operator.NOT; // not.
		case CPPASTUnaryExpression.op_plus: return Operator.UNKNOWN; // Operator plus.
		case CPPASTUnaryExpression.op_postFixDecr: return Operator.POSTFIX_DECREMENT; // Postfix decrement.
		case CPPASTUnaryExpression.op_postFixIncr: return Operator.POSTFIX_INCREMENT; // Postfix increment.
		case CPPASTUnaryExpression.op_prefixDecr: return Operator.PREFIX_DECREMENT; // Prefix decrement.
		case CPPASTUnaryExpression.op_prefixIncr: return Operator.PREFIX_INCREMENT; // Prefix increment.
		case CPPASTUnaryExpression.op_sizeof: return Operator.UNKNOWN; // sizeof.
		case CPPASTUnaryExpression.op_sizeofParameterPack: return Operator.UNKNOWN; // For c++, only: 'sizeof...
		case CPPASTUnaryExpression.op_star: return Operator.UNKNOWN; // Operator star.
		case CPPASTUnaryExpression.op_throw: return Operator.UNKNOWN; // for c++, only.
		case CPPASTUnaryExpression.op_tilde: return Operator.UNKNOWN; // Operator tilde.
		case CPPASTUnaryExpression.op_typeid: return Operator.UNKNOWN; // for c++, only.
		default: return Operator.UNKNOWN;
		}
	}

	public String getSymboleBinaryOperator(int operator){
		switch (operator) {
		case CPPASTBinaryExpression.op_assign: return "="; // assignment =
		case CPPASTBinaryExpression.op_binaryAnd: return "&"; // binary and &
		case CPPASTBinaryExpression.op_binaryAndAssign: return "&"; // binary and assign &=
		case CPPASTBinaryExpression.op_binaryOr: return "|"; // binary Or |
		case CPPASTBinaryExpression.op_binaryOrAssign: return "|"; // binary Or assign |=
		case CPPASTBinaryExpression.op_binaryXor: return "^"; // binary Xor ^
		case CPPASTBinaryExpression.op_binaryXorAssign: return "^"; // binary Xor assign ^=
		case CPPASTBinaryExpression.op_divide: return "/"; // divide /
		case CPPASTBinaryExpression.op_divideAssign: return "/"; // divide assignemnt /=
		case CPPASTBinaryExpression.op_equals: return "=="; // equals ==
		case CPPASTBinaryExpression.op_greaterEqual: return ">="; // greater than or equals >=
		case CPPASTBinaryExpression.op_greaterThan: return ">"; // greater than >
		case CPPASTBinaryExpression.op_lessEqual: return "<="; // less than or equals <=
		case CPPASTBinaryExpression.op_lessThan: return "<"; // less than <
		case CPPASTBinaryExpression.op_logicalAnd: return "&&"; // logical and &&
		case CPPASTBinaryExpression.op_logicalOr: return "||"; // logical or ||
		case CPPASTBinaryExpression.op_max: return "max"; // For g++, only.
		case CPPASTBinaryExpression.op_min: return "min"; // For g++, only.
		case CPPASTBinaryExpression.op_minus: return "-"; // minus -
		case CPPASTBinaryExpression.op_minusAssign: return "-"; // minus assignment -=
		case CPPASTBinaryExpression.op_modulo: return "%"; // modulo %
		case CPPASTBinaryExpression.op_moduloAssign: return "%"; // modulo assignment %=
		case CPPASTBinaryExpression.op_multiply: return "*"; // multiply *
		case CPPASTBinaryExpression.op_multiplyAssign: return "*"; // multiply assignment *=
		case CPPASTBinaryExpression.op_notequals: return "!="; // not equals !
		case CPPASTBinaryExpression.op_plus: return "+"; // plus +
		case CPPASTBinaryExpression.op_plusAssign: return "+"; // plus assignment +=
		case CPPASTBinaryExpression.op_pmarrow: return "op_pmarrow"; // For c++, only.
		case CPPASTBinaryExpression.op_pmdot: return "op_pmdot"; // For c==, only.
		case CPPASTBinaryExpression.op_shiftLeft: return "<<"; // shift left <<
		case CPPASTBinaryExpression.op_shiftLeftAssign: return "<<"; // shift left assignment <<=
		case CPPASTBinaryExpression.op_shiftRight: return ">>"; // shift right >>
		case CPPASTBinaryExpression.op_shiftRightAssign: return ">>"; // shift right assign >>=
		default: return "???";
		}
	}

	public String getSymboleUnaryOperator(int operator){
		switch (operator) {
		case CPPASTUnaryExpression.op_amper: return "op_amper"; // Operator ampersand.
		case CPPASTUnaryExpression.op_bracketedPrimary: return "op_bracketedPrimary"; // A bracketed expression.
		case CPPASTUnaryExpression.op_minus: return "-"; // Operator minus.
		case CPPASTUnaryExpression.op_not: return "!"; // not.
		case CPPASTUnaryExpression.op_plus: return "+"; // Operator plus.
		case CPPASTUnaryExpression.op_postFixDecr: return "--"; // Postfix decrement.
		case CPPASTUnaryExpression.op_postFixIncr: return "++"; // Postfix increment.
		case CPPASTUnaryExpression.op_prefixDecr: return "--"; // Prefix decrement.
		case CPPASTUnaryExpression.op_prefixIncr: return "++"; // Prefix increment.
		case CPPASTUnaryExpression.op_sizeof: return "sizeof"; // sizeof.
		case CPPASTUnaryExpression.op_sizeofParameterPack: return "'sizeof"; // For c++, only: 'sizeof...
		case CPPASTUnaryExpression.op_star: return "*"; // Operator star.
		case CPPASTUnaryExpression.op_throw: return "throw"; // for c++, only.
		case CPPASTUnaryExpression.op_tilde: return "~"; // Operator tilde.
		case CPPASTUnaryExpression.op_typeid: return "op_typeid"; // for c++, only.
		default: return "???";
		}
	}













	public void addAnError(String owner, String code, String comment){
		if(recognizer.methActive != null)
			recognizer.methActive.addStatement(new ExpressionUnknown(TypeNone.INSTANCE, code));
		else{
			ClassDef classdef = getClassDef(owner);
			if(classdef == null)
				recognizer.stackClassActives.peek().addDependency(
						new UnImplemented(code, comment));
			else
				classdef.addDependency(
						new UnImplemented(code, comment));
		}
	}

	public void addAnError(String code, String comment){
		if(recognizer.methActive != null)
			recognizer.methActive.addStatement(new ExpressionUnknown(TypeNone.INSTANCE, code));
		else
			recognizer.stackClassActives.peek().addDependency(new UnImplemented(code, comment));
	}

	public void createAndAddArrayParameter(IParameter param, List<Expression> dims){
		if(recognizer.methActive != null)
			recognizer.methActive.addParameter(createArrayParameter(param,dims));
	}



	public VarDeclaration createArrayParameter(IParameter param, List<Expression> dims){
		// Create the parameter.
		VarDeclaration toReturn = new VarDeclaration(
				convertArrayTypeGool(
						param.getType().toString()), param.getName());		
		// Treat the modifiers, of the parameter.
		toReturn.addModifiers(getModifierOfParam(param));

		return toReturn ;
	}
	public void createAndAddArrayField(String owner, IField field, 
			Modifier visibility, String nameDec, Expression init, List<Expression> dims){
		ClassDef classdef = getClassDef(owner);
		Field fieldtoAdd = getField(classdef, field.getName());
		if(fieldtoAdd == null){
			classdef.addField(createArrayField(field, visibility, field.getName(), init));
		}
		else{
			// We update the initiale value of the field.
			fieldtoAdd.setDefaultValue(init);
		}			
	}

	public VarDeclaration createAndAddArrayVariable(String owner, IVariable var,
			Modifier visibility, String nameDec, Expression init, List<Expression> dims){

		if(recognizer.methActive == null){
			// Add the field to the class active, ie. to the fileClass.
			if(var.getOwner() == null) 
				recognizer.stackClassActives.peek().addField(createArrayField(var, nameDec, init, dims)) ;
		}
		// Add the variable to the stack for the method active.
		else{
			return (createArrayVariable(var, nameDec, init, dims));
		}
		return null ;
	}




	public Field createArrayField(IField field, Modifier visibility, String fieldName, Expression init){
		List<Modifier> listModifiers = new ArrayList<Modifier>();
		listModifiers.add(visibility);
		listModifiers.addAll(getModifierOfField(field));
		return new Field(listModifiers, fieldName, 
				convertArrayTypeGool(field.getType().toString()), init);
	}

	public Field createArrayField(IVariable field, String fieldName, Expression init, List<Expression> dims){
		List<Modifier> listModifiers = new ArrayList<Modifier>();
		listModifiers.add(Modifier.PUBLIC);
		listModifiers.add(Modifier.STATIC);
		// TODO : listModifiers.addAll(getModifierOfVariable(field));
		return new Field(listModifiers, fieldName, 
				convertArrayTypeGool(field.getType().toString()), init);
	}

	public VarDeclaration createArrayVariable(IVariable var, String varName, Expression init, List<Expression> dims){
		List<Modifier> listModifiers = new ArrayList<Modifier>();
		//listModifiers.add(Modifier.PUBLIC);
		//listModifiers.add(Modifier.STATIC);
		// TODO : listModifiers.addAll(getModifierOfVariable(var));
		System.out.println(var.getType().toString());
		VarDeclaration toReturn = new VarDeclaration(convertArrayTypeGool(var.getType().toString()), varName);
		List<Expression> inits = new ArrayList<Expression>();
		inits.add(init);
		toReturn.setInitialValue(new ArrayNew(
				convertTypeGool(var.getType(), getPartNameTypeOfArray(toReturn.getType().toString())), dims,inits ));
		return toReturn ;
	}
}
