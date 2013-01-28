package gool.generator.java;

import gool.ast.constructs.BinaryOperation;
import gool.ast.constructs.Catch;
import gool.ast.constructs.ClassDef;
import gool.ast.constructs.ClassNew;
import gool.ast.constructs.Constant;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.Field;
import gool.ast.constructs.MainMeth;
import gool.ast.constructs.Meth;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.Operator;
import gool.ast.constructs.ParentCall;
import gool.ast.constructs.Throw;
import gool.ast.constructs.ToStringCall;
import gool.ast.constructs.Try;
import gool.ast.constructs.TypeDependency;
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
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeException;
import gool.ast.type.TypeFile;
import gool.ast.type.TypeInputStream;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeScanner;
import gool.ast.type.TypeString;
import gool.generator.GeneratorHelper;
import gool.generator.common.CodeGeneratorNoVelocity;
import gool.generator.common.CommonCodeGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class JavaGenerator extends CommonCodeGenerator implements CodeGeneratorNoVelocity {

	private static Map<String, Dependency> customDependencies = new HashMap<String, Dependency>();

	public void addCustomDependency(String key, Dependency value){
		customDependencies.put(key, value);
	}
	
	@Override
	public String getCode(BinaryOperation binaryOp) {
		if (!(binaryOp.getLeft() instanceof Constant) && binaryOp.getOperator() == Operator.EQUAL) {
			return String.format("%s.equals(%s)", binaryOp.getLeft(), binaryOp
					.getRight());
		} else {
			return super.getCode(binaryOp);
		}
	}

	public String getCode(ClassNew classNew) {
		return String.format("new %s(%s)", classNew.getType(), StringUtils
				.join(classNew.getParameters(), ", "));
	}

	public String getCode(CustomDependency customDependency) {
		if (!customDependencies.containsKey(customDependency.getName())) {
			System.out.println(String.format("Custom dependencies: %s, Desired: %s", customDependencies, customDependency.getName()));
			throw new IllegalArgumentException(String.format("There is no equivalent type in Java for the GOOL type '%s'.", customDependency.getName()));
		}
		return customDependencies.get(customDependency.getName()).toString();
	}

	@Override
	public String getCode(EnhancedForLoop enhancedForLoop) {
		return formatIndented("for (%s : %s){%1}",
				enhancedForLoop.getVarDec(), 
				(enhancedForLoop.getExpression().getType() instanceof TypeMap)?String.format("%s.entrySet()",enhancedForLoop.getExpression()):enhancedForLoop.getExpression(), 
				enhancedForLoop.getStatements());
	}

	@Override
	public String getCode(EqualsCall equalsCall) {
		return String.format("%s.equals(%s)", equalsCall.getTarget(), StringUtils.join(equalsCall.getParameters(), ", "));
	}

	public String getCode(ListAddCall lac) {
		return String.format("%s.add(%s)", lac.getExpression(), StringUtils
				.join(lac.getParameters(), ", "));
	}

	public String getCode(ListContainsCall lcc) {
		return String.format("%s.contains(%s)", lcc.getExpression(),
				StringUtils.join(lcc.getParameters(), ", "));
	}

	public String getCode(ListGetCall lgc) {
		return String.format("%s.get(%s)", lgc.getExpression(), StringUtils
				.join(lgc.getParameters(), ", "));
	}

	public String getCode(ListGetIteratorCall lgic) {
		return String.format("%s.getIterator()", lgic.getExpression());
	}

	public String getCode(ListIsEmptyCall liec) {
		return String.format("%s.isEmpty()", liec.getExpression());
	}
	
	public String getCode(ListRemoveAtCall lrc) {
		return String.format("%s.remove(%s)", lrc.getExpression(), StringUtils
				.join(lrc.getParameters(), ", "));
	}
	public String getCode(ListRemoveCall lrc) {
		return String.format("%s.remove(%s)", lrc.getExpression(), StringUtils
				.join(lrc.getParameters(), ", "));
	}

	public String getCode(ListSizeCall lsc) {
		return String.format("%s.size()", lsc.getExpression());
	}

	public String getCode(MainMeth mainMeth) {
		return "public static void main(String[] args)";
	}

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		return String.format("%s.containsKey(%s)", mapContainsKeyCall
				.getExpression(), StringUtils.join(mapContainsKeyCall
				.getParameters(), ", "));
	}

	@Override
	public String getCode(MapEntryGetKeyCall mapEntryGetKeyCall) {
		return String.format("%s.getKey()", mapEntryGetKeyCall
				.getExpression());
	}

	@Override
	public String getCode(MapEntryGetValueCall mapEntryGetKeyCall) {
		return String.format("%s.getValue()", mapEntryGetKeyCall
				.getExpression());
	}

	@Override
	public String getCode(MapGetCall mapGetCall) {
		return String.format("%s.get(%s)", mapGetCall.getExpression(),
				StringUtils.join(mapGetCall.getParameters(), ", "));
	}

	@Override
	public String getCode(MapGetIteratorCall mapGetIteratorCall) {
		return String.format("%s.getIterator()", mapGetIteratorCall
				.getExpression());
	}

	@Override
	public String getCode(MapIsEmptyCall mapIsEmptyCall) {
		return String.format("%s.isEmpty()", mapIsEmptyCall.getExpression());
	}

	@Override
	public String getCode(MapPutCall mapPutCall) {
		return String.format("%s.put(%s)", mapPutCall.getExpression(),
				StringUtils.join(mapPutCall.getParameters(), ", "));
	}

	@Override
	public String getCode(MapRemoveCall mapRemoveCall) {
		return String.format("%s.remove(%s)", mapRemoveCall.getExpression(),
				StringUtils.join(mapRemoveCall.getParameters(), ", "));
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		return String.format("%s.size()", mapSizeCall.getExpression());
	}

	@Override
	public String getCode(ParentCall parentCall) {
		String out = "super(";
		if (parentCall.getParameters() != null) {
			out += StringUtils.join(parentCall.getParameters(), ", ");
		}
		out += ")";
		return out;
	}
	
	public String getCode(SystemOutDependency systemOutDependency) {
		return "noprint";
	}

	@Override
	public String getCode(SystemOutPrintCall systemOutPrintCall) {
		return String.format("System.out.println(%s)", StringUtils.join(
				systemOutPrintCall.getParameters(), ","));
	}

	public String getCode(ToStringCall tsc) {
		return String.format("%s.toString()", tsc.getTarget());
		
	}

	@Override
	public String getCode(TypeBool typeBool) {
		return "Boolean";
	}

	@Override
	public String getCode(TypeDecimal typeReal) {
		return "Double";
	}
	
	@Override
	public String getCode(TypeChar typeChar) {
		return "char";
	}

	public String getCode(TypeDependency typeDependency) {
		if (typeDependency.getType() instanceof TypeList) {
			return "java.util.ArrayList";
		}
		if (typeDependency.getType() instanceof TypeMap) {
			return "java.util.HashMap";
		}
		if (typeDependency.getType() instanceof TypeEntry) {
			return "java.util.Map";
		}
		if (typeDependency.getType() instanceof TypeFile) {
			return "java.io.File";
		}
		return super.getCode(typeDependency);
	}
	
	@Override
	public String getCode(TypeEntry typeEntry) {
		return String.format("Map.Entry<%s, %s>",typeEntry.getKeyType(), typeEntry.getElementType());
	}

	@Override
	public String getCode(TypeInt typeInt) {
		return "Integer";
	}
	
	@Override
	public String getCode(TypeList typeList) {
		
		IType elementType = typeList.getElementType();
		if (elementType == null) {
			elementType = TypeObject.INSTANCE;
		}
		return String.format("ArrayList<%s>", elementType);
	}

	@Override
	public String getCode(TypeMap typeMap) {
		return String.format("HashMap<%s, %s>", typeMap.getKeyType(), typeMap
				.getElementType());
	}

	@Override
	public String getCode(TypeObject typeObject) {
		return "Object";
	}

	@Override
	public String getCode(TypeString typeString) {
		return "String";
	}
	
	@Override
	public String getCode(Modifier modifier) {
		if (modifier == Modifier.OVERRIDE) {
			return "";
		}
		if (modifier == Modifier.VIRTUAL) {
			return "";
		}

		return modifier.name().toLowerCase();

	}

	@Override
	public String getCode(SystemCommandDependency systemCommandDependency) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String printClass(ClassDef classDef) {
		StringBuilder sb = new StringBuilder (String.format("// Platform: %s\n\n", classDef.getPlatform()));
		// print the package containing the class
		if (classDef.getPpackage() != null)
			sb = sb.append(String.format("package %s;\n\n", classDef.getPackageName()));
		// print the includes
		Set<String> dependencies = GeneratorHelper.printDependencies(classDef);
		if (! dependencies.isEmpty()) {
			for (String dependency : dependencies){
				if(dependency != "noprint" && dependency.contains("."))
					sb = sb.append(String.format("import %s;\n", dependency));
			}
				
			sb = sb.append("\n");
		}
		// print the class prototype
		sb = sb.append(String.format("%s %s %s",
				StringUtils.join(classDef.getModifiers(), ' '),
				classDef.isInterface()?"interface":"class",
				classDef.getName()));
		if (classDef.getParentClass() != null)
			sb = sb.append(String.format(" extends %s",classDef.getParentClass()));
		if (! classDef.getInterfaces().isEmpty())
			sb = sb.append(String.format(" interfaces %s",StringUtils.join(classDef.getInterfaces(),", ")));
		sb = sb.append(" {\n\n");
		// print the fields
		for (Field field : classDef.getFields())
			sb = sb.append(formatIndented("%-1%s;\n\n", field));
		// print the methods
		for (Meth meth : classDef.getMethods()){
			// TODO: deal with constructors ?
			if (classDef.isInterface())
				sb = sb.append(formatIndented("%-1%s;\n\n", meth.getHeader()));
			else
				sb = sb.append(formatIndented("%-1%s {%2%-1}\n\n", meth.getHeader(), meth.getBlock()));
		}
		return sb.toString() + "}";
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
		return formatIndented("catch (%s %s) {%1}",
				catchStatement.getParameter().getType(),
				catchStatement.getParameter().getName(),
				catchStatement.getBlock());
	}

	@Override
	public String getCode(Try tryStatement) {
		String ret = formatIndented("try {%1}", tryStatement.getBlock());
		for (Catch c: tryStatement.getCatches()) {
			ret += " " + c;
		}
		if (!tryStatement.getFinilyBlock().getStatements().isEmpty())
			ret += formatIndented(" finally {%1}", tryStatement.getFinilyBlock());
		return ret;
	}

	@Override
	public String getCode(TypeException typeException) {
		switch (typeException.getKind()) {
		case GLOBAL:
			return "Exception";
		case ARITHMETIC:
			return "ArithmeticException";
		case COLLECTION:
			return "ArrayStoreException";
		case CAST:
			return "ClassCastException";
		case ENUM:
			return "EnumConstantNotPresentException";
		case ARGUMENT:
			return "IllegalArgumentException";
		case THREAD:
			return "IllegalMonitorStateException";
		case STATE:
			return "IllegalStateException";
		case ARRAY:
			return "IndexOutOfBoundsException";
		case ARRAYSIZE:
			return "NegativeArraySizeException";
		case NULLREFERENCE:
			return "NullPointerException";
		case SECURITY:
			return "SecurityException";
		case TYPE:
			return "TypeNotPresentException";
		case UNSUPORTED:
			return "UnsupportedOperationException";
		case CLASSNOTFOUND:
			return "ClassNotFoundException";
		case DEFAULT:
			return "CloneNotSupportedException";
		case ACCESS:
			return "IllegalAccessException";
		case NEWINSTANCE:
			return "InstantiationException";
		case INTERUPT:
			return "InterruptedException";
		case NOSUCHFIELD:
			return "NoSuchFieldException";
		case NOSUCHMETH:
			return "NoSuchMethodException";
		default:
			return typeException.getName();
		}
	}
	
}