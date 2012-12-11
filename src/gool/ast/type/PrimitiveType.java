package gool.ast.type;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is for primitive types such as Int, Bool, etc.
 * They do not have TypeArguments, hence they return, instead of them, the empty unmodifiable list.
 * @author parrighi
 */
public abstract class PrimitiveType extends IType {

	@Override
	public List<IType> getTypeArguments() {
		return Collections.unmodifiableList(new ArrayList<IType>());
	}

}
