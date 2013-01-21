package gool.generator.objc;

import gool.ast.constructs.Assign;
import gool.ast.constructs.BinaryOperation;
import gool.ast.constructs.ClassNew;
import gool.ast.constructs.Constructor;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.Expression;
import gool.ast.constructs.Field;
import gool.ast.constructs.MainMeth;
import gool.ast.constructs.MemberSelect;
import gool.ast.constructs.Meth;
import gool.ast.constructs.MethCall;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.Operator;
import gool.ast.constructs.ParentCall;
import gool.ast.constructs.This;
import gool.ast.constructs.ThisCall;
import gool.ast.constructs.ToStringCall;
import gool.ast.constructs.VarAccess;
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

		return String.format("[%s containsObject: %s]", lcc.getExpression(),
				lcc.getParameters());
	}

	@Override
	public String getCode(ListGetCall lgc) {
		return String.format("[%s ObjectsAtIndex:%s]", lgc.getExpression(),
				lgc.getParameters());
	}

	@Override
	public String getCode(Assign assign) {
		System.out.println("pass here");
		if (assign.getValue().getType() instanceof TypeString) {
			return assign.getLValue() + " = @" + assign.getValue();
		} else

			return super.getCode(assign);

	}

	@Override
	public String getCode(VarDeclaration varDec) {
		String initialValue = "";
		if (varDec.getInitialValue() != null) {
			if (varDec.getType() instanceof TypeString) {
				initialValue = " = @" + varDec.getInitialValue();
				return String.format("%s %s%s", varDec.getType(),
						varDec.getName(), initialValue);
			} else
				return super.getCode(varDec);
		}
		return null;
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
		return String.format("[[%s objectForKey:@%s]isEqualToString:@" + "("
				+ "null" + ")" + "]", mapContainsKeyCall.getExpression(),
				mapContainsKeyCall.getParameters());
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
		return String.format("[%s objectForKey:@%s]",
				mapGetCall.getExpression(),
				StringUtils.join(mapGetCall.getParameters(), ", "));
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
		return String.format("[%s setObject:@%s forKey:@%s]",
				mapPutCall.getExpression(), mapPutCall.getParameters().get(0),
				mapPutCall.getParameters().get(1));
	}

	@Override
	public String getCode(MapRemoveCall mapRemoveCall) {
		return String.format("[%s removeObjectForKey:@%s]",
				mapRemoveCall.getExpression());
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		return String.format(
				"NSArray * allKeys = [%s allKeys]%n[allKeys count]",
				mapSizeCall.getExpression());
		// TODO
	}

	@Override
	public String getCode(ParentCall parentCall) {
		return "self = [super init]";
	}

	@Override
	public String getCode(SystemOutDependency systemOutDependency) {
		return "Foundation/Foundation.h";
	}

	@Override
	public String getCode(SystemOutPrintCall systemOutPrintCall) {
		// NSLog(@"%@",[NSString stringWithFormat:@"%@%d",[NSString
		// stringWithFormat:@"%@%@",[NSString stringWithFormat:@"%@%d",[NSString
		// stringWithFormat:@"%d%@",a,@" + "],b],@" = "],[self addParam1:a
		// andParam2:b ]]);
		String nsString = (systemOutPrintCall.getParameters().get(0).getType()
				.equals(TypeString.INSTANCE)
				&& !systemOutPrintCall.getParameters().get(0).toString()
						.contains("[NSString stringWithFormat:@") ? "@" : "");

		return String.format("NSLog(@\"%s\",%s%s)", format(systemOutPrintCall
				.getParameters().get(0)), nsString, GeneratorHelper
				.joinParams(systemOutPrintCall.getParameters()));

	}

	private String format(Expression e) {
		if (e.getType().equals(TypeString.INSTANCE)) {
			return "%@";
		} else if (e.getType().equals(TypeInt.INSTANCE)) {
			return "%d";
		} else if (e.getType().equals(TypeChar.INSTANCE)) {
			return "%c";
		} else if (e.getType().equals(TypeDecimal.INSTANCE)) {
			return "%ld";
		}
		return null;
	}

	@Override
	public String getCode(BinaryOperation binaryOp) {
		String left = binaryOp.getLeft().toString();
		String right = binaryOp.getRight().toString();

		if (binaryOp.getOperator() == Operator.PLUS
				&& binaryOp.getType().equals(TypeString.INSTANCE)) {

			String nsStringLeft = (binaryOp.getLeft().getType()
					.equals(TypeString.INSTANCE)
					&& !left.contains("[NSString stringWithFormat:@") ? "@"
					: "");
			String nsStringRight = (binaryOp.getRight().getType()
					.equals(TypeString.INSTANCE)
					&& !right.contains("[NSString stringWithFormat:@") ? "@"
					: "");
			String fleft = format(binaryOp.getLeft());
			String fright = format(binaryOp.getRight());

			return String.format(
					"[NSString stringWithFormat:@\"%s%s\",%s%s,%s%s]", fleft,
					fright, nsStringLeft, left, nsStringRight, right);
		}

		return super.getCode(binaryOp);
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
		return "NSFileManager";
	}

	@Override
	public String getCode(This pthis) {
		return "self";
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
	public String getCode(ClassNew classNew) {

		String init = new String("init");

		boolean b = false;
		Integer numP = 1;

		for (Expression e : classNew.getParameters()) {
			if (!b) {
				init += String.format("WithParam%s:%s ", numP.toString(),
						e.toString());
				b = true;
			} else
				init += String.format("andParam%s:%s ", numP.toString(),
						e.toString());
			numP++;
		}

		return String.format("[[%s alloc]%s]",
				removePointer(classNew.getType()), init);
	}

	@Override
	public String getCode(MethCall methodCall) {
		String arg = new String();
		boolean b = false;
		Integer numP = 1;

		for (Expression e : methodCall.getParameters()) {
			if (!b) {
				arg += String.format("Param%s:%s ", numP.toString(),
						e.toString());
				b = true;
			} else
				arg += String.format("andParam%s:%s ", numP.toString(),
						e.toString());

			numP++;
		}

		if (methodCall.getTarget() instanceof VarAccess) {
			return String.format("[self %s%s]", methodCall.getTarget(), arg);
		}
		return String.format("[%s%s]", methodCall.getTarget(), arg);
	}

	@Override
	public String getCode(Meth meth) {
		String ret = new String();
		String arg = new String();
		String mod = (meth.getModifiers().contains(Modifier.STATIC) ? "+" : "-");
		Integer numP = 1;
		boolean b = false;

		if (!(meth.getType() instanceof TypeNone))
			ret = String.format("(%s)", meth.getType().toString());
		else
			ret = "(void)";

		for (VarDeclaration e : meth.getParams()) {
			if (!b) {
				arg += String.format("Param%s:(%s)%s ", numP.toString(),
						e.getType(), e.getName());
				b = true;
			} else
				arg += String.format("andParam%s:(%s)%s ", numP.toString(),
						e.getType(), e.getName());
			numP++;
		}

		return String.format("%s %s %s%s", mod, ret, meth.getName(), arg);
	}

	@Override
	public String getCode(Constructor cons) {
		String param = new String();
		boolean b = false;
		Integer numP = 1;

		for (VarDeclaration e : cons.getParams()) {
			if (!b) {
				param += String.format("WithParam%s:(%s)%s ", numP.toString(),
						e.getType(), e.getName());
				b = true;
			} else
				param += String.format("andParam%s:(%s)%s ", numP.toString(),
						e.getType(), e.getName());
			numP++;
		}

		return String.format("- (id)init%s", param);
	}

	@Override
	public String getCode(Field field) {
		String out = String.format("%s %s", field.getType(), field.getName());
		if (field.getDefaultValue() != null) {
			out = String.format("%s = %s", out, field.getDefaultValue());
		}
		return out;
	}

}
