package gool.generator.java;

import gool.ast.bufferedreader.BufferedReaderCloseCall;
import gool.ast.bufferedreader.BufferedReaderReadCall;
import gool.ast.bufferedreader.BufferedReaderReadLineCall;
import gool.ast.bufferedwriter.BufferedWriterCloseCall;
import gool.ast.bufferedwriter.BufferedWriterFlushCall;
import gool.ast.bufferedwriter.BufferedWriterNewLineCall;
import gool.ast.bufferedwriter.BufferedWriterWriteCall;
import gool.ast.constructs.BinaryOperation;
import gool.ast.constructs.Block;
import gool.ast.constructs.BufferedReaderMethCall;
import gool.ast.constructs.BufferedWriterMethCall;
import gool.ast.constructs.Catch;
import gool.ast.constructs.ClassDef;
import gool.ast.constructs.ClassNew;
import gool.ast.constructs.Constant;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.ExceptionMethCall;
import gool.ast.constructs.FileMethCall;
import gool.ast.constructs.Statement;
import gool.ast.constructs.Try;

import gool.ast.constructs.MainMeth;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.Operator;
import gool.ast.constructs.Package;
import gool.ast.constructs.ParentCall;
import gool.ast.constructs.ToStringCall;
import gool.ast.constructs.TypeDependency;
import gool.ast.exception.ExceptionPrintStackTraceCall;
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
import gool.ast.type.TypeChar;
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
import gool.generator.common.CommonCodeGenerator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class JavaGenerator extends CommonCodeGenerator {

	private static Map<String, Dependency> customDependencies = new HashMap<String, Dependency>();

	public void addCustomDependency(String key, Dependency value){
		customDependencies.put(key, value);
	}
	
	@Override
	public String getCode(BinaryOperation binaryOp) {
		if (!(binaryOp.getLeft() instanceof Constant) && binaryOp.getOperator() == Operator.EQUAL) {
			return String.format("%s==(%s)", binaryOp.getLeft(), binaryOp
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
		return String.format("for(%s : %s){%s}",
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
		return String.format("(%s.isEmpty()", mapIsEmptyCall.getExpression());
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
		if (typeDependency.getType() instanceof TypeFileReader) {
				return "java.io.FileReader";
		}
		if (typeDependency.getType() instanceof TypeBufferedReader) {
			return "java.io.BufferedReader";
		}
		if (typeDependency.getType() instanceof TypeFileWriter) {
			return "java.io.FileWriter";
		}
		if (typeDependency.getType() instanceof TypeBufferedWriter) {
			return "java.io.BufferedWriter";
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
	public String getCode(TypeChar typeChar) {
		return "char";
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
	public String getCode(TypeFile typeFile) {
		return "File";
	}
	
	@Override
	public String getCode(TypeFileReader typeFileReader) {
		return "FileReader";
	}
	
	@Override
	public String getCode(TypeBufferedReader typeBufferedReader) {
		return "BufferedReader";
	}
	
	@Override
	public String getCode(TypeFileWriter typeFileWriter) {
		return "FileWriter";
	}
	
	@Override
	public String getCode(TypeBufferedWriter typeBufferedWriter) {
		return "BufferedWriter";
	}


//	@Override
//	public String getCode(BufferedReaderMethCall bufferedReaderMethCall) {
//		// TODO Auto-generated method stub
//		return String.format("%s.readLine()", bufferedReaderMethCall.getExpression());
//	}

	@Override
	public String getCode(BufferedReaderReadLineCall bufferedReaderReadLineCall) {
		
		return String.format("%s.readLine()", bufferedReaderReadLineCall.getExpression());
	}

	@Override
	public String getCode(BufferedReaderReadCall bufferedReaderReadCall) {
		
		return String.format("%s.read()", bufferedReaderReadCall.getExpression());
	}
	@Override
	public String getCode(BufferedReaderCloseCall bufferedReaderCloseCall) {
		
		return String.format("%s.close()", bufferedReaderCloseCall.getExpression());
	}
	
	@Override
	public String getCode(
			ExceptionPrintStackTraceCall exceptionPrintStackTraceCall) {
		
		return "e.printStackTrace()";
	}

	@Override
	public String getCode(ExceptionMethCall exceptionMethCall) {
		
		return null;
	}
	@Override
	public String getCode(BufferedWriterMethCall bufferedWriterMethCall) {
		
		return String.format("%s", bufferedWriterMethCall.getExpression());
	}
	

	@Override
	public String getCode(BufferedWriterWriteCall bufferedWriterWriteCall) {
		
		return String.format("%s.write(%s)", bufferedWriterWriteCall.getExpression(),StringUtils.join(bufferedWriterWriteCall.getParameters(), ","));
	}

	@Override
	public String getCode(BufferedWriterCloseCall bufferedWriterCloseCall) {
		
		return String.format("%s.close()", bufferedWriterCloseCall.getExpression());
	}
	@Override
	public String getCode(BufferedWriterFlushCall bufferedWriterFlushCall) {
		
		return String.format("%s.flush()", bufferedWriterFlushCall.getExpression());
	}
	@Override
	public String getCode(BufferedWriterNewLineCall bufferedWriterNewLineCall) {
		
		return String.format("%s.newLine()", bufferedWriterNewLineCall.getExpression());
	}
	
	
	@Override
	public String getCode(FileMethCall fileMethCall) {
		
		return String.format("%s", fileMethCall.getExpression());
	}
	@Override
	public String getCode(FileIsDirectoryCall fileIsDirectoryCall) {
		
		return String.format("%s.isDirectory()", fileIsDirectoryCall.getExpression());
	}
	@Override
	public String getCode(FileIsFileCall fileIsFileCall) {
		
		return String.format("%s.isFile()", fileIsFileCall.getExpression());
	}
	@Override
	public String getCode(FileDeleteCall fileDeleteCall) {
		
		return String.format("%s.delete()", fileDeleteCall.getExpression());
	}
	@Override
	public String getCode(FileExistsCall fileExistsCall) {
		
		return String.format("%s.exists()", fileExistsCall.getExpression());
	}
	
	@Override
	public String getCode(Try t ) {
		StringBuilder result = new StringBuilder();
		result.append("try{\n");
		for (Statement statement : t.getBlock().getStatements()) {
			result.append(statement);
			if (!(statement instanceof Block)) {
				result.append(";").append("\n");
			}
			
		}
		result.append("\n}"); //closing bracket
		for (Catch c : t.getCatches()) {
			result.append(getCode(c));		
			
		}
		return result.toString();
	}
	
	@Override
	public String getCode(Catch c ) {
		StringBuilder result = new StringBuilder();
		result.append("\n catch(");
		result.append(c.getSingleParameter());
		result.append(") {\n");
		for (Statement statement : c.getBlock().getStatements()) {
			result.append(statement);
			if (!(statement instanceof Block)) {
				result.append(";").append("\n");
			}
			
		}
		
		result.append("\n}");
		return result.toString();
	}
	
}