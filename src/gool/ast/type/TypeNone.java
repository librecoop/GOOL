package gool.ast.type;


/**
 * A pseudo-type used when no any other type is appropriate. For example, to
 * represent the return type of a constructor.
 */
public final class TypeNone extends PrimitiveType {
	public static final TypeNone INSTANCE = new TypeNone();

	private TypeNone() {
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String callGetCode() {
		return getName();
	}
}
