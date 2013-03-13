package gool.executor.objc;

import gool.Settings;
import gool.executor.Command;
import gool.executor.common.SpecificCompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ObjcCompiler extends SpecificCompiler{

	private static Logger logger = Logger.getLogger(ObjcCompiler.class.getName());
	@SuppressWarnings("unused")
	private static final boolean IS_WINDOWS = System.getProperty("os.name")
			.toUpperCase().contains("WINDOWS");
	
	public ObjcCompiler(File outputDir, List<File> deps) {
		super(outputDir, deps);
	}

	@Override
	public File compileToExecutable(List<File> files, File mainFile, List<File> classPath, List<String> args)
			throws FileNotFoundException {
		
		List<String> params = new ArrayList<String>();
		if (mainFile == null) {
			mainFile = files.get(0);
		}
		
		logger.info("--->" + mainFile);
		String execFileName = mainFile.getName().replace(".m", "");
		params.addAll(Arrays.asList(Settings.get("objc_compiler_cmd")) );
		
		for (File file : files) {
			if(!params.contains(file.toString()))
					params.add(file.toString());
		}
		
		/*
		 * Add the needed dependencies to be able to compile programs.
		 */
		if (classPath != null) {
			for (File dependency : classPath) {
				params.add(dependency.getAbsolutePath());
			}
		}

		/*for (File dependency : getDependencies()) {
			params.add(dependency.getAbsolutePath());
		}*/
		
		
		//params.addAll(Arrays.asList("-MMD", "-MP", "-DGNUSTEP", "-DGNUSTEP_BASE_LIBRARY=1", "-DGNU_GUI_LIBRARY=1", "-DGNU_RUNTIME=1", "-DGNUSTEP_BASE_LIBRARY=1", "-fno-strict-aliasing", "-fexceptions", "-fobjc-exceptions", "-D_NATIVE_OBJC_EXCEPTIONS" , "-pthread", "-fPIC", "-Wall", "-DGSWARN", "-DGSDIAGNOSE", "-Wno-import", "-g", "-O2", "-fgnu-runtime", "-fconstant-string-class=NSConstantString", "-I.", "-I/home/zalgo/GNUstep/Library/Headers", "-I/usr/local/include/GNUstep", "-I/usr/include/GNUstep"));
       
		//params.addAll(Arrays.asList(Settings.get("objc_compiler_lib"), "-o", execFileName));
		
		
		
		
		for(int i=0; i<20; i++) System.out.println("ATTENTION DEBUG, LOLILOL");
		System.out.println("Repertoire de sortie : "+getOutputDir());
		System.out.println("Taille de files : "+files.size());
		for(int i=0; i<params.size(); i++) System.out.println("param["+i+"] = "+params.get(i));
		for(int i=0; i<20; i++) System.out.println("DEBUG TERMINE, TROLOLOL");
		
		List<String> params2 = new ArrayList<String>();
		params2.addAll(Arrays.asList("gnustep-config", "--objc-flags"));
		String libParams=Command.exec(getOutputDir(), params2);
		while(libParams.contains(" ")){
			System.out.println("libParams : "+libParams);
			int idx=libParams.indexOf(" ");
			System.out.println("idx = "+idx);
			String s = libParams.substring(0, idx);
			params.add(s);
			System.out.println("s = "+s+ "("+s.length()+" caracteres)");
			libParams=libParams.replace(s+" ","");
		}
		if(!libParams.isEmpty())
			params.add(libParams.substring(0,libParams.length()-1));
		params.addAll(Arrays.asList("-lgnustep-base", "-o", execFileName));
		Command.exec(getOutputDir(), params);
		return new File(getOutputDir(), execFileName);
	}

	@Override
	public String getSourceCodeExtension() {
		return "m";
	}
	@Override
	public String run(File file, List<File> classPath)
			throws FileNotFoundException {
		List<String> params = new ArrayList<String>();

		List<String> deps = new ArrayList<String>();
		if (classPath != null) {
			for (File dependency : classPath) {
				deps.add(dependency.getParent());
			}
		}
		for (File dependency : getDependencies()) {
			deps.add(dependency.getParent());
		}

		Map<String, String> env = new HashMap<String, String>();
		params.addAll(Arrays.asList("./"+file.getName()));
		return Command.exec(getOutputDir(), params, env);
	}

	@Override
	public File compileToObjectFile(List<File> files, File mainFile,
			List<File> classPath, List<String> args)
			throws FileNotFoundException {
		return null;
	}

}
