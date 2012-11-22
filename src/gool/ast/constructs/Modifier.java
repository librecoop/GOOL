package gool.ast.constructs;

public enum Modifier {
	PUBLIC, PRIVATE, PROTECTED, STATIC, ABSTRACT, FINAL, OVERRIDE, VIRTUAL, VOLATILE, TRANSIENT, NATIVE, STRICTFP, SYNCHRONIZED;
	
	@Override
	public String toString() {
		return name().toLowerCase();
	};
}
