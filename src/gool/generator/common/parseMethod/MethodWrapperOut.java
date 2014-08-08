package gool.generator.common.parseMethod;

import java.util.ArrayList;
import java.util.List;


public class MethodWrapperOut {

	private static final int paramName = 0 ;
	private static final int paramDefaultValue = 1 ;
	private static final int paramType = 2 ;

	
	private String goolMethod = "" ;
	private List<String[]> goolParams = new ArrayList<String[]>() ;
	private String concreteMethod = "" ;
	private String concreteMethodReturned = "" ;
	private List<String[]> concreteParams = new ArrayList<String[]>() ;
	
	public void setGoolMethod(String goolMethodName){
		this.goolMethod = goolMethodName ;
	}
	
	public void addParamGool(String goolParamName, String defaultValueParam){
		String[] toAdd = new String[]{goolParamName,defaultValueParam};
		goolParams.add(toAdd);
	}
	
	public void setConcreteMethod(String concreteMethodName){
		this.concreteMethod = concreteMethodName;
	}
	
	public void addParamConcrete(String concreteParamType, String concreteParamName, 
			 String defaultValueParam){
		String[] toAdd = new String[]{concreteParamName,defaultValueParam,concreteParamType};
		concreteParams.add(toAdd);
	}
	
	public void setConcreteMethodReturned(String concreteMethodTypeReturned){
		this.concreteMethodReturned = concreteMethodTypeReturned;
	}
	
	
	public void print(){
		System.out.print("[METHOD_WRAPPER_IN]: "+ this.goolMethod + " = " );
		for(String[] paramG : goolParams){
			System.out.print(paramG[MethodWrapperOut.paramName] + ":" 
							+ paramG[MethodWrapperOut.paramDefaultValue] + ", " );
		}
		System.out.print("\t|\t");
		System.out.print(this.concreteMethod + "("+ this.concreteMethodReturned +")" + " = ");
		for(String[] paramI : concreteParams){
			System.out.print(paramI[MethodWrapperOut.paramType] + " " +
					paramI[MethodWrapperOut.paramName] + ":" 
							+ paramI[MethodWrapperOut.paramDefaultValue] + ", " );
		}
		System.out.println();
	}
	
	public boolean validate(){
		for(String[] paramG : goolParams){
			int cpt = 0 ;
			for(String[] paramC : concreteParams){
				if(paramG[MethodWrapperOut.paramName]
						.equalsIgnoreCase(paramC[MethodWrapperOut.paramName]))
					cpt++;
			}
			if(cpt == 0)
				return false ;
		}
		return true ;
	}
	
}
