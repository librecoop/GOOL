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

package gool.recognizer.cpp.visitor;

import org.eclipse.cdt.core.dom.ast.IASTNode;

import gool.recognizer.cpp.ast.ASTCppNode;
import logger.Log;

/**
 * Helper of debug session for a C++ AST.
 * Pattern singleton.
 */
public class DebugASTCpp {

	/**
	 * Instance for the singleton.
	 */
	private static DebugASTCpp instance = new DebugASTCpp() ;
	
	/**
	 * True if you want to print the AST.
	 */
	private boolean printAstCDT = true ;
	
	/**
	 * True if you want to print the type of nodes.
	 */
	private boolean printAstCDTType = true ;
	
	/**
	 * For the indentation.
	 */
	private int printer_tab = 0 ;
	
	public boolean isPrintAstCDT() {
		return printAstCDT;
	}

	public void setPrintAstCDT(boolean printAstCDT) {
		this.printAstCDT = printAstCDT;
	}

	public boolean isPrintAstCDTType() {
		return printAstCDTType;
	}

	public void setPrintAstCDTType(boolean printAstCDTType) {
		this.printAstCDTType = printAstCDTType;
	}
	
	public DebugASTCpp(){
		setPrintAstCDT(printAstCDT);
		setPrintAstCDTType(printAstCDTType);
	}
	
	public static DebugASTCpp getInstance(){
		return instance;
	}

	public static enum EASTstatu {
		LEAVE, VISIT
	}
	
	public void printAstIfYouWant(EASTstatu statu, String nodeName, ASTCppNode node){
		String toPrint;
		if(isPrintAstCDT()){
			switch (statu) {
			case LEAVE:
				printer_tab-- ;
				toPrint = "";
				for(int i = 0 ; i < printer_tab ; i++)
					toPrint += "--";
				toPrint += "leave" + " : " + nodeName;
				if(isPrintAstCDTType())
					toPrint += " -> " + node.getClass().getName();
				Log.d(toPrint);
				break;

			case VISIT:
				toPrint = "";
				for(int i = 0 ; i < printer_tab ; i++)
					toPrint += "--";
				toPrint += "visit" + " : " + nodeName;
				if(isPrintAstCDTType())
					toPrint += " -> " + node.getClass().getName();
				Log.d(toPrint);
				printer_tab++ ;
				break; 
				
			default:
				break;
			}
		}
	}
	
	
	public void printChildrenTypes(IASTNode[] children){
		for(IASTNode child : children){
			Log.d(child.getClass().toString());
		}
	}
	
}
