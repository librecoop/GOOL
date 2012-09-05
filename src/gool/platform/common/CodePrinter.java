package gool.platform.common;

import gool.GoolGeneratorController;
import gool.ast.ClassDef;
import gool.ast.Dependency;
import gool.ast.INode;
import gool.ast.type.IType;
import gool.exception.VelocityException;
import gool.platform.Platform;
import gool.util.Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * Provides the basic functionality to generate code from a list of GOOL
 * classes. This class is used to parse the velocity templates depending of the
 * target language.
 * 
 * Note: Only one {@link CodeGenerator} at a time is supported.
 */
public abstract class CodePrinter {
	/**
	 * Logger.
	 */
	private static final Logger LOG = Logger.getLogger(CodePrinter.class);
	/**
	 * The directory where the generated code will be written.
	 */
	private File outputDir;

	/**
	 * The list of GOOL classes.
	 */
	private Set<ClassDef> printedClasses = new HashSet<ClassDef>();

	/**
	 * the Velocity template engine.
	 */
	private VelocityEngine engine;

	/**
	 * The selected code generator.
	 */
	private CodeGenerator generator;

	/**
	 * Creates a new {@link CodePrinter} with a specific {@link CodeGenerator}.
	 * 
	 * @param generator
	 *            the {@link CodeGenerator} used to produce the code.
	 * @param outputDir
	 * @throws Exception
	 * @throws Exception
	 *             when the velocity engine can not be properly initialized.
	 */
	public CodePrinter(CodeGenerator generator, File outputDir) {
		this.generator = generator;
		this.engine = new VelocityEngine();
		this.outputDir = outputDir;

		GoolGeneratorController.setCodeGenerator(generator);

		try {
			Properties p = new Properties();
			p.setProperty("resource.loader", "class");
			p
					.setProperty("class.resource.loader.class",
							"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			engine.init(p);
		} catch (Exception e) {
			throw new VelocityException(
					"The velocity engine can not be properly initialized.", e);
		}

	}

	/**
	 * Cleans the output directory.
	 */
	public void clean() {
		File[] files = outputDir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (!file.isFile()) {
					file.delete();
				}
			}
		}
	}

	/**
	 * Gets the {@link CodeGenerator} associated to this printer.
	 * 
	 * @return the {@link CodeGenerator} associated to this printer.
	 */
	public CodeGenerator getCodeGenerator() {
		return generator;
	}

	/**
	 * Parses a Velocity template with its required parameters.
	 * 
	 * @param template
	 *            the relative path to a velocity template.
	 * @param classDef
	 *            the appropriate node required by the template.
	 * @return the code generated for the selected template.
	 * @throws Exception
	 * @throws ParseErrorException
	 * @throws ResourceNotFoundException
	 * @throws Exception
	 *             when Velocity is unable to parse the template.
	 */
	public String processTemplate(String templateFilename, INode classDef) {

		try {
			String templateFile = getTemplateDir() + templateFilename;
			LOG.debug(String.format("Loading template: %s", templateFile));

			Template template = engine.getTemplate(templateFile);

			VelocityContext context = new VelocityContext();
			context.put("class", classDef);
			context.put("macros", getTemplateDir() + "macros.vm");
			context.put("Helper", Helper.class);
			StringWriter writer = new StringWriter();
			template.merge(context, writer);

			return writer.toString();
		} catch (Exception e) {

			throw new VelocityException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Generates the code for a GOOL class and its corresponding dependent
	 * classes.
	 * 
	 * @param pclass
	 *            the class to generate.
	 * @throws FileNotFoundException
	 * @throws Exception
	 *             when the generated code can not be written to the file system
	 *             or when there is a parsing error in Velocity templates.
	 */
	public List<File> print(ClassDef pclass) throws FileNotFoundException {
		/*
		 * Delegate the code generation to the ClassDef object, thus it can
		 * decide if the current printer needs to be changed depending on its
		 * platform.
		 */
		String code = pclass.getCode();

		PrintWriter writer;
		File dir = new File(getOutputDir().getAbsolutePath(), StringUtils
				.replace(pclass.getPackageName(), ".", File.separator));
		dir.mkdirs();
		File classFile = new File(dir, getFileName(pclass.getName()));

		LOG.debug(String.format("Writing to file %s", classFile));
		writer = new PrintWriter(classFile);
		writer.println(code);
		writer.close();
		printedClasses.add(pclass);
		List<File> result = new ArrayList<File>();
		result.add(classFile);
		for (Dependency dependency : pclass.getDependencies()) {
			if (!printedClasses.contains(dependency)
					&& dependency instanceof ClassDef) {
				result.addAll(print((ClassDef) dependency));
			}
		}
		return result;
	}

	/**
	 * Gets the filename that should be used for the corresponding class. For
	 * example, Java forces the same name to be used for the file and the
	 * (public) class.
	 * 
	 * @param className
	 *            a class name.
	 * @return the correct filename for the specified class.
	 */
	public abstract String getFileName(String className);

	/**
	 * Gets the path where the Velocity templates are located.
	 * 
	 * @return the path where the velocity templates are located.
	 */
	public abstract String getTemplateDir();

	/**
	 * Gets the output directory.
	 * 
	 * @return the output directory.
	 */
	public File getOutputDir() {
		return outputDir;
	}

	private static Map<Platform, CodePrinter> codePrinters = new HashMap<Platform, CodePrinter>();

	public static void registerWriter(Platform platform, CodePrinter writer) {
		codePrinters.put(platform, writer);
	}

	public static CodePrinter getPrinter(IType platform) {
		CodePrinter codePrinter = ((Platform) platform).getCodePrinter();
		if (codePrinter == null) {
			throw new IllegalStateException(
					String
							.format(
									"There are no registered code writers for the specified platform: %s",
									platform));
		}

		GoolGeneratorController
				.setCodeGenerator(codePrinter.getCodeGenerator());
		return codePrinter;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	public static Set<Platform> getRegistredPlatforms() {
		return codePrinters.keySet();
	}

	public Collection<File> print(Collection<ClassDef> generatedClassDefs,
			boolean isGool) throws FileNotFoundException {
		Collection<File> result = new ArrayList<File>();
		for (ClassDef classDef : generatedClassDefs) {
			result.addAll(print(classDef));
		}

		return result;
	}
}
