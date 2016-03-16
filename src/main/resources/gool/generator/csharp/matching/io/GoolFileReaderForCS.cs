
public class GoolFileReaderForCS {
	private GoolFileForCS f = null;

	public GoolFileReaderForCS(GoolFileForCS f){
		this.f = f;
	}

	public string getName(){
		return f.getName();
	}
}