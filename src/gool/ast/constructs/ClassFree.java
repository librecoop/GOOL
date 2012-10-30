package gool.ast.constructs;

/**
 * This class captures the "free" of the intermediate language, i.e freeing an
 * unused object.
 */
public final class ClassFree extends Statement {

	/**
	 * The pointer to the instance.
	 */
	private ClassNew classnew;

	/**
	 * @param classcall
	 *            is a pointer to the objects instantiation place.
	 */
	public ClassFree(ClassNew classcall) {
		this.setClassNew(classcall);
	}

	/**
	 * Sets the node that instantiates the object.
	 * 
	 * @param classnew
	 *            the class that instantiates the object.
	 */
	public final void setClassNew(ClassNew classnew) {
		this.classnew = classnew;
	}

	/**
	 * Gets the class instantiation node.
	 * 
	 * @return the node that instantiates the object.
	 */
	public final ClassNew getClassNew() {
		return classnew;
	}
}
