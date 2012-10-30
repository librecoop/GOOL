package gool.ast.constructs;

import gool.ast.type.IType;

import java.util.ArrayList;
import java.util.List;

/**
 * This class specifies that the respective GOOL node may have a list of parameters.
 */
public abstract class Parameterizable extends Expression{

	/**
	 * The parameters needed to call the method.
	 */
	private List<Expression> params = new ArrayList<Expression>();

	protected Parameterizable(IType type) {
		super(type);
	}

	public Expression addParameter(Expression param) {
		params.add(param);
		return this;
	}

	public void addParameters(List<Expression> parameters) {
		params.addAll(parameters);
	}

	public List<Expression> getParameters() {
		return params;
	}
}