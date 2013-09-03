








// Platform: JAVA

package gool.imports.java.io;
import java.io.File;




public  class GoolFile  {

    private File outputlangobject;
    private String filename;

		public GoolFile(String name){
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
