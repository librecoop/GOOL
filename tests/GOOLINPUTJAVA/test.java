import java.util.List;
import java.util.ArrayList;

public class test{
	public static void main(String[] args){
		try{
			List<String> list = new ArrayList<String>();
			List<String> list2 = new ArrayList<String>();
			list.add("toto");
			list.add("tata");
			list2.add("toto");
			list2.add("tata");
			if(list.equals(list2)){
				System.out.println("true");
			}
			else{
				System.out.println("false");
			}
			list2.add("titi");
			if(list.equals(list2)){
				System.out.println("true");
			}
			else{
				System.out.println("false");
			}
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
}
