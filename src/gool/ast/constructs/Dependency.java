package gool.ast.constructs;


/**
 * A dependency is the abstract GOOL representation 
 * of the existence of an external class
 * which is used in the code of some abstract GOOL class
 * Gets implemented by CustomDependency
 * TODO: Maybe the two could be merged
 */
public abstract class Dependency extends Node{

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Dependency) || !(obj instanceof String)) {
			return false;
		} else if (obj instanceof String) {
			return toString().equals(obj.toString());
		}
		return toString().equals(((Dependency) obj).toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
