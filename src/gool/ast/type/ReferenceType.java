package gool.ast.type;



public abstract class ReferenceType extends IType {
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof TypeClass
				&& getName().equals(((IType) obj).getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}
}
