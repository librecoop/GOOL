








// Platform: JAVA

package gool.io;
import gool.io.GoolFileImpl;
import gool.io.GoolFileWriterImpl;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;




public  class GoolBufferedWriterImpl  {
	
    private BufferedWriter outputlangobject;
    private String filename;

	public GoolBufferedWriterImpl(GoolFileWriterImpl goolFileWriterImpl){
		try{		
		this.filename = goolFileWriterImpl.getFilename();
		this.outputlangobject = new BufferedWriter(goolFileWriterImpl.getOutputlangobject());
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


	public GoolBufferedWriterImpl(String name){
		try{
		this.filename = name;
		this.outputlangobject = new BufferedWriter(new FileWriter(name));
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


	public GoolBufferedWriterImpl(GoolFileImpl goolFileImpl){
		try{
		this.filename = goolFileImpl.getFilename();
		this.outputlangobject = new BufferedWriter(new FileWriter(goolFileImpl.getOutputlangobject()));
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
