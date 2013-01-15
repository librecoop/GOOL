package gool.ast.type;

import gool.ast.constructs.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * This interface accounts for all types in the intermediate language.
 */
public abstract class IType extends Node {
	
	/**
	 * The type arguments. It is used to store 'internal' types for List, Maps, etc.
	 */
	private List<IType> typeArguments = new ArrayList<IType>();

	/**
	 * Gets the type's name.
	 * @return a string representing the type's name.
	 */
	public abstract String  getName();

	/**
	 * Gets the arguments of a type.
	 * @return a list containing all the type arguments.
	 */
	
	public List<IType> getTypeArguments() {
		return typeArguments;
	}
	
	/**
	 * Add a new type argument to the current type.
	 * @param type the type to be added.
	 */
	public void addArgument(IType type) {
		getTypeArguments().add(type);
	}
	
	/**
	 * Adds a collection of type arguments.
	 * @param params the type collection to be added.
	 */
	public void addArguments(List<IType> params) {
		getTypeArguments().addAll(params);
	}
	
}