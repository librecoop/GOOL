import java.util.List;
import java.util.ArrayList;

public class test{
	public static void main(String[] args){
		try{
			List<String> list1 = new ArrayList<String>();
			List<String> list2 = new ArrayList<String>();
			list1.add("toto");
			list1.add("tata");
			list1.add("titi");
			if(1 == list1.indexOf("tata"))
			{
				System.out.println("true");
			}else{
				System.out.println("false");
			}
			
		}catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
}
