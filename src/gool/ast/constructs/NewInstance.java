package gool.ast.constructs;

import gool.ast.printer.GoolGeneratorController;

import java.util.Arrays;
import java.util.List;



/**
 * Represents the declaration and the creation of a new object instance in the intermediate language.
 */
public final class NewInstance extends Parameterizable {
	/**
	 * The variable's name.
	 */
	private VarDeclaration variable;

	public NewInstance(VarDeclaration variable) {
		super(variable.getType());
		this.variable = variable;
	}
	
	private NewInstance(VarDeclaration variable, List<Expression> parameters){
		this(variable);
		addParameters(parameters);
	}
		
	public NewInstance(VarDeclaration variable, Expression expression) {
		this(variable);
		addParameter(expression);
	}

	public static NewInstance create(
			VarDeclaration variable,
			Expression... params) {
		if (params != null) {
			return new NewInstance(variable, Arrays.asList(params));
		}
		return new NewInstance(variable);	
	}

	public VarDeclaration getVariable() {
		return variable;
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

	

}
