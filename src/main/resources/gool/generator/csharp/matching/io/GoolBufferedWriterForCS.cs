#include "GoolFileWriterForCS.cs"

public class GoolBufferedWriterForCS {
	private GoolBufferedWriterForCS reader = null ;
	private System.IO.StreamWriter file = null;
	public GoolBufferedWriterForCS(GoolBufferedWriterForCS reader){
		this.reader = reader;
		this.file = new System.IO.StreamWriter (reader.getName (), true);
	}

	public void write(int carac){
		file.Write (carac);
	}

	public void write(string toAdd,int start,int length){
		using (System.IO.StreamWriter file = new System.IO.StreamWriter("##GOOL##" + reader.getName (), true))
		{
			char[] text = System.IO.File.ReadAllText(reader.getName()).ToCharArray();
			int i = 0;
			for(;i< start; i++){
				file.Write (text [i]);
			}
			char[] p = toAdd.toCharArray ();
			for(int j=0;j<length ; i++ , j++){
				file.Write (p[j]);
			}
			for(; i < text.Length ; i++){
				file.Write (text [i]);
			}
			file.Close ();
			System.IO.File.Move ("##GOOL##" + reader.getName (), reader.getName ());
		}
	}

	public void close(){
		file.Close ();
	}
}