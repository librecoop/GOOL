package gool.recognizer.common.parseClass;

public class ClassWrapperIn {
	
	private String goolClass = "" ;
	private String inputClass = "" ;
	
	public void setGoolClass(String goolClassName){
		this.goolClass =  goolClassName;
	}
	
	public void setInputClass(String inputClassName){
		this.inputClass = inputClassName;
	}
	
	public void print(){
		System.out.println("[CLASS_WRAPPER_IN]: "+ this.goolClass + "\t|\t" 
				+ this.inputClass);
	}
	
}
