package gool.ast.constructs;

import gool.ast.printer.GoolGeneratorController;
import gool.ast.type.IType;

import java.util.ArrayList;
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

public class ArrayNew extends Expression {

	private final List<Expression> dimesExpressions = new ArrayList<Expression>();
	private final List<Expression> initialiList = new ArrayList<Expression>();


	public ArrayNew(IType type, List<Expression> dimesExpressions,
			List<Expression> initialiList) {
				super(type);
				this.dimesExpressions.addAll(dimesExpressions);
				this.initialiList.addAll(initialiList);
	}

	public List<IType> getTypeArguments() {
		return getType().getTypeArguments();
	}
	
	public List<Expression> getDimesExpressions() {
		return dimesExpressions;
	}
	
	public List<Expression> getInitialiList() {
		return initialiList;
	}
	
	@Override
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}


}
