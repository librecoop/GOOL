
public class GoolFileForCS
{
	private string name = null;
	public GoolFileForCS(string name){
		this.name = name ;
	}

	public bool createNewFile(){
		using (System.IO.FileStream fs = System.IO.File.Create(name)) {
			return System.IO.File.Exists (name);
		}
	}

	public bool exists(){
		return System.IO.File.Exists (name);
	}

	public bool deleteFile(){
		return System.IO.File.Delete(name);
	}
}

