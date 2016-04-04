package gool.recognizer.common;

public class RecognizerMatcherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		RecognizerMatcher.init("java");
		RecognizerMatcher.matchImport("java.io.File");
		RecognizerMatcher.printMatchTables();
		System.out.println(RecognizerMatcher.matchClass("java.io.File"));
		System.out.println(RecognizerMatcher.matchClass("java.io.FileReader"));
		System.out.println(RecognizerMatcher.matchClass("java.io.FileWriter"));
		System.out.println(RecognizerMatcher.matchClass("java.io.BufferedReader"));
		System.out.println(RecognizerMatcher.matchClass("java.io.BufferedWriter"));
		
		System.out.println(RecognizerMatcher.matchMethod("java.io.File.createNewFile():boolean"));

	}

}
