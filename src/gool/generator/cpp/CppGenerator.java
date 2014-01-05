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

import gool.ast.constructs.BinaryOperation;
import gool.ast.constructs.CastExpression;
import gool.ast.constructs.Catch;
import gool.ast.constructs.ClassDef;
import gool.ast.constructs.ClassNew;
import gool.ast.constructs.Constant;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.Expression;
import gool.ast.constructs.Field;
import gool.ast.constructs.Finally;
import gool.ast.constructs.MainMeth;
import gool.ast.constructs.MemberSelect;
import gool.ast.constructs.Meth;
import gool.ast.constructs.MethCall;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.Operator;
import gool.ast.constructs.ParentCall;
import gool.ast.constructs.RecognizedDependency;
import gool.ast.constructs.ThisCall;
import gool.ast.constructs.Throw;
import gool.ast.constructs.ToStringCall;
import gool.ast.constructs.Try;
import gool.ast.constructs.TypeDependency;
import gool.ast.constructs.VarDeclaration;
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
import gool.ast.type.TypeBool;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeException;
import gool.ast.type.TypeInputStream;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeMatchedGoolClass;
import gool.ast.type.TypeNull;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeScanner;
import gool.ast.type.TypeString;
import gool.generator.GeneratorHelper;
import gool.generator.common.CodeGeneratorNoVelocity;
import gool.generator.common.CommonCodeGenerator;
import gool.generator.common.GeneratorMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class CppGenerator extends CommonCodeGenerator /*implements
		CodeGeneratorNoVelocity*/ {

	private String removePointer(IType type) {
		return removePointer(type.toString());
	}

	private String removePointer(String type) {
		return type.replaceAll("[\\s*]+$", "");
	}

	@Override
	public String getCode(Modifier modifier) {
		if (modifier == Modifier.FINAL) {
			return "const";
		}
		return super.getCode(modifier);
	}

	@Override
	public String getCode(Field field) {
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
				throw new IllegalArgumentException(
						String.format(
								"The field '%s' should be initialized within one of the class constructors.",
								field.getName()));
			}
		}

		return out;
	}

	@Override
	public String getCode(MethCall methodCall) {
		String out = "";
		if (methodCall.getTarget() != null) {
			out = methodCall.getTarget().toString();
			String goolMethod = methodCall.getGoolLibraryMethod();
			if(goolMethod!=null){
				//here, get matched output method name with the GeneratorMatcher
				String methodName=GeneratorMatcher.matchGoolMethod(goolMethod);
				if(methodName!=null)
					out=out.substring(0, out.lastIndexOf("->")+2)+methodName;
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
		return out;
	}

	@Override
	public String getCode(TypeBool typeBool) {
		return "int";
	}

	@Override
	public String getCode(TypeClass typeClass) {
		String pointer = typeClass.isEnum() ? "" : "*";
		return super.getCode(typeClass).replaceAll("\\.", "::") + pointer;
	}

	@Override
	public String getCode(TypeInt typeInt) {
		return "int";
	}

	@Override
	public String getCode(TypeString typeString) {
		return "std::string*";
	}

	@Override
	public String getCode(TypeObject typeObject) {
		return "boost::any";
	}

	@Override
	public String getCode(CastExpression cast) {
		if (cast.getType().equals(cast.getExpression().getType())) {
			return String.format("%s", cast.getExpression());
		} else if (cast.getExpression().getType() == TypeObject.INSTANCE) {
			return String.format("any_cast< %s >( %s )", cast.getType(),
					cast.getExpression());
		} else {
			return String.format("( ( %s )( %s ) )", cast.getType(),
					cast.getExpression());
		}
	}

	@Override
	public String getCode(Constant constant) {
		if (constant.getType().equals(TypeString.INSTANCE)) {
			return String.format("( new std::string ( %s ) )",
					super.getCode(constant));
		} else if (constant.getType().equals(TypeNull.INSTANCE)) {
			return "NULL";
		} else if (constant.getType().equals(TypeBool.INSTANCE)) {
			return String.valueOf(constant.getValue().toString()
					.equalsIgnoreCase("true") ? 1 : 0);
		} else {
			return super.getCode(constant);
		}
	}

	@Override
	public String getCode(TypeList typeList) {
		if (typeList.getElementType() == null) {
			/*
			 * TODO: Avoid elements of different types within the same list.
			 * Currently, the remove method is broken due to the cast made of
			 * every element in the list (the both operands in the comparison
			 * should have the same type, otherwise the C++ compiler throws an
			 * error).
			 */
			return "std::vector<" + TypeObject.INSTANCE + ">*";
		}
		return "std::vector<" + typeList.getElementType().toString() + ">*";
	}

	@Override
	public String getCode(MemberSelect memberSelect) {

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
			memberAccess += " :: "; // A static member access
		} else {
			memberAccess += " -> ";
		}

		return String
				.format("%s%s", memberAccess, memberSelect.getIdentifier());
	}

	@Override
	public String getCode(TypeDecimal typeReal) {
		return "double";
	}

	@Override
	public String getCode(TypeChar typeChar) {
		// TODO Auto-generated method stub
		return "char";
	}

	@Override
	public String getCode(SystemOutPrintCall systemOutPrintCall) {
		Expression toPrint = systemOutPrintCall.getParameters().get(0);
		if (toPrint.getType().equals(TypeString.INSTANCE)) {
			return String.format("std::cout << (%s)->data() << std::endl",
					GeneratorHelper.joinParams(systemOutPrintCall
							.getParameters()));
		} else {
			return String.format("std::cout << (%s) << std::endl",
					GeneratorHelper.joinParams(systemOutPrintCall
							.getParameters()));
		}
	}

	@Override
	public String getCode(ParentCall parentCall) {
		return null;

	}

	public String getCode(SystemOutDependency systemOutDependency) {
		return "iostream";
	}

	@Override
	public String getCode(TypeDependency typeDependency) {
		if (typeDependency.getType() instanceof TypeList) {
			// Use the vector type because it allows random access over
			// lists.
			if (((TypeList) typeDependency.getType()).getElementType() == null) {
				return "vector";
			}
			return "vector"; // TODO what type should be used when it is not a
			// generic list?
		}
		if (typeDependency.getType() instanceof TypeMap) {
			return "map";
		}
		if (typeDependency.getType() instanceof TypeEntry) {
			return "map";
		}
		if (typeDependency.getType() instanceof TypeString) {
			return "string";
		}
		if (typeDependency.getType() instanceof TypeBool) {
			return "noprint";
		}
		if (typeDependency.getType() instanceof TypeInt) {
			return "noprint";
		}
		return removePointer(super.getCode(typeDependency)).concat(".h");
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		return String.format("%s -> size()", mapSizeCall.getExpression());
	}

	@Override
	public String getCode(MapRemoveCall mapRemoveCall) {
		return String.format("%s -> erase(%s)", mapRemoveCall.getExpression(),
				GeneratorHelper.joinParams(mapRemoveCall.getParameters()));
	}

	@Override
	public String getCode(MapPutCall mapPutCall) {
		return String.format("%s -> insert( std::make_pair( %s, %s ) )",
				mapPutCall.getExpression(), mapPutCall.getParameters().get(0),
				mapPutCall.getParameters().get(1));
	}

	@Override
	public String getCode(MapIsEmptyCall mapIsEmptyCall) {
		return String.format("%s -> size() == 0",
				mapIsEmptyCall.getExpression());
	}

	@Override
	public String getCode(BinaryOperation binaryOp) {
		String left = binaryOp.getLeft().toString();
		String right = binaryOp.getRight().toString();

		if (binaryOp.getOperator() == Operator.PLUS
				&& binaryOp.getType().equals(TypeString.INSTANCE)) {
			if (!binaryOp.getLeft().getType().equals(TypeString.INSTANCE)) {
				left = String
						.format("new std::string(boost::lexical_cast<std::string>(%s))",
								left);
			}
			if (!binaryOp.getRight().getType().equals(TypeString.INSTANCE)) {
				right = String
						.format("new std::string(boost::lexical_cast<std::string>(%s))",
								right);
			}
			left = String.format("(%s)", left);
			right = String.format("(%s)", right);

			return String.format("new std::string(%s -> append(* (%s)))", left,
					right);
		}

		return super.getCode(binaryOp);
	}

	@Override
	public String getCode(MapGetIteratorCall mapGetIteratorCall) {
		// TODO Auto-generated method stub
		return mapGetIteratorCall.getClass().toString();
	}

	@Override
	public String getCode(MapGetCall mapGetCall) {
		return String.format("%s -> find( %s ) -> second",
				mapGetCall.getExpression(),
				GeneratorHelper.joinParams(mapGetCall.getParameters()));
	}

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		String expr = mapContainsKeyCall.getExpression().toString();
		return String.format("(%s) -> find(%s) != (%s) -> end()", expr,
				GeneratorHelper.joinParams(mapContainsKeyCall.getParameters()),
				expr);
	}

	@Override
	public String getCode(TypeMap typeMap) {
		/*
		 * Maps with with keys of type Object are not allowed in C++ because
		 * there is not a base type.
		 */
		if (typeMap.getKeyType() == null
				|| typeMap.getKeyType().equals(TypeObject.INSTANCE)) {
			throw new IllegalStateException(
					"The map's key is of type Object, this is not supported in C++.");
		}
		if (typeMap.getElementType() == null) {
			return "std::map<" + typeMap.getKeyType() + ", "
					+ TypeObject.INSTANCE + ">*";
		}
		return "std::map<" + StringUtils.join(typeMap.getTypeArguments(), ", ")
				+ ">*";
	}

	@Override
	public String getCode(EnhancedForLoop enhancedForLoop) {
		VarDeclaration varDec = enhancedForLoop.getVarDec();
		String varName = varDec.getName();
		Expression expression = enhancedForLoop.getExpression();
		String expressionToString = enhancedForLoop.getExpression().toString();
		return String
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
								.getStatements());

	}

	private static Map<String, Dependency> customDependencies = new HashMap<String, Dependency>();

	public String getCode(CustomDependency customDependency) {
		if (!customDependencies.containsKey(customDependency.getName())) {
			throw new IllegalArgumentException(
					String.format(
							"There is no equivalent type in C++ for the GOOL type '%s'.",
							customDependency.getName()));
		}
		return customDependencies.get(customDependency.getName()).toString();
	}

	public void addCustomDependency(String key, Dependency value) {
		customDependencies.put(key, value);
	}

	@Override
	public String getCode(TypeEntry typeEntry) {
		return String.format("std::pair<%s, %s>", typeEntry.getKeyType(),
				typeEntry.getElementType());
	}

	@Override
	public String getCode(MapEntryGetKeyCall mapEntryGetKeyCall) {
		return String.format("%s->first", mapEntryGetKeyCall.getExpression());
	}

	@Override
	public String getCode(MapEntryGetValueCall mapEntryGetValueCall) {
		return String
				.format("%s->second", mapEntryGetValueCall.getExpression());
	}

	@Override
	public String getCode(ThisCall thisCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(EqualsCall equalsCall) {

		return String.format("%s -> equals(%s)", equalsCall.getTarget(),
				StringUtils.join(equalsCall.getParameters(), ", "));
	}

	@Override
	public String getCode(ToStringCall tsc) {
		return String.format("%s -> toString()", tsc.getTarget());
	}

	@Override
	public String getCode(ListContainsCall lcc) {

		return String
				.format("/* ListContainsCall not implemented in C++ at the moment */");
	}

	@Override
	public String getCode(ListGetCall lgc) {
		return String.format("%s->%s(%s)", lgc.getExpression(), "at",
				GeneratorHelper.joinParams(lgc.getParameters()));
	}

	@Override
	public String getCode(ListGetIteratorCall lgic) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(ListIsEmptyCall liec) {
		return String.format("%s -> empty()", liec.getExpression());
	}

	@Override
	public String getCode(ListRemoveAtCall lrc) {
		return String.format("%s -> erase(%s -> begin()+%s)",
				lrc.getExpression(), lrc.getExpression(),
				GeneratorHelper.joinParams(lrc.getParameters()));
	}

	@Override
	public String getCode(ListRemoveCall lrc) {
		/*
		 * for(std::vector<int>::size_type i = 0; i != v->size(); i++) { if (
		 * v->at(i) == 1 ) { v->erase(v->begin()+i); break; } }
		 */
		String expr = lrc.getExpression().toString();
		return String
				.format("for(std::vector<int>::size_type i = 0; i != %s->size(); i++) {"
						+ "if ( boost::any_cast<%s>(%s->at(i)) == %s ) {%s->erase(%s->begin()+i);break;}}",
						expr, lrc.getParameters().get(0).getType(), expr, lrc
								.getParameters().get(0), expr, expr);
	}

	@Override
	public String getCode(ListSizeCall lsc) {
		return String.format("%s -> size()", lsc.getExpression());
	}

	@Override
	public String getCode(ListAddCall lac) {
		return String.format("%s->%s(%s)", lac.getExpression(), "push_back",
				GeneratorHelper.joinParams(lac.getParameters()));
	}

	/**
	 * It is meant to be only used in the CPP file (it does not print the
	 * modifiers).
	 */
	@Override
	public String getCode(Meth meth) {
		List<Modifier> modifiers = new ArrayList<Modifier>(meth.getModifiers());
		// Remove (public, private, protected, final) invalid modifiers.
		modifiers.remove(meth.getAccessModifier());
		modifiers.remove(Modifier.FINAL);

		return String.format("%s %s %s::%s(%s)", getCode(modifiers),
				meth.getType(), meth.getClassDef().getName(), meth.getName(),
				StringUtils.join(meth.getParams(), ", "));
	}

	@Override
	public String getCode(ClassNew classNew) {
		return String.format("(new %s(%s))", removePointer(classNew.getType()),
				StringUtils.join(classNew.getParameters(), ", "));
	}

	@Override
	public String getCode(MainMeth mainMeth) {
		return "int main()";
	}

	@Override
	public String getCode(SystemCommandDependency systemCommandDependency) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public String printClass(ClassDef classDef) {
		StringBuilder sb = new StringBuilder(String.format(
				"// Platform: %s\n\n", classDef.getPlatform()));
		// print the package containing the class
		if (classDef.getPpackage() != null)
			sb = sb.append(String.format("namespace %s {",
					classDef.getPackageName()));
		sb = sb.append("#include <boost/any.hpp>\n");
		sb = sb.append("#include <boost/lexical_cast.hpp>\n");
		sb = sb.append("#include \"finally.h\"\n\n");
		sb = sb.append(String.format("#include \"%s.h\"\n\n",
				classDef.getName()));
		Set<String> dependencies = GeneratorHelper.printDependencies(classDef);
		if (!dependencies.isEmpty()) {
			for (String dependency : dependencies) {
				if (!dependency.equals("noprint"))
					sb = sb.append(String.format("#include \"%s\"\n",
							dependency));
			}
			sb = sb.append("\n");
		}
		for (Meth meth : classDef.getMethods()) {
			// TODO: deal with constructors ?
			if (classDef.isInterface())
				sb = sb.append(formatIndented("%-1%s;\n\n", meth.getHeader()));
			else
				sb = sb.append(formatIndented("%-1%s {%2%-1}\n\n",
						meth.getHeader(), meth.getBlock()));
		}
		if (classDef.getPpackage() != null)
			sb = sb.append("}");
		return sb.toString();
	}

	@Override
	public String getCode(TypeScanner typeScanner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(TypeInputStream typeInputStream) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(Throw throwStatement) {
		return String.format("throw %s", throwStatement.getExpression());
	}

	@Override
	public String getCode(Catch catchStatement) {
		return formatIndented("catch (%s * %s)\n{%1}\n", catchStatement
				.getParameter().getType(), catchStatement.getParameter()
				.getName(), catchStatement.getBlock());
	}

	@Override
	public String getCode(Try tryStatement) {
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
		return ret;
	}

	@Override
	public String getCode(TypeException typeException) {
		switch (typeException.getKind()) {
		case GLOBAL:
			return "exception";
		default:
			return typeException.getName();
		}
	}
	
	public String getCode(RecognizedDependency recognizedDependency) {
		List<String> imports = GeneratorMatcher.matchImports(recognizedDependency.getName());
		if(imports.isEmpty())
			return "/* import "+recognizedDependency.getName()+" not generated by GOOL, passed on. */";
		String result = "";
		for (String Import : imports) {
			if (Import.startsWith("+"))
				Import = Import.substring(1);
			result += "#include \"" + Import.replace(".", "/") + ".h\"\n";
		}

		return result;
	}
	
	@Override
	public String getCode(TypeMatchedGoolClass typeMatchedGoolClass) {
		String res = GeneratorMatcher.matchGoolClass(typeMatchedGoolClass
				.getGoolclassname());
		if (res == null)
			return typeMatchedGoolClass.getGoolclassname()
					+ " /* Ungenerated by GOOL, passed on. */";
		else {
			return res+"*";
		}
	}
}
