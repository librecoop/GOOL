import java.util.List;
import java.util.ArrayList;

public class test{
	public static void main(String[] args){
		try{
			List<String> list = new ArrayList<String>();
			List<String> list2 = new ArrayList<String>();
			list.add("toto");
			list.add("tata");
			list.add("titi");
			if(1 == list.indexOf("tata"))
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
