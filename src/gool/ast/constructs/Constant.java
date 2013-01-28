package gool.ast.constructs;

import gool.ast.type.IType;
import gool.ast.type.TypeArray;
import gool.ast.type.TypeByte;
import gool.ast.type.TypeChar;
import gool.ast.type.TypeString;
import gool.generator.GoolGeneratorController;


/**
 * This class captures all the expressions of the intermediate language
 * which we have not bothered to represent in the Abstract Syntax Tree.
 * Its code will compile in the target language just as it is.
 * It is an OOTExpr.
 * @param T is the type of this expression, if known at compile time, otherwise put OOTType. 
 * That way java generics grant us some level of type checking of the generated code at compiler design time.
 * Sometimes we will not be able to use this though, because we will not know T at compiler design time. 
 */
public class Constant extends Expression {

	/**
	 * The constant value.
	 */
	private Object value;
	/**
	 * The type of the expression.
	 */

	/**
	 * The type of the return value is T
	 * @param type
	 * @param code 
	 */
	public Constant(IType type, Object code){
		super(type);
		if(type instanceof TypeChar) {
			this.value= (Object) ("'"+code.toString()+"'");
		}
		else 
		{
		this.value=code;
		}
	}
	public Constant(byte[] value) {
		this(new TypeArray(TypeByte.INSTANCE), value);
	}
	
	public Object getValue() {
		return value;
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}	
	
	
}
