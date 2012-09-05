package gool.ast;

import gool.GoolGeneratorController;
import gool.ast.type.TypeVoid;

/**
 * This class accounts for method declarations in the intermediate language.
 * Hence it is an OOTDec.
 * 
 * @param T
 *            is the return type, if known at compile time, otherwise put
 *            OOTType. That way java generics grant us some level of type
 *            checking of the generated code at compiler design time. Sometimes
 *            we will not be able to use this though, because we will not know T
 *            at compiler design time.
 */
public class MainMeth extends Meth {
	public MainMeth() {
		super(TypeVoid.INSTANCE, Modifier.STATIC, "main");
		addModifier(Modifier.PUBLIC);
	}

	@Override
	public String getHeader() {
		return GoolGeneratorController.generator().getCode(this);
	}
	
	@Override
	public boolean isMainMethod() {
		return true;
	}
}
