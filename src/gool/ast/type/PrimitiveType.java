package gool.ast.type;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PrimitiveType extends IType {

	@Override
	public List<IType> getTypeArguments() {
		return Collections.unmodifiableList(new ArrayList<IType>());
	}

	@Override
	public boolean isReferenceType() {
		return !isValueType();
	}
	
	@Override
	public boolean isValueType() {
		return true;
	}
}
