package gool.test.lib;

import gool.Settings;
import gool.generator.common.Platform;
import gool.generator.cpp.CppPlatform;
import gool.generator.csharp.CSharpPlatform;
import gool.generator.java.JavaPlatform;
import gool.generator.python.PythonPlatform;
import gool.generator.objc.ObjcPlatform;
import gool.test.TestHelperJava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
public class GoolTestAPINet {

	/*
	 * At this day, the GOOL system supports 6 output languages that are
	 * symbolized by Platforms. You may comment/uncomment these platforms to
	 * enable/disable tests for the corresponding output language.
	 * 
	 * You may also add your own tests by creating a new method within this
	 * class preceded by a @Test annotation.
	 */
	private List<Platform> platforms = Arrays.asList(

			(Platform) JavaPlatform.getInstance(Settings.get("java_out_dir")),
			(Platform) CSharpPlatform.getInstance(Settings.get("csharp_out_dir")),
			(Platform) PythonPlatform.getInstance(Settings.get("python_out_dir")),
			(Platform) CppPlatform.getInstance(Settings.get("cpp_out_dir"))//,
			//(Platform) ObjcPlatform.getInstance(Settings.get("objc_out_dir"))

			);

	private static class GoolTestExecutor {
		private static final String CLEAN_UP_REGEX = "Note:.*?[\r\n]|(\\w+>\\s)|[\\r\\n]+";
		private String input;
		private String expected;
		private List<Platform> testedPlatforms;
		private List<Platform> excludedPlatforms;

		public GoolTestExecutor(String input, String expected,
				List<Platform> testedPlatforms, List<Platform> excludedPlatforms) {
			this.input = input;
			this.expected = expected;
			this.testedPlatforms = testedPlatforms;
			this.excludedPlatforms = excludedPlatforms;
		}

		public void compare(Platform platform, int test) throws Exception {
			if (excludedPlatforms.contains(platform)) {
				String errorMsg = "The following target platform(s) have been excluded for this test: ";
				for (Platform p : excludedPlatforms)
					if (testedPlatforms.contains(p))
						errorMsg += p + " ";
				Assert.fail(errorMsg
						+ "\nThis test may contain some patterns that are not supported by GOOL at the moment for these target platforms. You may see the GOOL wiki for further documentation.");
			}

			String result = compileAndRun(platform);
			// The following instruction is used to remove some logging data
			// at the beginning of the result string
			if (platform == ObjcPlatform.getInstance()
					&& result.indexOf("] ") != -1)
				result = result.substring(result.indexOf("] ") + 2);

			//Assert.assertEquals(String.format("The platform %s", platform),
			//		expected, result);
			TestHelperJava.assertTestAPINet(String.format("The platform %s", platform),
							expected, result, test);
			
		}

		protected String compileAndRun(Platform platform) throws Exception {
			String cleanOutput = cleanOutput(TestHelperJava.generateCompileRun(
					platform, input, MAIN_CLASS_NAME));
			return cleanOutput;
		}

		private static String cleanOutput(String result) {
			return result.replaceAll(CLEAN_UP_REGEX, "").trim();
		}
	}

	private static final String MAIN_CLASS_NAME = "Test";

	private List<Platform> testNotImplementedOnPlatforms = new ArrayList<Platform>();

	private void excludePlatformForThisTest(Platform platform) {
		testNotImplementedOnPlatforms.add(platform);
	}

	@BeforeClass
	public static void init() {
	}

	

	@Test
	public void goolLibraryNetTest1() throws Exception {
		String input = "import java.lang.Thread;"
				+ "import java.lang.Runnable;"
				+ "import java.net.DatagramPacket;"
				+ "import java.net.DatagramSocket;"
				+ "import java.net.InetAddress;"
				+ TestHelperJava
						.surroundWithClassMainNet(
								"/* création de 5 envoie au serveur qui les affiches */"
								+ "public static class ServeurUDPEcho{"
								+ "	final static int port = 8532; "
								+ "	final static int taille = 1024; "
								+ "	final byte buffer[] = new byte[taille];"
								+ ""
								+ "	public void run() throws Exception"
								+ "	{ "
								+ "		DatagramSocket socket = null;"
								+ "		socket = new DatagramSocket(port);"
								+ "		for(int i = 0 ; i < 5 ; i++){"
								+ "			DatagramPacket data = new DatagramPacket(buffer,buffer.length); "
								+ "			socket.receive(data);"
								+ "			System.out.println(new String(data.getData())); "
								+ "			"
								+ "		}"
								+ ""
								+ "	}"
								+ "}"
								+ ""
								+ "public static class ClientUDPEcho {"
								+ "	final static int taille = 1024; "
								+ "	final byte buffer[] = new byte[taille];"
								+ ""
								+ "	public void run() throws Exception"
								+ "	{ "
								+ "		InetAddress serveur = InetAddress.getByName(\"localhost\"); "
								+ "		String message = \"HELLO\";"
								+ "		int length = message.length();"
								+ "		byte buffer[] = message.getBytes(); "
								+ "		DatagramPacket dataSent = new DatagramPacket(buffer,length,serveur,8532); "
								+ "		DatagramSocket socket = new DatagramSocket(); "
								+ "		socket.send(dataSent);"
								+ "		"
								+ "	"
								+ "	} "
								+ "}"
								+ ""
								+ "public static void main(String[] args) {"
								+ "	final ServeurUDPEcho server = new ServeurUDPEcho();"
								+ "	Thread serverThread =new Thread(new Runnable() {"
								+ "		"
								+ "		@Override"
								+ "		public void run() {"
								+ "			try {"
								+ "				server.run();"
								+ "			} catch (Exception e) {"
								+ "				"
								+ "			}"
								+ "		}"
								+ "	});"
								+ "	serverThread.start();"
								+ "	"
								+ "	for(int i = 0 ; i < 5 ; i++){"
								+ "		final ClientUDPEcho client = new ClientUDPEcho();"
								+ "		new Thread(new Runnable() {"
								+ "			"
								+ "			@Override"
								+ "			public void run() {"
								+ "				try {"
								+ "					client.run();"
								+ "				} catch (Exception e) {"
								+ "					"
								+ "				}"
								+ "			}"
								+ "		}).start();"
								+ "	}"
								+ "	try {"
								+ "		serverThread.join();"
								+ "	} catch (InterruptedException e) {"
								+ "		e.printStackTrace();"
								+ "	}"
								+ "}"
								,MAIN_CLASS_NAME);
		String expected = "HELLO" + "HELLO" + "HELLO" + "HELLO" + "HELLO";

		// Matching of the GoolFile library class and of its method
		// work only for the Java target language at the moment,
		// so we exclude the other platforms for this test.
		excludePlatformForThisTest((Platform) ObjcPlatform.getInstance());

		compareResultsDifferentPlatforms(input, expected, 1);
	}

	@Test
	public void goolLibraryNetTest2() throws Exception {
		String input = "import java.lang.Thread;"
				+ "import java.lang.Runnable;"
				+ "import java.net.DatagramPacket;"
				+ "import java.net.DatagramSocket;"
				+ "import java.net.InetAddress;"
				+ TestHelperJava
						.surroundWithClassMainNet(
								"/* création de 5 envoie au serveur qui les affiches */"
								+ "public static class ServeurUDPEcho{"
								+ "	final static int port = 8532; "
								+ "	final static int taille = 1024; "
								+ "	final byte buffer[] = new byte[taille];"
								+ ""
								+ "	public void run() throws Exception"
								+ "	{ "
								+ "		DatagramSocket socket = null;"
								+ "		socket = new DatagramSocket(port);"
								+ "		for(int i = 0 ; i < 10 ; i++){"
								+ "			DatagramPacket data = new DatagramPacket(buffer,buffer.length); "
								+ "			socket.receive(data);"
								+ "			System.out.println(new String(data.getData())); "
								+ "			"
								+ "		}"
								+ ""
								+ "	}"
								+ "}"
								+ ""
								+ "public static class ClientUDPEcho {"
								+ "	final static int taille = 1024; "
								+ "	final byte buffer[] = new byte[taille];"
								+ ""
								+ "	public void run() throws Exception"
								+ "	{ "
								+ "		InetAddress serveur = InetAddress.getByName(\"localhost\"); "
								+ "		String message = \"HELLO\";"
								+ "		int length = message.length();"
								+ "		byte buffer[] = message.getBytes(); "
								+ "		DatagramPacket dataSent = new DatagramPacket(buffer,length,serveur,8532); "
								+ "		DatagramSocket socket = new DatagramSocket(); "
								+ "		socket.send(dataSent);"
								+ "		"
								+ "	"
								+ "	} "
								+ "}"
								+ ""
								+ "public static void main(String[] args) {"
								+ "	final ServeurUDPEcho server = new ServeurUDPEcho();"
								+ "	Thread serverThread =new Thread(new Runnable() {"
								+ "		"
								+ "		@Override"
								+ "		public void run() {"
								+ "			try {"
								+ "				server.run();"
								+ "			} catch (Exception e) {"
								+ "				"
								+ "			}"
								+ "		}"
								+ "	});"
								+ "	serverThread.start();"
								+ "	"
								+ "	for(int i = 0 ; i < 10 ; i++){"
								+ "		final ClientUDPEcho client = new ClientUDPEcho();"
								+ "		new Thread(new Runnable() {"
								+ "			"
								+ "			@Override"
								+ "			public void run() {"
								+ "				try {"
								+ "					client.run();"
								+ "				} catch (Exception e) {"
								+ "					"
								+ "				}"
								+ "			}"
								+ "		}).start();"
								+ "	}"
								+ "	try {"
								+ "		serverThread.join();"
								+ "	} catch (InterruptedException e) {"
								+ "		e.printStackTrace();"
								+ "	}"
								+ "}"
								,MAIN_CLASS_NAME);
		String expected = "HELLO" + "HELLO" + "HELLO" + "HELLO" + "HELLO"
								+ "HELLO" + "HELLO" + "HELLO" + "HELLO" + "HELLO";

		// Matching of the GoolFile library class and of its method
		// work only for the Java target language at the moment,
		// so we exclude the other platforms for this test.
		excludePlatformForThisTest((Platform) ObjcPlatform.getInstance());

		compareResultsDifferentPlatforms(input, expected, 2);
	}

	@Test
	public void goolLibraryNetTest3() throws Exception {
		String input = "import java.lang.Thread;"
				+ "import java.lang.Runnable;"
				+ "import java.net.DatagramPacket;"
				+ "import java.net.DatagramSocket;"
				+ "import java.net.InetAddress;"
				+ TestHelperJava
						.surroundWithClassMainNet(
								"/* création de 5 envoie au serveur qui les affiches */"
								+ "public static class ServeurTCPEcho{"
								+ ""
								+ "	public void run() throws Exception{"
								+ "		ServerSocket welcomeSocket = new ServerSocket(6789);"
								+ ""
								+ "		for(int i = 0 ; i < 5 ; i++)"
								+ "		{"
								+ "			Socket connectionSocket = welcomeSocket.accept();"
								+ "			"
								+ "			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());"
								+ "			System.out.println(\"1\");"
								+ "			outToClient.writeBytes(\"1\");"
								+ "		} "
								+ "	}"
								+ "}"
								+ ""
								+ "public static class ClientTCPEcho {"
								+ ""
								+ "	public void run() throws Exception"
								+ "	{ "
								+ "		String sentence;"
								+ "		  String modifiedSentence;"
								+ "		  Socket clientSocket = new Socket(\"localhost\", 6789);"
								+ "		  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());"
								+ "		  outToServer.writeBytes(\"HELLO\" + '\n');"
								+ "		  System.out.println(\"1\" );"
								+ "		  clientSocket.close();"
								+ "	} "
								+ "}"
								+ ""
								+ "public static void main(String[] args) {"
								+ "	final ServeurTCPEcho server = new ServeurTCPEcho();"
								+ "	Thread serverThread =new Thread(new Runnable() {"
								+ "		"
								+ "		@Override"
								+ "		public void run() {"
								+ "			try {"
								+ "				server.run();"
								+ "			} catch (Exception e) {"
								+ "				"
								+ "			}"
								+ "		}"
								+ "	});"
								+ "	serverThread.start();"
								+ "	"
								+ "	for(int i = 0 ; i < 5 ; i++){"
								+ "		final ClientTCPEcho client = new ClientTCPEcho();"
								+ "		new Thread(new Runnable() {"
								+ "			"
								+ "			@Override"
											+ "			public void run() {"
								+ "				try {"
								+ "					client.run();"
								+ "				} catch (Exception e) {"
								+ "					"
								+ "				}"
								+ "			}"
								+ "		}).start();"
								+ "	}"
								+ "	try {"
								+ "		serverThread.join();"
								+ "	} catch (InterruptedException e) {"
								+ "		"
								+ "	}"
								+ "}"
								,MAIN_CLASS_NAME);
		String expected = "1111111111" ;

		// Matching of the GoolFile library class and of its method
		// work only for the Java target language at the moment,
		// so we exclude the other platforms for this test.
		excludePlatformForThisTest((Platform) ObjcPlatform.getInstance());

		compareResultsDifferentPlatforms(input, expected, 3);
	}

	@Test
	public void goolLibraryNetTest4() throws Exception {
		String input = "import java.lang.Thread;"
				+ "import java.lang.Runnable;"
				+ "import java.net.DatagramPacket;"
				+ "import java.net.DatagramSocket;"
				+ "import java.net.InetAddress;"
				+ TestHelperJava
						.surroundWithClassMainNet(
								"/* création de 10 envoie au serveur qui les affiches */"
								+ "public static class ServeurTCPEcho{"
								+ ""
								+ "	public void run() throws Exception{"
								+ "		ServerSocket welcomeSocket = new ServerSocket(6789);"
								+ ""
								+ "		for(int i = 0 ; i < 10 ; i++)"
								+ "		{"
								+ "			Socket connectionSocket = welcomeSocket.accept();"
								+ "			"
								+ "			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());"
								+ "			System.out.println(\"1\");"
								+ "			outToClient.writeBytes(\"1\");"
								+ "		} "
								+ "	}"
								+ "}"
								+ ""
								+ "public static class ClientTCPEcho {"
								+ ""
								+ "	public void run() throws Exception"
								+ "	{ "
								+ "		String sentence;"
								+ "		  String modifiedSentence;"
								+ "		  Socket clientSocket = new Socket(\"localhost\", 6789);"
								+ "		  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());"
								+ "		  outToServer.writeBytes(\"HELLO\" + '\n');"
								+ "		  System.out.println(\"1\" );"
								+ "		  clientSocket.close();"
								+ "	} "
								+ "}"
								+ ""
								+ "public static void main(String[] args) {"
								+ "	final ServeurTCPEcho server = new ServeurTCPEcho();"
								+ "	Thread serverThread =new Thread(new Runnable() {"
								+ "		"
								+ "		@Override"
								+ "		public void run() {"
								+ "			try {"
								+ "				server.run();"
								+ "			} catch (Exception e) {"
								+ "				"
								+ "			}"
								+ "		}"
								+ "	});"
								+ "	serverThread.start();"
								+ "	"
								+ "	for(int i = 0 ; i < 10 ; i++){"
								+ "		final ClientTCPEcho client = new ClientTCPEcho();"
								+ "		new Thread(new Runnable() {"
								+ "			"
								+ "			@Override"
											+ "			public void run() {"
								+ "				try {"
								+ "					client.run();"
								+ "				} catch (Exception e) {"
								+ "					"
								+ "				}"
								+ "			}"
								+ "		}).start();"
								+ "	}"
								+ "	try {"
								+ "		serverThread.join();"
								+ "	} catch (InterruptedException e) {"
								+ "		"
								+ "	}"
								+ "}"
								,MAIN_CLASS_NAME);
		String expected = "11111111111111111111" ;

		// Matching of the GoolFile library class and of its method
		// work only for the Java target language at the moment,
		// so we exclude the other platforms for this test.
		excludePlatformForThisTest((Platform) ObjcPlatform.getInstance());

		compareResultsDifferentPlatforms(input, expected, 4);
	}
	
	private void compareResultsDifferentPlatforms(String input, String expected, int test)
			throws Exception {
		compareResultsDifferentPlatforms(new GoolTestExecutor(input, expected,
				platforms, testNotImplementedOnPlatforms), test);
		this.testNotImplementedOnPlatforms = new ArrayList<Platform>();
	}

	private void compareResultsDifferentPlatforms(GoolTestExecutor executor, int test)
			throws Exception {
		for (Platform platform : platforms) {
			executor.compare(platform,test);
		}
	}
}
