/*
 * Copyright 2010 Pablo Arrighi, Alex Concha, Miguel Lezama for version 1.
 * Copyright 2013 Pablo Arrighi, Miguel Lezama, Kevin Mazet for version 2.    
 *
 * This file is part of GOOL.
 *
 * GOOL is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, version 3.
 *
 * GOOL is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License along with GOOL,
 * in the file COPYING.txt.  If not, see <http://www.gnu.org/licenses/>.
 */

package gool.recognizer.cpp;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit.IDependencyTree.IASTInclusionNode;

import gool.recognizer.common.RecognizerMatcher;
import gool.recognizer.cpp.ast.ASTCppNode;
import gool.recognizer.cpp.ast.other.ASTCppIncludeStatement;
import gool.recognizer.cpp.ast.other.ASTCppTranslationUnit;

/**
 * This class is the recognizer for the import of the language C++. It is used
 * for the RecognizerMatcher (indirect input usage case).
 */
public class CppRecognizerImport extends CppRecognizer {

	/**
	 * List of file to add.
	 */
	private List<String> filesAdd = new ArrayList<String>();
	
	/**
	 * Gets the list of file to add.
	 * @return
	 * 		The list of file adding for the indirect input usage case of the RecognizerMatcher.
	 */
	public List<String> getFilesAdd(){
		return filesAdd;
	}
	
	@Override
	public Object visit(ASTCppTranslationUnit node, Object data) {
		IASTTranslationUnit tu = node.getNode() ;
				
		// Init the recognizer matcher.
		RecognizerMatcher.init("cpp");
		
		// Visits all include directive.
		for(IASTInclusionNode incAst : tu.getDependencyTree().getInclusions()){
			List<String> toAdd = null ;
			ASTCppNode incNode = ASTCppNode.transforme(incAst.getIncludeDirective()) ;
			if(incNode instanceof ASTCppIncludeStatement){
				toAdd = (List<String>) ((ASTCppIncludeStatement)incNode).accept(this, data);
				if(!toAdd.isEmpty())
					filesAdd.addAll(toAdd);
			}
			else
				System.out.println("Impossible de visiter : " + incAst.getClass());
		}
		return null;
	}

	@Override
	public Object visit(ASTCppIncludeStatement node, Object data) {
		IASTPreprocessorIncludeStatement inc = node.getNode() ;
		
		// Matchs the include directive.
		return RecognizerMatcher.matchImportAdd(inc.getName().toString());
	}

}
