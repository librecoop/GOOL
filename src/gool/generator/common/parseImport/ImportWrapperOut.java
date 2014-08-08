package gool.generator.common.parseImport;

import java.util.ArrayList;
import java.util.List;

public class ImportWrapperOut {
	
	private String goolImport = "" ;
	private List<String> goolIndirectImports = new ArrayList<String>() ;
	private List<String> inputImports = new ArrayList<String>() ;
	
	public void setGoolImport(String goolImportName){
		this.goolImport =  goolImportName;
	}
	
	public void addGoolIndirect(String goolImportName){
		this.goolIndirectImports.add(goolImportName);
	}
	
	public void addInputImport(String inputImportName){
		this.inputImports.add(inputImportName);
	}
	
	public void print(){
		System.out.print("[IMPORT_WRAPPER_IN]: "+ this.goolImport + " " );
		for(String importG : goolIndirectImports){
			System.out.print(importG + " : ");
		}
		System.out.print("\t|\t");
		for(String importI : inputImports){
			System.out.print(importI + " : ");
		}
		System.out.println();
	}
	
}
