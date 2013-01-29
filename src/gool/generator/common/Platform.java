package gool.generator.common;

import gool.ast.constructs.Dependency;
import gool.ast.type.PrimitiveType;
import gool.executor.common.SpecificCompiler;

import java.util.HashMap;
import java.util.Map;

/**
 * Platforms are PrimitiveTypes.
 * They specify the Target language for a piece of the abstract GOOL.
 * They are listed in a global register Platform.registeredPlatforms.
 * @author parrighi
 */
public abstract class Platform extends PrimitiveType {
	/**
	 * The static, global platform register.
	 */
	private static final Map<String, Platform> registeredPlatforms = new HashMap<String, Platform>();
	/**
	 * Chosen name for the platform.
	 */
	private String name;
	/**
	 * Which gool.generator generates the concrete Target for this platform.
	 */
	private CodePrinter codePrinter;
	/**
	 * which gool.executor executes the generated concrete Target code.
	 */
	private SpecificCompiler compiler;
	
	/**
	 * This creates a platform and puts it in the global platform register.
	 * @param name
	 */
	protected Platform(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("The name parameter can not be null or empty.");
		}
		this.name = name.toUpperCase();
		registeredPlatforms.put(name, this);
	}
	
	/**
	 * Finds a platform in the global register
	 * @param Name of a platform
	 * @return A previously registered platform of this same name
	 */
	public static Platform valueOf(String platform) {
		Platform p = registeredPlatforms.get(platform);
		if (p == null) {
			throw new IllegalArgumentException(String.format("Unknown platform %s.", platform));
		}
		return p;
	}
	
	/**
	 * This is the method which instantiates a gool.generator.CodePrinter, which then works out a CodeGenerator, for this platform.
	 * @return A generator
	 */
	protected abstract CodePrinter initializeCodeWriter();
	

	
	/**
	 * This is the method which instantiates a gool.executor for this platform.
	 * @return An executor
	 */
	protected abstract SpecificCompiler initializeCompiler();

	

	/**
	 * Tell the CodeGenerator of this platform that some class has been generated, so that it gets imported by the other classes.
	 * @param key
	 * @param value
	 */
	public void registerCustomDependency(String key,
			Dependency value) {
		getCodePrinter().getCodeGenerator().addCustomDependency(key, value);
	}
	
	/**
	 * Getters and Setters
	 */
	
	/**
	 * @return the CodePrinter associated to a given Platform
	 */
	public CodePrinter getCodePrinter() {
		if (codePrinter == null) {
			codePrinter = initializeCodeWriter();
		}
		return codePrinter;
	}
	
	public void reInitializeCodePrinter() {
		codePrinter = initializeCodeWriter();
	}
	
	public void setCompiler(SpecificCompiler compiler) {
		this.compiler = compiler;
	}
	
	public SpecificCompiler getCompiler() {
		if (compiler == null) {
			compiler = initializeCompiler();
		}
		return compiler;
	}	
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		return name.equalsIgnoreCase(((Platform)obj).name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * This is redefinition of toString which calls the visitor's getCode in such a way that it generates code specifically fo this leaf of the abstract GOOL tree.
	 */
	@Override
	public String callGetCode() {
		return codePrinter.getCodeGenerator().getCode(this);
	}
}
