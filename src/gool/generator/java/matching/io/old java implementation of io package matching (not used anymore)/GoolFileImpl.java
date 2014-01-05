








// Platform: JAVA

package gool.io;
import java.io.File;




public  class GoolFileImpl  {

    private File outputlangobject;
    private String filename;

	public GoolFileImpl(String name){
		this.outputlangobject = new File(name);
		this.filename = name;
	}


	public boolean createNewFile(){
		try{
		return outputlangobject.createNewFile();
		}catch (Exception e) {
			System.out.println(e.toString());
		}
		return false;
	}


	public boolean exists(){
		return outputlangobject.exists();
	}


	public boolean deleteFile(){
		return outputlangobject.delete();
	}


	public File getOutputlangobject(){
		return this.outputlangobject;
	}


	public String getFilename(){
		return this.filename;
	}


}
