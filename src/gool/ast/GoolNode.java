package gool.ast;

import gool.parser.stubs.Language;

import java.util.ArrayList;
import java.util.List;


/**
 * The type of the nodes which allow us to represent the Abstract Syntax Tree of the 
 * intermediate language.
 */

public abstract class GoolNode implements INode {

	private List<Language> supportedLanguages = new ArrayList<Language>();  
	
	public void setSpecificLanguage(Language lang){
		supportedLanguages.add(lang);
	}
	
	public boolean isSupportedLanguage(Language lang){
		return supportedLanguages.isEmpty() || supportedLanguages.contains(lang);
	}
}
