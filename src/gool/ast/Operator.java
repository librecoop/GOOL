package gool.ast;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import gool.ast.type.IType;
import gool.ast.type.TypeBool;
import gool.ast.type.TypeDecimal;
import gool.ast.type.TypeInt;
import gool.ast.type.TypeObject;

public enum Operator {
	PLUS, MINUS, MULT, AND, OR, EQUAL, NOT, LT, LEQ, GT, GEQ, DECIMAL_PLUS, DECIMAL_MINUS, DIV, DECIMAL_DIV, DECIMAL_MULT, DECIMAL_LT, DECIMAL_LEQ, DECIMAL_GT, DECIMAL_GEQ, PREFIX_DECREMENT, POSTFIX_DECREMENT, PREFIX_INCREMENT, POSTFIX_INCREMENT, NOT_EQUAL;
	
	private static class Op {
		private String operator;
		private IType inputType;
		private IType returnType;
		
		public Op(String operator, IType inputType, IType returnType) {
			this.operator = operator;
			this.inputType = inputType;
			this.returnType = returnType;
		}
		
		public String getOperator() {
			return operator;
		}
		public IType getInputType() {
			return inputType;
		}
		public IType getReturnType() {
			return returnType;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Op) {
				return ((Op) obj).getOperator().equals(getOperator());
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return getOperator().hashCode();
		}
	}
	private static Map<Operator, Op> ops = new HashMap<Operator, Op>();
	static {
		ops.put(PLUS, new Op("+", TypeInt.INSTANCE, TypeInt.INSTANCE));
		ops.put(DECIMAL_PLUS, new Op("+", TypeDecimal.INSTANCE, TypeDecimal.INSTANCE));
		ops.put(MINUS, new Op("-", TypeInt.INSTANCE, TypeInt.INSTANCE));
		ops.put(DECIMAL_MINUS, new Op("-", TypeDecimal.INSTANCE, TypeDecimal.INSTANCE));
		ops.put(MULT, new Op("*", TypeInt.INSTANCE, TypeInt.INSTANCE));
		ops.put(DECIMAL_MULT, new Op("*", TypeDecimal.INSTANCE, TypeDecimal.INSTANCE));
		ops.put(AND, new Op("&&", TypeBool.INSTANCE, TypeBool.INSTANCE));
		ops.put(OR, new Op("||", TypeBool.INSTANCE, TypeBool.INSTANCE));
		ops.put(EQUAL, new Op("==", TypeObject.INSTANCE, TypeBool.INSTANCE));
		ops.put(NOT, new Op("!", TypeBool.INSTANCE, TypeBool.INSTANCE));
		ops.put(DECIMAL_LT, new Op("<", TypeDecimal.INSTANCE, TypeBool.INSTANCE));
		ops.put(LT, new Op("<", TypeInt.INSTANCE, TypeBool.INSTANCE));
		ops.put(DECIMAL_LEQ, new Op("<=", TypeDecimal.INSTANCE, TypeBool.INSTANCE));
		ops.put(LEQ, new Op("<=", TypeInt.INSTANCE, TypeBool.INSTANCE));
		ops.put(DECIMAL_GT, new Op(">", TypeDecimal.INSTANCE, TypeBool.INSTANCE));
		ops.put(GT, new Op(">", TypeInt.INSTANCE, TypeBool.INSTANCE));
		ops.put(DECIMAL_GEQ, new Op(">=", TypeDecimal.INSTANCE, TypeBool.INSTANCE));
		ops.put(GEQ, new Op(">=", TypeInt.INSTANCE, TypeBool.INSTANCE));
		ops.put(DIV, new Op("/", TypeInt.INSTANCE, TypeInt.INSTANCE));
		ops.put(DECIMAL_DIV, new Op("/", TypeDecimal.INSTANCE, TypeDecimal.INSTANCE));
		ops.put(PREFIX_DECREMENT, new Op("--", TypeInt.INSTANCE, TypeInt.INSTANCE));
		ops.put(POSTFIX_DECREMENT, new Op("--", TypeInt.INSTANCE, TypeInt.INSTANCE));
		ops.put(PREFIX_INCREMENT, new Op("++", TypeInt.INSTANCE, TypeInt.INSTANCE));
		ops.put(POSTFIX_INCREMENT, new Op("++", TypeInt.INSTANCE, TypeInt.INSTANCE));
		ops.put(NOT_EQUAL, new Op("!=", TypeObject.INSTANCE, TypeBool.INSTANCE));
	}

	public String toString() {
		return ops.get(this).getOperator();
	}

	public IType getInputType() {
		return ops.get(this).getInputType();
	}

	public IType getReturnType() {
		return ops.get(this).getReturnType();
	}
	public static Operator fromString(String value) {
		for (Entry<Operator, Op> op : ops.entrySet()) {
			if (op.getValue().getOperator().equals(value)) {
				return op.getKey();
			}
		}
		throw new IllegalArgumentException(String.format("Invalid operator %s", value));
	}
}
