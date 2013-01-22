package gool.generator.objc;

import gool.ast.constructs.ArrayNew;
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
import gool.ast.type.TypeClass;
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
		return String
					.format("for(%s %s in %s){%s}",
							enhancedForLoop.getVarDec().getType(),enhancedForLoop.getVarDec().getName(),
							(enhancedForLoop.getExpression().getType() instanceof TypeMap) ? String
									.format("%s.entrySet()",
											enhancedForLoop.getExpression())
									: enhancedForLoop.getExpression(),
							enhancedForLoop.getStatements());
	}

	@Override
	public String getCode(EqualsCall equalsCall) {
		return String.format("[%s isEqual: %s]", equalsCall.getTarget(),
				StringUtils.join(equalsCall.getParameters(), ", "));
	}

	@Override
	public String getCode(ListAddCall lac) {
		String nsObject = ((lac.getParameters().get(0).getType() instanceof PrimitiveType) && !(lac.getParameters().get(0) instanceof VarAccess) && !(lac.getParameters().get(0) instanceof MethCall) ? "@" : "" );
		if (lac.getParameters().size() == 1) {
			if(lac.getParameters().get(0).getType() instanceof PrimitiveType && !(lac.getParameters().get(0).getType() instanceof TypeString)){
				String nsNumber = "[[NSNumber alloc]initWith" + GeneratorHelperObjc.type(lac.getParameters().get(0).getType()) + ":" + lac.getParameters().get(0) + "]";
				return String.format("[%s addObject:%s]", lac.getExpression(),nsNumber);
			}
			return String.format("[%s addObject:%s%s]", lac.getExpression(),nsObject,
					lac.getParameters().get(0));
		} else {
			String s = String.format("[%s arrayWithObjects:%s%s]",
					lac.getExpression(),nsObject,
					StringUtils.join(lac.getParameters(), ", "));
			String str[] = new String[lac.getParameters().size()];
			for (int i = 2; i <= lac.getParameters().size(); i++) {
				nsObject = ((lac.getParameters().get(i).getType() instanceof PrimitiveType) ? "@" : "" );
				str[i] = (String) String.format("[%s addObject:%s%s]%n",
						lac.getExpression(),nsObject, lac.getParameters().get(i));
			}
			return String.format("%s%n%s", s, str);
		}
	}

	@Override
	public String getCode(ListContainsCall lcc) {
		String nsObject = ((lcc.getParameters().get(0).getType() instanceof PrimitiveType) ? "@" : "" );
		return String.format("[%s containsObject:%s%s]", lcc.getExpression(),nsObject,
				lcc.getParameters().get(0));
	}

	@Override
	public String getCode(ListGetCall lgc) {
		return String.format("[%s ObjectsAtIndex:%s]", lgc.getExpression(),
				lgc.getParameters().get(0));
	}

	@Override
	public String getCode(Assign assign) {
		/*Assignation et accès a une table ... langage stupide*/
		
		/*if(assign.getLValue() instanceof ArrayAccess)
			return getCode((ArrayAccess)assign.getLValue(),assign.getValue());*/
		 if (assign.getValue().getType() instanceof TypeString)
			return assign.getLValue() + " = @" + assign.getValue();
		else 
			return super.getCode(assign);	
	}

	@Override
	public String getCode(VarDeclaration varDec) {
		String initialValue = "";
		String type = varDec.getType().toString();
		if (varDec.getInitialValue() != null) {
			if (varDec.getType() instanceof TypeString)
				initialValue = " = @" + varDec.getInitialValue();
			else if(varDec.getType() instanceof TypeChar)
				initialValue = " = '" + varDec.getInitialValue() + "'";
			else
				initialValue = " = " + varDec.getInitialValue();
		}
		if(varDec.getType() instanceof TypeClass 
				|| ( (varDec.getType() instanceof TypeArray) 
						&& (((TypeArray)varDec.getType()).getElementType() instanceof TypeClass ))  )
			type += " *";
		
		if(varDec.getType() instanceof TypeArray)
			return String.format("%s %s[%s]", type, varDec.getName(), ((ArrayNew)varDec.getInitialValue()).getDimesExpressions().get(0));
		
		return String.format("%s %s%s", type, varDec.getName(), initialValue);
		
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
				lrc.getExpression(), lrc.getParameters().get(0));
	}

	@Override
	public String getCode(ListRemoveCall lrc) {
		String nsObject = ((lrc.getParameters().get(0).getType() instanceof PrimitiveType) ? "@" : "" );
		return String.format("[%s removeObject:%s%s]", lrc.getExpression(),nsObject,
				lrc.getParameters().get(0));
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
				"[(NSArray * )%s allKeys count]",
				mapSizeCall.getExpression());
		//TODO
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
	public String getCode(SystemOutPrintCall systemOutPrintCall){
		String nsString = (systemOutPrintCall.getParameters().get(0).getType().equals(TypeString.INSTANCE) 
				&& !(systemOutPrintCall.getParameters().get(0) instanceof VarAccess) 
				&& !systemOutPrintCall.getParameters().get(0).toString().contains("[NSString stringWithFormat:@") ? "@" : "");
		String out = null;
		
		if(systemOutPrintCall.getParameters().get(0).getType() instanceof TypeClass)
			out = "["+ systemOutPrintCall.getParameters().get(0) + " toString]";
		
		return String.format("NSLog(@\"%s\",%s%s)",GeneratorHelperObjc.format(systemOutPrintCall.getParameters().get(0)),nsString,out == null ? GeneratorHelper.joinParams(systemOutPrintCall.getParameters()) : out);
	}
	
	@Override
	public String getCode(BinaryOperation binaryOp) {
		String left = binaryOp.getLeft().toString();
		String right = binaryOp.getRight().toString();
		
		if (binaryOp.getOperator() == Operator.PLUS && binaryOp.getType().equals(TypeString.INSTANCE)) {
			
			String nsStringLeft =  (binaryOp.getLeft().getType().equals(TypeString.INSTANCE) 
					&& !(binaryOp.getLeft() instanceof VarAccess) 
					&& !left.contains("[NSString stringWithFormat:@") 
					? "@" : "");
			String nsStringRight =  (binaryOp.getRight().getType().equals(TypeString.INSTANCE) 
					&& !(binaryOp.getRight() instanceof VarAccess) 
					&& !right.contains("[NSString stringWithFormat:@") 
					? "@" : "");
			
			if(binaryOp.getLeft().getType() instanceof TypeClass)
				left = "["+ left + " toString]";
			if(binaryOp.getRight().getType() instanceof TypeClass)
				right = "["+ right + " toString]";
			
			String fleft = GeneratorHelperObjc.format(binaryOp.getLeft());
			String fright = GeneratorHelperObjc.format(binaryOp.getRight());
			
			return String.format("[NSString stringWithFormat:@\"%s%s\",%s%s,%s%s]",fleft,fright,nsStringLeft,left,nsStringRight,right);
		}
		
		return super.getCode(binaryOp);
	}

	@Override
	public String getCode(ToStringCall tsc) {
		return String.format("[%s toString]", tsc.getTarget());
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
	
	/*@Override
	public String getCode(TypeArray typeArray) {
		return String.format("NSMutableArray *");
	}
	*/
	
	@Override
	public String getCode(TypeArray typeArray) {
		return String.format("%s", typeArray.getElementType());
	}
	
	/*
	@Override
	public String getCode(ArrayNew arrayNew){
		return String.format("[[NSMutableArray alloc]initWithCapacity:%s]", GeneratorHelperObjc.evalIntExpr(arrayNew.getDimesExpressions().get(0)));
	}
	
	private String getCode(ArrayAccess lValue, Expression value) {
		String nsObject = ((value.getType() instanceof PrimitiveType) ? "@" : "" );
		return String.format("[%s replaceObjectAtIndex:%s WithObject:%s%s]",lValue.getExpression(),lValue.getIndex(),nsObject,value.toString());
	}
	
	@Override
	public String getCode(ArrayAccess arrayAccess) {
		return String.format("[%s objectAtIndex: %s]", arrayAccess.getExpression(), arrayAccess.getIndex());
	}*/

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
		
		for(Expression e : classNew.getParameters()) {
			if(!b){
				init += String.format("WithParam%s:%s ", numP.toString(), e.toString());
				b = true;
			}
			else
				init += String.format("andParam%s:%s ", numP.toString(), e.toString());
			numP++;
		}
		
		return String.format("[[%s alloc]%s]", removePointer(classNew.getType()), init);
	}
	
	@Override
	public String getCode(MethCall methodCall) {
		String arg = new String();
		boolean b = false;
		Integer numP = 1;
	
		for(Expression e : methodCall.getParameters()) {
			if(!b){
				arg += String.format("Param%s:%s ", numP.toString(), e.toString());
				b = true;
			}
			else
				arg += String.format("andParam%s:%s ", numP.toString(), e.toString());
			
			numP++;
		}
		
		if(methodCall.getTarget() instanceof VarAccess){
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

		for(VarDeclaration e : meth.getParams()) {
			if(!b){
				arg += String.format("Param%s:(%s)%s ", numP.toString(), e.getType(), e.getName());
				b = true;
			}
			else
				arg += String.format("andParam%s:(%s)%s ", numP.toString(), e.getType(), e.getName());
			numP++;
		}
		
		return String.format("%s %s %s%s", mod, ret, meth.getName(),arg);
	}

	@Override
	public String getCode(Constructor cons) {
		String param = new String();
		boolean b = false;
		Integer numP = 1;
		
		for(VarDeclaration e : cons.getParams()) {
			if(!b){
				param += String.format("WithParam%s:(%s)%s ", numP.toString(), e.getType(), e.getName());
				b = true;
			}
			else
				param += String.format("andParam%s:(%s)%s ", numP.toString(), e.getType(), e.getName());
			numP++;
		}

		return String.format("- (id)init%s", param);
	}
	
	@Override
	public String getCode(Field field) {
		String out = String.format("%s %s",field.getType(), field.getName());
		if (field.getDefaultValue() != null) {
			out = String.format("%s = %s", out, field.getDefaultValue());
		}
		return out;
	}

}
