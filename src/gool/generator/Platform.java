package gool.generator;

import gool.ast.Dependency;
import gool.ast.type.PrimitiveType;
import gool.executor.SpecificCompiler;
import gool.generator.common.CodePrinter;

import java.util.HashMap;
import java.util.Map;

public abstract class Platform extends PrimitiveType {
	private static final Map<String, Platform> registeredPlatforms = new HashMap<String, Platform>();
	private String name;
	private CodePrinter codePrinter;
	private SpecificCompiler compiler;
	
	protected Platform(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("The name parameter can not be null or empty.");
		}
		this.name = name.toUpperCase();
		registeredPlatforms.put(name, this);
	}
	
	@Override
	public String toString() {
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

	@Override
	public String getName() {
		return name;
	}
	
	protected abstract CodePrinter initializeCodeWriter();
	
	public CodePrinter getCodePrinter() {
		if (codePrinter == null) {
			codePrinter = initializeCodeWriter();
		}
		return codePrinter;
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
	
	protected abstract SpecificCompiler initializeCompiler();

	public static Platform valueOf(String platform) {
		Platform p = registeredPlatforms.get(platform);
		if (p == null) {
			throw new IllegalArgumentException(String.format("Unknown platform %s.", platform));
		}
		return p;
	}
	
	public void registerCustomDependency(String key,
			Dependency value) {
		getCodePrinter().getCodeGenerator().addCustomDependency(key, value);
	}

}
