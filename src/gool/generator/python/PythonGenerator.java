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
import gool.ast.constructs.Constructor;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.Expression;
import gool.ast.constructs.ExpressionUnknown;
import gool.ast.constructs.Field;
import gool.ast.constructs.For;
import gool.ast.constructs.If;
import gool.ast.constructs.InitCall;
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
import gool.ast.type.IType;
import gool.ast.type.TypeArray;
import gool.ast.type.TypeBool;
import gool.ast.type.TypeBufferedReader;
import gool.ast.type.TypeByte;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeFile;
import gool.ast.type.TypeFileReader;
import gool.ast.type.TypeInputStream;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeNone;
import gool.ast.type.TypeNull;
import gool.ast.type.TypeObject;
import gool.ast.type.TypeScanner;
import gool.ast.type.TypeString;
import gool.ast.type.TypeUnknown;
import gool.ast.type.TypeVoid;
import gool.generator.GeneratorHelper;
import gool.generator.common.CodeGeneratorNoVelocity;
import gool.generator.common.CommonCodeGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import logger.Log;

import org.apache.commons.lang.StringUtils;

public class PythonGenerator extends CommonCodeGenerator implements CodeGeneratorNoVelocity {
	
	public PythonGenerator() {
		super();
		// As a tradition, Python is indented with 4 spaces
		indentation = "    ";
	}
	

	/**
	 * The class currently printed.
	 * Updated every time we start to print a new class.
	 */
	private ClassDef currentClass;
	
	/**
	 * Used to store comments.
	 * Used by the 'comment' and 'printWithComment' methods.
	 */
	private ArrayList<String> comments = new ArrayList<String>();
	
	/**
	 * The local variables (and parameters) of the method currently printed.
	 * The names in this list are not prefixed with 'self.'
	 * Emptied  every time we start to print a new method.
	 */
	private ArrayList<String> localIndentifiers = new ArrayList<String>();
	
	/**
	 * Register a comment to be printed alongside the statement being parsed.
	 * @param newcomment string without '#' nor newline
	 */
	private void comment(String newcomment) {
		String com = "# " + newcomment + "\n";
		if (! comments.contains(com))
			comments.add(com);
	}
	
	/**
	 * Print a statement with optional comments taken from the 'PytonGenerator.comments'.
	 * If only one comment is present, it is put at the end of the line, otherwise
	 * they are added before the statement.
	 * 'PytonGenerator.comments' is emptied.
	 * @param statement
	 * @return the corresponding python code 
	 */
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

	/**
	 * Holds the names of every method as they are outputed.
	 * Used to rename methods.
	 */
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
		// a newly declared array is a list of nulls
		// or a list of list ... of nulls for multidimensional arrays
		String ret = "None";
		for (Expression e : arrayNew.getDimesExpressions())
			ret = String.format("[%s]*%s", ret, e);
		return "(" + ret + ")";
	}
	
	@Override
	public String getCode(Block block) {
		StringBuilder result = new StringBuilder();
		for (Statement statement : block.getStatements()) {
			if(statement.toString().contains("goolHelperUtil.Scanner(System.in"))
			Log.e("mon stat :"+statement.toString());
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
				// for compatibility with different versions of Python
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
			} else {
				textualOp = binaryOp.getTextualoperator();
			}
			break;
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
			String ret = super.getCode(constant);
			// unicode strings have to be prefixed with a 'u'
			if (constant.getType() == TypeString.INSTANCE && ret.contains("\\u")) {
				return "u" + ret;
			} else {
				return ret;
			}
		}
	}

	/**
	 * Types to witch a cast is necessary.
	 * As Python is dynamically typed, it only need type casting for conversions.
	 */
	static private ArrayList<Class<? extends IType>> castableTypes = new ArrayList<Class<? extends IType>>();
	static {
		castableTypes.add(  TypeArray.class);
		castableTypes.add(   TypeBool.class);
		castableTypes.add(   TypeByte.class);
		castableTypes.add(   TypeChar.class);
		castableTypes.add(    TypeInt.class);
		castableTypes.add(TypeDecimal.class);
		castableTypes.add(   TypeList.class);
		castableTypes.add(    TypeMap.class);
		castableTypes.add( TypeString.class);
	}
	
	@Override
	public String getCode(CastExpression cast) {
		if (castableTypes.contains(cast.getType().getClass())) {
			return String.format("%s(%s)", cast.getType(), cast.getExpression());
		} else {
			return cast.getExpression().toString();
		}
	}

	@Override
	public String getCode(ClassNew classNew) {
		if(classNew.getName().equals("goolHelperUtil.Scanner"))
			return String.format("%s()", classNew.getName());
		return String.format("%s(%s)", classNew.getName(), StringUtils
				.join(classNew.getParameters(), ", "));
	}

	@Override
	public String getCode(Comment comment) {
		// only works with comments that are alone on their line(s)
		return comment.getValue().replaceAll("(^ *)([^ ])", "$1# $2");
	}
	
	@Override
	public String getCode(EnhancedForLoop enhancedForLoop) {
		// foreach-style loops define a local variable, we register it
		localIndentifiers.add(enhancedForLoop.getVarDec().getName());
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
		// there is no 'for(;;)' construct in Python
		return formatIndented("%swhile %s:%1",
				printWithComment(forr.getInitializer()),forr.getCondition(),
				forr.getWhileStatement().toString() + printWithComment(forr.getUpdater()));
	}

	@Override
	public String getCode(If pif) {
		String out = formatIndented ("if %s:%1", pif.getCondition(), pif.getThenStatement());
		if (pif.getElseStatement() != null){
			if (pif.getElseStatement() instanceof If) {
				// the concatenation with the next 'if' produces a 'elif'
				out += formatIndented ("el%s", pif.getElseStatement());
			} else {
				out += formatIndented ("else:%1", pif.getElseStatement());
			}
		}
		return out;
	}

	@Override
	public String getCode(Collection<Modifier> modifiers) {
		// there are no modifiers in Python
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
		// An empty list is a list whose boolean value is false.
		// It is the official recommended 'pythonic' way to do it.
		return String.format("(not %s)", liec.getExpression());
	}

	@Override
	public String getCode(ListRemoveAtCall lrc) {
		return String.format("%s.pop(%s)", lrc.getExpression(), StringUtils
				.join(lrc.getParameters(), ", "));
	}

	@Override
	public String getCode(ListRemoveCall lrc) {
		if (!lrc.getType().getTypeArguments().contains("[Int]"))		
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
		// the 'main' is not a method in python
		return mainMeth.getBlock().toString();
	}

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		return String.format("%s in %s",
				mapContainsKeyCall.getParameters().get(0), mapContainsKeyCall.getExpression());
	}

	@Override
	public String getCode(MapEntryGetKeyCall mapEntryGetKeyCall) {
		// a map entry is simply a tuple of the form (key, value)
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
		// c.f. getCode(ListIsEmptyCall)
		return String.format("(not %s)", mapIsEmptyCall.getExpression());
	}

	@Override
	public String getCode(MapMethCall mapMethCall) {
		//TODO: unbalanced parenthesis in produced code
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
		// we don't use 'dict[key]' for compatibility with the java API
		// if the key does'nt exists
		return String.format("%s.pop(%s, None)", mapRemoveCall.getExpression(),
				StringUtils.join(mapRemoveCall.getParameters(), ", "));
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		return String.format("len(%s)", mapSizeCall.getExpression());
	}

	/**
	 * Helper method to produce the code of a method
	 * @param meth
	 * 		the method to output
	 * @param prefix
	 * 		code to add at the beginning of the method
	 * @return the Python code of the method
	 */
	private String printMeth(Meth meth, String prefix) {
		// register parameters as local identifiers
		localIndentifiers.clear();
		for (VarDeclaration p : meth.getParams()) {
			localIndentifiers.add(p.getName());
		}
		// all methods that aren't used by a wrapper have a header to
		// allow inheritance when multiple methods have the same name
		if (methodsNames.get(meth).equals(meth.getName())) {
			prefix = formatIndented (
					"if not goolHelper.test_args(args%s):\n%-1super(%s, self).%s(*args)\n",
					printMethParamsTypes(meth, ", "),
					currentClass.getName(),
					meth.isConstructor()?"__init__":meth.getName());
			if (! meth.getParams().isEmpty())
				prefix += printMethParamsNames(meth).substring(2) + " = args\n";
			prefix += "# end of GOOL header\n";
		}
		if (meth.isConstructor()) {
			for (InitCall init : ((Constructor)meth).getInitCalls()) {
				//TODO: is'nt this already done at the start of the method?
				for (VarDeclaration param : meth.getParams()) {
					localIndentifiers.add(param.getName());
				}
				prefix += String.format("super(%s, self).__init__(%s)\n",
						currentClass.getName(),
						StringUtils.join(init.getParameters(), ", "));
			}					
		}
		// Python dosn't allow a empty block (it messes the indentation)
		if (prefix == "" && meth.getBlock().getStatements().isEmpty())
			prefix = "pass";
		return formatIndented("%sdef %s(self%s):%1",
				meth.getModifiers().contains(Modifier.STATIC)?"@classmethod\n":"",
				methodsNames.get(meth),
				methodsNames.get(meth).equals(meth.getName())?", *args":printMethParamsNames(meth),
				prefix + meth.getBlock());
	}
	
	/**
	 * Produces the comma-separated list of the names of the parameters of a
	 * method, with a leading comma.
	 * @param meth
	 * @return the corresponding string
	 */
	private String printMethParamsNames(Meth meth) {
		String ret = "";
		for (VarDeclaration p : meth.getParams()) {
			ret += ", " + p.getName();
		}
		return ret;
	}
	
	/**
	 * Produces the comma-separated list of the types of the parameters of a
	 * method, with a leading comma.
	 * @param meth
	 * @return the corresponding string
	 */
	private String printMethParamsTypes(Meth meth, String separator) {
		String ret = "";
		for (VarDeclaration p : meth.getParams()) {
			ret += separator + p.getType();
		}
		return ret;
	}
	
	@Override
	public String getCode(Meth meth) {
		return printMeth(meth, "");
	}

	@Override
	public String getCode(VarAccess varAccess) {
		String name = varAccess.getDec().getName();
		// packages names are not modified
		// is'nt there a better test?
		if(varAccess.getType() == null || varAccess.getType().getName().isEmpty()) {
			return name;
		}
		// this method seems to be called before dealing with any class
		// so we have to check for 'null'
		if (name.equals("super") && currentClass != null)
			return String.format("super(%s, self)", currentClass.getName());
		if (name.equals("this"))
			return "self";

		if (varAccess.getDec().getModifiers().contains(Modifier.PRIVATE)
				&& ! name.startsWith("__")) {
			name = "__" + name;
		}
		if (localIndentifiers.contains(name))
			return name;
		else
			return "self." + name;
			
	}
	
	@Override
	public String getCode(Modifier modifier) {
		// there are no modifiers in Python
		return "";
	}

	@Override
	public String getCode(NewInstance newInstance) {
		return String.format("%s = %s(%s)",
				newInstance.getVariable(),
				// why would there be any trailing '\'?
				newInstance.getVariable().getType().toString().replaceFirst("\\*$", ""),
				StringUtils.join(newInstance.getParameters(), ", "));
	}

	@Override
	public String getCode(ParentCall parentCall) {
		return String.format("super(%s, self).__init__(%s)",
				currentClass, StringUtils.join(parentCall.getParameters(), ", "));
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
		//TODO: what about the new 'print is a function' standard?
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
		if(typeDependency.getType() instanceof TypeFile)
			return "noprint";
		if(typeDependency.getType() instanceof TypeFileReader)
			return "noprint";
		if(typeDependency.getType() instanceof TypeScanner)
			return "noprint";
		return super.getCode(typeDependency);
	}

	@Override
	public String getCode(TypeEntry typeEntry) {
		// it's just a tuple
		return String.format("(%s, %s)",typeEntry.getKeyType(), typeEntry.getElementType() );
	}

	@Override
	public String getCode(TypeFile typeFile){
		return "goolHelperIO.File";
	}
	
	@Override
	public String getCode(TypeFileReader typeFileReader){
		return "goolHelperIO.FileReader";
	}
	
	@Override
	public String getCode(TypeBufferedReader tbr) {
		return "goolHelperIO.BufferedReader";
	}
	

	@Override
	public String getCode(TypeScanner typeScanner) {
		return "goolHelperUtil.Scanner";
	}
	
	@Override
	public String getCode(TypeInt typeInt) {
		// should we use 'long' instead (infinite precision)?
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
		// does not support unicode is Python 2.x
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
//TODO: Python is said to be 'call-by-assignment'.
//      Increment and decrement helper functions do not update the variable.
//      For now we only support incrementation and decrementation as statements
//			case POSTFIX_INCREMENT:
//				comment("GOOL warning: post-incrementation became pre-incrementation");
//				// no break: follow to the next case
//			case PREFIX_INCREMENT:
//				return String.format("goolHelper.increment(%s)", unaryOperation.getExpression());
//			case POSTFIX_DECREMENT:
//				comment("GOOL warning: post-decrementation became pre-decrementation");
//				// no break: follow to the next case
//			case PREFIX_DECREMENT:
//				return String.format("goolHelper.decrement(%s)", unaryOperation.getExpression());
		case POSTFIX_INCREMENT:
		case PREFIX_INCREMENT:
			comment("GOOL warning: semantic may have changed");
			return unaryOperation.getExpression() + " +=1";
		case POSTFIX_DECREMENT:
		case PREFIX_DECREMENT:
			comment("GOOL warning: semantic may have changed");
			return unaryOperation.getExpression() + " -=1";
		case NOT:
			return String.format("(not %s)",  unaryOperation.getExpression());
		case UNKNOWN:
			comment("Unrecognized by GOOL, passed on: " + unaryOperation.getTextualoperator());
			// no break: follow to the next case
		default:
			return String.format("%s %s", unaryOperation.getTextualoperator(), unaryOperation.getExpression());
		}
	}

	@Override
	public String getCode(VarDeclaration varDec) {
		// It's just an assignment. Even though it is not needed if there is no
		// Initialization, we can't distinguish between no initialization and
		// initialization to 'null'
		// TODO: Can we make this distinction?
		String value;
		if(varDec.getInitialValue() != null) {
			value = varDec.getInitialValue().toString();
		} else { 
			value = "None";
		}
		// even if the declaration were not to be outputted, we would still need
		// to register the local identifier
		localIndentifiers.add(varDec.getName());
		return String.format("%s = %s", varDec.getName(), value);
	}

	@Override
	public String getCode(While whilee) {
		return formatIndented("while %s:%1", whilee.getCondition(), whilee.getWhileStatement());
	}

	@Override
	public String getCode(TypeArray typeArray) {
		// Should we use the 'array' module?
		return "list";
	}

	@Override

	public String getCode(CustomDependency customDependency) {		
		if (customDependency.getName().startsWith("java.io")) {
			return "goolHelperIO";
		}
		if (!customDependencies.containsKey(customDependency.getName())) {
			Log.e(String.format("Custom dependencies: %s, Desired: %s", customDependencies, customDependency.getName()));
			throw new IllegalArgumentException(String.format("There is no equivalent type in Python for the GOOL type '%s'.", customDependency.getName()));
		}
		return customDependencies.get(customDependency.getName()).toString();
	}

	@Override
	public String getCode(TypeUnknown typeUnknown) {
		return "noprint";
	}
	
	@Override
	public String getCode(CompoundAssign compoundAssign) {
		String textualOp;
		if (compoundAssign.getOperator() == Operator.DIV
				&& compoundAssign.getType().equals(TypeInt.INSTANCE)) {
			// for compatibility with different versions of Python
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
		StringBuilder code = new StringBuilder ("#!/usr/bin/env python\n\nimport goolHelper\nimport goolHelperIO\nimport goolHelperUtil\n\n");
		
		// Printing the imports.
		// This is not the best way to write it in Python, but doing it right
		// would require a lot of renaming in other places.
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
		
		// We only produce new-style classes. Theses have to explicitly
		// inherit from the 'object' class.
		// Not needed in Python 3, but kept for compatibility.
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

		// renaming private methods
		for (Meth meth : classDef.getMethods()){
			if (meth.getModifiers().contains(Modifier.PRIVATE))
				meth.setName("__" + meth.getName());
		}
		
		// dealing with multiple methods having the same name,
		// which is forbidden in Python
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
					boolean first = true;
					boolean someStatics = false;
					boolean someDynamics = false;
					
					for(Meth m2 : meths) {
						
						if (m2.getModifiers().contains(Modifier.STATIC))
							someStatics = true;
						else
							someDynamics = true;
						int i = m2.getParams().size();
						newName = String.format("__%s_%d", method.getName().replaceFirst("^_*", ""), i++);
						while(methodsNames.containsValue(newName)) {
							newName = String.format("__%s_%d", method.getName().replaceFirst("^_*", ""), i++);
						}
						methodsNames.put(m2, newName);
						
						block += formatIndented("%sif goolHelper.test_args(args%s):\n%-1self.%s(*args)\n",
								first?"":"el", printMethParamsTypes(m2, ", "), newName);  
						first = false;
					}
					if (! method.getModifiers().contains(Modifier.PRIVATE) && ! method.isConstructor()) {
						block += formatIndented("else:\n%-1super(%s, self).%s(args*)",
								classDef.getName(), method.isConstructor()?"__init__":method.getName());
					}
					// If all methods under the wrapper are statics of dynamics,
					// the wrapper is of the same kind. If both exists, we can't
					// properly deal with it so we output a warning in the code.
					if (someStatics && someDynamics) {
						code = code.append(formatIndented(
								"%-1# GOOL warning: static and dynamic methods under a same wrapper\n" +
								"%-1#               impossible to call static methods\n"));
					} else if (someStatics) {
						// '@static' would be more accurate, but we would have
						// to replace 'self' with the name of the class.
						code = code.append(formatIndented("%-1@classmethod\n"));
					}
					
					String name = method.getName();
					if(method.isConstructor()) {
						name = "__init__";
						// We add the declarations for dynamics attributes to
						// the constructor if it is a wrapper.
						block = dynamicAttributs + block;
					}
					
					// We have to manually print the header as it is not a
					// method known to GOOL.
					code = code.append(formatIndented("%-1def %s(self, *args):%2", name, block));
					
				} else {
					if(method.isConstructor()) {
						methodsNames.put(method, "__init__");
					} else {
						methodsNames.put(method, method.getName());
					}
				}
			}
		}
		
		for(Meth method : classDef.getMethods()) {
			if (! method.isMainMethod()) {
				// we add a comment for renamed methods
				if (! methodsNames.get(method).equals(method.getName()) && ! methodsNames.get(method).equals("__init__")) {
					code = code.append(formatIndented("\n%-1# used in wrapper '%s'",
							method.isConstructor()?"__init__":method.getName()));
				}
				// We add the declarations for dynamic attributes to the constructor
				// if there is no wrapper for it.
				if (method.isConstructor() && methodsNames.get(method).equals(method.getName())) {
					code = code.append(formatIndented("%1", printMeth(method, dynamicAttributs)));
				} else {
					code = code.append(formatIndented("%1", method));
				}
			}
		}
		
		if (mainMeth != null) {
			// As it is not a method, we have do do everything manually.
			// The condition is not needed, but is customary.
			// TODO: find a better way to deal with 'self'
			// TODO: deal with command line arguments
			localIndentifiers.clear();
			code = code.append(formatIndented("\n# main program\nif __name__ == '__main__':%1",
					mainMeth.getBlock().toString().replaceAll("([^\\w])self([^\\w])", "$1"+classDef.getName()+"$2")));
		}

		return code.toString();
	}


	@Override
	public String getCode(TypeChar typeChar) {
		// does not support unicode is Python 2.x
		return "str";
	}

	@Override
	public String getCode(TypeInputStream typeInputStream) {
		return "noprint";
	}


}
