package gool.generator.objc;

import gool.ast.constructs.ArrayAccess;
import gool.ast.constructs.BinaryOperation;
import gool.ast.constructs.Expression;
import gool.ast.constructs.MethCall;
import gool.ast.constructs.VarAccess;
import gool.ast.type.IType;
import gool.ast.type.TypeBool;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeClass;
import gool.ast.type.TypeDecimal;
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
		else{
			return "PROBLEM";
		}
	}
	
	public static String format(Expression e){
		if(e.getType().equals(TypeString.INSTANCE)){
			return "%@";
		}
		else if(e.getType().equals(TypeInt.INSTANCE)){
			return "%d";
		}
		else if(e.getType().equals(TypeChar.INSTANCE)){
			return "%c";
		}
		else if(e.getType().equals(TypeDecimal.INSTANCE)){
			return "%f";
		}
		else if(e.getType().equals(TypeBool.INSTANCE)){
			return "%d";
		}
		else if(e.getType() instanceof TypeClass){
			return "%@";
		}
		return null;
	}
	
	public static String evalIntExpr(Expression e){
		if(e instanceof BinaryOperation)
			return "(" + evalIntExpr(((BinaryOperation) e).getLeft()) + ((BinaryOperation) e).getTextualoperator() + evalIntExpr(((BinaryOperation) e).getRight()) + ")";
		else 
			return e.toString();
	}
	
	public static String staticString(Expression e){
		return ((e.getType() instanceof TypeString) 
				&& !(e instanceof VarAccess) 
				&& !(e instanceof MethCall)) 
				&& !(e instanceof ArrayAccess) 
				&& !(e.toString().contains("[NSString stringWithFormat"))
				? "@" : "";
	}
	
}
