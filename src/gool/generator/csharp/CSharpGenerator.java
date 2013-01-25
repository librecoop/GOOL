package gool.generator.csharp;

import gool.ast.bufferedreader.BufferedReaderCloseCall;
import gool.ast.bufferedreader.BufferedReaderReadCall;
import gool.ast.bufferedreader.BufferedReaderReadLineCall;
import gool.ast.bufferedwriter.BufferedWriterCloseCall;
import gool.ast.bufferedwriter.BufferedWriterWriteCall;
import gool.ast.constructs.BinaryOperation;
import gool.ast.constructs.BufferedWriterMethCall;
import gool.ast.constructs.ClassDef;
import gool.ast.constructs.ClassFree;
import gool.ast.constructs.ClassNew;
import gool.ast.constructs.Constant;
import gool.ast.constructs.Constructor;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.ExceptionMethCall;
import gool.ast.constructs.FileMethCall;
import gool.ast.constructs.MainMeth;
import gool.ast.constructs.Meth;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.Operator;
import gool.ast.constructs.Package;
import gool.ast.constructs.ParentCall;
import gool.ast.constructs.ToStringCall;
import gool.ast.constructs.Try;
import gool.ast.constructs.TypeDependency;
import gool.ast.file.FileDeleteCall;
import gool.ast.file.FileExistsCall;
import gool.ast.file.FileIsDirectoryCall;
import gool.ast.file.FileIsFileCall;

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
import gool.ast.type.TypeBufferedReader;
import gool.ast.type.TypeBufferedWriter;
import gool.ast.type.TypeByte;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeFile;
import gool.ast.type.TypeFileReader;
import gool.ast.type.TypeFileWriter;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeString;
import gool.ast.type.TypeVoid;
import gool.generator.GeneratorHelper;
import gool.generator.common.CommonCodeGenerator;

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
		return String.format("new %s( %s )", classNew.getName(), GeneratorHelper
				.joinParams(classNew.getParameters()));
	}

	@Override
	public String getCode(TypeString typeString) {
		return "string";
	}
	
	@Override
	public String getCode(TypeChar typeChar) {
		return "char";
	}

	@Override
	public String getCode(TypeVoid typeVoid) {
		return "void";
	}

	@Override
	public String getCode(ListAddCall lac) {
		String method = lac.getParameters().size() > 1 ? "Insert" : "Add";
		return String.format("%s.%s(%s)", lac.getExpression(), method, GeneratorHelper
				.joinParams(lac.getParameters()));
	}

	@Override
	public String getCode(ListRemoveCall lrc) {
		return String.format("%s.Remove(%s)", lrc.getExpression(), GeneratorHelper
				.joinParams(lrc.getParameters()));
	}

	@Override
	public String getCode(ListRemoveAtCall lrc) {
		return String.format("%s.RemoveAt(%s)", lrc.getExpression(), GeneratorHelper
				.joinParams(lrc.getParameters()));
	}

	@Override
	public String getCode(ListContainsCall lcc) {
		return String.format("%s.Contains(%s)", lcc.getExpression(), GeneratorHelper
				.joinParams(lcc.getParameters()));
	}

	@Override
	public String getCode(ListGetCall lgc) {
		return String.format("%s[%s]", lgc.getExpression(), GeneratorHelper
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
		if (typeDependency.getType() instanceof TypeClass ) {
			return "noprint";
		}
		return super.getCode(typeDependency);
	}
	
	@Override
	public String getCode(ClassDef classDef) {
		if (classDef.getPpackage() == null) {
			return "noprint";
		}
		return classDef.getPackageName();
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
		return String.format("Console.WriteLine(%s)", GeneratorHelper
				.joinParams(systemOutPrintCall.getParameters()));
	}

	@Override
	public String getCode(ParentCall parentCall) {
		String out = "base(";
		if (parentCall.getParameters() != null) {
			out += GeneratorHelper.joinParams(parentCall.getParameters());
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
				.getType(), name, GeneratorHelper.joinParams(meth.getParams()));
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
				GeneratorHelper.joinParams(mapRemoveCall.getParameters()));
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
		return String.format("%s[%s]", mapGetCall.getExpression(), GeneratorHelper
				.joinParams(mapGetCall.getParameters()));
	}

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		return String.format("%s.ContainsKey(%s)", mapContainsKeyCall
				.getExpression(), GeneratorHelper.joinParams(mapContainsKeyCall
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
		
		if (dependency instanceof ClassDef) { // It is already a package.
			// Return only the package. C# does not support individual class
			// importation.
			return ((ClassDef) dependency).getPackageName();
		}

		return dependency.toString();

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
		return String.format("%s.Equals(%s)", equalsCall.getTarget(), GeneratorHelper
				.joinParams(equalsCall.getParameters()));
	}

	@Override
	public String getCode(ToStringCall tsc) {
		return String.format("%s.ToString()", tsc.getTarget());
	}

	@Override
	public String getCode(Package _package) {
		return _package.getName();
	}

	@Override
	public String getCode(SystemCommandDependency systemCommandDependency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(TypeFile typeFile) {
		return "String";
	}
	
	@Override
	public String getCode(TypeFileReader typeFileReader) {
		//TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(TypeBufferedReader typeBufferedReader) {
		//TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(TypeFileWriter typeFileWriter) {
		return "FileStream";
	}

	@Override
	public String getCode(TypeBufferedWriter typeBufferedWriter) {
		return "StreamWriter";
	}

	@Override
	public String getCode(BufferedReaderReadLineCall bufferedReaderReadLineCall) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public String getCode(BufferedReaderReadCall bufferedReaderReadCall) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getCode(BufferedReaderCloseCall bufferedReaderCloseCall) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getCode(BufferedWriterWriteCall bufferedWriterWriteCall) {
		return String.format("%s.Write(%s)", bufferedWriterWriteCall.getExpression(),StringUtils.join(bufferedWriterWriteCall.getParameters(), ","));
	}
	

	@Override
	public String getCode(BufferedWriterCloseCall bufferedWriterCloseCall) {
		return String.format("%s.Close()", bufferedWriterCloseCall.getExpression());
	}
	
	@Override
	public String getCode(BufferedWriterMethCall bufferedWriterMethCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(ExceptionMethCall exceptionMethCall) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getCode(FileMethCall fileMethCall) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getCode(FileIsDirectoryCall fileIsDirectoryCall) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getCode(FileIsFileCall fileIsFileCall) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getCode(FileDeleteCall fileDeleteCall) {
		return String.format("File.Delete(%s)", fileDeleteCall.getExpression());
	}
	@Override
	public String getCode(FileExistsCall fileExistsCall) {
		return String.format("File.Exists(%s)", fileExistsCall.getExpression());
	}

	@Override
	public String getCode(Try t) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
