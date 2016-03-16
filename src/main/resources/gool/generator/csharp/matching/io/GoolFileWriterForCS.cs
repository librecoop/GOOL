
public class GoolFileWriterForCS {
	private GoolFileForCS f = null;

	public GoolFileWriterForCS(GoolFileForCS f){
		this.f = f;
	}

	public string getName(){
		return f.getName();
	}
}