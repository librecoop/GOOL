using System;
using System.IO;
using System.Text;

public class GoolBufferedWriterForCS {
	private StreamWriter f = null;

	public GoolBufferedWriterForCS(GoolFileWriterForCS reader){
		this.f = new StreamWriter(reader.getName(), true);
	}

	public void write(Char carac){
		f.Write(carac);
	}

	public void write(string toAdd,int start,int length){
		if (length < 0)
			return;
		if (start > toAdd.Length)
			return;
		if (start + length > toAdd.Length)
			return;
		for(int i=start; i<length; i++)
			f.Write (toAdd[i]);
	}

	public void close(){
		f.Close ();
	}
}