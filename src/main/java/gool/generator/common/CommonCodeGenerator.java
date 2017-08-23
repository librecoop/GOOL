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

package gool.generator.common;

import gool.ast.core.ArrayAccess;
import gool.ast.core.ArrayNew;
import gool.ast.core.Assign;
import gool.ast.core.BinaryOperation;
import gool.ast.core.Block;
import gool.ast.core.CastExpression;
import gool.ast.core.ClassDef;
import gool.ast.core.ClassFree;
import gool.ast.core.Comment;
import gool.ast.core.CompoundAssign;
import gool.ast.core.Constant;
import gool.ast.core.Constructor;
import gool.ast.core.Expression;
import gool.ast.core.ExpressionUnknown;
import gool.ast.core.Field;
import gool.ast.core.FieldAccess;
import gool.ast.core.For;
import gool.ast.core.GoolCall;
import gool.ast.core.Identifier;
import gool.ast.core.If;
import gool.ast.core.ListMethCall;
import gool.ast.core.MapEntryMethCall;
import gool.ast.core.MapMethCall;
import gool.ast.core.MemberSelect;
import gool.ast.core.Meth;
import gool.ast.core.MethCall;
import gool.ast.core.Modifier;
import gool.ast.core.NewInstance;
import gool.ast.core.Operator;
import gool.ast.core.Package;
import gool.ast.core.Return;
import gool.ast.core.Statement;
import gool.ast.core.StringIsEmptyCall;
import gool.ast.core.This;
import gool.ast.core.ThisCall;
import gool.ast.core.TypeDependency;
import gool.ast.core.UnImplemented;
import gool.ast.core.UnaryOperation;
import gool.ast.core.UnrecognizedDependency;
import gool.ast.core.VarAccess;
import gool.ast.core.VarDeclaration;
import gool.ast.core.While;
import gool.ast.list.ListClearCall;
import gool.ast.list.ListIndexOfCall;
import gool.ast.list.ListSetCall;
import gool.ast.type.TypeArray;
import gool.ast.type.TypeByte;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeGoolLibraryClass;
import gool.ast.type.TypeMethod;
import gool.ast.type.TypeNone;
import gool.ast.type.TypeNull;
import gool.ast.type.TypePackage;
import gool.ast.type.TypeString;
import gool.ast.type.TypeUnknown;
import gool.ast.type.TypeVar;
import gool.ast.type.TypeVoid;
import gool.generator.GeneratorHelper;
import logger.Log;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Basic code generator. It does what is common to the code generation of all
 * output object oriented languages.
 */
public abstract class CommonCodeGenerator implements CodeGenerator {

	/**
	 * String used to produce one level of indentation. Can be overwritten in
	 * the constructor of the concrete generator.
	 */

	protected String indentation = "\t";


	/**
	 * <pre>
	 * Produce indented code in a manner similar to printf but with custom conversions.
	 * %%  a single "%"
	 * %s  Print an argument as a string, without indentation or newlines added.
	 *     Similar to the corresponding flag of <i>String.format</i>.
	 * %<i>n</i>  (where <i>n</i> is a digit)
	 *     Print an argument as a bloc indented <i>n</i> times from the current indentation level.
	 *     Newlines are inserted before and after the bloc.
	 * %-<i>n</i> (where <i>n</i> is a digit)
	 *     <i>n</i> times the indentation string, does not consumes a argument.
	 *     %-0 becomes a empty string (it does nothing but is still parsed)
	 * 
	 * @param format
	 *            the format string
	 * @param arguments
	 *            the objects to format, each one corresponding to a % code
	 * @return the formated string
	 */
	protected String formatIndented(String format, Object... arguments) {
		StringBuilder sb = new StringBuilder(format);
		int pos = sb.indexOf("%");
		int arg = 0;
		while (pos != -1) {
			if (sb.charAt(pos + 1) == '%') {
				sb = sb.replace(pos, pos + 2, "%");
			} else if (sb.charAt(pos + 1) == 's') {
				sb = sb.replace(pos, pos + 2, arguments[arg].toString());
				pos += arguments[arg].toString().length() - 1;
				arg++;
			} else if (Character.isDigit(sb.charAt(pos + 1))) {
				String replacement = ("\n" + arguments[arg].toString()
						.replaceFirst("\\s*\\z", ""))
						.replace(
								"\n",
								"\n"
										+ StringUtils.repeat(
												indentation,
												Character.digit(
														sb.charAt(pos + 1), 10)))
						+ "\n";
				sb = sb.replace(pos, pos + 2, replacement);
				pos += replacement.length() - 1;
				arg++;
			} else if (sb.charAt(pos + 1) == '-'
					&& Character.isDigit(sb.charAt(pos + 2))) {
				String replacement = StringUtils.repeat(indentation,
						Character.digit(sb.charAt(pos + 2), 10));
				sb = sb.replace(pos, pos + 3, replacement);
				pos += replacement.length();
			}
			pos = sb.indexOf("%", pos);
		}
		return sb.toString();
	}

	@Override
	public String getCode(Identifier identifier) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), identifier.getName());
	}

	@Override
	public String getCode(ArrayAccess arrayAccess) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), String.format(
				"%s[%s]", arrayAccess.getExpression(),
				arrayAccess.getIndex()));
	}

	@Override
	public String getCode(ArrayNew arrayNew) {
		Log.MethodIn(Thread.currentThread());
		if (arrayNew.getInitialiList().isEmpty())
			return (String)Log.MethodOut(Thread.currentThread(), String.format(
					"new %s[%s]", arrayNew.getType(),
					StringUtils.join(arrayNew.getDimesExpressions(), ", ")));

		return (String)Log.MethodOut(Thread.currentThread(), String.format(
				"{%s}",StringUtils.join(arrayNew.getInitialiList(), ", ")));


	}


	/**
	 * Produces code for an assign statement.
	 * 
	 * @param assign
	 *            the assign statement.
	 * @return the formatted assign statement.
	 */
	@Override
	public String getCode(Assign assign) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				assign.getLValue() + " = " + assign.getValue());
	}

	@Override
	public String getCode(CompoundAssign compoundAssign) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),  String
				.format("%s %s=%s %s",
						compoundAssign.getLValue(),
						compoundAssign.getTextualoperator(),
						compoundAssign.getOperator().equals(
								Operator.UNKNOWN) ? "/* Unrecognized by GOOL, passed on */"
										: "", compoundAssign.getValue()));
	}

	/**
	 * Produces code for a binary operation.
	 * 
	 * @param binaryOp
	 *            a binary operation.
	 * @return the formatted binary operation.
	 */
	@Override
	public String getCode(BinaryOperation binaryOp) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),   String
				.format("(%s %s%s %s)",
						binaryOp.getLeft(),
						binaryOp.getTextualoperator(),
						binaryOp.getOperator().equals(
								Operator.UNKNOWN) ? "/* Unrecognized by GOOL, passed on */"
										: "", binaryOp.getRight()));
	}

	/**
	 * Produces code for a block of statements.
	 * 
	 * @param block
	 *            the block of statements.
	 * @return the formatted block of statements.
	 */
	@Override
	public String getCode(Block block) {
		Log.MethodIn(Thread.currentThread());
		StringBuilder result = new StringBuilder();
		for (Statement statement : block.getStatements()) {
			String statementStr = statement.callGetCode();
			if (!statementStr.isEmpty()){
				result.append(statementStr);
				Log.d("<CommonCodeGenerator - getCode(Block block)> statement : " + statementStr);
				if (!(statement instanceof Block))
				{
					if ((result.charAt(result.length()-1) != '}') ||
							(statement instanceof VarDeclaration))
						result.append(";");
					result.append("\n");
				}
			}
		}
		if (GeneratorHelper.generatingMainMethod){
			GeneratorHelper.generatingMainMethod = false;
		}
		return (String)Log.MethodOut(Thread.currentThread(), result.toString());
	}

	/**
	 * Produces code for a cast expression.
	 * 
	 * @param cast
	 *            the cast expression.
	 * @return the formatted cast expression.
	 */
	@Override
	public String getCode(CastExpression cast) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("((%s) (%s))", cast.getType(),
						cast.getExpression()));
	}

	/**
	 * Generates code for a GOOL comment block.
	 * 
	 * @param comment
	 *            the comment to be generated.
	 */
	@Override
	public String getCode(Comment comment) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("/*\n%s\n*/", comment.getValue()));
	}

	/**
	 * Produces code for a constant.
	 * 
	 * @param constant
	 *            a constant value.
	 * @return the formatted constant value.
	 */
	@Override
	public String getCode(Constant constant) {
		Log.MethodIn(Thread.currentThread());
		if (constant.getType() instanceof TypeArray) {
			StringBuffer sb = new StringBuffer();

			int size = Array.getLength(constant.getValue());
			boolean escape = ((TypeArray) constant.getType()).getElementType() instanceof TypeString;

			sb.append("{");
			for (int i = 0; i < size; i++) {
				if (escape) {
					return (String)Log.MethodOut(Thread.currentThread(), "\"" +
							StringEscapeUtils.escapeJava(Array.get(constant.getValue(),
									i).toString()) + "\"");
				} else {
					sb.append(Array.get(constant.getValue(), i));
				}
				sb.append(",");
			}
			sb.append("}");
			return (String)Log.MethodOut(Thread.currentThread(), sb.toString());
		} else if (constant.getType() == TypeString.INSTANCE) {
			return (String)Log.MethodOut(Thread.currentThread(), "\""
					+ StringEscapeUtils.escapeJava(constant.getValue()
							.toString()) + "\"");
		} else if (constant.getType() == TypeChar.INSTANCE) {
			return (String)Log.MethodOut(Thread.currentThread(), "'"
					+ StringEscapeUtils.escapeJava(constant.getValue()
							.toString()) + "'");
		}
		return (String)Log.MethodOut(Thread.currentThread(),
				constant.getValue().toString());
	}

	@Override
	public String getCode(Constructor cons) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				getCode((Meth) cons));
	}

	/**
	 * Produces code for a field, i.e. a class attribute declaration.
	 * 
	 * @param field
	 *            the abstract GOOL field
	 * @return the string corresponding to such a declaration in the concrete
	 *         target language
	 */
	@Override
	public String getCode(Field field) {
		Log.MethodIn(Thread.currentThread());
		String out = String.format("%s %s %s", getCode(field.getModifiers()),
				field.getType(), field.getName());
		if (field.getType().toString().equals("noprint"))
			return (String)Log.MethodOut(Thread.currentThread(), "");
		if (field.getDefaultValue() != null) {
			// Notice that this will call a toString() on the field.defaultValue
			// Which will become a JavaGenerator.getCode(defaultValue)
			// Hence this seemingly simple statement
			// Is in fact a recursive descent on the abstract GOOL tree.
			out = String.format("%s = %s", out, field.getDefaultValue());
		}
		return (String)Log.MethodOut(Thread.currentThread(), out);
	}

	@Override
	public String getCode(FieldAccess sfa) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				sfa.getTarget() + "." + sfa.getMember());
	}

	@Override
	public String getCode(For forInstruction) {
		Log.MethodIn(Thread.currentThread());
		String initializerCode = "";
		String updaterCode = "";
		if (forInstruction.getInitializer() != null){
			initializerCode = forInstruction.getInitializer().callGetCode();
		}
		if (forInstruction.getUpdater() != null){
			updaterCode = forInstruction.getUpdater().callGetCode();
		}
		return (String)Log.MethodOut(Thread.currentThread(),
				formatIndented("for (%s ; %s ; %s) {%1}",
						initializerCode, forInstruction.getCondition(),
						updaterCode, forInstruction.getWhileStatement()));
	}

	@Override
	public String getCode(GoolCall goolCall) {
		Log.MethodIn(Thread.currentThread());
		throw new IllegalStateException(String.format(
				"Invalid unimplemented Gool Method: (%s).",
				goolCall.getMethod()));
	}

	/**
	 * Produces code for an if statement.
	 * 
	 * @param pif
	 *            the if statement.
	 * @return the formatted if statement.
	 */
	@Override
	public String getCode(If pif) {
		Log.MethodIn(Thread.currentThread());
		String out = formatIndented("if (%s) {%1}", pif.getCondition(),
				pif.getThenStatement());
		if (pif.getElseStatement() != null) {
			if (pif.getElseStatement() instanceof If)
				out += formatIndented("else %s", pif.getElseStatement());
			else
				out += formatIndented("else {%1}", pif.getElseStatement());
		}
		return (String)Log.MethodOut(Thread.currentThread(), out);
	}

	@Override
	public String getCode(Collection<Modifier> modifiers) {
		Log.MethodIn(Thread.currentThread());
		StringBuilder sb = new StringBuilder();
		for (Modifier modifier : modifiers) {
			sb.append(getCode(modifier)).append(" ");
		}
		return (String)Log.MethodOut(Thread.currentThread(), sb.toString().trim());
	}

	@Override
	public String getCode(StringIsEmptyCall lmc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), getCode((MemberSelect)lmc));
	}

	@Override
	public String getCode(ListMethCall lmc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "===ListMethCall====");
	}

	@Override
	public String getCode(ListClearCall lcc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s.clear() /* Default translation, GOOL may be wrong on it */",
						lcc.getExpression()));
	}

	@Override
	public String getCode(ListSetCall lsc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s.set(%s) /* Default translation, GOOL may be wrong on it */",
						lsc.getExpression(), StringUtils.join(lsc.getParameters(), ", ")));
	}

	@Override
	public String getCode(ListIndexOfCall lioc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s.indexOf(%s) /* Default translation, GOOL may be wrong on it */",
						lioc.getExpression(),
						StringUtils.join(lioc.getParameters(), ", ")));
	}

	@Override
	public String getCode(MapEntryMethCall mapEntryMethCall) {
		Log.MethodIn(Thread.currentThread());
		throw new IllegalStateException("Unsupported MapEntryMethCall: "
				+ mapEntryMethCall.getExpression());
	}

	@Override
	public String getCode(MapMethCall mapMethCall) {
		Log.MethodIn(Thread.currentThread());
		throw new IllegalStateException(String.format(
				"Invalid method call over maps (%s).",
				mapMethCall.getExpression()));
	}

	@Override
	public String getCode(MemberSelect memberSelect) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s.%s", memberSelect.getTarget(),
						memberSelect.getIdentifier()));		
	}

	@Override
	public String getCode(Meth meth) {
		Log.MethodIn(Thread.currentThread());
		Log.d("==================================> Meth Call");
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s %s %s (%s)", getCode(meth.getModifiers()),
						meth.getType(), meth.getName(),
						StringUtils.join(meth.getParams(), ", ")));
	}

	/**
	 * Produces code for a method invocation.
	 * 
	 * @param methodCall
	 *            the method to be invoked.
	 * @return the formatted method invocation.
	 */
	@Override
	public String getCode(MethCall methodCall) {
		Log.MethodIn(Thread.currentThread());
		String target = methodCall.getTarget().toString();
		String goolMethod = methodCall.getGoolLibraryMethod();
		if(goolMethod!=null){
			//here, get matched output method name with the GeneratorMatcher
			String methodName=GeneratorMatcher.matchGoolMethod(goolMethod);
			if(methodName!=null)
				target=target.substring(0, target.lastIndexOf(".")+1)+methodName;
		}
		Log.d("<CommonCodeGenerator - getCode(MethCall)> target " + target);
		List<Expression> params = methodCall.getParameters();
		Iterator<Expression> it = params.iterator();
		while(it.hasNext()){
			Expression next = it.next();
			if(next == null){
				it.remove();
			}
			else if(next.callGetCode().isEmpty()){
				it.remove();
			}
		}
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s(%s)", target,
						StringUtils.join(params, ", ")));
	}

	@Override
	public String getCode(Modifier modifier) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				modifier.name().toLowerCase());
	}

	/**
	 * Produces code for an object instantiation. This is different from
	 * ClassNew in the sense that it includes a variable declaration and
	 * assignment in the same line.
	 * 
	 * @param newInstance
	 *            the object instantiation.
	 * @return the formatted object instantiation.
	 */
	@Override
	public String getCode(NewInstance newInstance) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s = new %s( %s )",
						newInstance.getVariable(),
						newInstance.getVariable().getType().toString()
						.replaceAll("\\*$", ""),
						StringUtils.join(newInstance.getParameters(), ", ")));
	}

	/**
	 * Produces code for a return statement.
	 * 
	 * @param returnExpr
	 *            the return statement.
	 * @return the formatted return statement.
	 */
	@Override
	public String getCode(Return returnExpr) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("return (%s)", returnExpr.getExpression()));
	}

	/**
	 * Produces code for the reference to the current object.
	 * 
	 * @param pthis
	 *            the reference to the current object.
	 * @return the formatted self reference expression.
	 */
	@Override
	public String getCode(This pthis) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				"this");
	}

	@Override
	public String getCode(TypeByte typeByte) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				"byte");
	}

	/**
	 * @param typeClass
	 *            the class to be formatted.
	 * @return the formatted class type.
	 */
	@Override
	public String getCode(TypeClass typeClass) {
		Log.MethodIn(Thread.currentThread());
		String code = "";
		if (typeClass.getPackageName() != null) {
			code += typeClass.getPackageName() + ".";
		}
		code += typeClass.getName();
		return (String)Log.MethodOut(Thread.currentThread(), code);
	}

	@Override
	public String getCode(TypeDependency typeDependency) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				typeDependency.getType().toString());
	}

	/**
	 * Produces code for the pseudo-type.
	 * 
	 * @param type
	 *            a pseudo-type.
	 * @return an empty string.
	 */
	@Override
	public String getCode(TypeNone type) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "");
	}

	@Override
	public String getCode(TypeNull typeNull) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "null");
	}

	/**
	 * Produces code for a type that does not return anything.
	 * 
	 * @param typeVoid
	 *            the void type
	 * @return the formatted void type.
	 */
	@Override
	public String getCode(TypeVoid typeVoid) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "void");
	}

	@Override
	public String getCode(UnaryOperation unaryOperation) {
		Log.MethodIn(Thread.currentThread());
		switch (unaryOperation.getOperator()) {
		case POSTFIX_DECREMENT:
		case POSTFIX_INCREMENT:
			return (String)Log.MethodOut(Thread.currentThread(),
					String.format("(%s)%s%s",
							unaryOperation.getExpression(),
							unaryOperation.getTextualoperator(),
							unaryOperation.getOperator().equals(
									Operator.UNKNOWN) ? "/* Unrecognized by GOOL, passed on */"
											: ""));
		default:
			return (String)Log.MethodOut(Thread.currentThread(),
					String.format("%s%s(%s)",
							unaryOperation.getTextualoperator(),
							unaryOperation.getOperator().equals(
									Operator.UNKNOWN) ? "/* Unrecognized by GOOL, passed on */"
											: "", unaryOperation.getExpression()));
		}
	}

	/**
	 * Produces code for a variable declaration.
	 * 
	 * @param varDec
	 *            the variable to be declared.
	 * @return the formatted variable declaration.
	 */
	@Override
	public String getCode(VarDeclaration varDec) {
		Log.MethodIn(Thread.currentThread());
		String initialValue = "";
		if (varDec.getInitialValue() != null) {
			initialValue = " = " + varDec.getInitialValue();
		}
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s %s%s", varDec.getType(), varDec.getName(),
						initialValue));
	}

	@Override
	public String getCode(VarAccess varAccess) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				varAccess.getDec().getName());
	}

	@Override
	public String getCode(While whilee) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				formatIndented("while (%s) {%1}", whilee.getCondition(),
						whilee.getWhileStatement()));
	}

	@Override
	public String getCode(TypeArray typeArray) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s[]", typeArray.getElementType()));
	}

	@Override
	public String getCode(ThisCall thisCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("this (%s)",
						GeneratorHelper.joinParams(thisCall.getParameters())));
	}

	@Override
	public String getCode(TypeUnknown typeUnknown) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s", typeUnknown.getTextualtype())
				+ " /* Unrecognized by GOOL, passed on */");
	}

	@Override
	public String getCode(ExpressionUnknown unknownExpression) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s /* Unrecognized by GOOL, passed on */",
						unknownExpression.getTextual()));
	}

	@Override
	public String getCode(ClassFree classFree) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				"free /* Not Implemented, passed on by GOOL */");
	}

	@Override
	public String getCode(Platform platform) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				platform.getName());
	}

	@Override
	public String getCode(ClassDef classDef) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s.%s", classDef.getPackageName(),
						classDef.getName()));
	}

	@Override
	public String getCode(Package _package) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				_package.getName());
	}

	@Override
	public String getCode(TypePackage typePackage) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				typePackage.getTextualtype());
	}

	@Override
	public String getCode(TypeVar typeVar) {
		Log.MethodIn(Thread.currentThread());
		// For now if one wants to print the type of a TypeVar, this returns
		// just the name of the TypeVar.
		return (String)Log.MethodOut(Thread.currentThread(),
				typeVar.getTextualtype());
	}

	@Override
	public String getCode(TypeMethod typeMethod) {
		Log.MethodIn(Thread.currentThread());
		// For now if one wants to print the type of a Method, this returns just
		// the name of the method.
		return (String)Log.MethodOut(Thread.currentThread(),
				typeMethod.getTextualtype());
	}

	@Override
	public String getCode(TypeGoolLibraryClass typeMatchedGoolClass) {
		Log.MethodIn(Thread.currentThread());
		String res = GeneratorMatcher.matchGoolClass(typeMatchedGoolClass
				.getGoolclassname());
		if (res == null)
			return (String)Log.MethodOut(Thread.currentThread(),
					typeMatchedGoolClass.getGoolclassname()
					+ " /* Ungenerated by GOOL, passed on. */");
		else {
			return (String)Log.MethodOut(Thread.currentThread(), res);
		}
	}

	public String getCode(UnrecognizedDependency unrecognizedDependency) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				"/* "+ unrecognizedDependency.getName()
				+ " unrecognized by GOOL, passed on. */");
	}	

	public String getCode(UnImplemented unImplemented) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				"/* "+ unImplemented.getCommentUnImplemented()
				+ " unimplemented by GOOL, passed on : "
				+ unImplemented.getCodeUnImplemented()
				+ " */");
	}

}
