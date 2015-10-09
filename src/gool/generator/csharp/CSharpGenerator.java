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

package gool.generator.csharp;

import gool.ast.core.BinaryOperation;
import gool.ast.core.Catch;
import gool.ast.core.ClassDef;
import gool.ast.core.ClassNew;
import gool.ast.core.Constant;
import gool.ast.core.Constructor;
import gool.ast.core.CustomDependency;
import gool.ast.core.Dependency;
import gool.ast.core.EnhancedForLoop;
import gool.ast.core.EqualsCall;
import gool.ast.core.Expression;
import gool.ast.core.Field;
import gool.ast.core.Finally;
import gool.ast.core.MainMeth;
import gool.ast.core.MemberSelect;
import gool.ast.core.Meth;
import gool.ast.core.MethCall;
import gool.ast.core.Modifier;
import gool.ast.core.Operator;
import gool.ast.core.Package;
import gool.ast.core.ParentCall;
import gool.ast.core.RecognizedDependency;
import gool.ast.core.StringIsEmptyCall;
import gool.ast.core.Throw;
import gool.ast.core.ToStringCall;
import gool.ast.core.Try;
import gool.ast.core.TypeDependency;
import gool.ast.list.ListAddCall;
import gool.ast.list.ListClearCall;
import gool.ast.list.ListContainsCall;
import gool.ast.list.ListGetCall;
import gool.ast.list.ListGetIteratorCall;
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
import gool.ast.type.TypeBool;
import gool.ast.type.TypeByte;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeException;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeString;
import gool.ast.type.TypeVoid;
import gool.generator.GeneratorHelper;
import gool.generator.common.CodeGeneratorNoVelocity;
import gool.generator.common.CommonCodeGenerator;
import gool.generator.common.GeneratorMatcher;
import logger.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * It generates specific C# code for certain GOOL nodes.
 */
public class CSharpGenerator extends CommonCodeGenerator /* implements
		CodeGeneratorNoVelocity */{
	@Override
	public String getCode(TypeBool typeBool) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "bool");
	}

	@Override
	public String getCode(BinaryOperation binaryOp) {
		Log.MethodIn(Thread.currentThread());
		if (!(binaryOp.getLeft() instanceof Constant)
				&& binaryOp.getOperator() == Operator.EQUAL) {
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("%s.Equals(%s)", binaryOp.getLeft(),
					binaryOp.getRight()));
		} else {
			return (String)Log.MethodOut(Thread.currentThread(), 
					super.getCode(binaryOp));
		}
	}

	@Override
	public String getCode(TypeInt typeInt) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "int");
	}

	@Override
	public String getCode(TypeByte t) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "byte");
	}

	@Override
	public String getCode(TypeChar typeChar) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "char");
	}

	/**
	 * Produces code for an object instantiation.
	 * 
	 * @param classNew
	 *            the object instantiation node.
	 * @return the formatted object instantiation.
	 */
	@Override
	public String getCode(ClassNew classNew) {
		Log.MethodIn(Thread.currentThread());
		if (classNew.getType() instanceof TypeString){
			if(classNew.getParameters().isEmpty())
				return (String)Log.MethodOut(Thread.currentThread(), "\"\"");
			return (String)Log.MethodOut(Thread.currentThread(),
					String.format("%s", GeneratorHelper.joinParams(classNew.getParameters())));
		}
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("new %s( %s )", classNew.getType(),
						GeneratorHelper.joinParams(classNew.getParameters())));
	}

	@Override
	public String getCode(TypeString typeString) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "string");
	}

	@Override
	public String getCode(TypeVoid typeVoid) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "void");
	}

	@Override
	public String getCode(ListAddCall lac) {
		Log.MethodIn(Thread.currentThread());
		String method = lac.getParameters().size() > 1 ? "Insert" : "Add";
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.%s(%s)", lac.getExpression(), method,
				GeneratorHelper.joinParams(lac.getParameters())));
	}

	@Override
	public String getCode(ListRemoveCall lrc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.Remove(%s)", lrc.getExpression(),
				GeneratorHelper.joinParams(lrc.getParameters())));
	}

	@Override
	public String getCode(ListRemoveAtCall lrc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.RemoveAt(%s)", lrc.getExpression(),
				GeneratorHelper.joinParams(lrc.getParameters())));
	}

	@Override
	public String getCode(ListContainsCall lcc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.Contains(%s)", lcc.getExpression(),
				GeneratorHelper.joinParams(lcc.getParameters())));
	}

	@Override
	public String getCode(ListGetCall lgc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s[%s]", lgc.getExpression(),
				GeneratorHelper.joinParams(lgc.getParameters())));
	}

	@Override
	public String getCode(ListSizeCall lsc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.Count", lsc.getExpression()));
	}

	@Override
	public String getCode(ListIsEmptyCall liec) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.Count == 0", liec.getExpression()));
	}

	@Override
	public String getCode(ListGetIteratorCall lgic) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.GetIterator()", lgic.getExpression()));
	}

	@Override
	public String getCode(ListClearCall lcc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s.Clear()", lcc.getExpression()));
	}
	
	@Override
	public String getCode(ListSetCall lsc) {
		Log.MethodIn(Thread.currentThread());
		List<Expression> param = lsc.getParameters();
		if (param.size() == 0){
			return (String)Log.MethodOut(Thread.currentThread(),
					String.format("%s[]", lsc.getExpression()));
		}else if (param.size() == 1){
			return (String)Log.MethodOut(Thread.currentThread(),
					String.format("%s[%s]", lsc.getExpression(),
							param.get(0)));
		}
		return (String)Log.MethodOut(Thread.currentThread(),
				String.format("%s[%s] = %s", lsc.getExpression(),param.get(0),
						param.get(1), StringUtils.join(param.subList(2, param.size()), ", ")));
	}
	
	@Override
	public String getCode(TypeDependency typeDependency) {
		Log.MethodIn(Thread.currentThread());
		if (typeDependency.getType() instanceof TypeList) {
			if (((TypeList) typeDependency.getType()).getElementType() == null) {
				return (String)Log.MethodOut(Thread.currentThread(), "System.Collections");
			}
			return (String)Log.MethodOut(Thread.currentThread(), "System.Collections.Generic");
		}
		if (typeDependency.getType() instanceof TypeMap) {
			return (String)Log.MethodOut(Thread.currentThread(), "System.Collections.Generic");
		}
		if (typeDependency.getType() instanceof TypeEntry) {
			return (String)Log.MethodOut(Thread.currentThread(), "System.Collections.Generic");
		}
		if (typeDependency.getType() instanceof TypeString) {
			return (String)Log.MethodOut(Thread.currentThread(), "noprint");
		}
		if (typeDependency.getType() instanceof TypeBool) {
			return (String)Log.MethodOut(Thread.currentThread(), "noprint");
		}
		if (typeDependency.getType() instanceof TypeInt) {
			return (String)Log.MethodOut(Thread.currentThread(), "noprint");
		}
		if (typeDependency.getType() instanceof TypeClass) {
			return (String)Log.MethodOut(Thread.currentThread(), "noprint");
		}
		return (String)Log.MethodOut(Thread.currentThread(), super.getCode(typeDependency));
	}

	@Override
	public String getCode(ClassDef classDef) {
		Log.MethodIn(Thread.currentThread());
		if (classDef.getPpackage() == null) {
			return (String)Log.MethodOut(Thread.currentThread(), "noprint");
		}
		return (String)Log.MethodOut(Thread.currentThread(), classDef.getPackageName());
	}

	@Override
	public String getCode(TypeObject typeObject) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "object");
	}

	@Override
	public String getCode(TypeList typeList) {
		Log.MethodIn(Thread.currentThread());
		if (typeList.getElementType() == null) {
			return (String)Log.MethodOut(Thread.currentThread(), "ArrayList");
		}
		return (String)Log.MethodOut(Thread.currentThread(), 
				"List<" + typeList.getElementType().toString() + ">");
	}

	@Override
	public String getCode(TypeDecimal typeReal) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), "double");
	}

	@Override
	public String getCode(SystemOutPrintCall systemOutPrintCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("Console.WriteLine(%s)",
				GeneratorHelper.joinParams(systemOutPrintCall.getParameters())));
	}

	@Override
	public String getCode(ParentCall parentCall) {
		Log.MethodIn(Thread.currentThread());
		String out = "base(";
		if (parentCall.getParameters() != null) {
			out += GeneratorHelper.joinParams(parentCall.getParameters());
		}
		out += ")";
		return (String)Log.MethodOut(Thread.currentThread(), out);
	}

	@Override
	public String getCode(SystemOutDependency systemOutDependency) {
		Log.MethodIn(Thread.currentThread());
		Log.d("<CSharpGenerator - getCode(SystemOutDependency)> return System");
		return (String)Log.MethodOut(Thread.currentThread(), "System");
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
		String out = "";
		if (methodCall.getTarget() != null) {
			out = methodCall.getTarget().toString();
			String goolMethod = methodCall.getGoolLibraryMethod();
			if(goolMethod!=null){
				Log.d(String.format("<CSharpGenerator - getCode(MethCall> out = %s | goolMethod = %s ", out, goolMethod));
				//here, get matched output method name with the GeneratorMatcher
				out = GeneratorMatcher.matchGoolMethod(goolMethod);
			}
		}
		if (methodCall.getTarget() instanceof StringIsEmptyCall)
			return (String)Log.MethodOut(Thread.currentThread(), out);
		if (methodCall.getTarget().toString().endsWith("Length"))
			return (String)Log.MethodOut(Thread.currentThread(), out);
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
	public String getCode(MemberSelect memberSelect) {
		Log.MethodIn(Thread.currentThread());
		if (memberSelect.getIdentifier().equals("length"))			
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("%s.Length", memberSelect.getTarget()));
		return (String)Log.MethodOut(Thread.currentThread(), 
				super.getCode(memberSelect));
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
		String out = "String.IsNullOrEmpty(" + lmc.getTarget().toString() + ")";
		return (String)Log.MethodOut(Thread.currentThread(), out);
	}
	
	@Override
	public String getCode(Meth meth) {
		Log.MethodIn(Thread.currentThread());

		// #join ( $method.Modifiers " ") #if($method.isInherited())override#end
		// $method.Type $method.Name ( #params( $method.getParams() ) )

		String name = replaceMethodName(meth, meth.getName());

		customizeModifiers(meth);

		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s %s %s(%s)", getCode(meth.getModifiers()),
				meth.getType(), name,
				GeneratorHelper.joinParams(meth.getParams())));
	}

	private void customizeModifiers(Meth meth) {
		if (!meth.getModifiers().contains(Modifier.ABSTRACT)
				&& !meth.getModifiers().contains(Modifier.STATIC)
				&& !meth.isConstructor()) {
			if (meth.isInherited()) {
				meth.getModifiers().add(Modifier.OVERRIDE);
			} else if (!meth.getClassDef().isInterface()
					&& meth.getModifiers().contains(Modifier.PUBLIC)) {
				meth.getModifiers().add(Modifier.VIRTUAL);
			}
		}
	}

	/*
	 * Override properly the ToString, Equals, GetHashCode method in C#
	 */
	private String replaceMethodName(Meth meth, String name) {
		if (meth.getModifiers().contains(Modifier.PUBLIC) && meth.isInherited()) {
			if (name.equals("toString") && meth.getParams().isEmpty()) {
				return "ToString";
			} else if (name.equals("equals")
					&& meth.getParams().size() == 1
					&& meth.getParams().get(0).getType()
							.equals(TypeObject.INSTANCE)) {
				return "Equals";
			} else if (name.equals("hashCode") && meth.getParams().isEmpty()) {
				return "GetHashCode";
			}
		}
		return name;
	}

	@Override
	public String getCode(Modifier modifier) {
		Log.MethodIn(Thread.currentThread());
		if (modifier == Modifier.FINAL) {
			return (String)Log.MethodOut(Thread.currentThread(), "");
		}
		return (String)Log.MethodOut(Thread.currentThread(), super.getCode(modifier));
	}

	@Override
	public String getCode(MainMeth mainMeth) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				"public static void Main(string[] args)");
	}

	@Override
	public String getCode(Constructor cons) {
		Log.MethodIn(Thread.currentThread());
		// #if($method.isConstructor())
		// #if(!$method.ParentCalls.isEmpty()):#end
		// #foreach( $parentCall in $method.ParentCalls )
		// $parentCall
		// #end
		// #end
		return (String)Log.MethodOut(Thread.currentThread(), super.getCode(cons)
				+ ((cons.getInitCalls().size() > 0) ? " : "
						+ StringUtils.join(cons.getInitCalls(), " ") : ""));
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.Count", mapSizeCall.getExpression()));
	}

	@Override
	public String getCode(MapRemoveCall mapRemoveCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.Remove(%s)", mapRemoveCall.getExpression(),
				GeneratorHelper.joinParams(mapRemoveCall.getParameters())));
	}

	@Override
	public String getCode(MapPutCall mapPutCall) {
		Log.MethodIn(Thread.currentThread());
		if (mapPutCall.getParameters().isEmpty())
			return (String)Log.MethodOut(Thread.currentThread(), 
					String.format("%s[] =", mapPutCall.getExpression()));
//		if (mapPutCall.getParameters().size() == 1)
//			return String.format("%s[%s] =", mapPutCall.getExpression(), 
//					mapPutCall.getParameters().get(0));
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s[%s] = %s", mapPutCall.getExpression(),
				mapPutCall.getParameters().get(0), mapPutCall.getParameters()
						.get(1)));
	}

	@Override
	public String getCode(MapIsEmptyCall mapIsEmptyCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("(%s.Count == 0)", mapIsEmptyCall.getExpression()));
	}

	@Override
	public String getCode(MapGetIteratorCall mapGetIteratorCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("(%s.GetIterator() == 0)",
				mapGetIteratorCall.getExpression()));
	}

	@Override
	public String getCode(MapGetCall mapGetCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s[%s]", mapGetCall.getExpression(),
				GeneratorHelper.joinParams(mapGetCall.getParameters())));
	}

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.ContainsKey(%s)",
				mapContainsKeyCall.getExpression(),
				GeneratorHelper.joinParams(mapContainsKeyCall.getParameters())));
	}

	@Override
	public String getCode(TypeMap typeMap) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("Dictionary<%s, %s>", typeMap.getKeyType(),
				typeMap.getElementType()));
	}

	@Override
	public String getCode(EnhancedForLoop enhancedForLoop) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("foreach(%s in %s){%s}",
				enhancedForLoop.getVarDec(), enhancedForLoop.getExpression(),
				enhancedForLoop.getStatements()));
	}

	private static Map<String, Dependency> customDependencies = new HashMap<String, Dependency>();
	
	@Override
	public String getCode(CustomDependency customDependency) {
		Log.MethodIn(Thread.currentThread());
		if (!customDependencies.containsKey(customDependency.getName())) {
			throw new IllegalArgumentException(
					String.format(
							"There is no equivalent type in C# for the GOOL type '%s'.",
							customDependency.getName()));
		}

		Dependency dependency = customDependencies.get(customDependency
				.getName());

		if (dependency instanceof ClassDef) { // It is already a package.
			// Return only the package. C# does not support individual class
			// importation.
			return (String)Log.MethodOut(Thread.currentThread(), 
					((ClassDef) dependency).getPackageName());
		}

		return (String)Log.MethodOut(Thread.currentThread(), dependency.toString());

	}

	public void addCustomDependency(String key, Dependency value) {
		customDependencies.put(key, value);
	}

	@Override
	public String getCode(TypeEntry typeEntry) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("KeyValuePair<%s, %s>", typeEntry.getKeyType(),
				typeEntry.getElementType()));
	}

	@Override
	public String getCode(MapEntryGetKeyCall mapEntryGetKeyCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.Key", mapEntryGetKeyCall.getExpression()));
	}

	@Override
	public String getCode(MapEntryGetValueCall mapEntryGetKeyCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.Value", mapEntryGetKeyCall.getExpression()));
	}

	@Override
	public String getCode(EqualsCall equalsCall) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.Equals(%s)", equalsCall.getTarget(),
				GeneratorHelper.joinParams(equalsCall.getParameters())));
	}

	@Override
	public String getCode(ToStringCall tsc) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), 
				String.format("%s.ToString()", tsc.getTarget()));
	}

	@Override
	public String getCode(Package _package) {
		Log.MethodIn(Thread.currentThread());
		return (String)Log.MethodOut(Thread.currentThread(), _package.getName());
	}

	@Override
	public String getCode(SystemCommandDependency systemCommandDependency) {
		Log.MethodIn(Thread.currentThread());
		// TODO Auto-generated method stub
		return (String)Log.MethodOut(Thread.currentThread(), null);
	}

	//@Override
	public String printClass(ClassDef classDef) {
		StringBuilder sb = new StringBuilder(String.format(
				"// Platform: %s\n\n", classDef.getPlatform()));

		// BUG: yield a stack overflow
		Set<String> dependencies = GeneratorHelper.printDependencies(classDef);
		if (!dependencies.isEmpty()) {
			for (String dependency : dependencies)
				sb = sb.append(String.format("using %s;\n", dependency));
			sb = sb.append("\n");
		}
		
		sb = sb.append(GeneratorHelper.printRecognizedDependencies(classDef));
		
		if (classDef.getPpackage() != null)
			sb = sb.append(String.format("namespace %s;\n\n",
					classDef.getPackageName()));

		// print the class prototype
		sb = sb.append(String.format("%s %s %s",
				StringUtils.join(classDef.getModifiers(), ' '),
				classDef.isInterface() ? "interface" : "class",
				classDef.getName()));
		if (classDef.getParentClass() != null)
			sb = sb.append(String.format(" : %s", classDef.getParentClass()));
		if (!classDef.getInterfaces().isEmpty())
			sb = sb.append(String.format(" : %s",
					StringUtils.join(classDef.getInterfaces(), ", ")));
		sb = sb.append(" {\n\n");
		// print the fields
		for (Field field : classDef.getFields())
			sb = sb.append(formatIndented("%-1%s;\n\n", field));
		// print the methods
		for (Meth meth : classDef.getMethods()) {
			// TODO: deal with constructors ?
			if (classDef.isInterface())
				sb = sb.append(formatIndented("%-1%s;\n\n", meth.getHeader()));
			else
				sb = sb.append(formatIndented("%-1%s {%2%-1}\n\n",
						meth.getHeader(), meth.getBlock()));
		}

		return sb.toString() + "}";
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
				formatIndented("catch (%s %s)\n{%1}\n", catchStatement
				.getParameter().getType(), catchStatement.getParameter()
				.getName(), catchStatement.getBlock()));
	}

	@Override
	public String getCode(Try tryStatement) {
		Log.MethodIn(Thread.currentThread());
		String ret = formatIndented("try\n{%1}", tryStatement.getBlock());
		for (Catch c : tryStatement.getCatches()) {
			ret += "\n" + c;
		}
		if (!tryStatement.getFinilyBlock().getStatements().isEmpty())
			ret += formatIndented(" finally\n{%1}",
					tryStatement.getFinilyBlock());
		return (String)Log.MethodOut(Thread.currentThread(), ret);
	}

	@Override
	public String getCode(TypeException typeException) {
		Log.MethodIn(Thread.currentThread());
		switch (typeException.getKind()) {
		case GLOBAL:
			return (String)Log.MethodOut(Thread.currentThread(), "Exception");
		case ARITHMETIC:
			return (String)Log.MethodOut(Thread.currentThread(), "ArithmeticException");
		case CAST:
			return (String)Log.MethodOut(Thread.currentThread(), "InvalidCastException");
		case ARGUMENT:
			return (String)Log.MethodOut(Thread.currentThread(), "ArgumentException");
		case NULLREFERENCE:
			return (String)Log.MethodOut(Thread.currentThread(), "NullReferenceException");
		case SECURITY:
			return (String)Log.MethodOut(Thread.currentThread(), "SecurityException");
		case UNSUPORTED:
			return (String)Log.MethodOut(Thread.currentThread(), "InvalidOperationException");
		case DEFAULT:
			return (String)Log.MethodOut(Thread.currentThread(), "Exception");
		case ACCESS:
			return (String)Log.MethodOut(Thread.currentThread(), "AccessViolationException");
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
		Log.d("<CSharpGenerator - getCode(RecognizedDependency)> Print RecognizedDependencies :");
		String result = "";
		for (String Import : imports) {
			if (Import.startsWith("+"))
				Import = Import.substring(1);
			Log.d("---> " + Import);
			result += "using " + Import + ";\n";
		}

		return (String)Log.MethodOut(Thread.currentThread(),result);
	}

}
