import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
* HelloYou class
* @author Denis Arrivault
*/
public class HelloYou {
	BufferedReader inputBuffer;
	private String myName;
	private int myAge;

	public HelloYou() {
		inputBuffer = new BufferedReader(new InputStreamReader(System.in));
		myName = "";
		myAge = -1;
	}

	public void askAge(){
		System.out.println("How old are you?");
		try{
			myAge = Integer.parseInt(inputBuffer.readLine());
		}
		catch(IOException e ) {
			e.printStackTrace();
		} 
		while(myAge < 0 || myAge > 120){
			System.out.println(myAge + "! Come on!");
			System.out.println("Please, try again : ");
			try{
				myAge = Integer.parseInt(inputBuffer.readLine());
			}
			catch(IOException e ) {
				e.printStackTrace();
			}
		}
	}

	public void askName(){
		while(myName.isEmpty()){
			System.out.println("What is your name?");
			try{
				myName = inputBuffer.readLine();
			}
			catch(IOException e ) {
				e.printStackTrace();
			}
		}
	}

	public void display(){
		System.out.println("Hi " + myName);
		if(myAge < 15){
			System.out.println("\"I'm youth, I'm joy, I'm a little bird that has broken out of the egg.\" James M. Barrie");
		}
		else if(myAge < 25){
			System.out.println("\"At 19, everything is possible and tomorrow looks friendly.\" Jim Bishop");
		}
		else if(myAge < 60){
			System.out.println("\"Middle age is youth without levity, and age without decay.\" Doris Day");
		}
		else{
			System.out.println("\"With age comes wisdom, but sometimes age comes alone.\" Oscar Wilde.");
		}
	}

	public static void main(String[] args) {
		HelloYou mc = new HelloYou();
		mc.askAge();
		mc.askName();
		mc.display();

	}

}
