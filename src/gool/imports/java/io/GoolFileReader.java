








// Platform: JAVA

package gool.imports.java.io;
import gool.imports.java.io.GoolFile;
import java.io.File;
import java.io.FileReader;




public  class GoolFileReader  {

    private FileReader outputlangobject;
    private String filename;

		public GoolFileReader(GoolFile goolFile){
		try{		
		this.outputlangobject = new FileReader(goolFile.getOutputlangobject());
		this.filename = goolFile.getFilename();
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


		public GoolFileReader(String name){
		try{
		this.outputlangobject = new FileReader(name);
		this.filename = name;
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


		public String getFilename(){
		return this.filename;
	}


		public FileReader getOutputlangobject(){
		return this.outputlangobject;
	}


}
