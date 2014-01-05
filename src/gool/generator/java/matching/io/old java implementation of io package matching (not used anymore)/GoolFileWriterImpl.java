








// Platform: JAVA

package gool.io;
import gool.io.GoolFileImpl;
import java.io.File;
import java.io.FileWriter;




public  class GoolFileWriterImpl  {

    private FileWriter outputlangobject;
    private String filename;

		public GoolFileWriterImpl(GoolFileImpl goolFileImpl){
		try{
		this.outputlangobject = new FileWriter(goolFileImpl.getOutputlangobject());
		this.filename = goolFileImpl.getFilename();
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


	public GoolFileWriterImpl(String name){
		try{
		this.outputlangobject = new FileWriter(name);
		this.filename = name;
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


	public String getFilename(){
		return this.filename;
	}


	public FileWriter getOutputlangobject(){
		return this.outputlangobject;
	}


}
