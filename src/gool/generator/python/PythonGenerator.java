package gool.generator.python;

import gool.ast.constructs.ArrayNew;
import gool.ast.constructs.Block;
import gool.ast.constructs.CastExpression;
import gool.ast.constructs.ClassDef;
import gool.ast.constructs.ClassFree;
import gool.ast.constructs.ClassNew;
import gool.ast.constructs.Comment;
import gool.ast.constructs.Constant;
import gool.ast.constructs.Constructor;
import gool.ast.constructs.CustomDependency;
import gool.ast.constructs.Dependency;
import gool.ast.constructs.EnhancedForLoop;
import gool.ast.constructs.EqualsCall;
import gool.ast.constructs.ExpressionUnknown;
import gool.ast.constructs.Field;
import gool.ast.constructs.FieldAccess;
import gool.ast.constructs.For;
import gool.ast.constructs.GoolCall;
import gool.ast.constructs.Identifier;
import gool.ast.constructs.If;
import gool.ast.constructs.ListMethCall;
import gool.ast.constructs.MainMeth;
import gool.ast.constructs.MapEntryMethCall;
import gool.ast.constructs.MapMethCall;
import gool.ast.constructs.MemberSelect;
import gool.ast.constructs.Meth;
import gool.ast.constructs.MethCall;
import gool.ast.constructs.Modifier;
import gool.ast.constructs.NewInstance;
import gool.ast.constructs.Package;
import gool.ast.constructs.ParentCall;
import gool.ast.constructs.Return;
import gool.ast.constructs.Statement;
import gool.ast.constructs.This;
import gool.ast.constructs.ThisCall;
import gool.ast.constructs.ToStringCall;
import gool.ast.constructs.TypeDependency;
import gool.ast.constructs.UnaryOperation;
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
import gool.ast.type.TypeClass;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeEntry;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeList;
import gool.ast.type.TypeMap;
import gool.ast.type.TypeMethod;
import gool.ast.type.TypeNone;
import gool.ast.type.TypeNull;
import gool.ast.type.TypeObject;
import gool.ast.type.TypePackage;
import gool.ast.type.TypeString;
import gool.ast.type.TypeUnknown;
import gool.ast.type.TypeVar;
import gool.ast.type.TypeVoid;
import gool.generator.common.CommonCodeGenerator;
import gool.generator.common.Platform;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

public class PythonGenerator extends CommonCodeGenerator {

	@Override
	public void addCustomDependency(String key, Dependency value) {
	}
	
	@Override
	public String getCode(ArrayNew arrayNew) {
		return String.format("%s[%s]", arrayNew.getType(), StringUtils
				.join(arrayNew.getDimesExpressions(), ", "));
	}

	@Override
	public String getCode(Block block) {
		StringBuilder result = new StringBuilder();
		for (Statement statement : block.getStatements()) {
			result.append(statement);
		}
		return result.toString();
	}
	
	@Override
	public String getCode(CastExpression cast) {
		return String.format("%s(%s)", cast.getType(), cast
				.getExpression());
	}

	@Override
	public String getCode(ClassNew classNew) {
		return String.format("%s(%s)", classNew.getType(), StringUtils
				.join(classNew.getParameters(), ", "));
	}

	@Override
	public String getCode(Comment comment) {
		return String.format("\"\"\"\n%s\n\"\"\"", comment.getValue());
	}

	@Override
	public String getCode(Constant constant) {
		return constant.getValue().toString();
	}

	@Override
	public String getCode(Constructor cons) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(EnhancedForLoop enhancedForLoop) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(EqualsCall equalsCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(Field field) {
		String value;
		if (field.getDefaultValue() != null) {
			value =  field.getDefaultValue().toString();
		}
		else {
			value = "None";
		}
		
		return String.format("%s = %s\n", field.getName(), value);
	}

	@Override
	public String getCode(FieldAccess sfa) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(For forr) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(GoolCall goolCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(If pif) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(Collection<Modifier> modifiers) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ListAddCall lac) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ListContainsCall lcc) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ListGetCall lgc) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ListGetIteratorCall lgic) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ListIsEmptyCall liec) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ListMethCall lmc) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ListRemoveAtCall lrc) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ListRemoveCall lrc) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ListSizeCall lsc) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MainMeth mainMeth) {
		return formatIndented("%", mainMeth) + "\n" + mainMeth.getName();
	}

	@Override
	public String getCode(MapContainsKeyCall mapContainsKeyCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MapEntryGetKeyCall mapEntryGetKeyCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MapEntryGetValueCall mapEntryGetKeyCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MapEntryMethCall mapEntryMethCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MapGetCall mapGetCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MapGetIteratorCall mapGetIteratorCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MapIsEmptyCall mapIsEmptyCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MapMethCall mapMethCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MapPutCall mapPutCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MapRemoveCall mapRemoveCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MapSizeCall mapSizeCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(MemberSelect memberSelect) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(Meth meth) {
		String out = String.format("def %s():\n", meth.getName(),StringUtils.join(meth.getParams(),", "));
		out = out + formatIndented("%s", meth.getBlock());
		return out;
	}

	@Override
	public String getCode(MethCall methodCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(Modifier modifier) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(NewInstance newInstance) {
		// TODO Auto-generated method stub
		return "";
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
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(SystemOutDependency systemOutDependency) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(SystemOutPrintCall systemOutPrintCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(This pthis) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ThisCall thisCall) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ToStringCall tsc) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(TypeBool typeBool) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(TypeByte typeByte) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(TypeClass typeClass) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(TypeDecimal typeReal) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(TypeDependency typeDependency) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(TypeEntry typeEntry) {
		// TODO Auto-generated method stub
		return "";
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
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(VarDeclaration varDec) {
		String value;
		if(varDec.getInitialValue() != null) {
			value = varDec.getInitialValue().toString();
		}
		else {
			value = "None";
		}
		
		return String.format("%s = %s\n", varDec.getName(), value);
	}

	@Override
	public String getCode(While whilee) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(TypeArray typeArray) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(CustomDependency customDependency) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(Identifier identifier) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(TypeUnknown typeUnknown) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ExpressionUnknown unknownExpression) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ClassFree classFree) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(Platform platform) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(ClassDef classDef) {
		String code = String.format("%s%s:\n", classDef.getName(),
				(classDef.getParentClass() != null) ? "(" + classDef.getParentClass().getName() + ")" : "");
		
		for(Field f : classDef.getFields()) {
			code = code + formatIndented("%s", f);
		}

		for(Meth method : classDef.getMethods()) {
			code = code + formatIndented("%s", method);
		}
		
		return code;
	}

	@Override
	public String getCode(Package _package) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(SystemCommandDependency systemCommandDependency) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(TypePackage typePackage) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(TypeMethod typeMethod) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getCode(TypeVar typeVar) {
		// TODO Auto-generated method stub
		return "";
	}


	@Override
	public String getCode(TypeChar typeChar) {
		// TODO Auto-generated method stub
		return null;
	}

}
