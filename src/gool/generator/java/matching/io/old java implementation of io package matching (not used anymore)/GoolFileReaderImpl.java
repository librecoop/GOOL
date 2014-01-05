








// Platform: JAVA

package gool.io;
import gool.io.GoolFileImpl;
import java.io.File;
import java.io.FileReader;




public  class GoolFileReaderImpl  {

    private FileReader outputlangobject;
    private String filename;

	public GoolFileReaderImpl(GoolFileImpl goolFileImpl){
		try{		
		this.outputlangobject = new FileReader(goolFileImpl.getOutputlangobject());
		this.filename = goolFileImpl.getFilename();
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


	public GoolFileReaderImpl(String name){
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
