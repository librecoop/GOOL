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

package gool.generator.objc;

import gool.ast.core.ArrayAccess;
import gool.ast.core.ArrayNew;
import gool.ast.core.Assign;
import gool.ast.core.BinaryOperation;
import gool.ast.core.Catch;
import gool.ast.core.ClassDef;
import gool.ast.core.ClassNew;
import gool.ast.core.Constructor;
import gool.ast.core.Dependency;
import gool.ast.core.EnhancedForLoop;
import gool.ast.core.EqualsCall;
import gool.ast.core.Expression;
import gool.ast.core.Field;
import gool.ast.core.FieldAccess;
import gool.ast.core.Finally;
import gool.ast.core.Language;
import gool.ast.core.MainMeth;
import gool.ast.core.MemberSelect;
import gool.ast.core.Meth;
import gool.ast.core.MethCall;
import gool.ast.core.Modifier;
import gool.ast.core.Operator;
import gool.ast.core.ParentCall;
import gool.ast.core.RecognizedDependency;
import gool.ast.core.Return;
import gool.ast.core.This;
import gool.ast.core.ThisCall;
import gool.ast.core.Throw;
import gool.ast.core.ToStringCall;
import gool.ast.core.Try;
import gool.ast.core.TypeDependency;
import gool.ast.core.VarAccess;
import gool.ast.core.VarDeclaration;
import gool.ast.list.ListAddCall;
import gool.ast.list.ListContainsCall;
import gool.ast.list.ListGetCall;
import gool.ast.list.ListGetIteratorCall;
import gool.ast.list.ListIsEmptyCall;
import gool.ast.list.ListRemoveAtCall;
import gool.ast.list.ListRemoveCall;
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
import gool.ast.type.TypeMethod;
import gool.ast.type.TypeNone;
import gool.ast.type.TypeNull;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeString;
import gool.generator.GeneratorHelper;
import gool.generator.common.CodeGeneratorNoVelocity;
import gool.generator.common.CommonCodeGenerator;
import gool.generator.common.GeneratorMatcher;
import logger.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class ObjcGenerator extends CommonCodeGenerator implements
CodeGeneratorNoVelocity {

	private String removePointer(IType type) {
		return removePointer(type.toString());
	}

	private String removePointer(String type) {
		return type.replaceAll("[\\s*]+$", "");
	}

	private static Set<String> customDependencies = new HashSet<String>();

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
	public String getCode(EnhancedForLoop enhancedForLoop) {
		return String
				.format("for(%s %s in %s){\n\t%s}",
						enhancedForLoop.getVarDec().getType(),
						enhancedForLoop.getVarDec().getName(),
						(enhancedForLoop.getExpression().getType() instanceof TypeMap) ? String
								.format("%s.entrySet()",
										enhancedForLoop.getExpression())
								: enhancedForLoop.getExpression(),
								enhancedForLoop.getStatements());
	}

	// List Methods
	@Override
	public String getCode(ListAddCall lac) {
		if (lac.getParameters().isEmpty()){
			return String.format("[%s]", lac.getExpression());
		}
		String nsObject = GeneratorHelperObjc.staticString(lac.getParameters()
				.get(0));
		if (lac.getParameters().size() == 1) {
			if (lac.getParameters().get(0).getType() instanceof PrimitiveType
					&& !(lac.getParameters().get(0).getType() instanceof TypeString)) {
				String nsNumber = "[[NSNumber alloc]initWith"
						+ GeneratorHelperObjc.type(lac.getParameters().get(0)
								.getType()) + ":" + lac.getParameters().get(0)
						+ "]";
				return String.format("[%s addObject:%s]", lac.getExpression(),
						nsNumber);
			}
			return String.format("[%s addObject:%s%s]", lac.getExpression(),
					nsObject, lac.getParameters().get(0));
		} else {
			String s = String.format("[%s arrayWithObjects:%s%s]",
					lac.getExpression(), nsObject,
					StringUtils.join(lac.getParameters(), ", "));
			String str[] = new String[lac.getParameters().size()];
			for (int i = 2; i <= lac.getParameters().size(); i++) {
				nsObject = GeneratorHelperObjc.staticString(lac.getParameters()
						.get(0));
				str[i] = (String) String.format("[%s addObject:%s%s]\n",
						lac.getExpression(), nsObject,
						lac.getParameters().get(i));
			}
			return String.format("%s\n%s", s, str);
		}
	}

	@Override
	public String getCode(ListContainsCall lcc) {
		if (lcc.getParameters().isEmpty())
			return String.format("[%s containsObject:]", lcc.getExpression());
		String param0 = GeneratorHelperObjc.initWithObject(lcc.getParameters()
				.get(0));

		return String.format("[%s containsObject:%s]", lcc.getExpression(),
				param0);
	}

	@Override
	public String getCode(ListGetCall lgc) {
		return String.format("[%s objectAtIndex:%s]", lgc.getExpression(), 
				GeneratorHelper.joinParams(lgc.getParameters()));
	}

	@Override
	public String getCode(ListGetIteratorCall lgic) {
		return String.format("[%s objectEnumerator]", lgic.getExpression());
	}

	@Override
	public String getCode(ListIsEmptyCall liec) {
		String s = "( " + String.format("[%s count]", liec.getExpression())
		+ "== 0)";
		return s;
	}

	@Override
	public String getCode(ListRemoveAtCall lrc) {
		if (lrc.getParameters().isEmpty())
			return String.format("[%s removeObjectAtIndex:]", lrc.getExpression());
		return String.format("[%s removeObjectAtIndex:%s]",
				lrc.getExpression(), lrc.getParameters().get(0));
	}

	@Override
	public String getCode(ListRemoveCall lrc) {
		if (lrc.getParameters().isEmpty())
			return String.format("[%s removeObject:]", lrc.getExpression());

		String param0 = GeneratorHelperObjc.initWithObject(lrc.getParameters()
				.get(0));
		return String.format("[%s removeObject:%s]", lrc.getExpression(),
				param0);
	}

	@Override
	public String getCode(ListSizeCall lsc) {
		return String.format("[%s count]", lsc.getExpression());
	}

	// /////////////

	// Map Methods

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		/*
		 * return String.format("[[%s objectForKey:@%s]isEqualToString:@" + "("
		 * + "null" + ")" + "]", mapContainsKeyCall.getExpression(),
		 * mapContainsKeyCall.getParameters());
		 */// TODO
		return " /* MapContainsKeyCall non implemente */ ";
	}

	@Override
	public String getCode(MapEntryGetKeyCall mapEntryGetKeyCall) {
		return String
				.format("[%s allKeys]", mapEntryGetKeyCall.getExpression());
	}

	@Override
	public String getCode(MapEntryGetValueCall mapEntryGetValueCall) {
		return String.format("[%s allValues]",
				mapEntryGetValueCall.getExpression());
	}

	@Override
	public String getCode(MapGetCall mapGetCall) {
		if (mapGetCall.getParameters().isEmpty())
			return String.format("[%s]",
					mapGetCall.getExpression());

		String param0 = GeneratorHelperObjc.initWithObject(mapGetCall
				.getParameters().get(0));

		return String.format("[%s objectForKey:%s]",
				mapGetCall.getExpression(), param0);
	}

	@Override
	public String getCode(MapGetIteratorCall mapGetIteratorCall) {
		return null;
	}

	@Override
	public String getCode(MapIsEmptyCall mapIsEmptyCall) {
		String s = "( "
				+ String.format("[%s count]", mapIsEmptyCall.getExpression())
				+ "== 0)";
		return s;
	}

	@Override
	public String getCode(MapPutCall mapPutCall) {
		if (mapPutCall.getParameters().isEmpty())
			return String.format("[%s]", mapPutCall.getExpression());

		String param0 = GeneratorHelperObjc.initWithObject(mapPutCall
				.getParameters().get(0));

		//		if (mapPutCall.getParameters().size() == 1)
		//			return String.format("[%s setObject:%s]", 
		//					mapPutCall.getExpression(), param0);


		String param1 = GeneratorHelperObjc.initWithObject(mapPutCall
				.getParameters().get(1));

		return String.format("[%s setObject:%s forKey:%s]",
				mapPutCall.getExpression(), param0, param1);
	}

	@Override
	public String getCode(MapRemoveCall mapRemoveCall) {
		String param = GeneratorHelperObjc.initWithObject(mapRemoveCall
				.getParameters().get(0));

		return String.format("[%s removeObjectForKey:%s]",
				mapRemoveCall.getExpression(), param);
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		return String.format("[[(NSArray * )%s allKeys] count]",
				mapSizeCall.getExpression());
		// TODO
	}

	// /////////////

	// Meth Methods

	@Override
	public String getCode(MainMeth mainMeth) {
		return "int main(int argc, const char * argv[])";
	}

	@Override
	public String getCode(ClassNew classNew) {

		String init = new String("init");

		boolean b = false;

		for (Expression e : classNew.getParameters()) {
			String nsString = GeneratorHelperObjc.staticStringMini(e);
			if (!b) {
				init += "With";
				init += String.format("%s:%s%s ", removePointer(e.getType()),
						nsString, e.toString());
				b = true;
			} else {
				init += "With";
				init += String.format("and%s:%s%s ",
						removePointer(e.getType()), nsString, e.toString());
			}
		}
		return String.format("[[%s alloc]%s]",
				removePointer(classNew.getType()), init);
	}

	// retourne l'expression appellant la méthode
	private Expression getTarget(Object n) {
		if (n instanceof MemberSelect) {
			return ((MemberSelect) n).getTarget();
		} else if (n instanceof MethCall) {
			return ((MethCall) n).getTarget();
		} else if (n instanceof FieldAccess) {
			return ((FieldAccess) n).getTarget();
		} else if (n instanceof EqualsCall) {
			return ((EqualsCall) n).getTarget();
		} else
			return null;
	}

	// génère la liste des paramètres pour l'appel d'une méthode
	private String getMethCallName(List<Expression> param,
			boolean typeFirstParam) {
		String arg = "";
		String p;
		String nsString;
		boolean b = false;

		for (Expression e : param) {
			nsString = GeneratorHelperObjc.staticStringMini(e);
			if (e.getType() == TypeChar.INSTANCE)
				p = "'" + e.toString() + "'";
			else
				p = e.toString();

			if (!b) {
				if (typeFirstParam)
					arg += String.format("%s:%s%s ",
							removePointer(e.getType()), nsString, p);
				else
					arg += String.format("%s%s ", nsString, p);

				b = true;
			} else
				arg += String.format("and%s:%s%s ", removePointer(e.getType()),
						nsString, p);
		}

		return arg;
	}

	// Génère la liste des paramètres pour la définition d'une méthode
	private String getMethDefName(List<VarDeclaration> param) {
		String arg = "";
		boolean b = false;

		for (VarDeclaration e : param) {
			if (!b) {
				arg += String.format("%s:(%s)%s ", removePointer(e.getType()),
						e.getType(), e.getName());
				b = true;
			} else
				arg += String.format("and%s:(%s)%s ",
						removePointer(e.getType()), e.getType(), e.getName());
		}

		return arg;
	}

	private String methUnknow(String generalName, String library) {
		return String
				.format("/* La méthode %s de la bibliothèque %s n'est pas implémenté pour le langage */",
						generalName.replaceAll("\\s", ""), library);
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
		String arg = new String();
		String out = "";
		if (methodCall.getTarget() != null) {
			out = methodCall.getTarget().toString();
			String goolMethod = methodCall.getGoolLibraryMethod();
			if(goolMethod!=null){
				//here, get matched output method name with the GeneratorMatcher
				out = GeneratorMatcher.matchGoolMethod(goolMethod);
				//then check for a c function
				String outtype = GeneratorMatcher.matchGoolClass(goolMethod.substring(0,goolMethod.lastIndexOf(".")));
				Log.d(String.format("<ObjcGenerator - getCode(MethCall> out = %s | goolMethod = %s | outtype = %s", out, goolMethod, outtype));
				if(outtype.equalsIgnoreCase("c")){
					out += "(";
					if (methodCall.getParameters() != null) {
						out += StringUtils.join(methodCall.getParameters(), ", ");
					}
					out += ")";
					return out;
				}
			}

			arg = getMethCallName(methodCall.getParameters(), true);

			if (methodCall.getTarget() instanceof VarAccess)
				return String.format("[%s%s]", out, arg);

			if (methodCall.getTarget() instanceof ParentCall)
				return getCode((ParentCall) methodCall.getTarget());
			return String.format("[%s%s]",out, arg);
		}
		return out;

	}

	@Override
	public String getCode(Meth meth) {
		String ret = new String();
		String arg = new String();
		String mod = (meth.getModifiers().contains(Modifier.STATIC) ? "+" : "-");

		if (!(meth.getType() instanceof TypeNone))
			ret = String.format("(%s)", meth.getType().toString());
		else
			ret = "(void)";

		arg = getMethDefName(meth.getParams());

		return String.format("%s %s %s%s", mod, ret, meth.getName(), arg);
	}

	@Override
	public String getCode(Constructor cons) {
		String param = new String();

		param = getMethDefName(cons.getParams());
		if(param == "")
			return String.format("- (%s *)init", cons.getClassDef().getName());
		return String.format("- (%s *)initWith%s", cons.getClassDef().getName(), param);
	}

	@Override
	public String getCode(SystemOutPrintCall systemOutPrintCall) {
		String nsString = GeneratorHelperObjc.staticString(systemOutPrintCall
				.getParameters().get(0));

		String out = null;
		String format = null;

		if (systemOutPrintCall.getParameters().get(0).getType() instanceof TypeClass)
			out = "[" + systemOutPrintCall.getParameters().get(0)
			+ " toString]";

		format = (systemOutPrintCall.getParameters().get(0) instanceof ArrayAccess)
				&& ((ArrayAccess) systemOutPrintCall.getParameters().get(0))
				.getExpression().getType() instanceof TypeArray ? GeneratorHelperObjc
						.format(((TypeArray) ((ArrayAccess) systemOutPrintCall
								.getParameters().get(0)).getExpression().getType())
								.getElementType()) : GeneratorHelperObjc
						.format(systemOutPrintCall.getParameters().get(0));

						// I added the two following lines to fix a bug dealing with
						// non-string parameters in NSLog calls.
						if (!format.equals("%@"))
							nsString = "";

						return String.format(
								"NSLog(@\"%s\",%s%s)",
								format,
								nsString,
								out == null ? GeneratorHelper.joinParams(systemOutPrintCall
										.getParameters()) : out);
	}

	@Override
	public String getCode(ToStringCall tsc) {
		return String.format("[%s toString]", tsc.getTarget());
	}

	@Override
	public String getCode(EqualsCall equalsCall) {
		String arg = getMethCallName(equalsCall.getParameters(), false);
		return String.format("[%s isEqual: %s]", equalsCall.getTarget(), arg);
	}

	@Override
	public String getCode(ParentCall parentCall) {
		String out = "self = [super init";

		out += getMethCallName(parentCall.getParameters(), true);

		out += "]";
		return out;
	}

	@Override
	public String getCode(ThisCall thisCall) {
		return "self()";
	}

	@Override
	public String getCode(Return returnExpr) {
		String nsString = GeneratorHelperObjc.staticString(returnExpr
				.getExpression());
		return String.format("return (%s%s)", nsString,
				returnExpr.getExpression());
	}

	// /////////////

	// Type Methods

	@Override
	public String getCode(TypeBool typeBool) {
		return "BOOL";
	}

	@Override
	public String getCode(TypeDecimal typeReal) {
		return "double";
	}

	@Override
	public String getCode(TypeEntry typeEntry) {
		return String.format("[dictionary setObject:@%s forKey:@%s]",
				typeEntry.getElementType(), typeEntry.getKeyType());
	}

	public String getCode(TypeObject typeObject) {
		// type java.object
		// return NSObject
		return "id";
	}

	@Override
	public String getCode(TypeInt typeInt) {
		return "int";
	}

	@Override
	public String getCode(TypeArray typeArray) {
		return String.format("%s", typeArray.getElementType());
	}

	@Override
	public String getCode(TypeList typeList) {
		return String.format("NSMutableArray *");
	}

	@Override
	public String getCode(TypeMap typeMap) {
		return "NSMutableDictionary *";
	}

	@Override
	public String getCode(TypeString typeString) {
		return "NSMutableString *";
	}

	@Override
	public String getCode(TypeChar typeChar) {
		return "char";
	}


	@Override
	public String getCode(This pthis) {
		return "self";
	}

	@Override
	public String getCode(TypeNull typeNull) {
		return "nil";
	}

	@Override
	public String getCode(TypeClass typeClass) {
		String pointer = typeClass.isEnum() ? "" : "*";
		return super.getCode(typeClass) + pointer;
	}

	@Override
	public String getCode(TypeDependency typeDependency) {
		if (typeDependency.getType() instanceof TypeList) {
			return "Foundation/Foundation.h";
		}
		if (typeDependency.getType() instanceof TypeMap) {
			return "Foundation/Foundation.h";
		}
		if (typeDependency.getType() instanceof TypeEntry) {
			return "Foundation/Foundation.h";
		}
		if (typeDependency.getType() instanceof TypeString) {
			return "Foundation/Foundation.h";
		}
		if (typeDependency.getType() instanceof TypeBool) {
			return "noprint";
		}
		if (typeDependency.getType() instanceof TypeInt) {
			return "noprint";
		}
		if (typeDependency.getType() instanceof TypeException) {
			return "Foundation/Foundation.h";
		}
		return removePointer(super.getCode(typeDependency)).concat(".h");
	}

	// /////////////

	// Dependency methods

	@Override
	public String getCode(SystemOutDependency systemOutDependency) {
		return "Foundation/Foundation.h";
	}

	@Override
	public String getCode(SystemCommandDependency systemCommandDependency) {
		// TODO Auto-generated method stub
		return null;
	}

	// /////////////

	@Override
	public String getCode(Assign assign) {
		return assign.getLValue() + " = "
				+ GeneratorHelperObjc.staticStringMini(assign.getValue())
				+ assign.getValue();
	}

	@Override
	public String getCode(VarDeclaration varDec) {

		// TODO Si une variable se nomme id .... fail

		String initialValue = "";
		String type = varDec.getType().toString();
		if (varDec.getInitialValue() != null) {
			if (varDec.getInitialValue().getType() instanceof TypeNull)
				initialValue = " = nil"; // TODO pas normal, nil est mit dans le
			// type de la valeur initital et pas
			// dans la valeur initiale
			else if (varDec.getInitialValue().getType() instanceof TypeString
					&& !(varDec.getInitialValue() instanceof MethCall))
				initialValue = " = @" + varDec.getInitialValue();
			else if (varDec.getInitialValue().getType() instanceof TypeChar
					&& !(varDec.getInitialValue() instanceof MethCall))
				initialValue = " = " + varDec.getInitialValue();
			else
				initialValue = " = " + varDec.getInitialValue();
		}

		if (varDec.getType() instanceof TypeArray)
			return String.format("%s %s[%s]", type, varDec.getName(),
					((ArrayNew) varDec.getInitialValue()).getDimesExpressions()
					.get(0));

		return String.format("%s %s%s", type, varDec.getName(), initialValue);

	}

	@Override
	public String getCode(BinaryOperation binaryOp) {
		String left = binaryOp.getLeft().toString();
		String right = binaryOp.getRight().toString();

		if (binaryOp.getOperator() == Operator.PLUS
				&& binaryOp.getType().equals(TypeString.INSTANCE)) {

			String nsStringLeft = GeneratorHelperObjc.staticString(binaryOp
					.getLeft());
			String nsStringRight = GeneratorHelperObjc.staticString(binaryOp
					.getRight());

			if (binaryOp.getLeft().getType() instanceof TypeClass)
				left = "[" + left + " toString]";
			if (binaryOp.getRight().getType() instanceof TypeClass)
				right = "[" + right + " toString]";

			String fleft = (binaryOp.getLeft() instanceof ArrayAccess)
					&& ((ArrayAccess) binaryOp.getLeft()).getExpression()
					.getType() instanceof TypeArray ? GeneratorHelperObjc
							.format(((TypeArray) ((ArrayAccess) binaryOp.getLeft())
									.getExpression().getType()).getElementType())
							: GeneratorHelperObjc.format(binaryOp.getLeft());
							String fright = (binaryOp.getRight() instanceof ArrayAccess)
									&& ((ArrayAccess) binaryOp.getRight()).getExpression()
									.getType() instanceof TypeArray ? GeneratorHelperObjc
											.format(((TypeArray) ((ArrayAccess) binaryOp.getRight())
													.getExpression().getType()).getElementType())
											: GeneratorHelperObjc.format(binaryOp.getRight());

											return String.format(
													"[NSString stringWithFormat:@\"%s%s\",%s%s,%s%s]", fleft,
													fright, nsStringLeft, left, nsStringRight, right);
		}
		if (binaryOp.getOperator() == Operator.REMAINDER
				&& ((binaryOp.getLeft().getType() instanceof TypeDecimal) || 
						(binaryOp.getRight().getType() instanceof TypeDecimal))) 
		{
			return String.format("fmod(%s,%s)\n/* Add -lm as compilation option */", left,	right);
		}

		return super.getCode(binaryOp);
	}

	@Override
	public String getCode(MemberSelect memberSelect) {
		String target = memberSelect.getTarget().toString().equals("this") ? "self"
				: memberSelect.getTarget().toString();
		String sep;

		if (memberSelect.getType() instanceof TypeMethod)
			sep = " ";
		else
			sep = "->";

		return String.format("%s%s%s", target, sep,
				memberSelect.getIdentifier());
	}

	@Override
	public String getCode(Modifier modifier) {
		switch (modifier) {
		case ABSTRACT:
			return "";
		case FINAL:
			return "const";
		case PRIVATE:
			return "@private";
		case PROTECTED:
			return "@protected";
		case PUBLIC:
			return "@public";
		case STATIC:
			return "+";
		default:
			return super.getCode(modifier);
		}
	}

	@Override
	public String getCode(Field field) {
		String out = String.format("%s %s", field.getType(), field.getName());
		if (field.getDefaultValue() != null) {
			out = String.format("%s = %s", out, field.getDefaultValue());
		}
		if (field.getType().toString().equals("noprint"))
			return "";
		return out;
	}

	@Override
	public String getCode(TypeException typeException) {
		return "NSException *";
	}

	@Override
	public String getCode(Catch catchBlock) {
		return "@catch";
	}

	@Override
	public String getCode(Try tryBlock) {
		return "@try ";
	}

	@Override
	public String getCode(Throw throwexpression) {
		return "@throw";
	}

	public String getCode(RecognizedDependency recognizedDependency) {
		List<String> imports = GeneratorMatcher.matchImports(recognizedDependency.getName());
		if(imports == null)
			return "";
		if(imports.isEmpty())
			return "/* import "+recognizedDependency.getName()+" not generated by GOOL, passed on. */";
		String result = "";
		for (String Import : imports) {
			if (Import.startsWith("+"))
				Import = Import.substring(1);
			result += "#import <" + Import.replace(".", "/") + ".h>\n";
		}

		return result;
	}

	public String getDependenciesCode(ClassDef cl){
		String res = "";		
		Set<String> dependencies = GeneratorHelper.printDependencies(cl);
		dependencies.addAll(getCustomDependencies());
		dependencies.add("Foundation/Foundation.h");
		clearCustomDependencies();
		String recogDependencies = GeneratorHelper.printRecognizedDependencies(cl);
		if (!dependencies.isEmpty()) {
			for (String dependency : dependencies){
				if (dependency != "noprint"){
					String incdep = String.format("#import \"%s\"", dependency);
					if (recogDependencies.indexOf(incdep) == -1)
						res += incdep + "\n";
				}
			}
			res += "\n";
		}
		res += recogDependencies + "\n\n";
		res += String.format("#import \"%s.h\"\n", cl.getName());
		return res;
	}

	//@Override
	public String printClass(ClassDef classDef) {
		Log.MethodIn(Thread.currentThread());

		String header = "";
		//String.format("// Platform: %s\n\n", classDef.getPlatform());


		String body = "@implementation " + classDef.getName() + "\n\n";
		boolean containsToString = false;
		for (Meth meth : classDef.getMethods()) {
			if (meth.isGoolMethodImplementation()){
				body += meth.callGetCode();
			}
			else if(!meth.isMainMethod() && !meth.isAbstract()){
				body += meth.getHeader();
				if (classDef.isInterface()){
					body += ";\n";
				}
				else{
					body += "{\n";
					body += meth.getBlock().callGetCode();
					if (meth.isConstructor())
						body += "return self;\n}\n\n";
				}
			}
			if (meth.getName() == "toString")
				containsToString = true;
		}
		if(!containsToString){
			body += "-(NSString *)toString{\nreturn @\"" + classDef.getName() +
					"\";\n}\n\n";
		}
		body += "@end\n\n";

		for (Meth meth : classDef.getMethods()) {
			if(meth.isMainMethod()){
				body += meth.getHeader();
				body += "{\n";
				body += meth.getBlock().callGetCode();
				body += "\n";
				body += "return 0;\n}";
			}
		}

		return (String)Log.MethodOut(Thread.currentThread(),
				header + "\n" + getDependenciesCode(classDef) + "\n" + body);
	}


}
