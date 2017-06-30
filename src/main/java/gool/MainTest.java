package gool;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import gool.GOOLCompiler;
import gool.executor.cpp.CppCompiler;
import gool.executor.csharp.CSharpCompiler;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;


public class MainTest {

	
	public static String commandBuilder (Map<String,String> fichiers) {
		
		Map.Entry<String, String> firstFile = fichiers.entrySet().iterator().next();
		String compileFile = firstFile.getKey();   /* docker run gcc:4.9*/
		String commande = "docker run reaverproject/gcc-boost:5_1_0-1.60.0 /bin/bash -c '";
		//String commande = "/bin/bash -c '";
		
		for (Map.Entry<String, String> entree : fichiers.entrySet()) {
			
			commande += "echo -e \"" + StringEscapeUtils.escapeJava(entree.getValue()) + "\" > " + entree.getKey() + " && ";
		}
		commande += "g++ " + compileFile + "&& ./a.out'";
		//commande += "g++ " + compileFile + " && ./a.out " +"'";
		
		return commande;
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main_old(String[] args) throws IOException {

		String srcinput = new String();
		try {
			srcinput = readFile("/home/arrivault/Codes/GOOL_ALL/GOOL_Gitlab/tests/GOOLINPUTJAVA/HelloWorld.java");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map <String, String> input = new HashMap<String,String>();
		input.put("HelloWorld.java", srcinput);
		
		Map <String, String> result = null;
		
		
		try {
			result = GOOLCompiler.launchTranslation("java", "cpp", input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String commande = commandBuilder(result);
		System.out.println(commande);
		//System.out.println(StringEscapeUtils.escapeJava(commande));
		
		/*for (Map.Entry entree : result.entrySet()) {
			
			System.out.println(entree.getKey());
			System.out.println();
			System.out.println(entree.getValue());
			
		}*/
		
		Runtime runtime = Runtime.getRuntime();
		final Process process;
		
		process = runtime.exec(new String [] {"/bin/bash","-c", commande}); 
		
		//process = runtime.exec(new String [] {"/bin/bash","-c", "docker run gcc:4.9 /bin/bash -c echo 1 && echo 2"});
		
		new Thread() {
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String line = "";
					try {
						while((line = reader.readLine()) != null) {
							// Traitement du flux de sortie de l'application
							
							System.out.println(line);
							
						}
					} finally {
						reader.close();
					}
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}.start();

		// Consommation de la sortie d'erreur de l'application externe dans un Thread separe
		new Thread() {
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					String line = "";
					try {
						while((line = reader.readLine()) != null) {
							// Traitement du flux d'erreur de l'application si besoin est
							
							System.err.println(line);
							
						}
						
					} finally {
						reader.close();
					}
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}.start();
		
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		//File fileinput = new File("/home/arrivault/Codes/GOOL_ALL/GOOL_Gitlab/tests/GOOLINPUTJAVA/HelloWorld.java");
		
		Map <String, String> input = new HashMap<String,String>();
		input.put("HelloWorld.java", readFile("/home/arrivault/Codes/GOOL_ALL/GOOL_Gitlab/tests/GOOLINPUTJAVA/HelloWorld.java"));
		
		Map <String, String> result = null;		
		
		try {
			//result = GOOLCompiler.launchTranslation("java", "cpp", input);
			result = GOOLCompiler.launchTranslation("java", "cs", input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//CppCompiler comp = (CppCompiler) CppPlatform.getInstance().getCompiler();
		CSharpCompiler comp = (CSharpCompiler) CSharpPlatform.getInstance().getCompiler();
		
		//List<String> std = comp.compileToExecutableWithDocker(result, null, "reaverproject/gcc-boost:5_1_0-1.60.0");
		List<String> std = comp.compileToExecutableWithDocker(result, null, "mono:latest");
		System.out.println("********************");
		for(String s : std){
			System.out.println(s);
		}
		System.out.println("********************");
	}
	
	static String readFile(String path) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded);
			}

}
