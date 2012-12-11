package gool.ast.constructs;

import gool.ast.type.IType;
import gool.generator.GoolGeneratorController;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * This class captures the "new" of the intermediate language, i.e object
 * 
 * instantiation from a class definition Hence is is an OOTExpr.
 * 
 * 
 * 
 * @param T
 * 
 *            is the return type In generic OO languages each object
 * 
 *            instantiations may generate new types, hence this is an OOType.
 */

public class ClassNew extends Parameterizable {
	
	/**
	 * 
	 * Creates an instance with a class name and a type.
	 * 
	 * @param name
	 *            the class name.
	 * 
	 * @param type
	 *            the type of the instance.
	 */

	public ClassNew(IType type) {
		super(type);
	}

	/**
	 * 
	 * Creates an instance with a defining class and a list of parameters.
	 * 
	 * 
	 * 
	 * @param method
	 * 
	 *            is a pointer to the previous declaration of the class to be
	 * 
	 *            instantiated.
	 * 
	 * @param params
	 * 
	 *            are the constructor's parameters
	 */
	public ClassNew(ClassDef mclass, List<Expression> params) {
		this(mclass.getType());
		addParameters(params);
	}

	private ClassNew(IType type,
			List<Expression> params) {
		this(type);
		addParameters(params);
	}

	public ClassNew(IType type, Expression expression) {
		this(type);
		addParameter(expression);
	}

	public ClassNew(IType type,
			Expression... paramArray) {
		this(type);
		if (paramArray != null) {
			addParameters(Arrays.asList(paramArray));
		}
	}


	public String getName() {
		return getType().getName();
	}
	
	public List<IType> getTypeArguments() {
		return getType().getTypeArguments();
	}

	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
