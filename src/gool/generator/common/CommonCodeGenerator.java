package gool.generator.common;

import gool.ast.constructs.ArrayAccess;
import gool.ast.constructs.ArrayNew;
import gool.ast.constructs.Assign;
import gool.ast.constructs.BinaryOperation;
import gool.ast.constructs.Block;
import gool.ast.constructs.CastExpression;
import gool.ast.constructs.ClassDef;
import gool.ast.constructs.ClassFree;
import gool.ast.constructs.Comment;
import gool.ast.constructs.CompoundAssign;
import gool.ast.constructs.Constant;
import gool.ast.constructs.Constructor;
import gool.ast.constructs.ExpressionUnknown;
import gool.ast.constructs.Field;
import gool.ast.constructs.FieldAccess;
import gool.ast.constructs.For;
import gool.ast.constructs.GoolCall;
import gool.ast.constructs.Identifier;
import gool.ast.constructs.If;
import gool.ast.constructs.ListMethCall;
import gool.ast.constructs.MapEntryMethCall;
import gool.ast.constructs.MapMethCall;
import gool.ast.constructs.MemberSelect;
import gool.ast.constructs.Meth;
import gool.ast.constructs.MethCall;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.NewInstance;
import gool.ast.constructs.Operator;
import gool.ast.constructs.Package;
import gool.ast.constructs.Return;
import gool.ast.constructs.Statement;
import gool.ast.constructs.This;
import gool.ast.constructs.ThisCall;
import gool.ast.constructs.TypeDependency;
import gool.ast.constructs.UnaryOperation;
import gool.ast.constructs.VarAccess;
import gool.ast.constructs.VarDeclaration;
import gool.ast.constructs.While;
import gool.ast.type.TypeArray;
import gool.ast.type.TypeBufferedReader;
import gool.ast.type.TypeByte;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeFile;
import gool.ast.type.TypeFileReader;
import gool.ast.type.TypeMethod;
import gool.ast.type.TypeNone;
import gool.ast.type.TypeNull;
import gool.ast.type.TypePackage;
import gool.ast.type.TypeString;
import gool.ast.type.TypeUnknown;
import gool.ast.type.TypeVar;
import gool.ast.type.TypeVoid;
import gool.generator.GeneratorHelper;

import java.lang.reflect.Array;
import java.util.Collection;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Basic code generator. It does what is common to the code generation of all output object oriented
 * languages.
 */
public abstract class CommonCodeGenerator implements CodeGenerator {
	
	/**
	 * String used to produce one level of indentation.
	 * Can be overwritten in the constructor of the concrete generator.
	 */
	protected String indentation = "\t";
	
	/**<pre>
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
	protected String formatIndented(String format, Object... arguments){
		StringBuilder sb = new StringBuilder(format);
		int pos = sb.indexOf("%");
		int arg = 0;
		while (pos != -1){
			if (sb.charAt(pos +1) == '%'){
				sb = sb.replace(pos, pos +2, "%");
			} else if (sb.charAt(pos +1) == 's') {
				sb = sb.replace(pos, pos +2, arguments[arg].toString());
				pos += arguments[arg].toString().length() -1;
				arg++;
			} else if (Character.isDigit(sb.charAt(pos +1))) {
				String replacement = ("\n" + arguments[arg].toString().replaceFirst("\\s*\\z", "")).replace("\n",
						"\n"+StringUtils.repeat(indentation, Character.digit(sb.charAt(pos +1), 10))) + "\n";
				sb = sb.replace(pos, pos +2, replacement);
				pos += replacement.length() -1;
				arg++;
			} else if (sb.charAt(pos +1) == '-' && Character.isDigit(sb.charAt(pos +2))) {
				String replacement = StringUtils.repeat(indentation, Character.digit(sb.charAt(pos +2), 10));
				sb = sb.replace(pos, pos +3, replacement);
				pos += replacement.length();
			}
			pos = sb.indexOf("%", pos);
		}
		return sb.toString();
	}
	
	
	@Override
	public String getCode(Identifier identifier) {
		return identifier.getName();
	}
	@Override
	public String getCode(ArrayAccess arrayAccess) {
		return String.format("%s[%s]", arrayAccess.getExpression(), arrayAccess.getIndex());
	}

	@Override
	public String getCode(ArrayNew arrayNew) {
		return String.format("new %s[%s]", arrayNew.getType(), StringUtils
				.join(arrayNew.getDimesExpressions(), ", "));
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
		return assign.getLValue() + " = " + assign.getValue();
	}
	
	@Override
	public String getCode(CompoundAssign compoundAssign) {
		return String.format("%s %s=%s %s", compoundAssign.getLValue(), compoundAssign.getTextualoperator(),
				compoundAssign.getOperator().equals(Operator.UNKNOWN)?"/* Unrecognized by GOOL, passed on */":"",
				compoundAssign.getValue());
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
		return String.format("(%s %s%s %s)", binaryOp.getLeft(), binaryOp.getTextualoperator(), binaryOp.getOperator().equals(Operator.UNKNOWN)?"/* Unrecognized by GOOL, passed on */":"", binaryOp.getRight());
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
		StringBuilder result = new StringBuilder();
		for (Statement statement : block.getStatements()) {
			result.append(statement);
			
			if (!(statement instanceof Block)) {
				result.append(";").append("\n");
			}
		}
		return result.toString();
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
		return String.format("((%s) (%s))", cast.getType(), cast
				.getExpression());
	}

	/**
	 * Generates code for a GOOL comment block.
	 * 
	 * @param comment
	 *            the comment to be generated.
	 */
	@Override
	public String getCode(Comment comment) {
		return String.format("/*\n%s\n*/", comment.getValue());
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
		if (constant.getType() instanceof TypeArray) {
			StringBuffer sb = new StringBuffer();
			
			int size = Array.getLength(constant.getValue());
			boolean escape = ((TypeArray)constant.getType()).getElementType() instanceof TypeString;
			
			sb.append("{");
			for (int i=0; i<size; i++) {
				if (escape) {
					return "\"" + StringEscapeUtils.escapeJava(Array.get(constant.getValue(), i).toString())
					+ "\"";
				} else {
					sb.append(Array.get(constant.getValue(), i));
				}
				sb.append(",");
			}
			sb.append("}");
			return sb.toString();
		}
		else if (constant.getType() == TypeString.INSTANCE) {
			return "\"" + StringEscapeUtils.escapeJava(constant.getValue().toString())
					+ "\"";
		}
		else if (constant.getType() == TypeChar.INSTANCE){
			return "'" + StringEscapeUtils.escapeJava(constant.getValue().toString()) +"'";
		}
		return constant.getValue().toString();
	}
	

	@Override
	public String getCode(Constructor cons) {
		return getCode((Meth) cons);
	}

	/**
	 * Produces code for a field, i.e. a class attribute declaration.
	 * 
	 * @param field
	 *            the abstract GOOL field
	 * @return the string corresponding to such a declaration in the concrete target language
	 */
	@Override
	public String getCode(Field field) {
		String out = String.format("%s %s %s", getCode(field.getModifiers()),
				field.getType(), field.getName());
		if (field.getDefaultValue() != null) {
			//Notice that this will call a toString() on the field.defaultValue
			//Which will become a JavaGenerator.getCode(defaultValue)
			//Hence this seemingly simple statement
			//Is in fact a recursive descent on the abstract GOOL tree.
			out = String.format("%s = %s", out, field.getDefaultValue());
		}
		return out;
	}

	@Override
	public String getCode(FieldAccess sfa) {
		return sfa.getTarget() + "." + sfa.getMember();
	}
	
	@Override
	public String getCode(For forInstruction) {
		return formatIndented("for (%s ; %s ; %s) {%1}", forInstruction.getInitializer(), forInstruction
				.getCondition(), forInstruction.getUpdater(), forInstruction.getWhileStatement());
	}

	@Override
	public String getCode(GoolCall goolCall) {
		throw new IllegalStateException(String.format(
				"Invalid unimplemented Gool Method: (%s).", goolCall
						.getMethod()));
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
		String out = formatIndented ("if (%s) {%1}", pif.getCondition(), pif.getThenStatement());
		if (pif.getElseStatement() != null){
			if (pif.getElseStatement() instanceof If)
				out += formatIndented (" else %s", pif.getElseStatement());
			else
				out += formatIndented (" else {%1}", pif.getElseStatement());
		}
		return out;
	}

	@Override
	public String getCode(Collection<Modifier> modifiers) {
		StringBuilder sb = new StringBuilder();
		for (Modifier modifier : modifiers) {
			sb.append(getCode(modifier)).append(" ");
		}
		return sb.toString().trim();
	}

	@Override
	public String getCode(ListMethCall lmc) {
		return "===ListMethCall====";
	}

	@Override
	public String getCode(MapEntryMethCall mapEntryMethCall) {
		throw new IllegalStateException("Unsupported MapEntryMethCall: "
				+ mapEntryMethCall.getExpression());
	}

	@Override
	public String getCode(MapMethCall mapMethCall) {
		throw new IllegalStateException(String.format(
				"Invalid method call over maps (%s).", mapMethCall
						.getExpression()));
	}

	@Override
	public String getCode(MemberSelect memberSelect) {
		return String.format("%s.%s", memberSelect.getTarget(), memberSelect
				.getIdentifier());
	}

	@Override
	public String getCode(Meth meth) {
		return String.format("%s %s %s (%s)", getCode(meth.getModifiers()), meth
				.getType(), meth.getName(), StringUtils.join(meth.getParams(),
				", "));
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
		return String.format("%s (%s)", methodCall.getTarget(), StringUtils
				.join(methodCall.getParameters(), ", "));
	}

	@Override
	public String getCode(Modifier modifier) {
		return modifier.name().toLowerCase();
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
		return String.format("%s = new %s( %s )", newInstance.getVariable(),
				newInstance.getVariable().getType().toString().replaceAll(
						"\\*$", ""), StringUtils.join(newInstance
						.getParameters(), ", "));
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
		return String.format("return (%s)", returnExpr.getExpression());
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
		return "this";
	}

	@Override
	public String getCode(TypeByte typeByte) {
		return "byte";
	}

	/**
	 * @param typeClass
	 *            the class to be formatted.
	 * @return the formatted class type.
	 */
	@Override
	public String getCode(TypeClass typeClass) {
		String code = "";
		if (typeClass.getPackageName() != null) {
			code += typeClass.getPackageName() + ".";
		}
		code += typeClass.getName();
		return code;
	}

	@Override
	public String getCode(TypeDependency typeDependency) {
		return typeDependency.getType().toString();
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
		return "";
	}

	@Override
	public String getCode(TypeNull typeNull) {
		return "null";
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
		return "void";
	}

	@Override
	public String getCode(UnaryOperation unaryOperation) {
		switch (unaryOperation.getOperator()) {
		case POSTFIX_DECREMENT:
		case POSTFIX_INCREMENT:
			return String.format("(%s)%s%s", unaryOperation.getExpression(), unaryOperation.getTextualoperator(), unaryOperation.getOperator().equals(Operator.UNKNOWN)?"/* Unrecognized by GOOL, passed on */":"");
		default:
			return String.format("%s%s(%s)", unaryOperation.getTextualoperator(), unaryOperation.getOperator().equals(Operator.UNKNOWN)?"/* Unrecognized by GOOL, passed on */":"", unaryOperation.getExpression());
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
		String initialValue = "";
		if (varDec.getInitialValue() != null) {
			initialValue = " = " + varDec.getInitialValue();
		}
		return String.format("%s %s%s", varDec.getType(), varDec.getName(),
				initialValue);
	}
	
	@Override
	public String getCode(VarAccess varAccess) {
		return varAccess.getDec().getName();
	}
	
	@Override
	public String getCode(While whilee) {
		return formatIndented("while (%s) {%1}", whilee.getCondition(), whilee
				.getWhileStatement());
	}
	
	@Override
	public String getCode(TypeArray typeArray) {
		return String.format("%s[]", typeArray.getElementType());
	}
	
	@Override
	public String getCode(ThisCall thisCall) {
		return String.format("this (%s)", GeneratorHelper.joinParams(thisCall.getParameters()));
	}

	@Override
	public String getCode(TypeUnknown typeUnknown) {
		return String.format("%s /* Unrecognized by GOOL, passed on */", typeUnknown.getTextualtype());
	}
	
	@Override
	public String getCode(ExpressionUnknown unknownExpression) {
		return String.format("%s /* Unrecognized by GOOL, passed on */", unknownExpression.getTextual());
	}

	@Override
	public String getCode(ClassFree classFree) {
		return "free /* Not Implemented, passed on by GOOL */";
	}
	
	@Override
	public String getCode(Platform platform) {
		return platform.getName();
	}
	
	@Override
	public String getCode(ClassDef classDef) {
		return String.format("%s.%s", classDef.getPackageName(),classDef.getName());		
	}

	@Override
	public String getCode(Package _package) {
		return _package.getName();
	}
	
	@Override
	public String getCode(TypeFile typeFile){
		return "File";
	}
	
	@Override
	public String getCode(TypeFileReader typeFileReader){
		return "FileReader";
	}
	
	@Override
	public String getCode(TypeBufferedReader tbr) {
		return "BufferedReader";
	}
	
	@Override
	public String getCode(TypePackage typePackage){
		return typePackage.getTextualtype();
	}
	
	@Override
	public String getCode(TypeVar typeVar){
		//For now if one wants to print the type of a TypeVar, this returns just the name of the TypeVar.
		return typeVar.getTextualtype();
	}


	
	@Override
	public String getCode(TypeMethod typeMethod){
		//For now if one wants to print the type of a Method, this returns just the name of the method.
		return typeMethod.getTextualtype();
	}
}
