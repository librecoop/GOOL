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

/**
 * Visits the abstract GOOL tree to generate concrete C++
 */

package gool.generator.cpp;

import gool.ast.core.ArrayNew;
import gool.ast.core.BinaryOperation;
import gool.ast.core.CastExpression;
import gool.ast.core.Catch;
import gool.ast.core.ClassDef;
import gool.ast.core.ClassNew;
import gool.ast.core.Constant;
import gool.ast.core.Constructor;
import gool.ast.core.EnhancedForLoop;
import gool.ast.core.EqualsCall;
import gool.ast.core.Expression;
import gool.ast.core.Field;
import gool.ast.core.MainMeth;
import gool.ast.core.MemberSelect;
import gool.ast.core.Meth;
import gool.ast.core.MethCall;
import gool.ast.core.Modifier;
import gool.ast.core.Operator;
import gool.ast.core.ParentCall;
import gool.ast.core.RecognizedDependency;
import gool.ast.core.StringIsEmptyCall;
import gool.ast.core.ThisCall;
import gool.ast.core.Throw;
import gool.ast.core.ToStringCall;
import gool.ast.core.Try;
import gool.ast.core.TypeDependency;
import gool.ast.core.VarDeclaration;
import gool.ast.list.ListAddCall;
import gool.ast.list.ListClearCall;
import gool.ast.list.ListContainsCall;
import gool.ast.list.ListGetCall;
import gool.ast.list.ListGetIteratorCall;
import gool.ast.list.ListIndexOfCall;
import gool.ast.list.ListIsEmptyCall;
import gool.ast.list.ListRemoveAtCall;
import gool.ast.list.ListRemoveCall;
import gool.ast.list.ListSetCall;
import gool.ast.list.ListSizeCall;
import gool.ast.map.MapContainsKeyCall;
import gool.ast.map.MapEntryGetKeyCall;
import gool.ast.map.MapEntryGetValueCall;
import gool.ast.map.MapGetCall;
import gool.ast.map.MapGetIteratorCall;
import gool.ast.map.MapIsEmptyCall;
import gool.ast.map.MapPutCall;
import gool.ast.map.MapRemoveCall;
import gool.ast.map.MapSizeCall;
import gool.ast.system.SystemCommandDependency;
import gool.ast.system.SystemOutDependency;
import gool.ast.system.SystemOutPrintCall;
import gool.ast.type.IType;
import gool.ast.type.PrimitiveType;
import gool.ast.type.TypeArray;
import gool.ast.type.TypeBool;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeException;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeGoolLibraryClass;
import gool.ast.type.TypeNull;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeString;
import gool.generator.GeneratorHelper;
import gool.generator.common.CodeGeneratorNoVelocity;
import gool.generator.common.CommonCodeGenerator;
import gool.generator.common.GeneratorMatcher;
import logger.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class CppGenerator extends CommonCodeGenerator implements
CodeGeneratorNoVelocity {

	private static int compt = 0;

	private String removePointer(IType type) {
		return removePointer(type.toString());
	}

	private String removePointer(String type) {
		return type.replaceAll("[\\s*]+$", "");
	}

	private Set<String> customDependencies = new LinkedHashSet<String>();

	public void addCustomDependency(String dep) {
		customDependencies.add(dep);
	}

	public Set<String> getCustomDependencies(){
		return customDependencies;
	}

	public void clearCustomDependencies(){
		customDependencies.clear();
	}

	@Override
	public String getCode(Modifier modifier) {
		Log.MethodIn(Thread.currentThread());
		if (modifier == Modifier.FINAL) {
			return (String)Log.MethodOut(Thread.currentThread(), "const");
		}
		return (String)Log.MethodOut(Thread.currentThread(), super.getCode(modifier));
	}

	@Override
	public String getCode(Field field) {
		Log.MethodIn(Thread.currentThread());
		Modifier m = field.getAccessModifier();
		List<Modifier> modifiers = new ArrayList<Modifier>(field.getModifiers());
		modifiers.remove(m);
		String out = String.format("%s: %s %s %s", m, getCode(modifiers),
				field.getType(), field.getName());
		if (field.getDefaultValue() != null) {
			// out = String.format("%s = %s", out, field.getDefaultValue());

			// C++ does not seem to allow instance fields initialization.
			if (!field.getModifiers().contains(Modifier.FINAL)
					&& !field.getModifiers().contains(Modifier.STATIC)) {
				out += String.format(" /* GOOL WARNING : The field '%s' should be initialized. */",
						field.getName());
				//				throw new IllegalArgumentException(
				//						String.format(
				//								"The field '%s' should be initialized within one of the class constructors.",
				//								field.getName()));
			}
		}

		return (String)Log.MethodOut(Thread.currentThread(), out);
	}

	@Override
	public String getCode(MethCall methodCall) {
		Log.MethodIn(Thread.currentThread());
		String out = "";
		if (methodCall.getTarget() != null) {
			out = methodCall.getTarget().toString();
			String goolMethod = methodCall.getGoolLibraryMethod();
			if(goolMethod!=null){
				//here, get matched output method name with the GeneratorMatcher
				String methodName=GeneratorMatcher.matchGoolMethod(goolMethod);
				if(methodName!=null){
					Log.d("<CppGenerator - getCode(MethCall> method name = " + methodName);
					if (!methodName.startsWith("std::"))
						out=out.substring(0, out.lastIndexOf("->")+2)+methodName;
					else
						out = methodName;
				}
			}
		}

		// if (methodCall.getType() != null) {
		// out += "< " + methodCall.getType() + " >";
		// }
		out += "(";
		if (methodCall.getParameters() != null) {
			out += StringUtils.join(methodCall.getParameters(), ", ");
		}
		out += ")";
		//Log.i("<CppGenerator> " + out);
		return (String)Log.MethodOut(Thread.currentThread(), out);
	}

	@Override
	public String getCode(TypeBool typeBool) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "int");
	}

	@Override
	public String getCode(TypeClass typeClass) {
		Log.MethodIn(Thread.currentThread());
		String pointer = typeClass.isEnum() ? "" : "*";
		return (String)Log.MethodOut(Thread.currentThread(), 
				super.getCode(typeClass).replaceAll("\\.", "::") + pointer);
	}

	@Override
	public String getCode(TypeInt typeInt) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "int");
	}

	@Override
	public String getCode(TypeString typeString) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "std::string");
	}

	@Override
	public String getCode(TypeObject typeObject) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "boost::any");
	}

	@Override
	public String getCode(CastExpression cast) {
		Log.MethodIn(Thread.currentThread());
		if (cast.getType().equals(cast.getExpression().getType())) {
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("%s", cast.getExpression()));
		} else if (cast.getExpression().getType() == TypeObject.INSTANCE) {
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("any_cast< %s >( %s )", cast.getType(),
							cast.getExpression()));
		} else {
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("( ( %s )( %s ) )", cast.getType(),
							cast.getExpression()));
		}
	}

	@Override
	public String getCode(Constant constant) {
		Log.MethodIn(Thread.currentThread());
		if (constant.getType().equals(TypeString.INSTANCE)) {
			return (String)Log.MethodOut(Thread.currentThread(), 
					//					String.format("( new std::string ( %s ) )",
					String.format("%s",	super.getCode(constant)));
		} else if (constant.getType().equals(TypeNull.INSTANCE)) {
			return (String)Log.MethodOut(Thread.currentThread(), "NULL");
		} else if (constant.getType().equals(TypeBool.INSTANCE)) {
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.valueOf(constant.getValue().toString()
							.equalsIgnoreCase("true") ? 1 : 0));
		}else {
			return (String)Log.MethodOut(Thread.currentThread(), 
					super.getCode(constant));
		}
	}

	@Override
	public String getCode(TypeList typeList) {
		Log.MethodIn(Thread.currentThread());
		if (typeList.getElementType() == null) {
			/*
			 * TODO: Avoid elements of different types within the same list.
			 * Currently, the remove method is broken due to the cast made of
			 * every element in the list (the both operands in the comparison
			 * should have the same type, otherwise the C++ compiler throws an
			 * error).
			 */
			return (String)Log.MethodOut(Thread.currentThread(), 
					"std::vector<" + TypeObject.INSTANCE + ">*");
		}
		return (String)Log.MethodOut(Thread.currentThread(), 
				"std::vector<" + typeList.getElementType().toString() + ">*");
	}

	@Override
	public String getCode(MemberSelect memberSelect) {
		Log.MethodIn(Thread.currentThread());

		IType targetType = memberSelect.getTarget().getType();
		String memberAccess = memberSelect.getTarget().toString();

		/*
		 * Verify if the target type is an enumeration, so we can remove the
		 * generation of the target.
		 * 
		 * In C++, a "enum Foo {A, B}" should be accesed without using the
		 * prefix "Foo".
		 * 
		 * Related to "TODO identifiers".
		 */
		if (targetType instanceof TypeClass
				&& ((TypeClass) targetType).isEnum()) {
			memberAccess = "";
		} else if (memberSelect.getTarget() instanceof Constant) {
			memberAccess += "::"; // A static member access
		} else if (memberSelect.getTarget().getType() instanceof PrimitiveType) {
			memberAccess += "."; // A normal member access
		}else {// A pointer member access
			memberAccess += "->";
		}
		Log.d("<CppGenerator - getCode(MemberSelect)> return " + String
				.format("%s%s", memberAccess, memberSelect.getIdentifier()));
		return (String)Log.MethodOut(Thread.currentThread(), String
				.format("%s%s", memberAccess, memberSelect.getIdentifier()));
	}

	/**
	 * Produces code for the isEmpty() method of String
	 * 
	 * @param StringIsEmptyCall
	 *            the method to be invoked.
	 * @return the formatted method invocation.
	 */
	@Override
	public String getCode(StringIsEmptyCall lmc) {
		Log.MethodIn(Thread.currentThread());
		String out = lmc.getTarget().toString() + ".empty";
		Log.d("<CppGenerator - getCode(MemberSelect)> return " + out);
		return (String)Log.MethodOut(Thread.currentThread(), out);
	}

	@Override
	public String getCode(TypeDecimal typeReal) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "double");
	}

	@Override
	public String getCode(TypeChar typeChar) {
		Log.MethodIn(Thread.currentThread());
		// TODO Auto-generated method stub
		return (String)Log.MethodOut(Thread.currentThread(), "char");
	}

	@Override
	public String getCode(SystemOutPrintCall systemOutPrintCall) {
		Log.MethodIn(Thread.currentThread());
		Expression toPrint = systemOutPrintCall.getParameters().get(0);
		addCustomDependency("iostream");
		String endlstr = "";
		if (systemOutPrintCall.isEndofline())
			endlstr = " << std::endl";
		if (toPrint.getType().equals(TypeString.INSTANCE)) {
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("std::cout << %s%s",
							GeneratorHelper.joinParams(systemOutPrintCall
									.getParameters()), endlstr));
		} else {
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("std::cout << %s%s",
							GeneratorHelper.joinParams(systemOutPrintCall
									.getParameters()), endlstr));
		}
	}

	@Override
	public String getCode(ParentCall parentCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), null);

	}

	@Override
	public String getCode(SystemOutDependency systemOutDependency) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "#include <iostream>");
	}

	@Override
	public String getCode(TypeDependency typeDependency) {
		Log.MethodIn(Thread.currentThread());
		if (typeDependency.getType() instanceof TypeList) {
			// Use the vector type because it allows random access over
			// lists.
			if (((TypeList) typeDependency.getType()).getElementType() == null) {
				return (String)Log.MethodOut(Thread.currentThread(), "#include <vector>");
			}
			return (String)Log.MethodOut(Thread.currentThread(), "#include <vector>"); // TODO what type should be used when it is not a
			// generic list?
		}
		if (typeDependency.getType() instanceof TypeMap) {
			return (String)Log.MethodOut(Thread.currentThread(), "#include <map>");
		}
		if (typeDependency.getType() instanceof TypeEntry) {
			return (String)Log.MethodOut(Thread.currentThread(), "#include <map>");
		}
		if (typeDependency.getType() instanceof TypeString) {
			return (String)Log.MethodOut(Thread.currentThread(), "#include <string>");
		}
		if (typeDependency.getType() instanceof TypeBool) {
			return (String)Log.MethodOut(Thread.currentThread(), "noprint");
		}
		if (typeDependency.getType() instanceof TypeInt) {
			return (String)Log.MethodOut(Thread.currentThread(), "noprint");
		}
		String toReturn = "#include \"";
		toReturn += removePointer(super.getCode(typeDependency)).concat(".h");
		toReturn += "\"";
		return (String)Log.MethodOut(Thread.currentThread(), toReturn);
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s -> size()", mapSizeCall.getExpression()));
	}

	@Override
	public String getCode(MapRemoveCall mapRemoveCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s -> erase(%s)", mapRemoveCall.getExpression(),
						GeneratorHelper.joinParams(mapRemoveCall.getParameters())));
	}

	@Override
	public String getCode(MapPutCall mapPutCall) {
		Log.MethodIn(Thread.currentThread());
		if (mapPutCall.getParameters().isEmpty())
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("%s -> insert()", 
							mapPutCall.getExpression()));
		//		if (mapPutCall.getParameters().size() == 1)
		//			return String.format("%s -> insert( %s )", 
		//					mapPutCall.getExpression(), 
		//					mapPutCall.getParameters().get(0));		
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s -> insert( std::make_pair( %s, %s ) )",
						mapPutCall.getExpression(), mapPutCall.getParameters().get(0),
						mapPutCall.getParameters().get(1)));
	}

	@Override
	public String getCode(MapIsEmptyCall mapIsEmptyCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s -> empty()",
						mapIsEmptyCall.getExpression()));
	}

	private String getNullElement(Expression exp){
		IType expType = exp.getType();
		if (expType instanceof TypeString)
			return "\"\"";
		if (expType instanceof TypeChar)
			return "''";
		if (expType instanceof PrimitiveType)
			return "0";
		return exp.callGetCode();
	}

	@Override
	public String getCode(BinaryOperation binaryOp) {
		Log.MethodIn(Thread.currentThread());
		String left = binaryOp.getLeft().toString();
		String right = binaryOp.getRight().toString();

		if (binaryOp.getOperator() == Operator.PLUS
				&& binaryOp.getType().equals(TypeString.INSTANCE)) {
			if (!binaryOp.getLeft().getType().equals(TypeString.INSTANCE)) {
				left = String.format("boost::lexical_cast<std::string>(%s)", left);
			}
			if (!binaryOp.getRight().getType().equals(TypeString.INSTANCE)) {
				right = String.format("boost::lexical_cast<std::string>(%s)", right);
			}
			//			left = String.format("(%s)", left);
			//			right = String.format("(%s)", right);

			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("%s.append(%s)", left,
							right));
		}

		if (binaryOp.getOperator() == Operator.REMAINDER
				&& ((binaryOp.getLeft().getType() instanceof TypeDecimal) || 
						(binaryOp.getRight().getType() instanceof TypeDecimal))) 
		{
			addCustomDependency("cmath");
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("std::fmod(%s,%s)", left,	right));
		}

		if ((binaryOp.getRight().getType() instanceof TypeNull) && 
				(binaryOp.getLeft().getType() instanceof PrimitiveType)){
			return String.format("(%s %s%s %s)",
					binaryOp.getLeft(),
					binaryOp.getTextualoperator(),
					binaryOp.getOperator().equals(Operator.UNKNOWN) ? "/* Unrecognized by GOOL, passed on */"
							: "", getNullElement(binaryOp.getLeft()));			
		}

		if 	((binaryOp.getLeft().getType() instanceof TypeNull) && 
				(binaryOp.getRight().getType() instanceof PrimitiveType)){		
			return String.format("(%s %s%s %s)",
					getNullElement(binaryOp.getRight()),
					binaryOp.getTextualoperator(),
					binaryOp.getOperator().equals(Operator.UNKNOWN) ? "/* Unrecognized by GOOL, passed on */"
							: "", binaryOp.getRight());		
		}

		return (String)Log.MethodOut(Thread.currentThread(), 
				super.getCode(binaryOp));
	}

	@Override
	public String getCode(MapGetIteratorCall mapGetIteratorCall) {
		Log.MethodIn(Thread.currentThread());
		String toReturn = String.format("%s->begin()",
				mapGetIteratorCall.getExpression());
		return (String)Log.MethodOut(Thread.currentThread(), 
				toReturn);
	}

	@Override
	public String getCode(MapGetCall mapGetCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s -> find( %s ) -> second",
						mapGetCall.getExpression(),
						GeneratorHelper.joinParams(mapGetCall.getParameters())));
	}

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		Log.MethodIn(Thread.currentThread());
		String expr = mapContainsKeyCall.getExpression().toString();
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("(%s) -> find(%s) != (%s) -> end()", expr,
						GeneratorHelper.joinParams(mapContainsKeyCall.getParameters()),
						expr));
	}

	@Override
	public String getCode(TypeMap typeMap) {
		Log.MethodIn(Thread.currentThread());
		/*
		 * Maps with with keys of type Object are not allowed in C++ because
		 * there is not a base type.
		 */
		String toReturn = "std::map<";
		if (typeMap.getKeyType() == null
				|| typeMap.getKeyType().equals(TypeObject.INSTANCE)
				|| typeMap.getKeyType().callGetCode().contains("boost::any")) {
			toReturn = "/* GOOL WARNING : Undefined key type is not supported in C++ */ "+ toReturn;
			toReturn += TypeObject.INSTANCE + ", ";
		}else{
			toReturn += typeMap.getKeyType().callGetCode() + ", ";
		}
		if (typeMap.getElementType() == null) {
			toReturn += TypeObject.INSTANCE;
		}else{
			toReturn += typeMap.getElementType().callGetCode();
		}
		return (String)Log.MethodOut(Thread.currentThread(), 
				toReturn + ">*");
	}

	@Override
	public String getCode(EnhancedForLoop enhancedForLoop) {
		Log.MethodIn(Thread.currentThread());
		VarDeclaration varDec = enhancedForLoop.getVarDec();
		String varName = varDec.getName();
		Expression expression = enhancedForLoop.getExpression();
		String expressionToString = enhancedForLoop.getExpression().toString();
		return (String)Log.MethodOut(Thread.currentThread(), String
				.format("for(%s::iterator %sIterator = %s->begin(); %sIterator != %s->end(); ++%sIterator){\n"
						+ "%s %s *%sIterator;" + "%s" + "\n}",
						removePointer(expression.getType()),
						varName,
						expressionToString,
						varName,
						expressionToString,
						varName,
						varDec.getType(),
						(expression.getType() instanceof TypeMap) ? (String
								.format("* %s = (%s*)&", varName,
										varDec.getType())) : (String.format(
												"%s = ", varName)), varName, enhancedForLoop
								.getStatements()));

	}

	@Override
	public String getCode(TypeEntry typeEntry) {
		Log.MethodIn(Thread.currentThread());
		if (typeEntry.getTypeArguments().isEmpty()){
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("std::pair<%s, %s>", TypeObject.INSTANCE,
							TypeObject.INSTANCE));
		}
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("std::pair<%s, %s>", typeEntry.getKeyType(),
						typeEntry.getElementType()));
	}

	@Override
	public String getCode(MapEntryGetKeyCall mapEntryGetKeyCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s->first", mapEntryGetKeyCall.getExpression()));
	}

	@Override
	public String getCode(MapEntryGetValueCall mapEntryGetValueCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), String
				.format("%s->second", mapEntryGetValueCall.getExpression()));
	}

	@Override
	public String getCode(ThisCall thisCall) {
		Log.MethodIn(Thread.currentThread());
		// TODO Auto-generated method stub
		return (String)Log.MethodOut(Thread.currentThread(), null);
	}

	@Override
	public String getCode(EqualsCall equalsCall) {
		Log.MethodIn(Thread.currentThread());
		if(equalsCall.getTarget().getType() instanceof PrimitiveType){
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("%s == %s", equalsCall.getTarget(),
							StringUtils.join(equalsCall.getParameters(), ", ")));
		}
		if  (equalsCall.getTarget().getType()instanceof TypeList){
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("*%s == *%s", equalsCall.getTarget(),
							StringUtils.join(equalsCall.getParameters(), ", ")));
		}
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s -> equals(%s)", equalsCall.getTarget(),
						StringUtils.join(equalsCall.getParameters(), ", ")));
	}

	@Override
	public String getCode(ToStringCall tsc) {
		Log.MethodIn(Thread.currentThread());
		if(tsc.getTarget().getType() instanceof PrimitiveType)
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("std::to_string(%s) /*Since C++ 11 only. Do use -std=c++11 as compilation option.*/", tsc.getTarget()));
		if(tsc.getTarget().getType() instanceof TypeException)
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("%s->what()", tsc.getTarget()));
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("/* %s -> toString() Not recognized by GOOL*/", tsc.getTarget()));
	}

	@Override
	public String getCode(ListContainsCall lcc) {
		Log.MethodIn(Thread.currentThread());
		String exp = lcc.getExpression().callGetCode();
		String el = StringUtils.join(lcc.getParameters(), ", ");
		String toReturn  = String.format("(std::find(%s->begin(), %s->end(), %s) != %s->end())",
				exp, exp, el, exp);
		addCustomDependency("algorithm");
		return (String)Log.MethodOut(Thread.currentThread(), toReturn );
	}

	@Override
	public String getCode(ListGetCall lgc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s->%s(%s)", lgc.getExpression(), "at",
						GeneratorHelper.joinParams(lgc.getParameters())));
	}

	@Override
	public String getCode(ListGetIteratorCall lgic) {
		Log.MethodIn(Thread.currentThread());
		// TODO Auto-generated method stub
		return (String)Log.MethodOut(Thread.currentThread(), null);
	}

	@Override
	public String getCode(ListIsEmptyCall liec) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s -> empty()", liec.getExpression()));
	}

	@Override
	public String getCode(ListRemoveAtCall lrc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s -> erase(%s -> begin()+%s)",
						lrc.getExpression(), lrc.getExpression(),
						GeneratorHelper.joinParams(lrc.getParameters())));
	}

	@Override
	public String getCode(ListRemoveCall lrc) {
		Log.MethodIn(Thread.currentThread());
		String expName = lrc.getExpression().toString();
		int nbParameters = lrc.getParameters().size();
		boolean boostAny = lrc.getExpression().getType().toString().contains("boost::any");
		String boostAnyMessage = "/* An error will occur while comparing boost::any objects that way. Consider using typed vector.*/";
		String expr = String
				.format("%s->erase(std::find(%s->begin(),%s->end(),",
						expName,expName,expName);
		if (nbParameters == 1){
			Expression arg = lrc.getParameters().get(0);
			// Test the case of primitive type
			if (arg.getType() instanceof PrimitiveType){
				expr += GeneratorHelper.joinParams(lrc.getParameters()) + "))";
				if(boostAny)
					expr += boostAnyMessage;
				return (String)Log.MethodOut(Thread.currentThread(),expr);
			}
		}
		expr += "*" + GeneratorHelper.joinParams(lrc.getParameters()) + "))";
		Log.d("<CppGenerator - getCode(ListRemoveCall> Expression type : " + lrc.getExpression().getType().toString());
		if(boostAny)
			expr += boostAnyMessage;
		return (String)Log.MethodOut(Thread.currentThread(),expr);
		//		return (String)Log.MethodOut(Thread.currentThread(), String
		//				.format("%s->remove(%s)",lrc.getExpression(),
		//						GeneratorHelper.joinParams(lrc.getParameters())));
		//		return (String)Log.MethodOut(Thread.currentThread(), String
		//				.format("for (%s::iterator it = %s -> begin(); it != %s -> end(); ++it)\n\t"
		//						+ "if (**it == *%s)\n\t{\n\t\t"
		//						+ "list->erase(it);\n\t\tbreak;\n\t}\n",
		//						removePointer(lrc.getExpression().getType()),
		//						lrc.getExpression(), lrc.getExpression(),
		//						GeneratorHelper.joinParams(lrc.getParameters())));


	}

	@Override
	public String getCode(ListSizeCall lsc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s -> size()", lsc.getExpression()));
	}

	@Override
	public String getCode(ListClearCall lcc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s->clear()", lcc.getExpression()));
	}

	@Override
	public String getCode(ListSetCall lsc) {
		Log.MethodIn(Thread.currentThread());
		List<Expression> param = lsc.getParameters();
		if (param.size() == 0){
			return (String)Log.MethodOut(Thread.currentThread(),
					String.format("(*%s)[]", lsc.getExpression()));
		}else if (param.size() == 1){
			return (String)Log.MethodOut(Thread.currentThread(),
					String.format("(*%s)[%s]", lsc.getExpression(),
							param.get(0)));
		}
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("(*%s)[%s] = %s", lsc.getExpression(),param.get(0),
						param.get(1), StringUtils.join(param.subList(2, param.size()), ", ")));
	}

	@Override
	public String getCode(ListAddCall lac){
		Log.MethodIn(Thread.currentThread());
		int nbParameters = lac.getParameters().size();
		if (nbParameters == 0){
			return (String)Log.MethodOut(Thread.currentThread(),
					String.format("%s->push_back()", lac.getExpression()));
		}else if(nbParameters == 1){
			Expression arg = lac.getParameters().get(0);
			Log.d("<CppGenerator - getCode(ListAddCall> Expression type : " + lac.getExpression().getType().toString());
			// Test the case of generic list with a string constant entry
			if ((arg.getType() instanceof TypeString) && (arg instanceof Constant) 
					&& (lac.getExpression().getType().toString().contains("<boost::any>"))){
				Log.d("<CppGenerator - getCode(ListAddCall> No pointer case.");
				return (String)Log.MethodOut(Thread.currentThread(),
						String.format("%s->push_back(std::string(%s))", lac.getExpression(),
								GeneratorHelper.joinParams(lac.getParameters())));
			}
			return (String)Log.MethodOut(Thread.currentThread(),
					String.format("%s->push_back(%s)", lac.getExpression(),
							GeneratorHelper.joinParams(lac.getParameters())));
		}else if(nbParameters == 2){
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("%s->%s(%s -> begin() + %s, %s)", 
							lac.getExpression(), "insert",
							lac.getExpression(), lac.getParameters().get(0),
							GeneratorHelper.joinParams(lac.getParameters().
									subList(1, lac.getParameters().size()))));
		}else{
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("%s->%s(%s)", lac.getExpression(), "push_back",
							GeneratorHelper.joinParams(lac.getParameters())));
		}
	}

	@Override
	public String getCode(ListIndexOfCall lioc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("(std::find(%s->begin(),%s->end(),%s) - %s->begin())", lioc.getExpression(),
						lioc.getExpression(), StringUtils.join(lioc.getParameters(), ", "), lioc.getExpression()));
	}


	//	@Override
	//	public String getCode(Constructor cons) {
	//		Log.MethodIn(Thread.currentThread());
	//		if (cons.getName().isEmpty()){
	//			List<Modifier> modifiers = new ArrayList<Modifier>(cons.getModifiers());
	//			// Remove (public, private, protected, final) invalid modifiers.
	//			modifiers.remove(cons.getAccessModifier());
	//			modifiers.remove(Modifier.FINAL);
	//			String meth_pref = getCode(modifiers);
	//			if (!meth_pref.isEmpty())
	//				meth_pref += " ";
	//			meth_pref += cons.getType().callGetCode();
	//			if (!meth_pref.isEmpty())
	//				meth_pref += " ";
	//			return (String)Log.MethodOut(Thread.currentThread(), 
	//					meth_pref + String.format("%s::%s(%s)", cons.getClassDef().getName(), cons.getClassDef().getName(),
	//							StringUtils.join(cons.getParams(), ", ")));
	//		}
	//		return (String)Log.MethodOut(Thread.currentThread(), getCode((Meth) cons));
	//	}

	/**
	 * It is meant to be only used in the CPP file (it does not print the
	 * modifiers).
	 */
	@Override
	public String getCode(Meth meth) {
		Log.MethodIn(Thread.currentThread());
		List<Modifier> modifiers = new ArrayList<Modifier>(meth.getModifiers());
		// Remove (public, private, protected, final) invalid modifiers.
		modifiers.remove(meth.getAccessModifier());
		modifiers.remove(Modifier.FINAL);
		String meth_pref = getCode(modifiers);
		if (!meth_pref.isEmpty())
			meth_pref += " ";
		meth_pref += meth.getType().callGetCode();
		if (!meth_pref.isEmpty())
			meth_pref += " ";
		return (String)Log.MethodOut(Thread.currentThread(), 
				meth_pref + String.format("%s::%s(%s)", meth.getClassDef().getName(), meth.getName(),
						StringUtils.join(meth.getParams(), ", ")));
	}


	@Override
	public String getCode(VarDeclaration varDec) {
		Log.MethodIn(Thread.currentThread());

		if (varDec.getType() instanceof TypeArray){
			IType elemType = ((TypeArray)varDec.getType()).getElementType();
			String initialValue = "";
			String size = "";
			if (varDec.getInitialValue() != null) {
				String initialCode = varDec.getInitialValue().callGetCode();
				if (initialCode.startsWith("new"))
					size = initialCode.split("[\\[\\]]")[1];
				else
					initialValue = " = " + varDec.getInitialValue();
			}
			return (String)Log.MethodOut(Thread.currentThread(),
					String.format("%s %s[%s]%s", elemType, varDec.getName(),
							size, initialValue));
		}
		return (String)Log.MethodOut(Thread.currentThread(),
				super.getCode(varDec));
	}

	/**
	 * Warning, this method can be shortcut by getCode(VarDeclaration varDec)
	 */
	@Override
	public String getCode(TypeArray typeArray) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s*", typeArray.getElementType()));
	}

	@Override
	public String getCode(ClassNew classNew) {
		Log.MethodIn(Thread.currentThread());
		if (classNew.getType() instanceof PrimitiveType)
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("%s(%s)", removePointer(classNew.getType()),
							StringUtils.join(classNew.getParameters(), ", ")));
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("(new %s(%s))", removePointer(classNew.getType()),
						StringUtils.join(classNew.getParameters(), ", ")));
	}

	@Override
	public String getCode(MainMeth mainMeth) {
		Log.MethodIn(Thread.currentThread());
		GeneratorHelper.generatingMainMethod = true;
		return (String)Log.MethodOut(Thread.currentThread(), "int main()");
	}

	@Override
	public String getCode(SystemCommandDependency systemCommandDependency) {
		Log.MethodIn(Thread.currentThread());
		// TODO Auto-generated method stub
		return (String)Log.MethodOut(Thread.currentThread(), null);
	}


	public Set<String> printCustomDependencies(){
		Set<String> customDeps = new HashSet<String>();
		for(String Dep : customDependencies){
			String depStr = "#include <" + Dep + ">";
			if (!customDeps.contains(depStr)){
				customDeps.add(depStr);
			}
		}
		return customDeps;
	}

	public String getDependenciesCode(ClassDef cl){
		String res = "";
		res += "#include <boost/any.hpp>\n";
		res += "#include <boost/lexical_cast.hpp>\n";		
		Set<String> dependencies = GeneratorHelper.printDependencies(cl);
		dependencies.addAll(printCustomDependencies());
		clearCustomDependencies();
		String recogDependencies = GeneratorHelper.printRecognizedDependencies(cl);
		List<String> sortedDependencies = new ArrayList<String>(dependencies);
		Collections.sort(sortedDependencies);
		if (!sortedDependencies.isEmpty()) {
			for (String dependency : sortedDependencies){
				if (dependency.contains("/*")){
					String incdep = String.format("%s", dependency);
					if (recogDependencies.indexOf(incdep) == -1)
						res += incdep + "\n";
				}
				else if (dependency != "noprint"){
					String incdep = String.format("%s", dependency);
					if (recogDependencies.indexOf(incdep) == -1)
						res += incdep + "\n";
				}
			}
			res += "\n";
		}
		res += recogDependencies + "\n\n";
		//res += "#include \"finally.h\"\n";
		res += String.format("#include \"%s.h\"\n", cl.getName());
		return res;
	}

	//@Override
	public String printClass(ClassDef classDef) {
		Log.MethodIn(Thread.currentThread());

		//		String header = String.format("// Platform: %s\n\n",
		//				classDef.getPlatform());
		String header = "";

		String body = "";
		// print the package containing the class
		if (classDef.getPpackage() != null)
			body += String.format("namespace %s {",
					classDef.getPackageName());
		for (Meth meth : classDef.getMethods()) {
			// TODO: deal with constructors ?
			if (classDef.isInterface())
				body += formatIndented("%s;\n\n", meth.getHeader());
			else
				body += formatIndented("%s {%1}\n\n", meth.getHeader(),
						meth.getBlock());
		}
		if (classDef.getPpackage() != null)
			body += "}";

		return (String)Log.MethodOut(Thread.currentThread(),
				header + "\n" + getDependenciesCode(classDef) + "\n" + body);
	}


	@Override
	public String getCode(Throw throwStatement) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("throw %s", throwStatement.getExpression()));
	}

	@Override
	public String getCode(Catch catchStatement) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				formatIndented("catch (%s * %s)\n{%1}\n", catchStatement
						.getParameter().getType(), catchStatement.getParameter()
						.getName(), catchStatement.getBlock()));
	}

	@Override
	public String getCode(Try tryStatement) {
		Log.MethodIn(Thread.currentThread());
		String ret = "";
		if (tryStatement.getFinilyBlock().getStatements().isEmpty()) {
			ret = formatIndented("try\n{%1}", tryStatement.getBlock());
		} else {
			ret = formatIndented("try\n{\nfinally(%1) // finally%1}",
					tryStatement.getFinilyBlock(), tryStatement.getBlock());
		}
		for (Catch c : tryStatement.getCatches()) {
			ret += "\n" + c;
		}
		return (String)Log.MethodOut(Thread.currentThread(), ret);
	}

	@Override
	public String getCode(TypeException typeException) {
		Log.MethodIn(Thread.currentThread());
		switch (typeException.getKind()) {
		case GLOBAL:
			return (String)Log.MethodOut(Thread.currentThread(), "std::exception");
		default:
			return (String)Log.MethodOut(Thread.currentThread(), typeException.getName());
		}
	}

	@Override
	public String getCode(RecognizedDependency recognizedDependency) {
		Log.MethodIn(Thread.currentThread());
		List<String> imports = GeneratorMatcher.matchImports(recognizedDependency.getName());
		if(imports == null)
			return (String)Log.MethodOut(Thread.currentThread(), "");
		if(imports.isEmpty())
			return (String)Log.MethodOut(Thread.currentThread(), 
					"/* import "+recognizedDependency.getName() + 
					" not generated by GOOL, passed on. */");
		String result = "";
		String className = GeneratorMatcher.matchGoolClass(recognizedDependency.getName());
		if (className.equalsIgnoreCase("std")){
			for (String Import : imports) {
				if (Import.startsWith("+"))
					Import = Import.substring(1);
				result += "#include <" + Import + ">\n";
			}
		}
		else{
			for (String Import : imports) {
				if (Import.startsWith("+"))
					Import = Import.substring(1);
				result += "#include \"" + Import.replace(".", "/") + ".h\"\n";
			}
		}

		return (String)Log.MethodOut(Thread.currentThread(), result);
	}

	@Override
	public String getCode(TypeGoolLibraryClass typeMatchedGoolClass) {
		Log.MethodIn(Thread.currentThread());
		String res = GeneratorMatcher.matchGoolClass(typeMatchedGoolClass
				.getGoolclassname());
		Log.d("<CppGenerator - getCode : TypeGoolLibraryClass> return " + res + "*");
		if (res == null)
			return (String)Log.MethodOut(Thread.currentThread(), 
					typeMatchedGoolClass.getGoolclassname()
					+ " /* Ungenerated by GOOL, passed on. */");
		else {
			return (String)Log.MethodOut(Thread.currentThread(), res + "*");
		}
	}

}
