package gool.platform.csharp.generation;

import gool.ast.BinaryOperation;
import gool.ast.ClassDef;
import gool.ast.ClassNew;
import gool.ast.Constant;
import gool.ast.Constructor;
import gool.ast.Dependency;
import gool.ast.EnhancedForLoop;
import gool.ast.EqualsCall;
import gool.ast.MainMeth;
import gool.ast.Meth;
import gool.ast.Modifier;
import gool.ast.Operator;
import gool.ast.ParentCall;
import gool.ast.ToStringCall;
import gool.ast.gool.CustomDependency;
import gool.ast.gool.SystemOutDependency;
import gool.ast.gool.SystemOutPrintCall;
import gool.ast.gool.TypeDependency;
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
import gool.ast.type.TypeBool;
import gool.ast.type.TypeByte;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeString;
import gool.ast.type.TypeVoid;
import gool.platform.common.CommonCodeGenerator;
import gool.util.Helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * It generates specific C# code for certain GOOL nodes.
 */
public class CSharpGenerator extends CommonCodeGenerator {
	@Override
	public String getCode(TypeBool typeBool) {
		return "bool";
	}
	
	@Override
	public String getCode(BinaryOperation binaryOp) {
		if (!(binaryOp.getLeft() instanceof Constant) && binaryOp.getOperator() == Operator.EQUAL) {
			return String.format("%s.Equals(%s)", binaryOp.getLeft(), binaryOp
					.getRight());
		} else {
			return super.getCode(binaryOp);
		}
	}

	@Override
	public String getCode(TypeInt typeInt) {
		return "int";
	}

	@Override
	public String getCode(TypeByte t) {
		return "byte";
	}

	/**
	 * Produces code for an object instantiation.
	 * 
	 * @param classNew
	 *            the object instantiation node.
	 * @return the formatted object instantiation.
	 */
	public String getCode(ClassNew classNew) {
		return String.format("new %s( %s )", classNew.getName(), Helper
				.joinParams(classNew.getParameters()));
	}

	@Override
	public String getCode(TypeString typeString) {
		return "string";
	}

	@Override
	public String getCode(TypeVoid typeVoid) {
		return "void";
	}

	@Override
	public String getCode(ListAddCall lac) {
		String method = lac.getParameters().size() > 1 ? "Insert" : "Add";
		return String.format("%s.%s(%s)", lac.getExpression(), method, Helper
				.joinParams(lac.getParameters()));
	}

	@Override
	public String getCode(ListRemoveCall lrc) {
		return String.format("%s.Remove(%s)", lrc.getExpression(), Helper
				.joinParams(lrc.getParameters()));
	}

	@Override
	public String getCode(ListRemoveAtCall lrc) {
		return String.format("%s.RemoveAt(%s)", lrc.getExpression(), Helper
				.joinParams(lrc.getParameters()));
	}

	@Override
	public String getCode(ListContainsCall lcc) {
		return String.format("%s.Contains(%s)", lcc.getExpression(), Helper
				.joinParams(lcc.getParameters()));
	}

	@Override
	public String getCode(ListGetCall lgc) {
		return String.format("%s[%s]", lgc.getExpression(), Helper
				.joinParams(lgc.getParameters()));
	}

	@Override
	public String getCode(ListSizeCall lsc) {
		return String.format("%s.Count", lsc.getExpression());
	}

	@Override
	public String getCode(ListIsEmptyCall liec) {
		return String.format("%s.Count == 0", liec.getExpression());
	}

	@Override
	public String getCode(ListGetIteratorCall lgic) {
		return String.format("%s.GetIterator()", lgic.getExpression());
	}

	@Override
	public String getCode(TypeDependency typeDependency) {
		if (typeDependency.getType() instanceof TypeList) {
			if (((TypeList)typeDependency.getType()).getElementType() == null) {
				return "System.Collections";
			}
			return "System.Collections.Generic";
		}
		if (typeDependency.getType() instanceof TypeMap) {
			return "System.Collections.Generic";
		}
		if (typeDependency.getType() instanceof TypeEntry) {
			return "System.Collections.Generic";
		}
		if (typeDependency.getType() instanceof TypeString) {
			return "noprint";
		}
		if (typeDependency.getType() instanceof TypeBool) {
			return "noprint";
		}
		if (typeDependency.getType() instanceof TypeInt) {
			return "noprint";
		}
		if (typeDependency.getPackageName().equals("")) {
			return "noprint";
		}

		return super.getCode(typeDependency);
	}
	
	@Override
	public String getCode(Dependency dependency) {
		if (dependency instanceof ClassDef) {
			if (dependency.getPpackage() == null) {
				return "noprint";
			}
			return dependency.getPackageName();
		}
		
		return super.getCode(dependency);
	}

	@Override
	public String getCode(TypeObject typeObject) {
		return "object";
	}

	@Override
	public String getCode(TypeList typeList) {
		if (typeList.getElementType() == null) {
			return "ArrayList";
		}
		return "List<" + typeList.getElementType().toString() + ">";
	}

	@Override
	public String getCode(TypeDecimal typeReal) {
		return "double";
	}

	@Override
	public String getCode(SystemOutPrintCall systemOutPrintCall) {
		return String.format("Console.WriteLine(%s)", Helper
				.joinParams(systemOutPrintCall.getParameters()));
	}

	@Override
	public String getCode(ParentCall parentCall) {
		String out = "base(";
		if (parentCall.getParameters() != null) {
			out += Helper.joinParams(parentCall.getParameters());
		}
		out += ")";
		return out;
	}

	@Override
	public String getCode(SystemOutDependency systemOutDependency) {
		return "System";
	}

	@Override
	public String getCode(Meth meth) {

		// #join ( $method.Modifiers " ") #if($method.isInherited())override#end
		// $method.Type $method.Name ( #params( $method.getParams() ) )

		String name = replaceMethodName(meth, meth.getName());

		customizeModifiers(meth);

		return String.format("%s %s %s(%s)", getCode(meth.getModifiers()), meth
				.getType(), name, Helper.joinParams(meth.getParams()));
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
			} else if (name.equals("equals") && meth.getParams().size()==1&&meth.getParams().get(0).getType().equals(TypeObject.INSTANCE)) {
				return "Equals";
			} else if (name.equals("hashCode") && meth.getParams().isEmpty()) {
				return "GetHashCode";
			}
		}
		return name;
	}

	@Override
	public String getCode(Modifier modifier) {
		if (modifier == Modifier.FINAL) {
			return "";
		}
		return super.getCode(modifier);
	}

	@Override
	public String getCode(MainMeth mainMeth) {
		return "public static void Main(string[] args)";
	}

	@Override
	public String getCode(Constructor cons) {
		// #if($method.isConstructor())
		// #if(!$method.ParentCalls.isEmpty()):#end
		// #foreach( $parentCall in $method.ParentCalls )
		// $parentCall
		// #end
		// #end
		return super.getCode(cons)
				+ ((cons.getInitCalls().size() > 0) ? " : "
						+ StringUtils.join(cons.getInitCalls(), " ") : "");
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		return String.format("%s.Count", mapSizeCall.getExpression());
	}

	@Override
	public String getCode(MapRemoveCall mapRemoveCall) {
		return String.format("%s.Remove(%s)", mapRemoveCall.getExpression(),
				Helper.joinParams(mapRemoveCall.getParameters()));
	}

	@Override
	public String getCode(MapPutCall mapPutCall) {
		return String.format("%s[%s] = %s", mapPutCall.getExpression(), mapPutCall.getParameters().get(0), mapPutCall.getParameters().get(1));
	}

	@Override
	public String getCode(MapIsEmptyCall mapIsEmptyCall) {
		return String.format("(%s.Count == 0)", mapIsEmptyCall.getExpression());
	}

	@Override
	public String getCode(MapGetIteratorCall mapGetIteratorCall) {
		return String.format("(%s.GetIterator() == 0)", mapGetIteratorCall
				.getExpression());
	}

	@Override
	public String getCode(MapGetCall mapGetCall) {
		return String.format("%s[%s]", mapGetCall.getExpression(), Helper
				.joinParams(mapGetCall.getParameters()));
	}

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		return String.format("%s.ContainsKey(%s)", mapContainsKeyCall
				.getExpression(), Helper.joinParams(mapContainsKeyCall
				.getParameters()));
	}

	@Override
	public String getCode(TypeMap typeMap) {
		return String.format("Dictionary<%s, %s>", typeMap.getKeyType(),
				typeMap.getElementType());
	}

	@Override
	public String getCode(EnhancedForLoop enhancedForLoop) {
		return String.format("foreach(%s in %s){%s}", enhancedForLoop
				.getVarDec(), enhancedForLoop.getExpression(), enhancedForLoop
				.getStatements());
	}

	private static Map<String, Dependency> customDependencies = new HashMap<String, Dependency>();

	public String getCode(CustomDependency customDependency) {
		if (!customDependencies.containsKey(customDependency.getName())) {
			throw new IllegalArgumentException(
					String
							.format(
									"There is no equivalent type in C# for the GOOL type '%s'.",
									customDependency.getName()));
		}

		Dependency dependency = customDependencies.get(customDependency
				.getName());

		if (dependency.getPpackage() == null) { // It is already a package.
			return dependency.getFullName();
		}
		// Return only the package. C# does not support individual class
		// importation.
		return dependency.getPackageName();
	}

	public void addCustomDependency(String key, Dependency value) {
		customDependencies.put(key, value);
	}

	@Override
	public String getCode(TypeEntry typeEntry) {
		return String.format("KeyValuePair<%s, %s>", typeEntry.getKeyType(),
				typeEntry.getElementType());
	}

	@Override
	public String getCode(MapEntryGetKeyCall mapEntryGetKeyCall) {
		return String.format("%s.Key", mapEntryGetKeyCall.getExpression());
	}

	@Override
	public String getCode(MapEntryGetValueCall mapEntryGetKeyCall) {
		return String.format("%s.Value", mapEntryGetKeyCall.getExpression());
	}

	@Override
	public String getCode(EqualsCall equalsCall) {
		return String.format("%s.Equals(%s)", equalsCall.getTarget(), Helper
				.joinParams(equalsCall.getParameters()));
	}

	@Override
	public String getCode(ToStringCall tsc) {
		return String.format("%s.ToString()", tsc.getTarget());
	}
}
