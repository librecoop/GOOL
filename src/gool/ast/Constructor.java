package gool.ast;

import gool.GoolGeneratorController;
import gool.ast.type.TypeNone;

import java.util.ArrayList;
import java.util.List;

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
public class Constructor extends Meth {

	/**
	 * The constructor parent calls
	 */
	private List<InitCall> initCalls = new ArrayList<InitCall>();

	public Constructor() {
		super(TypeNone.INSTANCE, Modifier.PUBLIC, "init");
	}

	public void addInitCall(InitCall initCall) {
		initCalls.add(initCall);
	}

	public boolean isConstructor() {
		return true;
	}

	public List<InitCall> getInitCalls() {
		return initCalls;
	}
	
	@Override
	public String getHeader() {
		return GoolGeneratorController.generator().getCode(this);		
	}
}
