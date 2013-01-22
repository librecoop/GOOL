package gool.generator.python;

import gool.ast.constructs.ArrayNew;
import gool.ast.constructs.BinaryOperation;
import gool.ast.constructs.Block;
import gool.ast.constructs.CastExpression;
import gool.ast.constructs.ClassDef;
import gool.ast.constructs.ClassFree;
import gool.ast.constructs.ClassNew;
import gool.ast.constructs.Comment;
import gool.ast.constructs.CompoundAssign;
import gool.ast.constructs.Constant;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.Expression;
import gool.ast.constructs.ExpressionUnknown;
import gool.ast.constructs.Field;
import gool.ast.constructs.For;
import gool.ast.constructs.If;
import gool.ast.constructs.MainMeth;
import gool.ast.constructs.MapEntryMethCall;
import gool.ast.constructs.MapMethCall;
import gool.ast.constructs.MemberSelect;
import gool.ast.constructs.Meth;
import gool.ast.constructs.MethCall;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.NewInstance;
import gool.ast.constructs.Operator;
import gool.ast.constructs.ParentCall;
import gool.ast.constructs.Return;
import gool.ast.constructs.Statement;
import gool.ast.constructs.This;
import gool.ast.constructs.ThisCall;
import gool.ast.constructs.ToStringCall;
import gool.ast.constructs.TypeDependency;
import gool.ast.constructs.UnaryOperation;
import gool.ast.constructs.VarAccess;
import gool.ast.constructs.VarDeclaration;
import gool.ast.constructs.While;
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
import gool.ast.type.TypeArray;
import gool.ast.type.TypeBool;
import gool.ast.type.TypeByte;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeNone;
import gool.ast.type.TypeNull;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeString;
import gool.ast.type.TypeUnknown;
import gool.ast.type.TypeVoid;
import gool.generator.GeneratorHelper;
import gool.generator.common.CommonCodeGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logger.Log;

import org.apache.commons.lang.StringUtils;

public class PythonGenerator extends CommonCodeGenerator {
	
	public PythonGenerator() {
		super();
		indentation = "    ";
	}
	

	
	private ClassDef currentClass;
	
	private ArrayList<String> comments = new ArrayList<String>();
	
	private ArrayList<String> paramsMethCurrent = new ArrayList<String>();
	
	private void comment(String newcomment) {
		String com = "# " + newcomment + "\n";
		if (! comments.contains(com))
			comments.add(com);
	}
	
	private String printWithComment(Object statement) {
		String sttmnt = statement.toString().replaceFirst("\\s*\\z", "");
		if (comments.size() == 1 && ! sttmnt.contains("\n")) {
			sttmnt += " " + comments.get(0);
		} else {
			sttmnt = StringUtils.join(comments,"") + sttmnt + "\n";
		}
		comments.clear();
		return sttmnt;
	}

	private static Map<Meth, String> methodsNames = new HashMap<Meth, String>();
	
	private static Map<String, Dependency> customDependencies = new HashMap<String, Dependency>();

	private String getName(Meth meth) {
		return methodsNames.get(meth);
	}
	
	@Override
	public void addCustomDependency(String key, Dependency value) {
		customDependencies.put(key, value);
	}
	
	@Override
	public String getCode(ArrayNew arrayNew) {
		String ret = arrayNew.getType() + "()";
		for (Expression e : arrayNew.getDimesExpressions())
			ret = String.format("[%s]*%s", ret, e);
		return "(" + ret + ")";
	}
	
	@Override
	public String getCode(Block block) {
		StringBuilder result = new StringBuilder();
		for (Statement statement : block.getStatements()) {
			result.append(printWithComment(statement));
		}
		return result.toString();
	}
	
	@Override
	public String getCode(BinaryOperation binaryOp) {
		String textualOp;
		switch (binaryOp.getOperator()) {
		case AND :
			textualOp = "and";
			break;
		case OR :
			textualOp = "or";
			break;
		case DIV :
			if (binaryOp.getType().equals(TypeInt.INSTANCE))
				textualOp = "//";
			else
				textualOp = "/";
			break;
		case PLUS :
			if(binaryOp.getLeft().getType().getName().equals("str") && !binaryOp.getRight().getType().getName().equals("str")) {	
				return String.format("(%s %s str(%s))", binaryOp.getLeft(), "+", binaryOp.getRight());
			}
			else if(binaryOp.getRight().getType().getName().equals("str") && !binaryOp.getLeft().getType().getName().equals("str")) {	
				return String.format("(str(%s) %s %s)", binaryOp.getLeft(), "+", binaryOp.getRight());
			}
		default :
			textualOp = binaryOp.getTextualoperator();
		}
		if(binaryOp.getOperator().equals(Operator.UNKNOWN))
			comment("Unrecognized by GOOL, passed on: " + textualOp);
		return String.format("(%s %s %s)", binaryOp.getLeft(), textualOp, binaryOp.getRight());
	}
 
	@Override
	public String getCode(Constant constant) {
		if (constant.getType().equals(TypeBool.INSTANCE)) {
			return String.valueOf(constant.getValue().toString().equalsIgnoreCase("true") ? "True" : "False");
		} else {
			return super.getCode(constant);
		}
	}

	@Override
	public String getCode(CastExpression cast) {
		return String.format("%s(%s)", cast.getType(), cast
				.getExpression());
	}

	@Override
	public String getCode(ClassNew classNew) {
		return String.format("%s(%s)", classNew.getName(), StringUtils
				.join(classNew.getParameters(), ", "));
	}

	@Override
	public String getCode(Comment comment) {
		return comment.getValue().replaceAll("(^ *)([^ ])", "$1# $2");
	}
	
	@Override
	public String getCode(EnhancedForLoop enhancedForLoop) {
		this.paramsMethCurrent.add(enhancedForLoop.getVarDec().getName());
		if(enhancedForLoop.getExpression().getType() instanceof TypeMap)
			return formatIndented("for %s in %s.iteritems():%1", enhancedForLoop.getVarDec().getName(),
				enhancedForLoop.getExpression() ,enhancedForLoop.getStatements());
		return formatIndented("for %s in %s:%1", enhancedForLoop.getVarDec().getName(),
				enhancedForLoop.getExpression() ,enhancedForLoop.getStatements());
	}

	@Override
	public String getCode(EqualsCall equalsCall) {
		return String.format("%s == %s", equalsCall.getTarget(), equalsCall.getParameters().get(0));
	}

	@Override
	public String getCode(Field field) {
		String value;
		if (field.getDefaultValue() != null) {
			value = field.getDefaultValue().toString();
		}
		else {
			value = "None";
		}
		return printWithComment(String.format("%s = %s\n", field.getName(), value));
	}

	@Override
	public String getCode(For forr) {
		return formatIndented("%swhile %s:%1",
				printWithComment(forr.getInitializer()),forr.getCondition(),
				forr.getWhileStatement().toString() + printWithComment(forr.getUpdater()));
	}

	@Override
	public String getCode(If pif) {
		String out = formatIndented ("if %s:%1", pif.getCondition(), pif.getThenStatement());
		if (pif.getElseStatement() != null){
			if (pif.getElseStatement() instanceof If) {
				out += formatIndented ("el%s", pif.getElseStatement());
			} else {
				out += formatIndented ("else:%1", pif.getElseStatement());
			}
		}
		return out;
	}

	@Override
	public String getCode(Collection<Modifier> modifiers) {
		return "";
	}

	@Override
	public String getCode(ListAddCall lac) {
		switch (lac.getParameters().size()) {
		case 1:
			return String.format("%s.append(%s)",
					lac.getExpression(), lac.getParameters().get(0));
		case 2:
			return String.format("%s.insert(%s, %s)",
					lac.getExpression(), lac.getParameters().get(1), lac.getParameters().get(0));
		default:
			comment ("Unrecognized by GOOL, passed on: add");
			return String.format("%s.add(%s)",
					lac.getExpression(), StringUtils.join(lac.getParameters(), ", "));
		}
	}

	@Override
	public String getCode(ListContainsCall lcc) {
		return String.format("%s in %s", lcc.getParameters().get(0), lcc.getExpression());
	}

	@Override
	public String getCode(ListGetCall lgc) {
		return String.format("%s[%s]", lgc.getExpression(), lgc.getParameters().get(0));
	}

	@Override
	public String getCode(ListGetIteratorCall lgic) {
		return String.format("iter(%s)", lgic.getExpression());
	}

	@Override
	public String getCode(ListIsEmptyCall liec) {
		return String.format("(not %s)", liec.getExpression());
	}

	@Override
	public String getCode(ListRemoveAtCall lrc) {
		return String.format("%s.pop(%s)", lrc.getExpression(), StringUtils
				.join(lrc.getParameters(), ", "));
	}

	@Override
	public String getCode(ListRemoveCall lrc) {
		if (lrc.getType().getTypeArguments().contains("[Int]"))		
		return String.format("%s.remove(%s)", lrc.getExpression(), StringUtils
				.join(lrc.getParameters(), ", "));
		else
			return String.format("%s.pop(%s)", lrc.getExpression(), StringUtils
					.join(lrc.getParameters(), ", "));
	}

	@Override
	public String getCode(ListSizeCall lsc) {
		return String.format("len(%s)", lsc.getExpression());
	}

	@Override
	public String getCode(MainMeth mainMeth) {
		return mainMeth.getBlock().toString();
	}

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		return String.format("%s in %s",
				mapContainsKeyCall.getParameters().get(0), mapContainsKeyCall.getExpression());
	}

	@Override
	public String getCode(MapEntryGetKeyCall mapEntryGetKeyCall) {
		return String.format("%s[0]", mapEntryGetKeyCall.getExpression());
	}

	@Override
	public String getCode(MapEntryGetValueCall mapEntryGetKeyCall) {
		return String.format("%s[1]", mapEntryGetKeyCall.getExpression());
	}

	@Override
	public String getCode(MapEntryMethCall mapEntryMethCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MapGetCall mapGetCall) {
		return String.format("%s[%s]", mapGetCall.getExpression(), mapGetCall.getParameters().get(0));
	}

	@Override
	public String getCode(MapGetIteratorCall mapGetIteratorCall) {
		return String.format("iter(%s)", mapGetIteratorCall.getExpression());
	}

	@Override
	public String getCode(MapIsEmptyCall mapIsEmptyCall) {
		return String.format("(not %s)", mapIsEmptyCall.getExpression());
	}

	@Override
	public String getCode(MapMethCall mapMethCall) {
		return String.format("%s[%s])", mapMethCall.getExpression(),
				mapMethCall.getParameters().get(0));
	}

	@Override
	public String getCode(MapPutCall mapPutCall) {
		return String.format("%s[%s] = %s", mapPutCall.getExpression(), 
				mapPutCall.getParameters().get(0), mapPutCall.getParameters().get(1));
	}

	@Override
	public String getCode(MapRemoveCall mapRemoveCall) {
		return String.format("%s.pop(%s)", mapRemoveCall.getExpression(),
				StringUtils.join(mapRemoveCall.getParameters(), ", "));
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		return String.format("len(%s)", mapSizeCall.getExpression());
	}

	private String printMeth(Meth meth, String prefix){
		paramsMethCurrent.clear();
		for (VarDeclaration p : meth.getParams()) {
			paramsMethCurrent.add(p.getName());
		}
		if (prefix == "" && meth.getBlock().getStatements().isEmpty())
			prefix = "pass";
		return formatIndented("%sdef %s(self%s):%1",
				meth.getModifiers().contains(Modifier.STATIC)?"@classmethod\n":"",
				methodsNames.get(meth),
				methodsNames.get(meth).equals(meth.getName())?", *args":printMethParamsNames(meth),
				prefix + meth.getBlock());
	}
	
	private String printMethParamsNames(Meth meth) {
		if (meth.getParams().isEmpty())
			return "";
		String ret = "";
		for (VarDeclaration p : meth.getParams()) {
			ret += ", " + p.getName();
		}
		return ret;
	}
	
	private String printMethParamsTypes(Meth meth) {
		String ret = "";
		for (VarDeclaration p : meth.getParams()) {
			ret += ", " + p.getType();
		}
		return ret;
	}
	
	@Override
	public String getCode(Meth meth) {
		return printMeth(meth, "");
	}
	
	@Override
	public String getCode(MethCall methodCall) {
		return String.format("%s (%s)",
				methodCall.getTarget().toString(),
				StringUtils.join(methodCall.getParameters(), ", "));
	}

	@Override
	public String getCode(VarAccess varAccess) {
		String name = varAccess.getDec().getName();
		if(varAccess.getType() == null || varAccess.getType().getName().isEmpty()) {
			return name;
		}
		if (name.equals("super") && currentClass != null)
			return String.format("super(%s, self)", currentClass.getName());
		if (name.equals("this"))
			return "self";

		if (varAccess.getDec().getModifiers().contains(Modifier.PRIVATE)
				&& ! name.startsWith("__"))
			name = "__" + name;
		if (paramsMethCurrent.contains(name))
			return name;
		else
			return "self." + name;
			
	}

	@Override
	public String getCode(MemberSelect memberSelect) {
		String target = memberSelect.getTarget().toString();
		if (target.equals("this"))
			target = "self";
		return String.format("%s.%s", target, memberSelect.getIdentifier());
	}
	
	@Override
	public String getCode(Modifier modifier) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(NewInstance newInstance) {
		return String.format("%s = %s( %s )", newInstance.getVariable(),
				newInstance.getVariable().getType().toString().replaceAll(
						"\\*$", ""), StringUtils.join(newInstance
						.getParameters(), ", "));
	}

	@Override
	public String getCode(ParentCall parentCall) {
		String out = parentCall.getTarget() + "(";
		if (parentCall.getParameters() != null) {
			out += StringUtils.join(parentCall.getParameters(), ", ");
		}
		out += ")";
		return out;
	}

	@Override
	public String getCode(Return returnExpr) {
		return String.format("return %s", returnExpr.getExpression());
	}

	@Override
	public String getCode(SystemOutDependency systemOutDependency) {
		return "noprint";
	}

	@Override
	public String getCode(SystemOutPrintCall systemOutPrintCall) {
		return String.format("print %s", StringUtils.join(
				systemOutPrintCall.getParameters(), ","));
	}

	@Override
	public String getCode(This pthis) {
		return "self";
	}

	@Override
	public String getCode(ThisCall thisCall) {
		return String.format("self.__init__(%s)", GeneratorHelper.joinParams(thisCall.getParameters()));
	}

	@Override
	public String getCode(ToStringCall tsc) {
		return String.format("str(%s)", tsc.getTarget());
	}

	@Override
	public String getCode(TypeBool typeBool) {
		return "bool";
	}

	@Override
	public String getCode(TypeByte typeByte) {
		return "bytearray";
	}
	
	@Override
	public String getCode(TypeDecimal typeReal) {
		return "float";
	}

	@Override
	public String getCode(TypeDependency typeDependency) {

		if(typeDependency.getType() instanceof TypeInt){
			return "noprint";
		}
		if(typeDependency.getType() instanceof TypeString){
			return "noprint";
		}
		if(typeDependency.getType() instanceof TypeList){
			return "noprint";
		}
		if(typeDependency.getType() instanceof TypeMap){
			return "noprint";
		}
		if(typeDependency.getType() instanceof TypeEntry)
			return "noprint";
		return super.getCode(typeDependency);
	}

	@Override
	public String getCode(TypeEntry typeEntry) {
		return String.format("(%s, %s)",typeEntry.getKeyType(), typeEntry.getElementType() );
	}

	@Override
	public String getCode(TypeInt typeInt) {
		return "int";
	}

	@Override
	public String getCode(TypeList typeList) {
		return "list";
	}

	@Override
	public String getCode(TypeMap typeMap) {
		return "dict";
	}

	@Override
	public String getCode(TypeNone type) {
		return "None";
	}

	@Override
	public String getCode(TypeNull type) {
		return "None";
	}

	@Override
	public String getCode(TypeObject typeObject) {
		return "object";
	}

	@Override
	public String getCode(TypeString typeString) {
		return "str";
	}

	@Override
	public String getCode(TypeVoid typeVoid) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(UnaryOperation unaryOperation) {
		switch (unaryOperation.getOperator()){
// BUG : python is call-by-value, increment and decrement do not update the variable
// TODO: find a way to express this in python
//		case POSTFIX_INCREMENT:
//			comment("GOOL warning: post-incrementation became pre-incrementation");
//			// no break: follow to the next case
//		case PREFIX_INCREMENT:
//			return String.format("goolHelper.increment(%s)", unaryOperation.getExpression());
//		case POSTFIX_DECREMENT:
//			comment("GOOL warning: post-decrementation became pre-decrementation");
//			// no break: follow to the next case
//		case PREFIX_DECREMENT:
//			return String.format("goolHelper.decrement(%s)", unaryOperation.getExpression());
		case POSTFIX_INCREMENT:
		case PREFIX_INCREMENT:
			comment("GOOL warning: semantic may have changed");
			return unaryOperation.getExpression() + " +=1";
		case POSTFIX_DECREMENT:
		case PREFIX_DECREMENT:
			comment("GOOL warning: semantic may have changed");
			return unaryOperation.getExpression() + " -=1";
		case NOT:
			return "not " + unaryOperation.getExpression();
		case UNKNOWN:
			comment("Unrecognized by GOOL, passed on: " + unaryOperation.getTextualoperator());
			// no break: follow to the next case
		default:
			return String.format("%s %s", unaryOperation.getTextualoperator(), unaryOperation.getExpression());
		}
	}

	@Override
	public String getCode(VarDeclaration varDec) {
		String value;
		if(varDec.getInitialValue() != null) {
			value = varDec.getInitialValue().toString();
		} else { 
			value = "None";
		}
		paramsMethCurrent.add(varDec.getName());
		return String.format("%s = %s", varDec.getName(), value);
	}

	@Override
	public String getCode(While whilee) {
		return formatIndented("while %s:%1", whilee.getCondition(), whilee.getWhileStatement());
	}

	@Override
	public String getCode(TypeArray typeArray) {
		return "list";
	}

	@Override

	public String getCode(CustomDependency customDependency) {		

		if (!customDependencies.containsKey(customDependency.getName())) {
			Log.e(String.format("Custom dependencies: %s, Desired: %s", customDependencies, customDependency.getName()));
			throw new IllegalArgumentException(String.format("There is no equivalent type in Python for the GOOL type '%s'.", customDependency.getName()));
		}
		return customDependencies.get(customDependency.getName()).toString();
	}

	@Override
	public String getCode(TypeUnknown typeUnknown) {
		// TODO Auto-generated method stub
		return "noprint";
	}
	
	@Override
	public String getCode(CompoundAssign compoundAssign) {
		String textualOp;
		if (compoundAssign.getOperator() == Operator.DIV
				&& compoundAssign.getType().equals(TypeInt.INSTANCE)) {
			textualOp = "//";
		} else {
			textualOp = compoundAssign.getTextualoperator();
		}
		if (compoundAssign.getOperator().equals(Operator.UNKNOWN))
			comment("Unrecognized by GOOL, passed on: " + textualOp);
		return String.format("%s %s= %s", compoundAssign.getLValue(), textualOp,
				compoundAssign.getValue());
	}

	@Override
	public String getCode(ExpressionUnknown unknownExpression) {
		comment ("Unrecognized by GOOL, expression passed on");
		return unknownExpression.getTextual();
	}

	@Override
	public String getCode(ClassFree classFree) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(SystemCommandDependency systemCommandDependency) {
		// a verifier car de partout à null et je ne sais pas ce que ça fait
		return null;
	}
	
	@Override
	public String printClass(ClassDef classDef) {
		currentClass = classDef;
		
		// every python script has to start with a hash-bang:
		// do not change the "#!/usr/bin/env python" line!
		StringBuilder code = new StringBuilder ("#!/usr/bin/env python\n\nimport goolHelper\n\n");
		
		Set<String> dependencies = GeneratorHelper.printDependencies(classDef);
		for (String dependency : customDependencies.keySet()) {
			if(!dependency.isEmpty() && !dependency.equals("noprint")) {
	
				String name = dependency.substring(dependency.lastIndexOf('.') + 1);
				for(String dep : dependencies) {
					if(dep.equals(name)) {
						code = code.append(String.format("from %s import *\n", (dependency.startsWith(".")?name:dependency)));
						break;
					}
				}
			}
		}
		
		code = code.append(String.format("\nclass %s(%s):\n", classDef.getName(),
				(classDef.getParentClass() != null) ? classDef.getParentClass().getName()  : "object"));

		String dynamicAttributs = "";
		for(Field f : classDef.getFields()) {
			// renaming private fields
			if (f.getModifiers().contains(Modifier.PRIVATE)) {
				f.setName("__" + f.getName());
			}
			// static fields are declared in the class, dynamic ones in the constructor
			if (f.getModifiers().contains(Modifier.STATIC))
				code = code.append(formatIndented("%1", f));
			else
				dynamicAttributs += String.format("self.%s", f);
		}
		//dynamicAttributs = dynamicAttributs.replaceFirst("\\s+\\z", "\n");

		// renaming private methods
		for (Meth meth : classDef.getMethods()){
			if (meth.getModifiers().contains(Modifier.PRIVATE))
				meth.setName("__" + meth.getName());
		}
		
		List<Meth> meths = new ArrayList<Meth>();
		Meth mainMeth = null;
		for(Meth method : classDef.getMethods()) { //On parcourt les méthodes
			// the main method will be printed outside of the class later
			if (method.isMainMethod()) {
				mainMeth = method;
			}
			if(getName(method) == null) {	//Si la méthode n'a pas encore été renommée
				meths.clear();
				
				for(Meth m : classDef.getMethods()) { //On récupère les méthodes de mêmes noms
					if(m.getName().equals(method.getName())) {
						meths.add(m);
					}
				}
				
				if(meths.size()>1) { //Si il y a plusieurs méthodes de même nom
					code = code.append(formatIndented("\n%-1# wrapper generated by GOOL\n"));
					String block = "";
					String newName = method.getName();
					int i = 0;
					boolean first = true;
					boolean someStatics = false;
					boolean someDynamics = false;
					
					for(Meth m2 : meths) {
						
						if (m2.getModifiers().contains(Modifier.STATIC))
							someStatics = true;
						else
							someDynamics = true;
						newName = String.format("__%s_%d", method.getName().replaceFirst("^_*", ""), i++);
						while(methodsNames.containsValue(newName)) {
							newName = String.format("__%s_%d", method.getName().replaceFirst("^_*", ""), i++);
						}
						methodsNames.put(m2, newName);
						
						block += formatIndented("%sif goolHelper.test_args(args%s):\n%-1self.%s(*args)\n",
								first?"":"el", printMethParamsTypes(m2), newName);  
						first = false;
					}
					if (! method.getModifiers().contains(Modifier.PRIVATE)) {
						block += formatIndented("else:\n%-1super(%s, self).%s(args*)",
								classDef.getName(), method.isConstructor()?"__init__":method.getName());
					}
					if (someStatics && someDynamics) {
						code = code.append(formatIndented(
								"%-1# GOOL warning: static and dynamic methods under a same wrapper\n" +
								"%-1#               impossible to call static methods\n"));
					} else if (someStatics) {
						code = code.append(formatIndented("%-1@classmethod\n"));
					}
					
					String name = method.getName();
					if(method.isConstructor()) {
						name = "__init__";
						block = dynamicAttributs + block;
					}
					
					code = code.append(formatIndented("%-1def %s(self, *args):%2", name, block));
					
				}
				else {
					if(method.isConstructor()) {
						methodsNames.put(method, "__init__");
					}
					else {
						methodsNames.put(method, method.getName());
					}
				}
			}
		}
		
		for(Meth method : classDef.getMethods()) {
			if (! method.isMainMethod()) {
				if (! methodsNames.get(method).equals(method.getName()) && ! methodsNames.get(method).equals("__init__")) {
					code = code.append(formatIndented("\n%-1# used in wrapper '%s'",
							method.isConstructor()?"__init__":method.getName()));
				}
				String superPrefix = "";
				if (methodsNames.get(method).equals(method.getName())) {
					superPrefix = formatIndented (
							"if not GoolHelper.test_args(args%s):\n%-1super(%s, self).%s(*args)\n",
							printMethParamsTypes(method),
							classDef.getName(),
							method.isConstructor()?"__init__":method.getName());
					if (! method.getParams().isEmpty())
						superPrefix += printMethParamsNames(method).substring(2) + " = args\n";
				}
				if (method.isConstructor() && methodsNames.get(method).equals(method.getName()))
					code = code.append(formatIndented("%1", printMeth(method, superPrefix + dynamicAttributs)));
				else
					code = code.append(formatIndented("%1", printMeth(method, superPrefix)));
			}
		}
		
		if (mainMeth != null) {
			paramsMethCurrent.clear();
			code = code.append(formatIndented("\n# main program\nif __name__ == '__main__':%1",
					mainMeth.getBlock().toString().replaceAll("([^\\w])self([^\\w])", "$1"+classDef.getName()+"$2")));
		}

		return code.toString();
	}


	@Override
	public String getCode(TypeChar typeChar) {
		return "str";
	}

}
