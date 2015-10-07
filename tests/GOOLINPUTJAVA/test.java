import java.util.List;
import java.util.ArrayList;

public class test{
	public static void main(String[] args){
		/* creation of a list -- size + clear */
		try{
			List<String> list = new ArrayList<String>();
			if(list.isEmpty()){ 
				System.out.println("true");
			}else
			{ 
				System.out.println("false");
			}
			list.add("toto");
			list.add("tata");
			if(2 == list.size())
			{
				System.out.println("true"); 
			}else{
				System.out.println("false");
			}
			list.clear();
			if(list.isEmpty()){ 
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