package gool.parser.cpp;

import gool.ParseGOOL;
import gool.Settings;
import gool.ast.core.ClassDef;
import gool.recognizer.cpp.CppRecognizer;
import gool.recognizer.cpp.CppRecognizerImport;
import gool.recognizer.cpp.ast.ASTCppNode;
import logger.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;

/**
 * This class parses concrete Cpp into abstract GOOL. For this purpose it
 * relies on Eclipse's CDT parser.
 */
public class CppParser extends ParseGOOL {

	/**
	 * The real parser GOOL, which transforms a C++ AST into a GOOL AST.
	 * @param translationUnits
	 * 		: The collection of transition units which can be transformed into GOOL AST.
	 * @param dependencies
	 * 		: List of dependencies files.
	 * @param visitor
	 * 		: The visitor, which create the GOOL AST (ie. the Recognizer).
	 * @return
	 * 		The GOOL AST associated to the transition units.
	 * @throws Exception
	 */
	public static Collection<ClassDef> parseGool(
			Collection<IASTTranslationUnit> translationUnits,
			List<File> dependencies, CppRecognizer visitor) throws Exception {
		if (visitor == null) {
			throw new IllegalArgumentException("The gool visitor is null.");
		}
		Log.d("\n\n****************** Start Main Scan *******************\n\n");
		for(IASTTranslationUnit translationUnit : translationUnits){
			ASTCppNode.transforme(translationUnit).accept(visitor,null);
		}
		Log.d("\n\n****************** End Main Scan *******************\n\n");
//		for (ClassDef classDef : visitor.getGoolClasses()) {
//			classDef.getPlatform().registerCustomDependency(
//					classDef.getName(), new ClassDef(classDef.getName() + ".h"));
//		}
		
		return visitor.getGoolClasses();
	}
	
	
	@Override
	public Collection<ClassDef> parseGool(Map<String, String> input)
			throws Exception {
		Collection<IASTTranslationUnit> translationUnits = new ArrayList<IASTTranslationUnit>();
		for(Entry<String, String> entry : input.entrySet()){
			FileContent fc = FileContent.create(entry.getKey(), 
					entry.getValue().toCharArray());
			IASTTranslationUnit tu = creatIASTTranslationUnit(fc);
			// First step : indirect input usage case.
			CppRecognizerImport visitImport = new CppRecognizerImport() ;
			ASTCppNode.transforme(tu).accept(visitImport,null);
			translationUnits.add(tu);
			for(String dep : visitImport.getFilesAdd()){	
				translationUnits.add(creatIASTTranslationUnit(
					FileContent.createForExternalFileLocation(new File(dep).getPath())));
			}
		}
		
		// transformed into a GOOL AST : second step. Parse files in folder cpp_in_tmp.
		return parseGool(translationUnits, null, new CppRecognizer());
	}

	@Override
	public Collection<ClassDef> parseGool(Collection<? extends File> inputFiles)
			throws Exception {
		Collection<IASTTranslationUnit> translationUnits = new ArrayList<IASTTranslationUnit>();

		// First step : indirect input usage case.
		for(File input : inputFiles)
		{
			IASTTranslationUnit tu = creatIASTTranslationUnit(
					FileContent.createForExternalFileLocation(input.getPath())
					);
			
			CppRecognizerImport visitImport = new CppRecognizerImport() ;
			ASTCppNode.transforme(tu).accept(visitImport,null);
			translationUnits.add(tu);
			for(String dep : visitImport.getFilesAdd()){
				translationUnits.add(creatIASTTranslationUnit(
						FileContent.createForExternalFileLocation(new File(dep).getPath())));
			}
		}
		
		// transformed into a GOOL AST : second step. Parse files in folder cpp_in_tmp.
		return parseGool(translationUnits, null, new CppRecognizer());
	}
	
	
	/**
	 * To parse C++ as string.
	 * @param input
	 * 		: The code C++ in input as FileContent.
	 * @return
	 * 		A transition unit about the input string.
	 * @throws Exception
	 */
	public static IASTTranslationUnit creatIASTTranslationUnit(FileContent fc)
			throws Exception {
		Map<String, String> macroDefinitions = new HashMap<String, String>();
		String[] includeSearchPaths = Settings.get("cpp_in_libraries").split(" ");
		IScannerInfo si = new ScannerInfo(macroDefinitions, includeSearchPaths);
		IncludeFileContentProvider ifcp = IncludeFileContentProvider.getEmptyFilesProvider();
		IIndex idx = null;
		int options = ILanguage.OPTION_IS_SOURCE_UNIT;
		IParserLogService log = new DefaultLogService();
		return GPPLanguage.getDefault().getASTTranslationUnit(fc, si, ifcp,
				idx, options, log);
	}
}