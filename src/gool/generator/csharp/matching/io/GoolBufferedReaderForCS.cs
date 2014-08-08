#include "GoolFileReaderForCS.cs"

public class GoolBufferedReaderForCS {
	private char[] text = null ;
	long indice = 0 ;
	public GoolBufferedReaderForCS(GoolBufferedReaderForCS reader){
		text = System.IO.File.ReadAllText(reader.getName()).ToCharArray();
	}

	public int read(){
		text [indice];
		indice++;
	}

	public string readLine(){
		string toReturn = "";
		while(text [indice] != "\n"){
			toReturn += text [indice];
			indice++;
		}
		indice++;
		return toReturn;
	}

	public void close(){
		text = null ;
	}
}