








// Platform: JAVA

package gool.io;
import gool.io.GoolFileImpl;
import gool.io.GoolFileReaderImpl;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;




public  class GoolBufferedReaderImpl  {

    private BufferedReader outputlangobject;
    private String filename;

	public GoolBufferedReaderImpl(GoolFileReaderImpl goolFileReaderImpl){
		try{
		this.filename = goolFileReaderImpl.getFilename();
		this.outputlangobject = new BufferedReader(goolFileReaderImpl.getOutputlangobject());
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


	public GoolBufferedReaderImpl(String name){
		try{
		this.filename = name;
		this.outputlangobject = new BufferedReader(new FileReader(name));
		}catch(Exception e){
		System.out.println(e.toString());
		}
	}


	public GoolBufferedReaderImpl(GoolFileImpl goolFileImpl){
		try{
		this.filename = goolFileImpl.getFilename();
		this.outputlangobject = new BufferedReader(new FileReader(goolFileImpl.getOutputlangobject()));
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
