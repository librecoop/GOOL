package gool.generator.objc;

import gool.ast.constructs.ArrayAccess;
import gool.ast.constructs.BinaryOperation;
import gool.ast.constructs.Expression;
import gool.ast.constructs.MethCall;
import gool.ast.constructs.VarAccess;
import gool.ast.type.IType;
import gool.ast.type.PrimitiveType;
import gool.ast.type.TypeBool;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeException;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeString;
import gool.generator.GeneratorHelper;

public final class GeneratorHelperObjc extends GeneratorHelper {
	
	public static String type(IType type) {
		if(type instanceof TypeInt){
			return "Int";
		}
		else if(type instanceof TypeChar){
			return "Char";
		}
		else if(type instanceof TypeDecimal){
			return "Double";
		}
		else if(type instanceof TypeBool){
			return "Bool";
		}
		else if(type instanceof TypeException){
			return "NSException *";
		}
		else{
			return "/* Unrecognized by gool */";
		}
	}
	
	public static String format(Expression e){
		return format(e.getType());
	}
	
	public static String format(IType t) {
		if(t.equals(TypeString.INSTANCE)){
			return "%@";
		}
		else if(t.equals(TypeInt.INSTANCE)){
			return "%d";
		}
		else if(t.equals(TypeChar.INSTANCE)){
			return "%c";
		}
		else if(t.equals(TypeDecimal.INSTANCE)){
			return "%f";
		}
		else if(t.equals(TypeBool.INSTANCE)){
			return "%d";
		}
		else if(t instanceof TypeClass){
			return "%@";
		}
		else{
			return "/* Unrecognized by gool : " + t + " */";
		}
	}
	
	public static String evalIntExpr(Expression e){
		if(e instanceof BinaryOperation)
			return "(" + evalIntExpr(((BinaryOperation) e).getLeft()) + ((BinaryOperation) e).getTextualoperator() + evalIntExpr(((BinaryOperation) e).getRight()) + ")";
		else 
			return e.toString();
	}
	
	public static String staticStringMini(Expression e){
		return ((e.getType() instanceof TypeString) 
				&& !(e instanceof VarAccess)
				&& !(e instanceof MethCall))
				? "@" : "";
	}
	
	public static String staticString(Expression e){
		return ((e.getType() instanceof PrimitiveType) 
				&& !(e instanceof VarAccess) 
				&& !(e instanceof MethCall)) 
				&& !(e instanceof ArrayAccess) 
				&& !(e.toString().contains("[NSString stringWithFormat"))
				? "@" : "";
	}
	
	public static String initWithObject(Expression e){
		if(e.getType() instanceof PrimitiveType && !(e.getType() instanceof TypeString))
			return "[[NSNumber alloc]initWith" + GeneratorHelperObjc.type(e.getType()) + ":" + e + "]";
		else 
			return GeneratorHelperObjc.staticString(e) + e;
	}
}
