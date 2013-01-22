package gool.ast.constructs;
import gool.generator.GoolGeneratorController;

import java.util.ArrayList;
import java.util.List;

/**
 * This captures packages in the intermediate language.
 * For each object member of Package the compiler will
 * have to generate a separate folder containing classes and packages
 * in the target language.
 */
public class Package extends Dependency {

	/**
	 * The list of classes that belong to the package.
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
	public String callGetCode() {
		return GoolGeneratorController.generator().getCode(this);
	}

}
