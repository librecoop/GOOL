package gool.ast;
import java.util.ArrayList;
import java.util.List;

/**
 * This captures packages in the intermediate language.
 * For each object member of Package the compiler will
 * have to generate a separate folder containing classes and packages
 * in the target language.
 * It is at the root of the Abstract Syntax Tree of the intermediate language.
 */
public class Package extends Dependency {

	/**
	 * The list of classes that belong to the current package.
	 */
	private List<ClassDef> classes=new ArrayList<ClassDef>();
	private String name;
	
	public Package(String packageName) {
		this.name = packageName;
	}
	
	public String getName() {
		return name;
	}

	public final boolean addClass(ClassDef mclass){
		return classes.add(mclass);
	}

	@Override
	public String toString() {
		return name;
	}
}
