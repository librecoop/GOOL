package gool.generator.objc;

import gool.ast.constructs.ClassNew;
import gool.ast.constructs.Constructor;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.Expression;
import gool.ast.constructs.MainMeth;
import gool.ast.constructs.MemberSelect;
import gool.ast.constructs.Meth;
import gool.ast.constructs.MethCall;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.ParentCall;
import gool.ast.constructs.This;
import gool.ast.constructs.ThisCall;
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
import gool.ast.type.TypeFile;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeNone;
import gool.ast.type.TypeNull;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeString;
import gool.generator.GeneratorHelper;
import gool.generator.common.CommonCodeGenerator;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class ObjcGenerator extends CommonCodeGenerator {

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
	public String getCode(TypeNull typeNull) {
		return "nil";
	}

	@Override
	public String getCode(ThisCall thisCall) {
		return "self()";
	}

	@Override
	public String getCode(ClassNew classNew) {
		return String.format("[[[%s alloc]init]autorelease]",
				removePointer(classNew.getType()));
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
		return String.format("[%s isEqual: %s]", equalsCall.getTarget(),
				StringUtils.join(equalsCall.getParameters(), ", "));
	}

	@Override
	public String getCode(ListAddCall lac) {
		if (lac.getParameters().size() == 1) {
			return String.format("[%s addObject:%s]", lac.getExpression(),
					lac.getParameters());
		} else {
			String s = String.format("[%s arrayWithObjects:%s]",
					lac.getExpression(),
					StringUtils.join(lac.getParameters(), ", "));
			String str[] = new String[lac.getParameters().size()];
			for (int i = 2; i <= lac.getParameters().size(); i++) {
				str[i] = (String) String.format("[%s addObject:%s]%n",
						lac.getExpression(), lac.getParameters().get(i));
			}
			return String.format("%s%n%s", s, str);
		}
	}

	@Override
	public String getCode(ListContainsCall lcc) {
		return String.format(
				"([string rangeOfString:@%s].location == NSNotFound)",
				lcc.getParameters());
	}

	@Override
	public String getCode(ListGetCall lgc) {
		return String.format("[%s ObjectsAtIndex:%s]", lgc.getExpression(),
				lgc.getParameters());
		// the number of lgic.getParameters() ? TODO
	}

	@Override
	public String getCode(ListGetIteratorCall lgic) {
		return null;
	}

	@Override
	public String getCode(ListIsEmptyCall liec) {
		String s = "( " + String.format("[%s count]", liec.getExpression())
				+ "== 0)";
		return s;
	}

	@Override
	public String getCode(ListRemoveAtCall lrc) {
		return String.format("[%s removeObjectsAtIndex:%s]",
				lrc.getExpression(), lrc.getParameters());
	}

	@Override
	public String getCode(ListRemoveCall lrc) {
		return String.format("[%s removeObject:%s]", lrc.getExpression(),
				lrc.getParameters());
	}

	@Override
	public String getCode(ListSizeCall lsc) {
		return String.format("[%s count]", lsc.getExpression());
	}

	@Override
	public String getCode(MainMeth mainMeth) {
		return "int main(int argc, const char * argv[])";
	}

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		return String.format("[[%s objectForKey:@%s]isEqualToString:@"+"("+"null"+")"+"]",
				mapContainsKeyCall.getExpression(),
				mapContainsKeyCall.getParameters());
	}
	
	
	@Override
	public String getCode(MapEntryGetKeyCall mapEntryGetKeyCall) {
	//TODO
		return null;
	}

	@Override
	public String getCode(MapEntryGetValueCall mapEntryGetValueCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(MapGetCall mapGetCall) {
		return String.format("[%s objectForKey:@%s]", mapGetCall.getExpression(),
				StringUtils.join(mapGetCall.getParameters(), ", "));
	}

	@Override
	public String getCode(MapGetIteratorCall mapGetIteratorCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(MapIsEmptyCall mapIsEmptyCall) {
		String s = "( " + String.format("[%s count]", mapIsEmptyCall.getExpression())
				+ "== 0)";
		return s;
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
	// TODO super
	public String getCode(ParentCall parentCall) {
		return "self = [super init]";
	}

	@Override
	public String getCode(SystemOutDependency systemOutDependency) {
		return "Foundation/Foundation.h";
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
		return String.format("[dictionary setObject:@%s forKey@%s]",
				typeEntry.getElementType(), typeEntry.getKeyType());
	}

	public String getCode(TypeObject typeObject) {
		// type java.object
		return "id";
	}

	@Override
	public String getCode(TypeInt typeInt) {
		return "int";
	}

	public String getCode(TypeArray typeArray) {
		return String.format("NSArray");
	}

	@Override
	public String getCode(TypeList typeList) {
		return String.format("NSMutableArray");
	}

	@Override
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

	@Override
	public String getCode(TypeFile typeFile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode(This pthis) {
		return "self";
	}

	@Override
	public String getCode(MethCall methodCall) {
		return String.format("[%s:%s]", methodCall.getTarget(),
				StringUtils.join(methodCall.getParameters(), ":"));
	}

	@Override
	public String getCode(MemberSelect memberSelect) {
		return String.format("%s %s", memberSelect.getTarget(),
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
	public String getCode(Meth meth) {
		String ret = new String();
		String arg = new String();
		boolean b = false;
		if (!(meth.getType() instanceof TypeNone))
			ret = String.format("(%s)", meth.getType().toString());
		else
			ret = "(void)";

		for (VarDeclaration e : meth.getParams()) {
			if (!b) {
				arg += String.format(":(%s)%s ", e.getType(), e.getName());
				b = true;
			} else
				arg += String.format("and%s:(%s)%s ", e.getName(), e.getType(),
						e.getName());
		}

		return String.format("%s %s%s", ret, meth.getName(), arg);
	}

	@Override
	public String getCode(Constructor cons) {
		String param = new String();
		for (VarDeclaration e : cons.getParams()) {
			param += String.format("and%s:(%s)%s ", e.getName(), e.getType(),
					e.getName());
		}

		return String.format("- (id)init%s", param);
	}

}
