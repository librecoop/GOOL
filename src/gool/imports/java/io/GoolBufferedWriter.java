








// Platform: JAVA

package gool.imports.java.io;
import gool.imports.java.io.GoolFile;
import gool.imports.java.io.GoolFileWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;




public  class GoolBufferedWriter  {

    private BufferedWriter outputlangobject;
    private String filename;

		public GoolBufferedWriter(GoolFileWriter goolFileWriter){
		try{		
		this.filename = goolFileWriter.getFilename();
		this.outputlangobject = new BufferedWriter(goolFileWriter.getOutputlangobject());
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


		public GoolBufferedWriter(String name){
		try{
		this.filename = name;
		this.outputlangobject = new BufferedWriter(new FileWriter(name));
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


		public GoolBufferedWriter(GoolFile goolFile){
		try{
		this.filename = goolFile.getFilename();
		this.outputlangobject = new BufferedWriter(new FileWriter(goolFile.getOutputlangobject()));
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


		public void write(int x){
		try{
		this.outputlangobject.write(x);
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


		public void write(String s, int off, int len){
		try{
		this.outputlangobject.write(s, off, len);
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


		public void close(){
		try{
		this.outputlangobject.close();
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


}
