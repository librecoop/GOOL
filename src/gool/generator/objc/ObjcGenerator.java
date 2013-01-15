package gool.generator.objc;

import gool.ast.constructs.ClassNew;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.Expression;
import gool.ast.constructs.MainMeth;
import gool.ast.constructs.ParentCall;
import gool.ast.constructs.ToStringCall;
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
import gool.ast.type.PrimitiveType;
import gool.ast.type.TypeArray;
import gool.ast.type.TypeBool;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeString;
import gool.generator.GeneratorHelper;
import gool.generator.common.CommonCodeGenerator;
import java.util.HashMap;
import java.util.Map;

public class ObjcGenerator extends CommonCodeGenerator {

	public void ObjcGenrator() {
	}

	private String removePointer(IType type) {
		return removePointer(type.toString());
	}

	private String removePointer(String type) {
		return type.replaceAll("[\\s*]+$", "");
	}

	private static Map<String, Dependency> customDependencies = new HashMap<String, Dependency>();

	@Override
	public void addCustomDependency(String key, Dependency value) {

		customDependencies.put(key, value);

	}

	@Override
	public String getCode(ClassNew classNew) {
		return String.format("[%s new]", removePointer(classNew.getType())); // a
																				// completer
	}

	@Override
	public String getCode(EnhancedForLoop enhancedForLoop) {
		VarDeclaration varDec = enhancedForLoop.getVarDec();
		String varName = varDec.getName();
		Expression expression = enhancedForLoop.getExpression();
		String expressionToString = enhancedForLoop.getExpression().toString();
		if (varDec.getType() instanceof PrimitiveType) {
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
											varDec.getType())) : (String
									.format("%s = ", varName)), varName,
							enhancedForLoop.getStatements());
		} else {
			return String
					.format("for(%s in %s){%s}",
							enhancedForLoop.getVarDec(),
							(enhancedForLoop.getExpression().getType() instanceof TypeMap) ? String
									.format("%s.entrySet()",
											enhancedForLoop.getExpression())
									: enhancedForLoop.getExpression(),
							enhancedForLoop.getStatements());
		}
	}

	@Override
	public String getCode(EqualsCall equalsCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(ListAddCall lac) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(ListContainsCall lcc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(ListGetCall lgc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(ListGetIteratorCall lgic) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(ListIsEmptyCall liec) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(ListRemoveAtCall lrc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(ListRemoveCall lrc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(ListSizeCall lsc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(MainMeth mainMeth) {
		return "int main()";
	}

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(MapEntryGetKeyCall mapEntryGetKeyCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(MapEntryGetValueCall mapEntryGetKeyCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(MapGetCall mapGetCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(MapGetIteratorCall mapGetIteratorCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(MapIsEmptyCall mapIsEmptyCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(MapPutCall mapPutCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(MapRemoveCall mapRemoveCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	//TODO super
	public String getCode(ParentCall parentCall) {
		return "self =[super init]";
	}

	@Override
	public String getCode(SystemOutDependency systemOutDependency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(SystemOutPrintCall systemOutPrintCall) {
		return String.format("NSLog(@%s)",
				GeneratorHelper.joinParams(systemOutPrintCall.getParameters()));
	}

	@Override
	public String getCode(ToStringCall tsc) {
		return String.format("[%s ToString]", tsc.getTarget());
	}

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
		// TODO Auto-generated method stub
		return null;
	}

	public String getCode(TypeObject typeObject) {
		// type java.object
		return "id";
	}

	@Override
	public String getCode(TypeInt typeInt) {
		return "int";
	}
	
	public String getCode(TypeArray typeArray){
		return String.format("NSArray");
	}

	@Override
	public String getCode(TypeList typeList) {
		return String.format("NSMutableArray");
	}

	@Override
	//TODO NSMutableDictionary
	public String getCode(TypeMap typeMap) {
		return "NSMutableDictionary";
	}

	@Override
	public String getCode(TypeString typeString) {
		return "NSString *";
	}

	@Override
	public String getCode(CustomDependency customDependency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(SystemCommandDependency systemCommandDependency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(TypeChar typeChar) {
		return "char";
	}

}
