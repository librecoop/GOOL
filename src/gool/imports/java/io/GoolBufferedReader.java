








// Platform: JAVA

package gool.imports.java.io;
import gool.imports.java.io.GoolFile;
import gool.imports.java.io.GoolFileReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;




public  class GoolBufferedReader  {

    private BufferedReader outputlangobject;
    private String filename;

		public GoolBufferedReader(GoolFileReader goolFileReader){
		try{
		this.filename = goolFileReader.getFilename();
		this.outputlangobject = new BufferedReader(goolFileReader.getOutputlangobject());
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


		public GoolBufferedReader(String name){
		try{
		this.filename = name;
		this.outputlangobject = new BufferedReader(new FileReader(name));
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


		public GoolBufferedReader(GoolFile goolFile){
		try{
		this.filename = goolFile.getFilename();
		this.outputlangobject = new BufferedReader(new FileReader(goolFile.getOutputlangobject()));
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


		public int read(){
		int res=0;
		try{
		res=this.outputlangobject.read();
		}catch(Exception e){
		System.out.println(e.toString());
		}
		return res;
	}


		public String readLine(){
		String res=null;
		try{
		res=this.outputlangobject.readLine();
		}catch(Exception e){
		System.out.println(e.toString());
		}
		return res;
	}


		public void close(){
		try{
		this.outputlangobject.close();
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


}
