package gool.ast.constructs;
import gool.ast.printer.GoolGeneratorController;

import java.util.ArrayList;
import java.util.List;

/**
 * This captures packages in the intermediate language.
 * For each object member of Package the compiler will
 * have to generate a separate folder containing classes and packages
 * in the target language.
 * The root of the abstract GOOL tree is a Package.
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
		//return name;  XXXXXXXXXXXXXXXXXXXXXXXXXXX
		//Normally this should be performed by a codegenerator.getCode(this), 
		//but since Platforms are specified on a per class basis, be do not know which is our codegenerator for now.
		//Unless we go for:
		return GoolGeneratorController.generator().getCode(this);
	}
}
