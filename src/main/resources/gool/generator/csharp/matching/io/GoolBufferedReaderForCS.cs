using System;
using System.IO;
using System.Text;

public class GoolBufferedReaderForCS {
	private string text = null ;
	int indice = 0 ;

	public GoolBufferedReaderForCS(GoolFileReaderForCS reader){
		text = File.ReadAllText(reader.getName());
	}

	public int read(){
		if (indice >= text.Length)
			return -1;
		int res = (int)(text[indice]);
		indice++;
		return res;
	}

	public string readLine(){
		string toReturn = "";
		if (indice >= text.Length)
			return null;
		while(indice < text.Length && !text[indice].Equals('\n')){
			toReturn += text[indice];
			indice++;
		}
		indice++;
		return toReturn;
	}

	public void close(){
		text = null ;
	}
}