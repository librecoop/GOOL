package gool.generator.objc; 

import gool.ast.constructs.ArrayAccess;
import gool.ast.constructs.ArrayNew;
import gool.ast.constructs.Assign;
import gool.ast.constructs.BinaryOperation;
import gool.ast.constructs.Catch;
import gool.ast.constructs.ClassNew;
import gool.ast.constructs.Constructor;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.Expression;
import gool.ast.constructs.Field;
import gool.ast.constructs.FieldAccess;
import gool.ast.constructs.Finally;
import gool.ast.constructs.Language;
import gool.ast.constructs.MainMeth;
import gool.ast.constructs.MemberSelect;
import gool.ast.constructs.Meth;
import gool.ast.constructs.MethCall;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.Operator;
import gool.ast.constructs.ParentCall;
import gool.ast.constructs.Return;
import gool.ast.constructs.This;
import gool.ast.constructs.ThisCall;
import gool.ast.constructs.Throw;
import gool.ast.constructs.ToStringCall;
import gool.ast.constructs.Try;
import gool.ast.constructs.TypeDependency;
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
import gool.ast.type.TypeException;
import gool.ast.type.TypeFile;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeMethod;
import gool.ast.type.TypeNone;
import gool.ast.type.TypeNull;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeString;
import gool.generator.GeneratorHelper;
import gool.generator.common.CommonCodeGenerator;
import gool.methods.MethodManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	public String getCode(EnhancedForLoop enhancedForLoop) {
		return String
					.format("for(%s %s in %s){\n\t%s}",
							enhancedForLoop.getVarDec().getType(),enhancedForLoop.getVarDec().getName(),
							(enhancedForLoop.getExpression().getType() instanceof TypeMap) ? String
									.format("%s.entrySet()",
											enhancedForLoop.getExpression())
									: enhancedForLoop.getExpression(),
							enhancedForLoop.getStatements());
	}

//List Methods
	@Override
	public String getCode(ListAddCall lac) {
		String nsObject = GeneratorHelperObjc.staticString(lac.getParameters().get(0));
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
				nsObject = GeneratorHelperObjc.staticString(lac.getParameters().get(0));
				str[i] = (String) String.format("[%s addObject:%s%s]\n",
						lac.getExpression(),nsObject, lac.getParameters().get(i));
			}
			return String.format("%s\n%s", s, str);
		}
	}

	@Override
	public String getCode(ListContainsCall lcc) {
		String param0 = GeneratorHelperObjc.initWithObject(lcc.getParameters().get(0));
		
		return String.format("[%s containsObject:%s]", lcc.getExpression(),param0);
	}

	@Override
	public String getCode(ListGetCall lgc) {
		return String.format("[%s objectsAtIndex:%s]", lgc.getExpression(),
				lgc.getParameters().get(0));
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
		String param0 = GeneratorHelperObjc.initWithObject(lrc.getParameters().get(0));
		
		return String.format("[%s removeObject:%s]", lrc.getExpression(),param0);
	}

	@Override
	public String getCode(ListSizeCall lsc) {
		return String.format("[%s count]", lsc.getExpression());
	}

///////////////
	
//Map Methods

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		/*return String.format("[[%s objectForKey:@%s]isEqualToString:@" + "("
				+ "null" + ")" + "]", mapContainsKeyCall.getExpression(),
				mapContainsKeyCall.getParameters());*/ // TODO
		return " /* MapContainsKeyCall non implemente */ ";
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
		String param0 = GeneratorHelperObjc.initWithObject(mapGetCall.getParameters().get(0));
	
		return String.format("[%s objectForKey:%s]",mapGetCall.getExpression(),param0);
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
		String param0 = GeneratorHelperObjc.initWithObject(mapPutCall.getParameters().get(0));
		String param1 = GeneratorHelperObjc.initWithObject(mapPutCall.getParameters().get(1));
		
		return String.format("[%s setObject:%s forKey:%s]",mapPutCall.getExpression(),param0,param1);
	}

	@Override
	public String getCode(MapRemoveCall mapRemoveCall) {
		String param = GeneratorHelperObjc.initWithObject(mapRemoveCall.getParameters().get(0));
		
		return String.format("[%s removeObjectForKey:%s]",mapRemoveCall.getExpression(),param);
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		return String.format(
				"[(NSArray * )%s allKeys count]",
				mapSizeCall.getExpression());
		//TODO
	}
	
///////////////
	
//Meth Methods
	
	@Override
	public String getCode(MainMeth mainMeth) {
		return "int main(int argc, const char * argv[])";
	}
	
	@Override
	public String getCode(ClassNew classNew) {
		
		String init = new String("initWith");
		
		boolean b = false;
		
		for(Expression e : classNew.getParameters()) {
			String nsString = GeneratorHelperObjc.staticStringMini(e);
			if(!b){
				init += String.format("%s:%s%s ", removePointer(e.getType()), nsString, e.toString());
				b = true;
			}
			else
				init += String.format("and%s:%s%s ", removePointer(e.getType()), nsString, e.toString());
		}
		
		return String.format("[[%s alloc]%s]", removePointer(classNew.getType()), init);
	}
	
	//retourne l'expression appellant la méthode
	private Expression getTarget(Object n){
		if(n instanceof MemberSelect){
			return ((MemberSelect)n).getTarget();
		}else if(n instanceof MethCall){
			return ((MethCall)n).getTarget();
		}else if(n instanceof FieldAccess){
			return ((FieldAccess)n).getTarget();
		}else if(n instanceof EqualsCall){
			return ((EqualsCall)n).getTarget();
		}else 
			return null;
	}
	
	//génère la liste des paramètres pour l'appel d'une méthode
	private String getMethCallName(List<Expression> param, boolean typeFirstParam){
		String arg = "";
		String p;
		String nsString;
		boolean b = false;
		
		for(Expression e : param) {
			nsString = GeneratorHelperObjc.staticStringMini(e);
			if(e.getType() == TypeChar.INSTANCE) p = "'"+e.toString()+"'";
			else p = e.toString();
			
			if(!b){
				if(typeFirstParam) arg += String.format("%s:%s%s ", removePointer(e.getType()), nsString, p);
				else arg += String.format("%s%s ", nsString, p);
					
				b = true;
			}
			else
				arg += String.format("and%s:%s%s ", removePointer(e.getType()), nsString, p);
		}
		
		return arg;
	}
	
	//Génère la liste des paramètres pour la définition d'une méthode
	private String getMethDefName(List<VarDeclaration> param){
		String arg = "";
		boolean b = false;
		
		for(VarDeclaration e : param) {
			if(!b){
				arg += String.format("%s:(%s)%s ", removePointer(e.getType()), e.getType(), e.getName());
				b = true;
			}
			else
				arg += String.format("and%s:(%s)%s ", removePointer(e.getType()), e.getType(), e.getName());
		}
		
		return arg;
	}
	
	private String methUnknow(String generalName, String library){
		return String.format("/* La méthode %s de la bibliothèque %s n'est pas implémenté pour le langage */", generalName.replaceAll("\\s", ""), library);
	}
	
	//Génère l'appel d'une méthode généré automatiquement
	private String methPerso(MethCall methodCall, String specificName){
		ArrayList<String> typeParam = new ArrayList<String>();
		ArrayList<Expression> param = new ArrayList<Expression>();
		String arg;
		Expression firstParam = getTarget(methodCall.getTarget());
		
		if(MethodManager.isParam(firstParam,specificName)){
			param.add(firstParam);
			typeParam.add(firstParam.getType().toString());
		}
		
		for (Expression s : methodCall.getParameters()) {
			if(MethodManager.isParam(s,specificName)){
				typeParam.add(s.getType().toString());
				param.add(s);
			}
		}
		
		MethodManager.addMeth(methodCall.getGeneralName(), specificName, methodCall.getLibrary(), typeParam);
		
		arg = getMethCallName(param, false);		
		
		return String.format("[%sOBJC %s:%s]",
				methodCall.getLibrary(),
				methodCall.getGeneralName(),
				arg);
	}
	
	//Génère l'appel d'une méthode présente dans le langage
	private String methKnow(MethCall methodCall, String specificName){
		specificName = specificName.substring(0,specificName.indexOf("("));
		String p;
		String arg = "";
		
		for(Expression e : methodCall.getParameters()) {
			String nsString = GeneratorHelperObjc.staticStringMini(e);
			String partName = "";
			if(specificName.indexOf("/")==-1)
				partName = specificName;
			else {
				partName = specificName.substring(0, specificName.indexOf("/")); 
				specificName = specificName.substring(specificName.indexOf("/")+1);
			}
			if(e.getType() == TypeChar.INSTANCE)
				p = "'"+e.toString()+"'";
			else
				p = e.toString();
			arg += String.format("%s:%s%s ", partName, nsString, p);
		}
		
		if(methodCall.getParameters().size() == 0)
			arg = specificName;
		
		return String.format("[%s %s]", getTarget(methodCall.getTarget()), arg);
	}
	
	@Override
	public String getCode(MethCall methodCall) {
		String arg = new String();
		String specificName;
		

		if(methodCall.getGeneralName() != null){ //La méthode n'est pas une méthode appartenant aux classes du projet
			specificName = MethodManager.getSpecificName(methodCall.getGeneralName(),methodCall.getLibrary(),Language.OBJC);
			if(MethodManager.isAbsent(specificName)){ //La méthode n'a pas de correspondance
				return methUnknow(methodCall.getGeneralName(), methodCall.getLibrary());
			}
			else if(MethodManager.isMethPerso(specificName)){ 	//La méthode à une correspondance mais n'est pas une méthode standard du langage. 
				return methPerso(methodCall, specificName);		//elle sera généré dans un fichier a part
			}
			else {
				return methKnow(methodCall, specificName);	//La méthode à une correspondance direct dans le langage
			}
		}
		else {	//La méthode appartient aux méthodes du projet
			arg = getMethCallName(methodCall.getParameters(), true);
			
			if(methodCall.getTarget() instanceof VarAccess)
				return String.format("[self %s%s]", methodCall.getTarget(), arg);
			if(methodCall.getTarget() instanceof ParentCall)
				return getCode((ParentCall)methodCall.getTarget());
			return String.format("[%s%s]", methodCall.getTarget(), arg);
		}		
		
	}
	
	@Override
	public String getCode(Meth meth) {
		String ret = new String();
		String arg = new String();
		String mod = (meth.getModifiers().contains(Modifier.STATIC) ? "+" : "-");

		if (!(meth.getType() instanceof TypeNone))
			ret = String.format("(%s)", meth.getType().toString());
		else
			ret = "(void)";
		
		arg = getMethDefName(meth.getParams());
		
		return String.format("%s %s %s%s", mod, ret, meth.getName(),arg);
	}

	@Override
	public String getCode(Constructor cons) {
		String param = new String();
		
		param = getMethDefName(cons.getParams());

		return String.format("- (%s *)initWith%s",cons.getClassDef().getName(), param);
	}
	
	@Override
	public String getCode(SystemOutPrintCall systemOutPrintCall){
		String nsString = GeneratorHelperObjc.staticString(systemOutPrintCall.getParameters().get(0));
		
		
		String out = null;
		String format = null;
		
		if(systemOutPrintCall.getParameters().get(0).getType() instanceof TypeClass)
			out = "["+ systemOutPrintCall.getParameters().get(0) + " toString]";
		
		
		format = (systemOutPrintCall.getParameters().get(0) instanceof ArrayAccess) && ((ArrayAccess)systemOutPrintCall.getParameters().get(0)).getExpression().getType() instanceof TypeArray ? 
				GeneratorHelperObjc.format(((TypeArray)((ArrayAccess)systemOutPrintCall.getParameters().get(0)).getExpression().getType()).getElementType()) : 
				GeneratorHelperObjc.format(systemOutPrintCall.getParameters().get(0));
		
		//I added the two following lines to fix a bug dealing with
		//non-string parameters in NSLog calls.
		if(!format.equals("%@"))
			nsString="";
		
		return String.format("NSLog(@\"%s\",%s%s)",format,nsString,out == null ? GeneratorHelper.joinParams(systemOutPrintCall.getParameters()) : out);
	}
	
	@Override
	public String getCode(ToStringCall tsc) {
		return String.format("[%s toString]", tsc.getTarget());
	}
	
	@Override
	public String getCode(EqualsCall equalsCall) {
		String arg = getMethCallName(equalsCall.getParameters(), false);
		return String.format("[%s isEqual: %s]", equalsCall.getTarget(),arg);
	}
	
	@Override
	public String getCode(ParentCall parentCall) {
		String out = "self = [super init";
		
		out += getMethCallName(parentCall.getParameters(), true);
		
		out += "]";
		return out;
	}
	
	@Override
	public String getCode(ThisCall thisCall) {
		return "self()";
	}
	
	@Override
	public String getCode(Return returnExpr) {
		String nsString = GeneratorHelperObjc.staticString(returnExpr.getExpression());
		return String.format("return (%s%s)", nsString, returnExpr.getExpression());
	}
	
///////////////
	
	
//Type Methods
	
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
		return String.format("[dictionary setObject:@%s forKey:@%s]", typeEntry.getElementType(), typeEntry.getKeyType());
	}

	public String getCode(TypeObject typeObject) {
		// type java.object
		// return NSObject
		return "id";
	}

	@Override
	public String getCode(TypeInt typeInt) {
		return "int";
	}
	
	@Override
	public String getCode(TypeArray typeArray) {
		return String.format("%s", typeArray.getElementType());
	}

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
		return "NSMutableString *";
	}
	
	@Override
	public String getCode(TypeChar typeChar) {
		return "char";
	}

	@Override
	public String getCode(TypeFile typeFile) {
		return "NSFileManager *";
	}

	@Override
	public String getCode(This pthis) {
		return "self";
	}
	
	@Override
	public String getCode(TypeNull typeNull) {
		return "nil";
	}
	
	@Override
	public String getCode(TypeClass typeClass) {
		String pointer = typeClass.isEnum() ? "" : "*";
		return super.getCode(typeClass) + pointer;
	}
	
	@Override
	public String getCode(TypeDependency typeDependency) {
		if (typeDependency.getType() instanceof TypeList) {
			return "Foundation/Foundation.h";
		}
		if (typeDependency.getType() instanceof TypeMap) {
			return "Foundation/Foundation.h";
		}
		if (typeDependency.getType() instanceof TypeEntry) {
			return "Foundation/Foundation.h";
		}
		if (typeDependency.getType() instanceof TypeFile) {
			return "Foundation/Foundation.h";
		}
		if (typeDependency.getType() instanceof TypeString) {
			return "Foundation/Foundation.h";
		}
		if (typeDependency.getType() instanceof TypeBool) {
			return "noprint";
		}
		if (typeDependency.getType() instanceof TypeInt) {
			return "noprint";
		}
		if (typeDependency.getType() instanceof TypeException) {
			return "Foundation/Foundation.h";
		}
		return removePointer(super.getCode(typeDependency)).concat(".h");
	}

///////////////
	
//Dependency methods
	
	@Override
	public String getCode(SystemOutDependency systemOutDependency) {
		return "Foundation/Foundation.h";
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

	
///////////////
	
	@Override
	public String getCode(Assign assign) {		
		return assign.getLValue() + " = " + GeneratorHelperObjc.staticStringMini(assign.getValue()) + assign.getValue();
	}

	@Override
	public String getCode(VarDeclaration varDec) {
		
		//TODO Si une variable se nomme id .... fail
		
		String initialValue = "";
		String type = varDec.getType().toString();
		if (varDec.getInitialValue() != null) {
			if (varDec.getInitialValue().getType() instanceof TypeNull)
				initialValue = " = nil"; //TODO pas normal, nil est mit dans le type de la valeur initital et pas dans la valeur initiale
			else if (varDec.getInitialValue().getType() instanceof TypeString && !(varDec.getInitialValue() instanceof MethCall))
				initialValue = " = @" + varDec.getInitialValue();
			else if(varDec.getInitialValue().getType() instanceof TypeChar && !(varDec.getInitialValue() instanceof MethCall))
				initialValue = " = '" + varDec.getInitialValue() + "'";
			else
				initialValue = " = " + varDec.getInitialValue();
		}
		
		if(varDec.getType() instanceof TypeArray)
			return String.format("%s %s[%s]", type, varDec.getName(), ((ArrayNew)varDec.getInitialValue()).getDimesExpressions().get(0));
		
		return String.format("%s %s%s", type, varDec.getName(), initialValue);
		
	}
	
	@Override
	public String getCode(BinaryOperation binaryOp) {
		String left = binaryOp.getLeft().toString();
		String right = binaryOp.getRight().toString();
		
		if (binaryOp.getOperator() == Operator.PLUS && binaryOp.getType().equals(TypeString.INSTANCE)) {
			
			String nsStringLeft =  GeneratorHelperObjc.staticString(binaryOp.getLeft());
			String nsStringRight =  GeneratorHelperObjc.staticString(binaryOp.getRight());
			
			if(binaryOp.getLeft().getType() instanceof TypeClass)
				left = "["+ left + " toString]";
			if(binaryOp.getRight().getType() instanceof TypeClass)
				right = "["+ right + " toString]";
			
			String fleft = (binaryOp.getLeft() instanceof ArrayAccess) && ((ArrayAccess)binaryOp.getLeft()).getExpression().getType() instanceof TypeArray ? 
					GeneratorHelperObjc.format(((TypeArray)((ArrayAccess)binaryOp.getLeft()).getExpression().getType()).getElementType()) : 
					GeneratorHelperObjc.format(binaryOp.getLeft());
			String fright = (binaryOp.getRight() instanceof ArrayAccess) && ((ArrayAccess)binaryOp.getRight()).getExpression().getType() instanceof TypeArray ? 
					GeneratorHelperObjc.format(((TypeArray)((ArrayAccess)binaryOp.getRight()).getExpression().getType()).getElementType()) : 
					GeneratorHelperObjc.format(binaryOp.getRight());
			
			return String.format("[NSString stringWithFormat:@\"%s%s\",%s%s,%s%s]",fleft,fright,nsStringLeft,left,nsStringRight,right);
		}
		
		return super.getCode(binaryOp);
	}

	@Override
	public String getCode(MemberSelect memberSelect) {
		String target = memberSelect.getTarget().toString().equals("this") ? "self" : memberSelect.getTarget().toString();
		String sep;
		
		if(memberSelect.getType() instanceof TypeMethod)  sep = " ";
		else  sep = "->"; 
		
		return String.format("%s%s%s", target, sep, memberSelect.getIdentifier());
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
	public String getCode(Field field) {
		String out = String.format("%s %s",field.getType(), field.getName());
		if (field.getDefaultValue() != null) {
			out = String.format("%s = %s", out, field.getDefaultValue());
		}
		return out;
	}

	@Override
	public String getCode(TypeException typeException) {
		return "NSException *";
	}

	@Override
	public String getCode(Catch catchBlock) {
		return "@catch";
	}

	@Override
	public String getCode(Try tryBlock) {
		return "@try ";
	}

	@Override
	public String getCode(Throw throwexpression) {
		return "@throw";
	}

	@Override
	public String getCode(Finally finalyBlock) {
		return "@finally";
	}
}
